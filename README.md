# Antarmarg Backend (Last-Meter Intelligence Engine)

Spring Boot 3.3.4 REST API & algorithmic routing engine powered by Java 21, PostgreSQL, and Spring Security with Stateless JWT.

## Tech Stack
- **Framework**: Spring Boot 3.3.4, Java 21
- **Database**: PostgreSQL (PostGIS-ready)
- **Security**: Spring Security 6, JJWT 0.12.6, BCrypt
- **Routing Engine**: Custom Dijkstra graph algorithm with multi-gate evaluation, accessibility weights, and real-time obstacle bypass

## Database Configuration
Configured for Render PostgreSQL:
- **Host**: use the External Database URL shown in the Render PostgreSQL dashboard when the web service cannot resolve the internal hostname
- **Database**: `tt_wbyu`
- **User**: `tt_wbyu_user`

## Local Development
```bash
./mvnw spring-boot:run
```

## Docker Build & Run
```bash
docker build -t antarmarg-backend .
docker run -p 8080:8080 antarmarg-backend
```

## Deploy on Render (Web Service)
1. In Render Dashboard, click **New +** > **Web Service**.
2. Connect repository: `https://github.com/gellapranay/backend.git`.
3. Choose **Docker** as Runtime (Render automatically detects the multi-stage `Dockerfile`).
4. Set Environment Variables:
   - `SPRING_DATASOURCE_URL`: the database's External Database URL, with `?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME`: `tt_wbyu_user`
   - `SPRING_DATASOURCE_PASSWORD`: set from the PostgreSQL dashboard; do not commit it
   - `JWT_SECRET`: generate a new secret of at least 32 characters and set it in the Render dashboard
   - `SUPER_ADMIN_PASSWORD`: set a strong initial password for the seeded super-admin account
5. Click **Create Web Service**.

Rotate the database password and JWT secret if previously deployed from a revision that contained committed credentials.
