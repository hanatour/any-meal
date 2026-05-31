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
