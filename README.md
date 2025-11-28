# RafikiBora Microfinance API

Spring Boot REST API for the RafikiBora system. This document provides a practical overview of available endpoints, payload structures, authentication, and how to explore the API using Swagger UI.

Note: The project includes OpenAPI/Swagger documentation. You can browse interactive docs and try requests directly in your browser.

## Quick start

- Java: 21
- Spring Boot: 3.3.x
- Default port: 8081

Run the application (typical):

- Maven: `./mvnw spring-boot:run`

## API documentation (Swagger UI)

- Swagger UI: http://localhost:8081/swagger-ui.html (redirects to UI)
- Alternative UI path: http://localhost:8081/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs

Swagger/OpenAPI is configured with JWT Bearer security; you can add a token via the "Authorize" button for secured endpoints.

## Postman Collection Endpoints 

The following endpoints are defined in the provided Postman collection. Base URL assumed: http://localhost:8081

| Name                   | Method | Path                                   | Auth   |
|------------------------|--------|----------------------------------------|--------|
| Signup User            | POST   | /api/users/createuser                  | Bearer |
| GET user Profile       | GET    | /api/users/user/profile                | Bearer |
| Login User             | POST   | /api/auth/login                        | None   |
| Approve User           | POST   | /api/users/user/approve?email={email}  | Bearer |
| Create Role            | POST   | /api/roles                              | Bearer |
| Get All Terminals      | GET    | /api/terminals                         | Bearer |
| Create Terminal        | POST   | /api/terminals                         | Bearer |
| Get All Users          | GET    | /api/users                              | Bearer |
| Delete User            | DELETE | /api/users/{id}                        | Bearer |
| Get All Transactions   | GET    | /api/transactions/all                  | Bearer |

### Sample payloads (from Postman collection, anonymized)

- Signup User (POST /api/users/createuser)

```json
{
  "firstName": "Ava",
  "lastName": "Ngugi",
  "email": "ava.ngugi@example.com",
  "username": "ava.ngugi@example.com",
  "password": "P@ssw0rd!2025",
  "phoneNo": "+254700123456",
  "role": "ADMIN"
}
```

- Login User (POST /api/auth/login)

```json
{
  "email": "admin@example.com",
  "password": "P@ssw0rd!2025"
}
```

- Create Role (POST /api/roles)

```json
{
  "roleName": "MANAGER"
}
```

- Create Terminal (POST /api/terminals)

```json
{
  "modelType": "IWL250",
  "serialNo": "SN-002222"
}
```

Endpoints like Approve User and others using query/path parameters do not have request bodies in the collection. Example Approve URL:

```
POST /api/users/user/approve?email=user.to.approve@example.com
```

## Authentication

This API uses JWT Bearer tokens for securing most endpoints. Include your token in the Authorization header:

Authorization: Bearer <your-jwt-token>

