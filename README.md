# Node.js Back-end Refactoring Project

## 개요
### 프로젝트 명 - Node.js 백엔드 리펙토링 프로젝트

### 배경
#### 이전 현장 실습 프로젝트에서 진행 되었던 Hoonsletter Project의 Back-end를 Node.js로 구현을 했습니다. <br /> 하지만 이는 기초적인 Back-end 지식 없이 구현을 하게 되어 구현 이상을 바라보는 코드를 생각하지 못했습니다. <br />따라서 현재 Node.js로 구현된 백엔드 시스템은 여러 문제점이 있습니다.
- **코드 구조의 일관성 부족**: 코드 구조가 일관성이 없고 각 모듈의 목적이 명확하지 않아 가독성이 떨어지고 유지 보수가 불가능한 수준입니다.
- **계층 디렉터리 설계**: 백엔드 설계시 계층(Layer)에 따른 디렉터리 구조를 설계하지 못해 확장성에 제한이 있습니다. 
- **요구사항 정의 부족**: 요구사항을 완벽히 정의하지 못했습니다. 따라서 RESTful API를 완벽하게 설계하고 구현하지 못해 기능성에 제한이 있습니다.  
- **데이터베이스 설계 부족**: 데이터베이스 스키마를 확실히 설계하지 못해 팀원과의 협업 도중 추가적으로 데이터를 조정(Mapping)하는 과정이 발생했습니다.

### 개선 방향
#### 기존 프로젝트의 문제점에서 개선해야 할 여러 부분들이 있습니다.
- **구조화된 코드 구현**: Spring Boot로 리팩토링하여 일관성 있는 코드 구조를 구현하고, 각 모듈의 목적을 명확히 합니다.
- **계층화된 디렉터리 구조 설계**: 기능, 역할에 따라서 이를 계층(Layer)으로 나눈 디렉터리 구조를 설계하여 확장성과 모듈간의 독립성을 최대한 확보합니다.
- **명확한 요구사항 정의**: 요구사항을 명확히 정의하고, 이를 기반으로 RESTful API를 구현해서 완전성(Completeness)을 확보 합니다. 또한 각 요구사항 수행에 관한 일관성(Consistentcy)을 확보하기 위해 테스트 케이스를 생성합니다. 이로써 테스트 가능성(Testability)도 확보 합니다.   
- **데이터베이스 스키마 재설계**: 데이터베이스 스키마를 재설계하여 프론트엔드와 협업 시 데이터 매핑이나 구조 변경 과정을 최소화 할 수 있도록 설계를 합니다.


# 요구사항 정의
- 본 프로젝트에서 요구되는 기능적 요구사항을 정리하고 이를 [유스케이스 다이어그램](document/use-case-hoonsletter.drawio.svg)으로 디자인 해보았습니다.

## 비회원 요구사항 

