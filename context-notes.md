# Context notes

- Goal is SEO improvement planning for the current app, not implementation yet.
- Current repo appears to be a Gradle app with static resources under `src/main/resources/static`.
- Likely SEO entry points include `index.html`, route-specific HTML, and any server-rendered page metadata.
- Keep the plan minimal and focused on measurable SEO gains.
- Added basic metadata to the homepage, then created `robots.txt` and `sitemap.xml` for crawlability.
- Canonical and sitemap now use the confirmed production domain `https://anymeal.net/`.
- Verification still needed after deployment via source inspection and shared-link previews.
- GEO work should focus on clearer service wording, `WebApplication` schema, and a small FAQ section rather than expanding behavior.
- GEO items are now implemented in the homepage and README, and summarized in `docs/geo-summary.md`.
- FAQ was split into a dedicated page (`/faq.html`) to improve discoverability and make the homepage lighter.
