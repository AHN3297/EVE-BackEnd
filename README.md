# 🔌 Evision

전기차 충전소 관리 및 공유 서비스 플랫폼

## 📋 프로젝트 소개

Evision은 전기차 사용자들을 위한 종합 서비스 플랫폼입니다. 전기차 충전소 정보 조회, 차량 관리, 커뮤니티 기능을 제공하여 전기차 사용자들의 편리한 이용을 지원합니다.

### 주요 특징
- 🔐 JWT 기반 인증/인가 시스템
- 📍 전기차 충전소 정보 조회 및 관리
- 🚗 전기차 차량 등록 및 관리
- 💬 게시판 및 댓글 커뮤니티
- 📝 충전소 리뷰 및 신고 기능
- 🔒 역할 기반 접근 제어 (USER, OPERATOR, ADMIN)

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **Build Tool**: Gradle
- **Database**: Oracle Database (JDBC)
- **ORM**: MyBatis 3.0.5
- **Security**: Spring Security
- **Authentication**: JWT (JSON Web Token)
- **Validation**: Spring Boot Validation
- **Lombok**: 코드 간소화

### 주요 라이브러리
- `spring-boot-starter-web`: RESTful API 개발
- `spring-boot-starter-security`: 보안 및 인증
- `spring-boot-starter-jdbc`: 데이터베이스 연결
- `mybatis-spring-boot-starter`: MyBatis 통합
- `jjwt`: JWT 토큰 생성 및 검증
- `ojdbc11`: Oracle JDBC 드라이버

## ✨ 기능 리스트

### 🔑 인증/인가
- [o] 회원가입
- [o] 로그인/로그아웃
- [o] JWT 토큰 기반 인증
- [o] Refresh Token 관리
- [o] 비밀번호 변경
- [o] 회원정보 수정/삭제
- [o] 운전면허 인증

### 👥 회원 관리
- [o] 회원 정보 조회
- [o] 회원 권한 관리 (USER, OPERATOR, ADMIN)
- [o] 관리자 회원 관리
- [o] 비밀번호 검증
<<<<<<< HEAD
=======

### 🗣️ 공지사항
- [o] 공지 목록 조회(페이징)
- [o] 공지 상세 조회
- [o] 공지 작성/수정/삭제(관리자 및 운영자 전용 페이지 한정 구현)
- [o] 섬네일(대표 이미지) 설정 기능
- [o] 파일 업로드(이미지, 비이미지 불문)
>>>>>>> 524efb21947bac4f915601fc945df8d65a188c3c

### 📝 게시판
- [o] 게시글 작성/수정/삭제
- [o] 게시글 목록 조회 (페이징)
- [o] 게시글 상세 조회
- [o] 조회수 증가
- [o] 이미지 파일 업로드
- [o] 관리자 게시물 관리

### 💬 댓글
- [o] 댓글 작성
- [o] 댓글 조회
- [o] 댓글 삭제

### 🚗 차량 관리
- [o] 차량 등록 (이미지 첨부)
- [o] 차량 목록 조회 (페이징)
- [o] 차량 상세 조회
- [o] 차량 정보 수정
- [o] 차량 삭제

### 🔌 충전소 관리
- [o] 충전소 검색
- [o] 충전소 목록 조회
- [o] 충전소 상세 조회
- [o] 충전소 등록/삭제
- [o] 충전소 리뷰 작성/수정/삭제
- [o] 충전소별 리뷰 조회
- [o] 외부 API 연동 (충전소 정보)

### 📢 신고 기능
- [o] 신고 등록
- [o] 신고 목록 조회
- [o] 신고 상태 변경
- [o] 내 신고 조회
- [o] 키워드 검색

### 📅 예약 (구현 예정)
- [ ] 충전소 예약 기능

## 📦 설치 및 실행 방법

### 필수 요구사항
- Java 21 이상
- Oracle Database
- Gradle 7.x 이상

### 1. 저장소 클론
```bash
git clone <repository-url>
cd semi-workspace/evision
```

### 2. 데이터베이스 설정
`src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 수정합니다.

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@[호스트]:[포트]:[SID]
    username: [회원번호]
    password: [비밀번호]
```

### 3. JWT Secret Key 설정
`application.yml`에서 JWT Secret Key를 설정합니다.

```yaml
jwt:
  secret: [JWT_SECRET_KEY]
  expiration: 86400000  # 24시간 (밀리초)
```

### 4. 빌드 및 실행

#### Windows
```bash
gradlew.bat build
gradlew.bat bootRun
```

#### Linux/Mac
```bash
./gradlew build
./gradlew bootRun
```

### 5. 서버 접속
서버가 실행되면 다음 주소로 접속할 수 있습니다:
- **서버 포트**: `http://localhost:8081`
- **API 엔드포인트**: `http://localhost:8081/api/...`

## 📁 폴더 구조

