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

## Google request language
- [x] Extract the browser's preferred language from `Accept-Language`
- [x] Pass the preferred language through restaurant source selection
- [x] Send Google Places `languageCode` with Nearby Search requests
- [x] Verify controller, service, and Google request behavior with tests

## New York location verification
- [x] Verify New York coordinates choose Google even with Korean browser language
- [x] Run the full Gradle test suite
- [x] Confirm whether the Google integration test produced a real API result or was skipped by missing credentials

## English page typography
- [x] Compare English page font scale against the Korean page
- [x] Adjust English-only typography so longer English copy fits better
- [x] Verify the static page renders without breaking resource processing
