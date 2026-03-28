# QUICK START GUIDE 🚀

## Prerequisites
- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/#java17)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **Docker & Docker Compose** - [Download](https://www.docker.com/products/docker-desktop)
- **Git**

## 1️⃣ Setup Database (PostgreSQL)

```bash
# Dari folder root project
docker-compose up -d

# Verify database is running
docker ps
# Anda harus lihat container 'aicvbuilder-db' running
```

## 2️⃣ Setup Backend (Spring Boot)

```bash
cd backend

# Copy environment file
cp .env.example .env

# Edit .env dan tambahkan Azure credentials Anda
# AZURE_OPENAI_API_KEY=your_key
# AZURE_OPENAI_ENDPOINT=your_endpoint

# Build backend
mvn clean install

# Run backend
mvn spring-boot:run

# Backend akan start di http://localhost:8080/api
# Swagger UI: http://localhost:8080/api/swagger-ui.html
```

## 3️⃣ Setup Frontend (Next.js)

```bash
cd frontend

# Copy environment file
cp .env.local.example .env.local

# Install dependencies
npm install

# Run development server
npm run dev

# Frontend akan start di http://localhost:3000
```

## 🧪 Testing APIs

### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### Test Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/cv \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 📁 Project Structure

```
AI CV Builder/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/com/aicvbuilder/
│   │   ├── controller/        # REST APIs
│   │   ├── service/           # Business Logic
│   │   ├── repository/        # Data Access
│   │   ├── entity/            # JPA Entities
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── security/          # JWT & Security
│   │   └── config/            # Configuration
│   ├── pom.xml
│   └── .env.example
│
├── frontend/                   # Next.js Frontend
│   ├── src/
│   │   ├── pages/             # Next.js Pages
│   │   ├── store/             # Zustand Stores
│   │   ├── lib/               # Utilities & API
│   │   ├── styles/            # CSS
│   │   └── components/        # React Components (akan ditambah)
│   ├── package.json
│   └── .env.local.example
│
├── docker-compose.yml         # PostgreSQL
├── README.md
└── QUICKSTART.md              # File ini
```

## 🔑 Environment Variables

### Backend (.env)
```
AZURE_OPENAI_API_KEY=sk-...
AZURE_OPENAI_ENDPOINT=https://xxx.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT_NAME=gpt-35-turbo
JWT_SECRET=your-secret-key-min-32-chars
```

### Frontend (.env.local)
```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

## 🐛 Troubleshooting

### Backend tidak bisa connect ke database
```bash
# Check docker container
docker ps | grep postgres

# View logs
docker logs aicvbuilder-db

# Restart container
docker-compose restart
```

### Port sudah digunakan
```bash
# Backend port 8080
lsof -i :8080
kill -9 <PID>

# Frontend port 3000
lsof -i :3000
kill -9 <PID>
```

### Maven dependency error
```bash
# Clear cache
mvn clean

# Re-download dependencies
mvn install
```

## 📊 Swagger API Documentation

Setelah backend running, buka:
```
http://localhost:8080/api/swagger-ui.html
```

## 🚀 Next Steps

1. ✅ Setup database & backend
2. ✅ Setup frontend
3. ⬜ Test login/register flow
4. ⬜ Test CV CRUD operations
5. ⬜ Implement AI features dengan Azure OpenAI
6. ⬜ Add PDF export functionality
7. ⬜ Deploy to cloud (AWS, Azure, atau Vercel)

## 💡 Tips

- Gunakan Postman untuk testing API: [Postman](https://www.postman.com/)
- Monitor logs dengan `tail -f` di terminal baru
- Use `console.log` di frontend untuk debugging
- Check browser DevTools for network requests

## 📞 Support

Jika ada masalah:
1. Check logs terlebih dahulu
2. Review documentation di README.md
3. Pastikan semua dependencies ter-install
4. Verifikasi environment variables

---

**Happy Coding!** 🎉 Sekarang tinggal jalanin 3 command di atas dan project sudah ready!
