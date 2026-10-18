- [x] Audit current SEO surface (`index.html`, meta tags, titles, robots, sitemap)
- [x] Inspect app routes/pages for indexable content and duplicate titles
- [x] Add or improve page-level `<title>` and description tags
- [x] Add Open Graph and Twitter card metadata
- [x] Add canonical URLs where needed
- [x] Add structured data (JSON-LD) for key pages if applicable
- [x] Verify `robots.txt` and sitemap generation
- [x] Check mobile, performance, and image delivery for SEO impact
- [ ] Validate with browser view source and search preview checks

## GEO
- [x] Strengthen homepage wording for AI understanding
- [x] Add `WebApplication` JSON-LD if appropriate
- [x] Add an FAQ section for common user questions
- [x] Clarify service description in `README.md`
- [x] Keep a concise GEO summary document up to date
- [x] Create a dedicated FAQ page and link to it from the homepage
- [x] Add `FAQPage` JSON-LD to the FAQ page

## Google Places
- [x] Add `source=google` backed by Google Places Nearby Search (New)
- [x] Add Google API key configuration and map it into the search source
- [x] Add unit tests for Google response mapping and service fallback
- [x] Add an integration test that runs only when `ANYMEAL_GOOGLE_API_KEY` is set
- [x] Run Google Places integration test with the key loaded from `.env`
- [x] Increase Google candidates to 20, rank by distance, and jitter the search center within 500m
- [x] Verify Google request payload and full test suite
- [x] Choose Google as the default source for non-Korean language environments
- [x] Verify explicit `source` still overrides language-based default
- [x] Choose Google as the default source for coordinates outside South Korea

## English page
- [x] Add a separate English landing page
- [x] Route non-Korean browser environments to the English page
- [x] Keep Korean browser environments on the existing Korean page
- [x] Verify routing and static page behavior with tests
- [x] Make the English page the default root and move Korean content to `/ko.html`
- [x] Reconcile canonical, `hreflang`, and sitemap entries with the new language URLs
- [x] Remove or redirect the old `/en.html` alias so it does not compete with the root URL

## Google request language
- [x] Extract the browser's preferred language from `Accept-Language`
- [x] Pass the preferred language through restaurant source selection
- [x] Send Google Places `languageCode` with Nearby Search requests
- [x] Verify controller, service, and Google request behavior with tests
- [x] Default missing `Accept-Language` to English to match the root page

## New York location verification
- [x] Verify New York coordinates choose Google even with Korean browser language
- [x] Run the full Gradle test suite
- [x] Confirm whether the Google integration test produced a real API result or was skipped by missing credentials

## English page typography
- [x] Compare English page font scale against the Korean page
- [x] Adjust English-only typography so longer English copy fits better
- [x] Verify the static page renders without breaking resource processing
- [x] Reduce the supporting "for your" and "today?" text around the meal emphasis

## Codebase audit 2026-06-01
- [x] Map the project structure and runtime dependencies
- [x] Inspect controller, service, and search-source behavior for correctness risks
- [x] Inspect frontend/static resources for maintainability and user-facing risks
- [x] Review existing tests and run the relevant test suite
- [x] Summarize prioritized improvement candidates with evidence

## Root page split to en.html
- [x] Route the root English experience through `/en.html`
- [x] Redirect direct `/index.html` access to `/en.html`
- [x] Update language alternates, canonical URLs, and sitemap entries
- [x] Update routing tests for the new URL ownership
- [x] Run the Gradle test suite

## Language-specific FAQ pages
- [x] Add an English FAQ page
- [x] Add a Korean FAQ page
- [x] Link each main page to its matching FAQ
- [x] Update sitemap entries for the language-specific FAQ pages
- [x] Run the Gradle test suite

## Dependency security update 2026-10-17
- [x] Inspect declared and resolved Gradle dependencies
- [x] Check resolved versions against current security advisories
- [x] Apply the smallest safe dependency updates
- [x] Run the Gradle test suite and summarize any remaining findings

## Gradle wrapper update 2026-10-17
- [x] Check the current wrapper version and official stable release
- [x] Update the wrapper distribution URL to Gradle 9.8.1
- [x] Run the Gradle test suite

## Spring Boot 4 upgrade 2026-10-17
- [x] Confirm the current stable Spring Boot 4 release and migration requirements
- [x] Upgrade the Spring Boot plugin and align dependencies/starter names
- [x] Resolve compilation and application/test failures caused by the major upgrade
- [x] Run the Gradle test suite and record the result

## Security audit and remediation 2026-10-17
- [x] Inspect resolved Gradle runtime and test dependencies against current vulnerability advisories
- [x] Review application code and configuration for concrete security weaknesses
- [x] Apply minimal fixes for confirmed findings
- [x] Run the Gradle test suite and build after changes
- [x] Record unresolved risks and verification results

## Dependabot follow-up 2026-10-17
- [x] Upgrade Jackson 3 to 3.1.7 for open Dependabot advisories
- [x] Inspect Gradle plugin classpath reported by GitHub's dependency SBOM
- [x] Override vulnerable plugin transitive Jackson and Commons Lang versions
- [x] Run the Gradle build
- [x] Push plugin classpath remediation and verify GitHub alerts/SBOM

## 중복 코드 정리 2026-10-18
- [x] 한국어/영어 홈페이지의 공통 브라우저 동작을 확인하고 공유 파일로 추출
- [x] 서버의 중복 언어 판별을 공통 로직으로 통합
- [x] 관련 기존 동작과 변경 범위를 검토

## GraalVM 네이티브 빌드 환경
- [x] GraalVM, Native Image, 호스트 C 컴파일러 확인
- [x] Gradle GraalVM Native Build Tools 플러그인 추가
- [x] Gradle 작업 확인 및 네이티브 실행 파일 빌드
- [x] 빌드 산출물과 실행 명령 기록
- [x] 현재 x86_64 GraalVM과 ARM64 호스트의 아키텍처 차이 기록