```
evision/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kh/evision/
│   │   │       ├── api/              # 외부 API 연동
│   │   │       ├── auth/              # 인증/인가
│   │   │       ├── board/             # 게시판
│   │   │       ├── car/               # 차량 관리
│   │   │       ├── comment/           # 댓글
│   │   │       ├── configuration/     # 설정 (Security, Filter)
│   │   │       ├── exception/         # 예외 처리
│   │   │       ├── file/              # 파일 업로드
│   │   │       ├── member/            # 회원 관리
│   │   │       ├── notice/            # 공지사항
│   │   │       ├── report/            # 신고
│   │   │       ├── reserve/           # 예약
│   │   │       ├── station/           # 충전소 관리
│   │   │       ├── token/             # JWT 토큰 관리
│   │   │       └── util/              # 유틸리티
│   │   └── resources/
│   │       ├── application.yml        # 설정 파일
│   │       └── mapper/                # MyBatis Mapper XML
│   └── test/                          # 테스트 코드
├── build.gradle                        # Gradle 빌드 설정
├── settings.gradle                     # Gradle 프로젝트 설정
├── gradlew.bat                         # Windows Gradle Wrapper
└── uploads/                            # 업로드된 파일 저장소
```

### 주요 패키지 설명

- **api**: 외부 충전소 정보 API 연동
- **auth**: 로그인, 인증 관련 기능
- **board**: 게시판 CRUD 기능
- **car**: 전기차 차량 관리
- **comment**: 게시글 댓글 기능
- **configuration**: Spring Security, CORS, JWT Filter 설정
- **exception**: 전역 예외 처리 및 커스텀 예외
- **file**: 파일/이미지 업로드 서비스
- **member**: 회원 관리 (가입, 수정, 삭제, 권한 관리)
- **report**: 신고 기능
- **station**: 충전소 정보 관리 및 리뷰
- **token**: JWT 토큰 생성 및 검증
- **util**: 페이징, 페이지 정보 유틸리티

## 🖥 주요 화면 설명

### API 엔드포인트

#### 인증
- `POST /auth/login` - 로그인
- `POST /member/join` - 회원가입

#### 회원 관리
- `GET /member/info` - 내 정보 조회
- `PUT /member/info/update` - 회원정보 수정
- `PUT /member/changePwd` - 비밀번호 변경
- `DELETE /member/info/delete` - 회원 탈퇴
- `POST /member/infoLicense` - 운전면허 인증

#### 게시판
- `GET /boards` - 게시글 목록 조회
- `GET /boards/{boardNo}` - 게시글 상세 조회
- `POST /boards` - 게시글 작성
- `PUT /boards/{boardNo}` - 게시글 수정
- `DELETE /boards/{boardNo}` - 게시글 삭제

#### 댓글
- `GET /comments?boardNo={boardNo}` - 댓글 조회
- `POST /comments` - 댓글 작성
- `DELETE /comments/{commentNo}` - 댓글 삭제

#### 차량 관리
- `GET /cars` - 차량 목록 조회
- `GET /cars/{carNo}` - 차량 상세 조회
- `POST /cars` - 차량 등록
- `PUT /cars/{carNo}` - 차량 정보 수정
- `DELETE /cars/{carNo}` - 차량 삭제

#### 충전소 관리
- `GET /station` - 충전소 목록 조회
- `GET /station/{stationNo}` - 충전소 상세 조회
- `GET /station/search?keyword={keyword}` - 충전소 검색
- `POST /station` - 충전소 등록
- `DELETE /station?stationNo={stationNo}` - 충전소 삭제
- `GET /station/reviews/{stationNo}` - 충전소 리뷰 조회
- `POST /station/reviews` - 리뷰 작성
- `PUT /station/reviews` - 리뷰 수정
- `DELETE /station/reviews/{reviewNo}` - 리뷰 삭제

#### 신고
- `GET /reports` - 신고 목록 조회 (관리자)
- `GET /reports?memberNo={memberNo}` - 내 신고 조회
- `GET /reports?keyword={keyword}` - 신고 검색
- `POST /reports` - 신고 등록
- `PUT /reports` - 신고 상태 변경
- `DELETE /reports?reportNo={reportNo}` - 신고 삭제

#### 외부 API
- `GET /api/station?pageNo={pageNo}` - 외부 충전소 정보 API 호출

## 💻 개발 환경 정보

### 개발 환경
- **OS**: Windows 10
- **IDE**: IntelliJ IDEA / Eclipse
- **Java Version**: 21
- **Spring Boot Version**: 3.5.7
- **Database**: Oracle Database XE
- **Build Tool**: Gradle

### 서버 설정
- **Port**: 8081
- **CORS**: `http://localhost:5173` 허용
- **File Upload**: 최대 100MB
- **Session**: Stateless (JWT 기반)

### 데이터베이스
- **Driver**: Oracle JDBC Driver (ojdbc11)
- **Connection Pool**: Spring Boot Default (HikariCP)

### 보안 설정
- **Password Encoding**: BCrypt
- **JWT Expiration**: 24시간 (86400000ms)
- **Session Policy**: STATELESS

## 📝 추가 정보

### 파일 업로드
- 업로드된 파일은 `uploads/` 디렉토리에 저장됩니다.
- 파일명은 자동으로 변경되어 저장됩니다.
- 이미지 파일과 일반 파일을 구분하여 처리합니다.

### 권한 관리
- **USER**: 일반 사용자 권한
- **OPERATOR**: 운영자 권한 (회원 관리, 게시물 관리)
- **ADMIN**: 관리자 권한 (모든 기능 접근 가능)

### 예외 처리
- 전역 예외 핸들러(`GlobalExceptionHandler`)를 통해 일관된 에러 응답 제공
- 커스텀 예외 클래스를 통한 세밀한 예외 처리

## 📄 라이선스

이 프로젝트는 교육용 프로젝트입니다.

## 👥 기여자

프로젝트 개발팀

---

**문의사항이나 버그 리포트는 이슈로 등록해주세요.**

