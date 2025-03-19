![image](https://github.com/user-attachments/assets/b2afda80-e524-45da-98a2-f7dd9ec60d8f) <!-- 글 상세 보기 화면 -->
![image](https://github.com/user-attachments/assets/086b8ddf-dad4-43b8-97e1-31db3385c387) <!-- 로그인 화면   -->
![image](https://github.com/user-attachments/assets/17849ad2-ee12-47de-bbf8-d89ebe297964) <!-- 지하철역 검색 -->
![image](https://github.com/user-attachments/assets/1aeaa11e-667f-4935-91be-af39769c055a) <!-- 검색 & 위치기반 추천 -->
![image](https://github.com/user-attachments/assets/21101888-7e89-4d3b-950e-b1febfe1facc) <!-- 내 정보 -->
![image](https://github.com/user-attachments/assets/5645b561-c49b-498b-bb10-cf96b1d069b3) <!-- 아키텍쳐 v1  -->

- Stack
  - Java 21, Spring Boot 3.3.9, JPA, Spring Security Crypto, MariaDB, AWS S3, Open API

- 기능
  - User
    - 프로젝트 내의 회원가입, 로그인, 로그아웃 및 유저 프로필 사진 업로드
    - 카카오 소셜 로그인(회원가입) email, username 정보 수집
  - Subway
    - 지하철 역 정보 수집(역명, 위도, 경도, 상세정보)
  - Post
    - 글 작성, 수정, 삭제
    - 조회 (상세정보 확인 && no-offset 자신의 글 리스트 확인)
      - 지하철 태그된 글들 조회(검색) no-offset
      - 좋아요 누른 글들 조회 no-offset
      - 역 검색 기반 추천 글 조회 (3)
      - 현재 위치 기반 추천 글 조회 (3)
    - Tag
      - 현재 태그 정보 조회(지하철 명 or 분위기 해시태그)
    - Pedestrian
      - 유저 보행자 경로 추천
      - 경로 최대 경유지 포함 5곳 추가
    - Likes
      - 글 좋아요 등록 및 취소
    - Place
      - 키워드 검색 기반 장소 상세정보 확인