Public endpoints (no token required):
- Swagger/OpenAPI assets (/swagger-ui/**, /v3/api-docs/**)
- Health/framework endpoints (/actuator/**, /error)
- Specific transaction endpoints: `/api/transactions/sale`, `/api/transactions/deposit`, `/api/transactions/receive_money` (as configured)

Secured endpoints: everything else unless explicitly marked as permitAll.

## Users API

Base path: `/api/users`

- POST `/createuser` — Create a new user (requires an authenticated maker)
  - Request body: UserDto
    - firstName (string)
    - lastName (string)
    - email (string)
    - userName (string)
    - phoneNo (string)
    - password (string, write-only)
    - role (string: ADMIN | MERCHANT | CUSTOMER | AGENT)
  - Responses: 201 Created, 400 on validation error
  - Example:
    ```bash
    curl -X POST http://localhost:8081/api/users/createuser \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer <JWT>" \
      -d '{
        "firstName":"Jane", "lastName":"Doe", "email":"jane.doe@example.com",
        "userName":"jane.doe", "phoneNo":"0712345678", "password":"P@ssw0rd!", "role":"MERCHANT"
      }'
    ```

- GET `/user/profile` — Get the current authenticated user's profile
  - Response: User (entity)
  - Security: Requires Bearer token

- POST `/user/approve/{email}` — Approve a user account by email
  - Path: email (string)
  - Security: Requires Bearer token with appropriate role
  - Response: User (approved)

- DELETE `/{id}` — Soft delete user by id
  - Security: Requires Bearer token
  - Response: { status, message }

- GET `/{roleName}` — Find users by role
  - Response: List of SystemUser JSON objects (or `{ "found": false }`)

- GET `` (base) — List all users
  - Response: List of SystemUser JSON objects (or `{ "found": false }`)

- GET `/user/{id}` — Get user by id
  - Response: User

- GET `/id/{id}` — Find by id (alternate path)
  - Response: User

- POST `/addagent` — Create a new user with AGENT role under the current merchant
  - Request body: UserDto
  - Security: Current user must be a MERCHANT

- POST `/assignmerchantterminal` — Assign a terminal to a merchant
  - Request body: TerminalAssignmentRequest
    - email (string, merchant email)
    - tid (string, terminal identifier)

- POST `/agenttoterminal` — Assign a terminal to an agent (must belong to the same merchant)
  - Request body: TerminalToAgentResponse
    - agentEmail (string)
    - tid (string)

- PATCH `/{id}` — Update current user's details
  - Request body: UserDto (fields to update)
  - Note: Service updates the authenticated user regardless of the path id

- GET `/unAssignedAgents` — List agents of the current merchant that are not assigned to any terminal

### Sample payloads (Users)

User
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "userName": "jane.doe",
  "phoneNo": "0712345678",
  "password": "P@ssw0rd!",
  "role": "MERCHANT"
}
```

TerminalAssignmentRequest
```json
{
  "email": "merchant@example.com",
  "tid": "T1234567"
}
```

TerminalToAgentResponse
```json
{
  "agentEmail": "agent@example.com",
  "tid": "T1234567"
}
```
SystemUser (response item)
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "phoneNo": "0712345678",
  "createdBy": "admin@example.com",
  "approvedBy": "checker@example.com",
  "userid": "42",
  "username": "jane.doe@example.com",
  "mid": "MID0011223344",
  "businessName": "Acme Stores",
  "dateCreated": "2025-01-01 12:00:00",
  "status": "true",
  "deleted": "false"
}
```
## Transactions API

Base path: `/api/transactions`

- POST `/sale` — Perform a sale (purchase) transaction
  - Request body: SaleDto
    - pan (string)
    - amountTransaction (string)
    - transmissionDateTime (string, e.g., YYMMDDhhmmss)
    - terminal (string, TID)
    - merchant (string, MID)
    - currencyCode (string, ISO 4217)
    - processingCode (string)
  - Response: 201 Created with body "OK" on success; 400 with error message on failure
  - Example:
    ```bash
    curl -X POST http://localhost:8081/api/transactions/sale \
      -H "Content-Type: application/json" \
      -d '{
        "pan":"472500******1122","amountTransaction":"500.00","transmissionDateTime":"250101120000",
        "terminal":"T1234567","merchant":"MID0011223344","currencyCode":"KES","processingCode":"000000"
      }'
    ```

- POST `/deposit` — Perform a deposit from customer to merchant
  - Request body: DepositDto
    - merchantPan (string)
    - customerPan (string)
    - amountTransaction (string)
    - DateTimeTransmission (string)
    - terminal (string, TID)
    - merchant (string, MID)
    - currencyCode (string)
    - processingCode (string)
  - Response: 201 Created with body "OK" on success; 400 with error message on failure

- POST `/receive_money` — Process a receive-money payout to a customer
  - Request body: ReceiveMoneyRequestDto
    - pan (string)
    - processingCode (string)
    - txnAmount (string)
    - transmissionDateTime (string)
    - tid (string)
    - mid (string)
    - receiveMoneyToken (string)
    - currency (string)
  - Response: ReceiveMoneyResponseDto
    - message (string, e.g., "00" success, "96" error)
    - txnAmount (string)

### Sample payloads (Transactions)

Sale
```json
{
  "pan": "472500******1122",
  "amountTransaction": "500.00",
  "transmissionDateTime": "250101120000",
  "terminal": "T1234567",
  "merchant": "MID0011223344",
  "currencyCode": "KES",
  "processingCode": "000000"
}
```
Deposit
```json
{
  "merchantPan": "543212******7890",
  "customerPan": "472500******1122",
  "amountTransaction": "1000.00",
  "DateTimeTransmission": "250101120000",
  "terminal": "T1234567",
  "merchant": "MID0011223344",
  "currencyCode": "KES",
  "processingCode": "210000"
}
```
ReceiveMoneyRequest
```json
{
  "pan": "472500******1122",
  "processingCode": "260000",
  "txnAmount": "1500.00",
  "transmissionDateTime": "250101120000",
  "tid": "T1234567",
  "mid": "MID0011223344",
  "receiveMoneyToken": "845392761",
  "currency": "KES"
}
```
ReceiveMoneyResponse
```json
{
  "message": "00",
  "txnAmount": "1500.00"
}
```

## Using JWT with curl

Example calling a secured endpoint after obtaining a token:

```bash
TOKEN="<paste-your-jwt>"
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:8081/api/users/user/profile
```

## Configuration

Key application properties (src/main/resources/application.properties):
- server.port=8081
- rafiki-bora.auth.tokenSecret=... (configure a strong secret; Base64 preferred)
- rafiki-bora.auth.tokenExpirationMsec, refreshTokenExpirationMsec
- spring.datasource.* (configure your DB)

## Explore the API

The Swagger UI reflects the actual code and DTOs with descriptions and examples. If anything in this README differs, prefer the live Swagger schema at `/v3/api-docs` and UI at `/swagger-ui.html`.
