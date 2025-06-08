# 🚀 Module Boot MVP - 멀티모듈 스프링 부트 프로젝트

## 📌 프로젝트 개요

이 프로젝트는 **클린 아키텍처(Clean Architecture)** 원칙을 기반으로 설계된 엔터프라이즈급 스프링 부트 애플리케이션입니다. 멀티모듈 구조를 통해 각 계층을 명확히 분리하여 유지보수성과 확장성을
극대화했습니다.

## 👥 팀

- 프로젝트 리더: jklee
- 백엔드 개발: taehoon
- 프론트엔드 개발: jklee

### 🎯 프로젝트 목표

1. **관심사의 분리**: 각 모듈이 단일 책임을 가지도록 설계
2. **의존성 역전**: 비즈니스 로직이 인프라스트럭처에 의존하지 않도록 구성
3. **테스트 용이성**: 각 계층을 독립적으로 테스트 가능하도록 설계
4. **확장성**: 새로운 기능 추가 시 기존 코드 수정 최소화
5. **재사용성**: 공통 기능을 모듈화하여 재사용 가능하도록 구성

## 🏗️ 클린 아키텍처 레이어 구조

```
┌─────────────────────────────────────────────────────────┐
│                    Client Layer                         │
│                  (Client - REST/WebSocket)              │
├─────────────────────────────────────────────────────────┤
│                    Application Layer                    │
│                        (UseCase)                        │
├─────────────────────────────────────────────────────────┤
│                      Domain Layer                       │
│              (Board, User, Memory, etc.)                │
├─────────────────────────────────────────────────────────┤
│                  Infrastructure Layer                   │
│            (JPA, JOOQ, Valkey, DataSource)              │
└─────────────────────────────────────────────────────────┘
```

### 📁 프로젝트 디렉토리 구조

```
module-boot-mvp/
├── bootstrap/              # 애플리케이션 진입점 및 설정
├── client/                 # Client Layer (외부 인터페이스)
│   ├── rest/              # REST API 컨트롤러
│   └── websocket/         # WebSocket 컨트롤러
├── usecase/               # Application Layer (비즈니스 유스케이스)
├── domain/                # Domain Layer (핵심 비즈니스 로직)
│   ├── board/             # 게시판 도메인
│   ├── user/              # 사용자 도메인
│   ├── memory/            # 캐시 관련 도메인
│   ├── metadata/          # 메타데이터 도메인
│   ├── external/          # 외부 서비스 도메인
│   └── util/              # 도메인 유틸리티
├── infrastructure/        # Infrastructure Layer (기술적 구현)
│   ├── datasource/        # 데이터소스 설정
│   ├── jpa/               # JPA 리포지토리 구현체
│   ├── jooq/              # JOOQ 리포지토리 구현체
│   └── valkey/            # Redis/Valkey 구현체
├── security/              # Cross-Cutting Concerns (보안)
└── docker/                # Docker 관련 설정 파일
```

## 📊 클린 아키텍처 의존성 규칙

```
외부 → 내부 (단방향 의존성)

Security → Client(REST/WebSocket) → UseCase → Domain
                                       ↑          ↑
                                 Infrastructure   │
                                       └──────────┘

※ Domain은 어떤 외부 모듈에도 의존하지 않음
※ 외부 계층은 내부 계층에만 의존 가능
※ Security는 요청 필터 체인에서 Client보다 앞에 위치
```

### 📦 계층별 상세 설명

#### 1. **Security Layer** (`security/`)

- **책임**: 요청 필터링 및 보안 검증
- **위치**: 가장 외부 계층 (모든 요청이 먼저 거침)
- **구성**:
    - JWT 인증 필터
    - Spring Security 설정
    - CSRF/CORS 필터
    - 인증/인가 처리
- **의존성**: Domain 계층의 User 서비스 사용

#### 2. **Presentation Layer** (`client/`)

