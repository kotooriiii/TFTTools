# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

TFTTools is a toolkit for Teamfight Tactics (TFT) team building: a Spring Boot backend paired with a React + Vite frontend. Champion, trait, and emblem data is sourced live from Community Dragon and kept in sync with the current TFT set.

```
TFTTools/
├── backend/    Spring Boot REST API (Java 17, Maven)
└── frontend/   React + TypeScript UI (Vite)
```

Both the backend and frontend must run simultaneously for the app to work end to end. Frontend expects the backend on `http://localhost:8080`; CORS is wide open (`cors.allowed-origins: "*"` in `application.yml`) — this is a local dev tool, not a deployed service. The backend has real JWT-based user authentication on `/auth/**` (signup/login/me, backed by Postgres — see `com.tfttools.auth`), but all pre-existing tool endpoints remain intentionally unauthenticated; no per-endpoint auth was added to them.

`backend/docs/TFTEngine.pdf` has background on the composition-building engine design — read it before making non-trivial changes to `backend/src/main/java/com/tfttools/engine/`.

## Commands

### Backend (from repo root, then `backend/`)
```
docker compose up -d       # starts Postgres (used for user accounts); repo-root .env holds local-dev credentials
cd backend
mvn spring-boot:run       # run the API on :8080
mvn test                  # run all tests
mvn test -Dtest=ClassName # run a single test class
mvn compile                # compile only
```
Note: `TFTEngineTest` and `PrefixTrieTest` (under `backend/src/test`) are currently fully commented out — there is no active automated test coverage on the engine today.

### Frontend (from `frontend/`)
```
npm install
npm run dev          # dev server on :5173
npm run dev:network  # dev server bound to 0.0.0.0
npm run build        # tsc -b && vite build
npm run lint         # eslint .
npm run preview
```

## Architecture

### Backend: data flow

1. **`CommunityDragonWebClient`** fetches raw JSON from Community Dragon (`tft.communitydragon.url` in `application.yml`); `CommunityDragonDataService` caches it (`tft.communitydragon.cache.duration.hours`) and falls back to the bundled resource `backend/src/main/resources/com/tfttools/domain/repository/communitydragon/en_us.json` if the live fetch fails.
   - This is separate from `com.tfttools.auth`, which holds real Postgres-backed persistence (`User` JPA entity, `UserRepository extends JpaRepository`) for accounts — don't confuse `auth.repository.UserRepository` (Spring Data, DB-backed) with `TraitRepository`/`UnitRepository`/`EmblemRepository` above (in-memory, Community-Dragon-backed).
2. **`TFTSetContextService`** determines the current active TFT set number from that data.
3. Repositories (`TraitRepository` → `UnitRepository` → `EmblemRepository`, in that dependency order) build in-memory domain objects (`Trait`, `Unit`, `Emblem`) from the raw Community Dragon data at `@PostConstruct`.
4. **`DataRefreshService.refreshAllData()`** invalidates the cache and reloads everything in the same dependency order (traits → units → emblems → team planner codes) when a manual refresh is triggered.

### Backend: composition engine (`engine/` package)

This is the core "brain" of the app, invoked per-request (not a Spring singleton — a fresh `TFTEngine` is constructed per call in `CompositionService`):

- **Controller → Service → Adapter → Engine** pipeline: `ToolsController` receives a `HorizontalDTO`, `CompositionService` hands it to `EngineConfigurationAdapter`, which validates and resolves DTO strings against the repositories (unknown champion/trait names collect into a `ValidationContext` and throw together, not fail-fast) and produces an `EngineConfiguration`. `CompositionService` then builds a new `TFTEngine(config, unitRepository)` and calls `buildCompositions()`.
- **`TFTEngine.buildCompositions()`**: applies `EngineFilter`s (excluded units/traits) to the unit pool, then delegates to `EngineStrategyManager`.
- **Strategies** (`engine_strategy/`): pluggable composition-building algorithms implementing `TFTEngineStrategy` (greedy, beam search, exhaustive search exist; `EngineStrategyManager` currently only runs `TFTEngineGreedySearchStrategy` — the others are commented out but kept as alternatives). Each strategy builds one `EngineState` per composition and assembles a `Heuristic` from `WeightRegistry` (synergy lookahead, required traits/units weight, traits-added weight, emblem weight) plus a diversity tie-breaker against previously generated comps, then runs a `*SearchEngine` (`engine_search/`) to actually pick units.
- **`EngineTerminatorManager`** decides when a composition is "done" (e.g. `CompSizeEngineTerminator`).
- **`CompositionValidationManager`** (`engine/manager/`) re-validates finished compositions post-hoc against `EngineConfiguration` via a prioritized chain of `CompositionValidator`s (size, required/excluded units, required/excluded traits) — required validators reject the comp, non-required ones only warn.
- **`EngineStrategyManager.rankAndDeduplicate`** dedupes, ranks by number of activated traits, and truncates to `compSize` before returning.
- Results are mapped back to DTOs via `CompositionMapper`/`UnitMapper*`/`TraitMapper`/`EmblemMapper` for the HTTP response.

When adding a new engine capability, the typical touch points are: a new `EngineWeightScorer` (registered in `WeightRegistry`), a new `CompositionValidator` (registered in `CompositionValidationManager`), or a new `TFTEngineStrategy` (registered in `EngineStrategyManager`).

### Frontend

- Routing is centralized in `src/config/RouteConfig.ts` (`ROUTE_CONFIG` array) — add new tools here and they appear in the sidebar automatically (`getToolsForSidebar`). `MultiToolApp` renders the sidebar + routed tool.
- `src/tools/` holds top-level tool pages (`GraphCanvasTool`, `HorizontalCompositionGenerator`); `src/components/HorizontalCompositionGenerator/` holds that tool's search-box subcomponents.
- `src/services/` makes direct `fetch` calls to `http://localhost:8080` (no shared API client/base-URL config yet — new services should follow the existing pattern in `filterService.ts`/`searchService.ts`).
- `src/hooks/search/` contains generic + specialized (`EmblemSearchHook`, `ItemSearchHook`) search hooks used by the popup search panels in `src/components/search/`.
- Theming goes through `src/contexts/ThemeContext.tsx` + `src/themes/themeConfigurations.ts`; styling uses Tailwind CSS v4 (via `@tailwindcss/vite`) plus `App.css`/`index.css`.

# Project Conventions

## Branching
- Branch naming: `username_jiraticket_shortdesc`
    - Example: `kotooriiii_TFTTOOLS-1234_fix-memory-leak`
    - If no Jira ticket exists yet, use `na` in place of the ticket: `kotooriiii_na_fix-memory-leak`

## Commits
- Message style is not strict — write concise, meaningful commit messages.
- Always append the Jira ticket at the end of the message.
    - If no ticket exists, default to `N/A`.
    - Example: 
      ```
      Fix native memory leak in Jersey client pooling.
      
      JIRA: TFTTOOLS-1234
      ```
    - Example (no ticket):
      ```
      Fix native memory leak in Jersey client pooling.

      JIRA: N/A
      ```
## Git Safety Rules
- NEVER commit directly to `master` or `develop`.
- ALWAYS work on a feature branch following the naming convention above.
- ALWAYS open a PR for changes — never push directly to protected branches.
- NEVER merge any PR. The user merges manually after review.
- When a PR is ready, write a clear PR description (summary, why, testing done, linked Jira ticket) but stop there — do not merge.
