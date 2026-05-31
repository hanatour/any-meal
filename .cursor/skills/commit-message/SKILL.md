---
name: commit-message
description: Writes concise Git commit messages from staged or provided diffs. Use when the user asks for commit messages, 커밋 메시지, or before committing; follows Conventional Commits with optional Korean summary.
---

# 커밋 메시지 작성

## 원칙

1. **한 줄 제목** (50자 내외 권장, 최대 72자)
2. **Conventional Commits** 형식: `<type>(<scope>): <subject>`
3. 필요 시 **본문** 한두 문단 (72자 줄바꿈), **푸터**에 이슈 번호(`Closes #123`, `Refs #456`)
4. 제목 끝에 마침표 없음, 명령형/현재형 (“추가함” 대신 “추가”)

## type

| type | 용도 |
|------|------|
| feat | 새 기능 |
| fix | 버그 수정 |
| docs | 문서만 |
| style | 포맷·세미콜론 등 (동작 변화 없음) |
| refactor | 리팩터링 |
| test | 테스트 추가·수정 |
| chore | 빌드·설정·의존성 등 |
| perf | 성능 개선 |
| ci | CI 설정 |

`scope`는 선택. 예: `feat(api):`, `fix(naver):`, `chore(gradle):`

## 출력 형식

사용자가 “메시지만” 요청하면 **제목 한 줄**만 제안.

“자세히” 또는 변경이 크면:

```
<type>(<scope>): <subject>

<body: 무엇을 왜 바꿨는지, bullet 가능>

Refs #이슈번호
```

## 예시

- `feat: 네이버 지역검색 소스 및 지도 연동`
- `fix: 빈 추천 시 204 응답 처리`
- `docs: README에 source 쿼리 파라미터 설명 추가`
- `chore: Cursor CLI 권한 설정(.cursor/cli.json) 추가`

영문 팀이면 subject만 영어로:

- `feat(api): add Naver local search source`

## 절차

1. `git diff --staged` 또는 사용자가 준 변경 요약을 본다.
2. 변경의 **핵심 한 가지**에 맞는 type·scope를 고른다 (여러 목적이면 가장 큰 것 하나, 나머지는 본문).
3. 제목은 **무엇을** 했는지 명사구로 압축한다.
4. 비밀번호·키·토큰이 메시지에 들어가지 않게 한다.