- **책임**: 외부와의 통신 인터페이스 제공
- **위치**: Security 필터를 통과한 후 요청 처리
- **구성**:
    - REST API 컨트롤러
    - WebSocket 컨트롤러
    - DTO 정의 및 변환
    - 요청/응답 매핑
- **의존성**: UseCase 계층의 서비스만 사용

#### 3. **Application Layer** (`usecase/`)

- **책임**: 애플리케이션의 비즈니스 유스케이스 구현
- **특징**:
    - 도메인 모델을 조합하여 비즈니스 플로우 구현
    - 트랜잭션 경계 설정
    - 도메인 서비스 조율
- **의존성**: Domain 계층의 모델과 서비스만 사용

#### 4. **Domain Layer** (`domain/`)

- **책임**: 핵심 비즈니스 규칙과 도메인 모델
- **특징**:
    - 순수한 비즈니스 로직 (POJO)
    - 외부 프레임워크에 독립적
    - 도메인 모델과 비즈니스 규칙 포함
- **의존성**: 없음 (완전히 독립적)

#### 5. **Infrastructure Layer** (`infrastructure/`)

- **책임**: 기술적 세부사항 구현
- **구성**:
    - 데이터베이스 접근 (JPA, JOOQ)
    - 캐시 구현 (Valkey/Redis)
    - 외부 시스템 연동
- **의존성**: Domain 계층의 인터페이스 구현

## 🛠️ 기술 스택

### Backend

- **Java 21** - 최신 LTS 버전 사용
- **Spring Boot 3.4.5** - 마이크로서비스 프레임워크
- **Spring Security** - 보안 프레임워크
- **Spring Data JPA** - ORM
- **JOOQ** - 타입 세이프 SQL 빌더
- **Gradle 8.12.1** - 빌드 도구

### Database & Cache

- **MySQL** - 관계형 데이터베이스
- **Valkey (Redis 호환)** - 인메모리 캐시

### Monitoring & Logging

- **Prometheus** - 메트릭 수집
- **Grafana** - 시각화 대시보드
- **Loki** - 로그 수집 시스템
- **Log4j2** - 로깅 프레임워크

### Development Tools

- **Docker & Docker Compose** - 컨테이너화
- **Lombok** - 보일러플레이트 코드 제거
- **Jackson** - JSON 처리

## 🚀 시작하기

### 사전 요구사항

- JDK 21 이상
- Docker & Docker Compose
- IntelliJ IDEA (권장) 또는 다른 Java IDE

### 개발 환경 실행 방법

#### 1. **저장소 클론**

```bash
git clone [repository-url]
cd module-boot-mvp
```

#### 2. **로컬 개발 인프라 실행**

```bash
# MySQL, Valkey(Redis), Redis Insight 실행
docker compose up -d

# 실행 상태 확인
docker compose ps

# 로그 확인 (문제 발생 시)
docker compose logs -f [service-name]
```

#### 3. **데이터베이스 초기화 확인**

- MySQL이 자동으로 초기 스키마를 생성합니다 (`docker/mysql-init/init-ddl.sql`)
- 기본 계정 정보:
    - Database: `test_db`
    - Username: `test_id`
    - Password: `test_pw`

#### 4. **Valkey(Redis) 초기화 확인**

- 역할 계층 구조가 자동으로 초기화됩니다 (`docker/valkey/init_script.redis`)
- 접속 정보:
    - Host: `localhost:6379`
    - Password: `test_pw`

#### 5. **개발 도구 접속**

- **애플리케이션**: http://localhost:8080
- **Redis Insight**: http://localhost:5540
    - 연결 추가: Host=`host.docker.internal`, Port=`6379`, Password=`test_pw`

### 전체 모니터링 스택 실행 (선택사항)

```bash
# Prometheus, Grafana, Loki 등 모니터링 도구 포함
docker compose up -d

# Grafana 접속: http://localhost:3000 (익명 접속 가능)
# Prometheus 접속: http://localhost:9090
```

### 개발 환경 종료

