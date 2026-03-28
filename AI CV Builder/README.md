# AI CV Builder + Analyzer 🚀

Aplikasi yang menggabungkan kekuatan **AI (Azure OpenAI)** untuk membantu pengguna membuat, mengedit, dan menganalisis CV mereka dengan saran AI dan skor ATS real-time.

## 🎯 Fitur Utama

### ✅ Fitur Wajib
- **Login & Register** - Autentikasi dengan JWT
- **Create & Edit CV** - Interface yang user-friendly untuk membuat dan mengedit CV
- **Export PDF** - Download CV dalam format PDF
- **Dashboard User** - Lihat semua CV yang dibuat

### 🔥 Fitur Pembeda (AI-Powered)
- **AI Review CV** - Analisis mendalam CV menggunakan Azure OpenAI
- **ATS Score** - Skor compatibility dengan Applicant Tracking System
- **Keyword Suggestion** - Rekomendasi keyword yang meningkatkan visibility
- **Grammar Correction** - Perbaikan tata bahasa otomatis

## 🛠️ Tech Stack

### Backend
- **Spring Boot 3.2.3** - Java 17 LTS
- **Spring Security + JWT** - Autentikasi & Autorisasi
- **PostgreSQL** - Database
- **Azure OpenAI API** - AI Services
- **iText 7** - PDF Generation
- **Maven** - Build Tool

### Frontend
- **Next.js 14** - React Framework
- **TypeScript** - Type Safety
- **Tailwind CSS** - Styling
- **Zustand** - State Management
- **React Hook Form** - Form Management
- **Axios** - HTTP Client

### Infrastructure
- **Docker & Docker Compose** - PostgreSQL containerization
- **Azure OpenAI** - AI Services

## 📦 Setup Project

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL (atau gunakan Docker)
- Azure OpenAI API Key

### Backend Setup

```bash
cd backend

# 1. Copy environment file
cp .env.example .env

# 2. Edit .env dengan credentials Anda
# AZURE_OPENAI_API_KEY=xxx
# AZURE_OPENAI_ENDPOINT=xxx
# JWT_SECRET=xxx

# 3. Jalankan database
docker-compose up -d

# 4. Run backend
mvn spring-boot:run

# API akan berjalan di: http://localhost:8080/api
# Swagger UI: http://localhost:8080/api/swagger-ui.html
```

### Frontend Setup

```bash
cd frontend

# 1. Copy environment file
cp .env.local.example .env.local

# 2. Install dependencies
npm install

# 3. Run development server
npm run dev

# Frontend akan berjalan di: http://localhost:3000
```

## 📁 Project Structure

```
AI CV Builder/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aicvbuilder/
│   │   │   │   ├── entity/         # JPA Entities
│   │   │   │   ├── repository/     # Data Access Layer
│   │   │   │   ├── service/        # Business Logic
│   │   │   │   ├── controller/     # REST APIs
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── security/       # Auth & Security
│   │   │   │   └── util/           # Utilities
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   ├── pom.xml
│   └── .env.example
│
├── frontend/
│   ├── src/
│   │   ├── pages/           # Next.js Pages
│   │   ├── components/      # React Components
│   │   ├── hooks/          # Custom Hooks
│   │   ├── store/          # Zustand Stores
│   │   └── lib/            # Utilities
│   ├── package.json
│   ├── tsconfig.json
│   ├── tailwind.config.js
│   └── .env.local.example
│
├── docker-compose.yml
└── README.md
```

## 🔐 Environment Variables

### Backend (.env)
```
AZURE_OPENAI_API_KEY=your_key
AZURE_OPENAI_ENDPOINT=https://xxx.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT_NAME=gpt-35-turbo
JWT_SECRET=your_secret_key
```

### Frontend (.env.local)
```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

## 📚 API Endpoints (Rencana)

### Auth
- `POST /auth/register` - Register user
- `POST /auth/login` - Login user
- `POST /auth/refresh` - Refresh token

### CV Management
- `GET /cv` - Get all user's CVs
- `POST /cv` - Create new CV
- `GET /cv/:id` - Get CV detail
- `PUT /cv/:id` - Update CV
- `DELETE /cv/:id` - Delete CV
- `GET /cv/:id/export-pdf` - Export CV as PDF

### AI Analysis
- `POST /cv/:id/analyze` - Analyze CV with AI
- `POST /cv/:id/review` - Get AI review
- `POST /cv/:id/ats-score` - Calculate ATS score
- `POST /cv/:id/keywords` - Get keyword suggestions
- `POST /cv/:id/grammar` - Check grammar

## 🚀 Development Workflow

1. **Backend Development**
   - OpenAPI/Swagger di `http://localhost:8080/api/swagger-ui.html`
   - Jalankan tests sebelum commit
   - Follow REST conventions

2. **Frontend Development**
   - Hot reload enabled di `npm run dev`
   - TypeScript strict mode enabled
   - Component-based architecture

3. **Database Migrations**
   - JPA dengan `hibernate.ddl-auto=update`
   - Untuk production: gunakan Flyway atau Liquibase

## 📝 Next Steps

- [ ] Setup Authentication (JWT)
- [ ] Create CV Management APIs
- [ ] Integrate Azure OpenAI for AI features
- [ ] Build Frontend Pages
- [ ] Implement PDF Export
- [ ] Deploy to Cloud
- [ ] Setup CI/CD Pipeline

## 📄 License

MIT

---

**Ready to build something amazing?** 🎉 Let's go!
