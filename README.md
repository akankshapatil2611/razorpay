# Razorpay Payment Gateway — Backend

A Spring Boot–based payment gateway inspired by Razorpay — merchant onboarding, server-to-server payments, card vault, and bank simulation.

> **Status:** Core payment flows are live. Settlement, webhooks, and refunds are modeled but not yet exposed as APIs.

---

## Tech Stack

```mermaid
mindmap
  root((Razorpay Backend))
    Runtime
      Java
    Framework
      Spring Boot
      Spring Security
      Spring Data JPA
    Database
      MySQL 
      Hibernate
    Tools
      MapStruct
      Lombok
      Maven
    Auth
      JWT
      API Key Basic Auth
```

---

## Project Structure

```mermaid
flowchart LR
    subgraph Root["com.gayeway.Razorpay"]
        App["RazorpayApplication"]

        subgraph common["common"]
            C1["enums"]
            C2["entities"]
            C3["exceptions"]
            C4["utilities"]
        end

        subgraph merchant["merchant"]
            M1["controller"]
            M2["service"]
            M3["security"]
            M4["entity / dto"]
        end

        subgraph payment["payment"]
            P1["controller"]
            P2["service"]
            P3["gateway adapters"]
            P4["state machine"]
            P5["simulator"]
        end

        subgraph vault["vault"]
            V1["controller"]
            V2["encryption"]
            V3["tokenize"]
        end

        subgraph operations["operations"]
            O1["settlement"]
            O2["webhooks"]
            O3["DLQ"]
        end
    end

    App --> common & merchant & payment & vault & operations
```

---

## Architecture Overview

```mermaid
flowchart TB
    subgraph Client["Client Layer"]
        Dashboard["Merchant Dashboard<br/>(JWT)"]
        Server["Server-to-Server<br/>(API Key)"]
    end

    subgraph Security["Security Layer"]
        JWTFilter["JwtAuthenticationFilter"]
        ApiKeyFilter["ApiKeyAuthenticationFilter"]
    end

    subgraph API["REST Controllers"]
        Auth["/v1/auth"]
        ApiKeys["/v1/merchants/api-keys"]
        Orders["/v1/orders"]
        Payments["/v1/payments"]
        Vault["/v1/vault"]
    end

    subgraph Services["Business Services"]
        AuthSvc["AuthService"]
        ApiKeySvc["ApiKeyService"]
        OrderSvc["OrderService"]
        PaymentSvc["PaymentService"]
        VaultSvc["VaultService"]
        StateMachine["PaymentStateMachine"]
    end

    subgraph Gateway["Payment Gateway — Strategy Pattern"]
        Router["PaymentGatewayRouter"]
        Card["CardPaymentAdapter"]
        UPI["UpiPaymentAdapter"]
        NB["NetBankingAdapter"]
    end

    subgraph Infra["Infrastructure"]
        MySQL[(MySQL)]
        Simulator["BankCallbackSimulator"]
    end

    Dashboard --> JWTFilter
    Server --> ApiKeyFilter
    JWTFilter --> Auth & ApiKeys
    ApiKeyFilter --> Orders & Payments & Vault

    Auth --> AuthSvc
    ApiKeys --> ApiKeySvc
    Orders --> OrderSvc
    Payments --> PaymentSvc
    Vault --> VaultSvc

    PaymentSvc --> StateMachine
    PaymentSvc --> Router
    Router --> Card & UPI & NB
    Simulator --> PaymentSvc

    AuthSvc & ApiKeySvc & OrderSvc & PaymentSvc & VaultSvc --> MySQL
```

---

## Authentication Flow

