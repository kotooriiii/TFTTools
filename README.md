# TFTTools

A toolkit for Teamfight Tactics (TFT) team building, built as a Spring Boot backend paired with a React + Vite frontend. Champion, trait, and emblem data is sourced from Community Dragon and kept in sync with the current TFT set.

## Project Structure

```
TFTTools/
├── backend/    Spring Boot REST API (Java 17, Maven)
└── frontend/   React + TypeScript UI (Vite)
```

## Backend

The backend exposes composition-building and search endpoints backed by an in-memory engine that reasons about traits, units, and emblems pulled from Community Dragon.

- **Stack**: Java 17, Spring Boot 3.2, Maven, PostgreSQL (user accounts)
- **Run it**:
  ```
  docker compose up -d   # starts Postgres (used for user accounts)
  cd backend
  mvn spring-boot:run
  ```
  The API starts on `http://localhost:8080`. A `.env` file at the repo root holds local-dev-only default credentials for `docker-compose.yml` and `application.yml`.

### Notable endpoints

| Endpoint | Method | Description |
|---|---|---|
| `/tools/horizontal` | `POST` | Generates horizontal team compositions given required traits, champions, and board constraints |
| `/auth/signup` | `POST` | Creates a new user account |
| `/auth/login` | `POST` | Logs in and returns a JWT |
| `/auth/me` | `GET` | Returns the current user (requires `Authorization: Bearer <token>`) |

## Frontend

- **Stack**: React 19, TypeScript, Vite, Tailwind CSS, Framer Motion
- **Run it**:
  ```
  cd frontend
  npm install
  npm run dev
  ```
  The app starts on `http://localhost:5173` and expects the backend to be running on `http://localhost:8080`.

### Tools

- **Graph Canvas** — interactive graph visualization and editing for units and traits
- **Horizontal Comp Generator** — generates optimal horizontal compositions from filter criteria (required traits/champions, tactician level, gold, emblems, luck)

## Development

Both the backend and frontend need to be running simultaneously for the app to be functional end to end. See `backend/docs/TFTEngine.pdf` for background on the composition-building engine.
