# Environment Setup

## Backend (Spring Boot)

### 1. Recommended variables

Use values from [.env.backend.example](../.env.backend.example) as reference.

### 2. PowerShell example (temporary current terminal)

```powershell
$env:APP_DB_URL="jdbc:postgresql://localhost:5432/belajardb"
$env:APP_DB_USERNAME="postgres"
$env:APP_DB_PASSWORD="rahasia"
$env:APP_JWT_SECRET="replace-with-long-random-secret"
$env:APP_ADMIN_BOOTSTRAP_ENABLED="true"
$env:APP_ADMIN_USERNAME="admin"
$env:APP_ADMIN_EMAIL="admin@local.dev"
$env:APP_ADMIN_PASSWORD="Admin12345"
$env:APP_ADMIN_FULL_NAME="System Administrator"

./mvnw spring-boot:run
```

### 3. CMD example (temporary current terminal)

```cmd
set APP_DB_URL=jdbc:postgresql://localhost:5432/belajardb
set APP_DB_USERNAME=postgres
set APP_DB_PASSWORD=rahasia
set APP_JWT_SECRET=replace-with-long-random-secret
set APP_ADMIN_BOOTSTRAP_ENABLED=true
set APP_ADMIN_USERNAME=admin
set APP_ADMIN_EMAIL=admin@local.dev
set APP_ADMIN_PASSWORD=Admin12345
set APP_ADMIN_FULL_NAME=System Administrator

mvnw.cmd spring-boot:run
```

### 4. Linux/macOS example

```bash
export APP_DB_URL="jdbc:postgresql://localhost:5432/belajardb"
export APP_DB_USERNAME="postgres"
export APP_DB_PASSWORD="rahasia"
export APP_JWT_SECRET="replace-with-long-random-secret"
export APP_ADMIN_BOOTSTRAP_ENABLED="true"
export APP_ADMIN_USERNAME="admin"
export APP_ADMIN_EMAIL="admin@local.dev"
export APP_ADMIN_PASSWORD="Admin12345"
export APP_ADMIN_FULL_NAME="System Administrator"

./mvnw spring-boot:run
```

## Frontend (Next.js)

1. Use [.env.nextjs.example](../.env.nextjs.example) as template.
2. In Next.js project root, create `.env.local`:

```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_APP_NAME=ATS CV Builder
```

## Security Notes

1. Never commit real production secrets.
2. Change default admin password immediately.
3. For production, set `APP_ADMIN_BOOTSTRAP_ENABLED=false` after initial provisioning.
4. Use different JWT secret per environment (dev/staging/prod).