```mermaid
flowchart TD
    subgraph JWT["Dashboard Auth — JWT Bearer"]
        A1["POST /v1/auth/signup"] --> A2["POST /v1/auth/login"]
        A2 --> A3["Receive JWT token"]
        A3 --> A4["POST /v1/merchants/api-keys"]
        A4 --> A5["Create / List / Revoke / Rotate keys"]
    end

    subgraph APIKEY["Server Auth — API Key Basic"]
        B1["Authorization: Basic base64(keyId:secret)"]
        B1 --> B2["POST /v1/orders"]
        B1 --> B3["POST /v1/payments"]
        B1 --> B4["POST /v1/vault/tokenize"]
    end

    A5 -.->|"use generated key"| B1

    style JWT fill:#e8f4fd,stroke:#2196F3
    style APIKEY fill:#e8f5e9,stroke:#4CAF50
```

---

## API Surface

```mermaid
flowchart LR
    subgraph Public["Public — No Auth"]
        S1["POST /v1/auth/signup"]
        S2["POST /v1/auth/login"]
    end

    subgraph JWTRoutes["JWT Protected"]
        K1["POST /v1/merchants/api-keys"]
        K2["GET  /v1/merchants/api-keys"]
        K3["DELETE /v1/merchants/api-keys/{keyId}"]
        K4["POST /v1/merchants/api-keys/{keyId}/rotate"]
    end

    subgraph KeyRoutes["API Key Protected"]
        O1["POST /v1/orders"]
        P1["POST /v1/payments"]
        P2["POST /v1/payments/{id}/capture"]
        V1["POST /v1/vault/tokenize"]
    end

    Public --> JWTRoutes
    JWTRoutes --> KeyRoutes
```

Base URL: `http://localhost:9090`

---

## End-to-End Payment Flow

```mermaid
sequenceDiagram
    autonumber
    participant M as Merchant App
    participant API as Payment API
    participant GW as Gateway Adapter
    participant SM as State Machine
    participant Bank as Bank Simulator

    rect rgb(232, 244, 253)
        Note over M,API: Phase 1 — Onboard & get API key (JWT)
        M->>API: POST /v1/auth/signup
        M->>API: POST /v1/auth/login
        M->>API: POST /v1/merchants/api-keys
    end

    rect rgb(232, 245, 233)
        Note over M,API: Phase 2 — Create order (API Key)
        M->>API: POST /v1/orders
        API-->>M: orderId
    end

    rect rgb(255, 243, 224)
        Note over M,API: Phase 3 — Pay (optional card tokenize)
        opt Card payment
            M->>API: POST /v1/vault/tokenize
            API-->>M: cardToken
        end
        M->>API: POST /v1/payments
        API->>SM: AUTHORIZE_ATTEMPT
        API->>GW: initiate(payment)
        GW-->>API: Pending / Success / Failure
        API-->>M: paymentId + status
    end

    rect rgb(252, 228, 236)
        Note over Bank,API: Phase 4 — Async bank callback
        Bank->>API: simulate callback
        API->>SM: AUTHORIZE_SUCCESS / FAIL
    end

    rect rgb(237, 231, 246)
        Note over M,API: Phase 5 — Capture funds
        M->>API: POST /v1/payments/{id}/capture
        API->>SM: CAPTURE_REQUEST → CAPTURE_SUCCESS
        API-->>M: CAPTURED
    end
```

---

## Payment State Machine

```mermaid
stateDiagram-v2
    direction LR

    [*] --> CREATED

    CREATED --> AUTHORIZED: AUTHORIZE_ATTEMPT
    CREATED --> CANCELLED: CANCEL

    AUTHORIZING --> AUTHORIZED: AUTHORIZE_SUCCESS
    AUTHORIZING --> FAILED: AUTHORIZE_FAIL
    AUTHORIZING --> CANCELLED: CANCEL

    AUTHORIZED --> CAPTURED: CAPTURE_REQUEST
    AUTHORIZED --> AUTH_EXPIRED: CAPTURE_TIMEOUT

    CAPTURING --> CAPTURED: CAPTURE_SUCCESS
    CAPTURING --> AUTHORIZED: CAPTURE_FAIL

    CAPTURED --> SETTLED: SETTLE
    CAPTURED --> PARTIAL_REFUNDING: REFUND_INIT
    CAPTURED --> REFUNDING: REFUND_COMPLETE

    SETTLED --> PARTIAL_REFUNDING: REFUND_INIT
    PARTIAL_REFUNDING --> REFUNDING: REFUND_COMPLETE

    note right of CREATED
        All transitions logged
        in payment_transition_log
    end note
```