[1. **로그인**](#1-로그인)  
- 사용자의 아이디(username)와 비밀번호(password)를 입력하거나 카카오 인증을 통해 로그인을 할 수 있습니다.
   
[2. **회원가입**](#3-회원가입)
- 사용자의 정보를 입력해서 회원 가입을 하거나 카카오 인증을 통해 회원 가입을 할 수 있습니다.
   
[3. **편지 보기**](#5-편지-보기)
- 다른 사용자가 제작한 편지를 볼 수 있습니다.

[4. **편지 조회**](#4-전체-편지-조회)
- 다른 사용자가 제작한 편지를 조회할 수 있습니다.

   [4.1 **편지 검색**](#4-전체-편지-조회)
   - 사용자는 제작한 편지들을 사용자의 요구에 맞게 검색을 할 수 있습니다.
   
   [4.2 **편지 정렬**](#4-전체-편지-조회)
   - 사용자는 제작한 편지들을 사용자의 요구에 맞게 정렬을 할 수 있습니다.

## 회원 요구사항

[1. **로그아웃**](#2-로그아웃)
- 사용자는 로그아웃을 할 수 있습니다.

[2. **편지 제작**](#6-편지-생성)
- 사용자는 원하는 템플릿을 선택하여 편지를 제작할 수 있습니다.

#### [3. **편지 수정**](#7-편지-수정)
 - 사용자는 제작한 편지를 수정할 수 있습니다.
 - 제작한 편지는 제작 후 **3일** 까지만 수정이 가능하고 그 이후는 불가능 합니다.

[4. **편지 삭제**](#9-편지-삭제)
- 사용자는 제작한 편지를 삭제할 수 있습니다.

[5. **편지 공유**](#10-편지-공유)
- 사용자는 제작한 편지를 링크를 통해 공유할 수 있습니다.

[6. **편지 보기**](#5-편지-보기)
- 사용자는 다른 사용자가 제작한 편지를 볼 수 있습니다.

[7. **편지 조회**](#4-전체-편지-조회)
- 사용자는 다른 사용자가 제작한 편지를 조회할 수 있습니다.

   [7.1 **편지 검색**](#4-전체-편지-조회)
   - 사용자는 제작한 편지들을 사용자의 요구에 맞게 검색할 수 있습니다.

   [7.2 **편지 정렬**](#4-전체-편지-조회)
   - 사용자는 제작한 편지들을 사용자의 요구에 맞게 정렬할 수 있습니다.

# API 스펙
- 요구사항을 토대로 필요한 기능을 도출해서 이를 API로써 정의를 해보았습니다.

#### **1. 로그인**
- HTTP Request: `POST` /api/user/login
- 입력 데이터: 아이디, 패스워드
- 응답 데이터: X

#### **2. 로그아웃**
- HTTP Request: `POST` /api/user/logout
- 입력 데이터: X
- 응답 데이터: X
     
#### **3. 회원가입**
- HTTP Request: `POST` /api/user/signup
- 입력 데이터: 아이디, 비밀번호, 이메일, 닉네임, 프로필사진
- 응답 데이터: X

#### **4. 전체 편지 조회**
- HTTP Request: `GET` /api/letters
- 입력 데이터: X
- 응답 데이터: 전체 편지 데이터
- 쿼리 스트링:
   - `sort_by` : 정렬 기준 (작성자, 작성일)
   - `direction` : 정렬 순서(asc 또는 desc)
   - `page` : 페이지 번호
   - `search_param`: 검색어

#### **5. 편지 보기**
- HTTP Request: `GET` /api/letters/{id}
- 입력 데이터: X
- 응답 데이터: 편지 데이터

#### **6. 편지 생성**
- HTTP Request: `POST` /api/letters/{id}
- 입력 데이터: 편지 데이터
- 응답 데이터: X

#### **7. 편지 수정**
- HTTP Request: `PUT` /api/letters/{id}
- 입력 데이터: 편지 데이터
- 응답 데이터: X

#### **9. 편지 삭제**
- HTTP Request: `DELETE` /api/letters/{id}
- 입력 데이터: X
- 응답 데이터: X

#### **10. 편지 공유**
- HTTP Request: `POST` /api/letters/share/{id}
- 입력 데이터: X
- 응답 데이터: X

# 데이터베이스 설계
- 주어진 요구사항과 api 스펙을 토대로 [ERD](document/erd_hoonsletter.drawio.svg)를 설계해 보았습니다.

## Table
- ### name
  - [**user**](#user)
  - [**letter**](#letter)
  - [**letter_scene**](#letterscene)
  - [**scene_message**](#scenemessage)
  - [**scene_picture**](#scenepicture)
- ### Attributes
  - ### user
    - **username(PK)** `VARCHAR(255) NOT NULL updateable = false`
      - 유저 아이디
      - 변경할 수 없는 고유의 값입니다.
      - `user_id`를 사용하지 않고 username을 PK로 한 이유: 
        - **만약 user_id를 uid로써 정의하고 username을 변경 가능하게 할 경우, 유저의 username이 변경되면 다른 tuple의 데이터도 바뀐 username에 따라 update(SET)과정을 거치게 되기 때문에 자원 비효율적이라 판단했습니다. <br /> 그래서 username을 변경할 수 없게 하고 PK로 설계를 했습니다.**
    - **password** `VARCHAR(255) NOT NULL`
      - 유저 비밀번호
      - Hash 값이 저장됩니다.
    - **nickname** `VARCHAR(50) NOT NULL`
      - 유저 닉네임
    - **profile_url** `VARCHAR(255) NOT NULL`
      - 유저 프로필 사진
      - 사진의 주소값이 저정됩니다.
  - ### letter
    - **letter_id(PK)** `BIG_INT NOT NULL AUTO_INCREMENT`
      - 편지의 uid
    - **type** `VARCHAR(20) NOT NULL updatable = false`
      - 편지의 타입
      - enum string 값이 저장됩니다.
      - 편지는 여러 타입이 있고, 그 타입에 따라 포함하는 메시지, 사진의 개수가 다릅니다. 이를 구별 하기 위한 필드입니다.
    - **thumbnail_url** `VARCHAR(255) NOT NULL`
      - 편지 썸네일 사진
      - 사진의 주소값이 저장됩니다.
    - **created_at** `TIMESTAMP NOT NULL updateable = false`
      - 생성 일시
      - 생성 일시를 기준으로 정렬 기능을 구현 하기 위한 필드입니다.
    - **updateable** `BOOLEAN DEFAULT 'true'`
      - 수정 가능 여부
      - [요구사항](#3-편지-수정)에 따라 수정 가능 여부를 판단하기 위해 필드를 따로 생성합니다.
    - **username(FK)** `VARCHAR(50) NOT NULL ON DELETE CASCADE`
      - user를 참조하는 외래키
      - 편지는 반드시 소유자가 있어야 하므로 NULL값을 가질 수 없고 부모 엔티티가 삭제되면 자신도 삭제 됩니다.
  - ### letter_scene
    - **scene_id(PK)** `BIG_INT NOT NULL AUTO_INCREMENT`
      - 장면의 uid
    - **order** `INT(11) NOT NULL`
      - 편지에 포함된 장면의 순서.
      - 편지는 여러 장의 장면을 포함하고 정해진 순서대로 표현 되어야 합니다.
      - 장면을 정해진 순서대로 표현하기 위한 필드 입니다.
    - **letter_id(FK)** `BIGINT NOT NULL ON DELETE CASCADE`
      - letter를 참조하는 외래키
      - 장면 반드시 소유하는 편지가 있어야 하므로 NULL값을 가질 수 없고 부모 엔티티가 삭제되면 자신도 삭제 됩니다.
  - ### scene_picture
    - **picture_id(PK)** `BIG_INT NOT NULL`
      - 편지의 사진 엔티티 고유 id
    - **order** `INT(11) NOT NULL`
      - 장면에 포함된 사진의 순서.
      - 장면은 여러 장의 사진을 포함하고 정해진 순서대로 표현 되어야 합니다. 
      - 사진을 정해진 순서대로 표현하기 위한 필드 입니다.
    - **url** `VARCHAR(255) NOT NULL`
      - 사진의 url
    - **scene_id(FK)** `BIGINT NOT NULL ON DELETE CASCADE`
      - scene을 참조하는 외래키
  - ### scene_message
    - **message_id(PK)** `BIG_INT NOT NULL`
      - 편지의 사진 엔티티 고유 id
    - **order** `INT(11) NOT NULL`
      - 장면에 포함된 메시지의 순서.
      - 장면은 여러 메시지를 포함하고 정해진 순서대로 표현 되어야 합니다.
      - 메시지을 정해진 순서대로 표현하기 위한 필드 입니다.
    - **content** `VARCHAR(255) NOT NULL`
      - 메시지 내용
    - **size_type** `VARCHAR(20) NOT NULL`
      - 메시지 글자 크기 타입
    - **colot_type** `VARCHAR(20) NOT NULL`
      - 메시지 글자 색상 타입
    - **letter_id(FK)** `BIGINT NOT NULL ON DELETE CASCADE`
      - scene를 참조하는 외래키
