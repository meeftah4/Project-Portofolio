# API Contract

## Base URL

- Development: `http://localhost:8080`

## Supporting Files

1. Environment setup guide: [docs/ENV_SETUP.md](ENV_SETUP.md)
2. Backend env template: [.env.backend.example](../.env.backend.example)
3. Next.js env template: [.env.nextjs.example](../.env.nextjs.example)
4. Postman collection: [postman/belajarspringboot_api.postman_collection.json](../postman/belajarspringboot_api.postman_collection.json)
5. Postman environment: [postman/belajarspringboot_local.postman_environment.json](../postman/belajarspringboot_local.postman_environment.json)
6. Next.js integration guide: [docs/NEXTJS_INTEGRATION.md](NEXTJS_INTEGRATION.md)
7. Next.js API client example: [docs/examples/nextjs/api-client.example.ts](examples/nextjs/api-client.example.ts)

## Environment Variables

Set these values in your runtime environment (or container/orchestrator secrets) before running in shared or production environments:

- `APP_DB_URL`
- `APP_DB_USERNAME`
- `APP_DB_PASSWORD`
- `APP_JWT_SECRET`
- `APP_ADMIN_BOOTSTRAP_ENABLED`
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_EMAIL`
- `APP_ADMIN_PASSWORD`
- `APP_ADMIN_FULL_NAME`

Notes:

- Keep `APP_JWT_SECRET` long and random.
- For production, set `APP_ADMIN_BOOTSTRAP_ENABLED=false` after initial admin provisioning.
- Do not commit real secret values to source control.

## Authentication

- Access token: JWT Bearer token.
- Refresh token: opaque token stored in database.
- Header for protected endpoints:

```
Authorization: Bearer <accessToken>
```

## Standard Error Response

Most validation and application errors follow this JSON shape:

```json
{
  "timestamp": "2026-03-29T11:00:00Z",
  "status": 400,
  "error": "Validation Failed",
  "message": "Request body tidak valid",
  "path": "/api/auth/register",
  "details": {
    "email": "format email tidak valid"
  }
}
```

Auth-related forbidden or unauthorized responses may return a simplified structure from security handlers.

## Auth Endpoints

### Register

- Method: `POST`
- Path: `/api/auth/register`
- Auth: Public
- Request body:

```json
{
  "username": "john.doe",
  "email": "john@example.com",
  "password": "Password123",
  "fullName": "John Doe"
}
```

- Response body:

```json
{
  "token": "<accessToken>",
  "accessToken": "<accessToken>",
  "refreshToken": "<refreshToken>",
  "tokenType": "Bearer",
  "accessTokenExpiresInHours": 24,
  "user": {
    "id": 1,
    "username": "john.doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "USER",
    "createdAt": "2026-03-29T11:00:00Z"
  }
}
```

### Login

- Method: `POST`
- Path: `/api/auth/login`
- Auth: Public
- Request body:

```json
{
  "email": "john@example.com",
  "password": "Password123"
}
```

- Response: same shape as register.

### Refresh Access Token

- Method: `POST`
- Path: `/api/auth/refresh`
- Auth: Public
- Request body:

```json
{
  "refreshToken": "<refreshToken>"
}
```

- Behavior:
  - Current refresh token is revoked.
  - New access token and refresh token are returned.

### Logout

- Method: `POST`
- Path: `/api/auth/logout`
- Auth: Optional (recommended with Bearer token)
- Request body (optional):

```json
{
  "refreshToken": "<refreshToken>"
}
```

- Behavior:
  - Access token from Authorization header is blacklisted.
  - Provided refresh token is revoked.

### Current User

- Method: `GET`
- Path: `/api/auth/me`
- Auth: Bearer token

## CV Endpoints

All endpoints below require Bearer token and are scoped to the authenticated account.
If `cvId` belongs to another account, API returns `404 CV tidak ditemukan`.

- `POST /api/cvs`
- `GET /api/cvs`
- `GET /api/cvs/{cvId}`
- `PUT /api/cvs/{cvId}`
- `DELETE /api/cvs/{cvId}`

Sections:

- Personal information:
  - `POST /api/cvs/{cvId}/personal-information`
  - `PUT /api/cvs/{cvId}/personal-information`
  - `GET /api/cvs/{cvId}/personal-information`
  - `DELETE /api/cvs/{cvId}/personal-information`

- Summary:
  - `POST /api/cvs/{cvId}/summary`
  - `PUT /api/cvs/{cvId}/summary`
  - `GET /api/cvs/{cvId}/summary`
  - `DELETE /api/cvs/{cvId}/summary`

- Educations:
  - `POST /api/cvs/{cvId}/educations`
  - `GET /api/cvs/{cvId}/educations`
  - `PUT /api/cvs/{cvId}/educations/{educationId}`
  - `DELETE /api/cvs/{cvId}/educations/{educationId}`

- Work experiences:
  - `POST /api/cvs/{cvId}/work-experiences`
  - `GET /api/cvs/{cvId}/work-experiences`
  - `PUT /api/cvs/{cvId}/work-experiences/{workExperienceId}`
  - `DELETE /api/cvs/{cvId}/work-experiences/{workExperienceId}`

- Projects:
  - `POST /api/cvs/{cvId}/projects`
  - `GET /api/cvs/{cvId}/projects`
  - `PUT /api/cvs/{cvId}/projects/{projectId}`
  - `DELETE /api/cvs/{cvId}/projects/{projectId}`

- Certificates:
  - `POST /api/cvs/{cvId}/certificates`
  - `GET /api/cvs/{cvId}/certificates`
  - `PUT /api/cvs/{cvId}/certificates/{certificateId}`
  - `DELETE /api/cvs/{cvId}/certificates/{certificateId}`

- Skills:
  - `POST /api/cvs/{cvId}/skills`
  - `GET /api/cvs/{cvId}/skills`
  - `PUT /api/cvs/{cvId}/skills/{skillId}`
  - `DELETE /api/cvs/{cvId}/skills/{skillId}`

- Languages:
  - `POST /api/cvs/{cvId}/languages`
  - `GET /api/cvs/{cvId}/languages`
  - `PUT /api/cvs/{cvId}/languages/{languageId}`
  - `DELETE /api/cvs/{cvId}/languages/{languageId}`

## Admin Endpoints

All endpoints require Bearer token with role `ADMIN`.

- Dashboard summary:
  - `GET /api/admin/dashboard`

- User list:
  - `GET /api/admin/users`

- Promote user to admin:
  - `POST /api/admin/users/{userId}/promote`

## Notes For Next.js Integration

1. Store access token in memory and refresh token in secure storage strategy suitable for your app architecture.
2. Add an axios or fetch interceptor to retry once with `/api/auth/refresh` when receiving `401`.
3. On logout, call `/api/auth/logout` with both Authorization header and refresh token body.
4. After successful promote, user should login again to receive a token with updated role claims.
