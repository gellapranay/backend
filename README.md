# Antarmarg Backend (Last-Meter Intelligence Engine)

Spring Boot 3.3.4 REST API & algorithmic routing engine powered by Java 21, PostgreSQL, and Spring Security with Stateless JWT.

## Tech Stack
- **Framework**: Spring Boot 3.3.4, Java 21
- **Database**: PostgreSQL (PostGIS-ready)
- **Security**: Spring Security 6, JJWT 0.12.6, BCrypt
- **Routing Engine**: Custom Dijkstra graph algorithm with multi-gate evaluation, accessibility weights, and real-time obstacle bypass

## Database Configuration
Configured for Render PostgreSQL:
- **Host**: `dpg-db4em6vlk1mc73fps0ug-a` (Internal) / `dpg-db4em6vlk1mc73fps0ug-a.singapore-postgres.render.com:5432` (External)
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
   - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://dpg-db4em6vlk1mc73fps0ug-a:5432/tt_wbyu`
   - `SPRING_DATASOURCE_USERNAME`: `tt_wbyu_user`
   - `SPRING_DATASOURCE_PASSWORD`: `82mYVNCwvXBDTYrsTVk505bGgqaNSPi4`
   - `JWT_SECRET`: `AntarmargLastMeterIntelligenceSecretKey2026SecureTokenStringShouldBeLongEnoughForHS256Algorithm`
5. Click **Create Web Service**.