```bash
# 컨테이너 중지
docker compose stop

# 컨테이너 삭제 (데이터는 유지)
docker compose down

# 컨테이너 및 볼륨 모두 삭제 (초기화)
docker compose down -v
```

### 프로파일별 실행

```bash
# 로컬 환경 (기본값)
SPRING_PROFILES_ACTIVE=local ./gradlew :bootstrap:bootRun

# 테스트 환경
SPRING_PROFILES_ACTIVE=test ./gradlew :bootstrap:bootRun

# 운영 환경 (주의: 운영 DB 접속 정보 필요)
SPRING_PROFILES_ACTIVE=prod ./gradlew :bootstrap:bootRun
```

### 문제 해결

```bash
# 포트 충돌 시
lsof -i :3306  # MySQL
lsof -i :6379  # Redis
lsof -i :8080  # Spring Boot

# Docker 리소스 정리
docker system prune -a

# Gradle 캐시 정리
./gradlew clean
```

## 📊 주요 기능

### 1. 사용자 관리

- 사용자 등록/조회
- JWT 기반 인증
- 역할 기반 권한 관리
- 계층적 권한 구조 지원

### 2. 게시판 시스템

- 공지사항 작성/조회
- 댓글 기능
- 좋아요/싫어요 기능

### 3. 보안 기능

- JWT 토큰 인증
- Refresh Token 지원
- CSRF 보호
- CORS 설정

### 4. 캐싱

- Valkey를 활용한 캐싱
- 세션 관리
- 역할 계층 구조 캐싱

## 🔌 API 엔드포인트

### 인증 API

- `POST /api/sign/in` - 로그인
- `POST /api/auth/refresh` - 토큰 갱신

### 사용자 API

- `POST /api/user` - 사용자 생성
- `GET /api/user` - 사용자 목록 조회

### 게시판 API

- `GET /v1/notice` - 공지사항 조회
- `POST /v1/notice` - 공지사항 작성

## 📈 모니터링

### 접속 URL

- **Grafana**: http://localhost:3000
- **Prometheus**: http://localhost:9090
- **Redis Insight**: http://localhost:5540

### 헬스체크

- `GET /actuator/health` - 애플리케이션 상태 확인
- `GET /actuator/prometheus` - Prometheus 메트릭

## 🏛️ 클린 아키텍처 원칙

### 1. 의존성 규칙

- 의존성은 항상 외부에서 내부로 향함
- Domain 계층은 어떤 것에도 의존하지 않음
- 각 계층은 자신보다 안쪽 계층만 알 수 있음

### 2. 계층별 책임

- **Presentation**: 사용자 인터페이스와 데이터 표현
- **UseCase**: 특정 비즈니스 시나리오에 따른 도메인 서비스 조율 및 조합
- **Domain**: 핵심 비즈니스 규칙과 도메인 모델
- **Infrastructure**: 프레임워크와 도구들

### 3. 장점

- **독립성**: 각 계층이 독립적으로 개발/테스트 가능
- **유연성**: 기술 변경 시 Infrastructure만 수정 (예: JPA→MongoDB, Valkey→Hazelcast)하고 비즈니스 로직은 유지
- **테스트 용이성**: 비즈니스 로직을 격리하여 테스트
- **유지보수성**: 명확한 책임 분리로 수정 영향도 최소화

## 🧪 테스트 전략

### 1. 단위 테스트

- Domain 계층: 순수 비즈니스 로직 테스트
- UseCase 계층: 모킹을 통한 유스케이스 테스트

### 2. 통합 테스트

- Repository 테스트: @DataJpaTest
- API 테스트: @WebMvcTest

### 3. E2E 테스트

- Playwright를 활용한 UI 자동화 테스트
- 실제 사용자 시나리오 기반 테스트

## 📝 라이센스

이 프로젝트는 MIT 라이센스를 따릅니다.

---

**Note**: 이 프로젝트는 지속적으로 개발 중이며, 기능과 문서는 계속 업데이트될 예정입니다.