---

## Payment Method Routing

```mermaid
flowchart TD
    Request["POST /v1/payments"] --> Router["PaymentGatewayRouter"]

    Router --> CARD["CardPaymentAdapter"]
    Router --> UPI["UpiPaymentAdapter"]
    Router --> NB["NetBankingAdapter"]
    Router -.-> WALLET["Wallet — pending"]

    CARD --> Vault["VaultService<br/>tokenized PAN"]
    CARD --> Proc1["CardPaymentProcessor"]
    UPI --> Proc2["UpiPaymentProcessor"]
    NB --> Proc3["NetBankingPaymentProcessor"]

    Proc1 & Proc2 & Proc3 --> Sim["BankCallbackSimulator"]
    Sim --> SM["PaymentStateMachine"]

    style WALLET fill:#f5f5f5,stroke:#999,stroke-dasharray: 5 5
```

---

## Implementation Progress

```mermaid
timeline
    title Build Phases
    section Foundation
        Entity design : Merchant, Order, Payment, Refund
        Global exceptions : Structured error responses
        DB indexes : High-traffic columns
    section Merchant & Auth
        Signup & Login : JWT for AppUser
        Dual security chains : JWT + API Key filters
        API key CRUD : Create, list, revoke, rotate
    section Orders & Payments
        Order API : Expiry & amount validation
        Payment initiate & capture : Full lifecycle
        Gateway strategy : Router + adapters
        State machine : Transition audit log
    section Vault & Cards
        Card tokenization : DEK/KEK encryption
        Card adapter : Vault-integrated payments
    section Simulation
        Bank simulator : Delays, success rates, chaos modes
    section Upcoming
        Refunds : Entity ready
        Settlements : Entity ready
        Webhooks & DLQ : Entity ready
        Wallet adapter : Enum only
```

---

## Planned Work

```mermaid
flowchart LR
    Done["Implemented<br/>Auth · Orders · Payments<br/>Vault · Simulator"]

    Done --> R1["Refund APIs"]
    Done --> R2["Settlement batches"]
    Done --> R3["Webhook delivery + DLQ retry"]
    Done --> R4["Wallet adapter"]
    Done --> R5["Admin APIs /v1/admin"]
    Done --> R6["Enable @Scheduled simulator"]

    style Done fill:#c8e6c9,stroke:#4CAF50
    style R1 fill:#fff9c4,stroke:#FBC02D
    style R2 fill:#fff9c4,stroke:#FBC02D
    style R3 fill:#fff9c4,stroke:#FBC02D
    style R4 fill:#fff9c4,stroke:#FBC02D
    style R5 fill:#fff9c4,stroke:#FBC02D
    style R6 fill:#fff9c4,stroke:#FBC02D
```

---

## Getting Started

```mermaid
flowchart TD
    Start([Start]) --> P1["Install Java 21 + Maven + MySQL 8"]
    P1 --> P2["CREATE DATABASE razorpayDB"]
    P2 --> P3["Configure application.yaml credentials"]
    P3 --> P4["./mvnw spring-boot:run"]
    P4 --> P5["Server running on port 9090"]
    P5 --> P6["Signup → Login → Create API Key → Create Order → Pay"]

    style Start fill:#e3f2fd,stroke:#1976D2
    style P5 fill:#c8e6c9,stroke:#4CAF50
```

```bash
# Quick start
./mvnw spring-boot:run

# Build
./mvnw clean package
```
