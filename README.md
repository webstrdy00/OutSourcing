배달 어플 프로젝트의 README에 포함할 **API 명세서**, **ERD 설명**, **프로젝트 설명**을 아래와 같이 작성할 수 있습니다.

---

# 배달 어플 프로젝트

## 프로젝트 설명

이 프로젝트는 배달 애플리케이션으로, 고객은 레스토랑에서 메뉴를 선택하여 배달 주문을 하고, 레스토랑 소유자는 해당 주문을 확인하고 처리할 수 있습니다. 주요 기능으로는 주문 생성, 주문 상태 변경, 리뷰 작성 등이 포함되어 있습니다. 이 프로젝트는 **User**, **Restaurant**, **Order**, **Menu**, **Review**와 같은 도메인으로 나뉘며, 각 도메인이 중요한 역할을 수행합니다.

### 도메인 역할 설명
- **User (유저)**: 고객 또는 레스토랑 소유자로, 각 사용자는 주문을 생성하거나 레스토랑을 소유하며, 주문에 대한 리뷰를 작성할 수 있습니다.
- **Restaurant (레스토랑)**: 배달을 제공하는 레스토랑으로, 메뉴를 통해 고객에게 배달 서비스를 제공합니다. 레스토랑은 소유자가 관리하며, 운영 시간과 최소 배달 금액을 설정할 수 있습니다.
- **Order (주문)**: 고객이 레스토랑에서 메뉴를 선택하여 주문을 생성하면, 해당 주문은 레스토랑 소유자에게 전달되어 상태가 변경됩니다. 주문의 상태는 '주문 접수', '조리 중', '배달 중', '배달 완료' 등으로 변환됩니다.
- **Menu (메뉴)**: 레스토랑에서 제공하는 개별 메뉴로, 고객은 하나의 메뉴를 선택하여 주문을 할 수 있습니다. 메뉴는 가격과 설명을 포함합니다.
- **Review (리뷰)**: 주문이 완료된 후 고객은 리뷰를 작성할 수 있습니다. 리뷰에는 평점과 내용을 남겨 레스토랑과 메뉴에 대한 평가를 제공합니다.

## ERD 설명

프로젝트의 ERD(Entity-Relationship Diagram)는 각 엔티티 간의 관계를 시각적으로 표현한 다이어그램입니다. 아래는 주요 도메인 간의 관계 설명입니다:

- **User와 Restaurant**: 하나의 유저는 여러 개의 레스토랑을 소유할 수 있습니다 (1:N 관계).
- **User와 Order**: 한 명의 유저는 여러 개의 주문을 할 수 있습니다 (1:N 관계).
- **Restaurant과 Order**: 하나의 레스토랑은 여러 개의 주문을 받을 수 있습니다 (1:N 관계).
- **Order와 Menu**: 하나의 주문은 단 하나의 메뉴를 포함하며, 메뉴는 여러 주문에서 사용될 수 있습니다 (N:1 관계).
- **Order와 Review**: 하나의 주문에 대해 하나의 리뷰만 작성할 수 있으며, 리뷰는 특정 주문에 종속됩니다 (1:1 관계).
- **User와 Review**: 한 명의 유저는 여러 개의 리뷰를 작성할 수 있습니다 (1:N 관계).

### ERD 다이어그램
```plaintext
User -----< Restaurant
User -----< Order >------ Restaurant
Order ----> Menu
Order ----> Review
User -----< Review
```

## API 명세서

프로젝트에서 제공하는 REST API에 대한 명세입니다. 각 API는 유저, 레스토랑, 주문, 리뷰 관련 기능을 제공합니다.

### 1. 사용자 관련 API

- **공통 URL**: '/delivery/'

#### 회원가입
- **URL**: `/users/signup`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "role": "USER" // 또는 "OWNER"
  }
  ```
- **Response**:
  - 성공 시: HTTP 201 Created, 사용자 정보

#### 로그인
- **URL**: `/users/login`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response**:
  - 성공 시: JWT 토큰 발급 (HTTP 200 OK)

### 2. 레스토랑 관련 API

#### 레스토랑 목록 조회
- **URL**: `/restaurants`
- **Method**: `GET`
- **Description**: 모든 레스토랑 목록을 조회합니다.
- **Response**:
  - 성공 시: 레스토랑 목록 (HTTP 200 OK)

#### 레스토랑 상세 조회
- **URL**: `/restaurants/{restaurantId}`
- **Method**: `GET`
- **Description**: 특정 레스토랑의 상세 정보를 조회합니다.
- **Response**:
  - 성공 시: 레스토랑 정보 (HTTP 200 OK)

### 3. 주문 관련 API

#### 주문 생성
- **URL**: `/orders`
- **Method**: `POST`
- **Description**: 사용자가 레스토랑 메뉴를 선택하여 주문을 생성합니다.
- **Request Body**:
  ```json
  {
    "restaurantId": 1,
    "menuId": 1
  }
  ```
- **Response**:
  - 성공 시: 주문 정보 (HTTP 201 Created)

#### 주문 상태 변경
- **URL**: `/orders/{orderId}/status`
- **Method**: `PATCH`
- **Description**: 레스토랑 소유자가 주문 상태를 변경합니다.
- **Request Body**:
  ```json
  {
    "status": "COOKING" // 또는 "DELIVERED"
  }
  ```
- **Response**:
  - 성공 시: 상태 변경된 주문 정보 (HTTP 200 OK)

### 4. 리뷰 관련 API

#### 리뷰 작성
- **URL**: `/reviews`
- **Method**: `POST`
- **Description**: 주문 완료 후 리뷰를 작성합니다.
- **Request Body**:
  ```json
  {
    "orderId": 1,
    "rating": 5,
    "content": "음식이 맛있었어요!"
  }
  ```
- **Response**:
  - 성공 시: 리뷰 정보 (HTTP 201 Created)

#### 리뷰 조회
- **URL**: `/reviews/{reviewId}`
- **Method**: `GET`
- **Description**: 특정 리뷰를 조회합니다.
- **Response**:
  - 성공 시: 리뷰 정보 (HTTP 200 OK)

## 프로젝트 사용 기술 스택

- **언어**: Java
- **프레임워크**: Spring Boot
- **데이터베이스**: MySQL
- **ORM**: JPA (Java Persistence API)
- **보안**: Spring Security, JWT (JSON Web Token)
- **테스트**: JUnit, Mockito

---

이 구조를 통해 프로젝트의 전반적인 개요, 엔티티 간의 관계, 각 기능에 대한 API 명세 및 주요 기술 스택을 상세히 설명할 수 있습니다.
