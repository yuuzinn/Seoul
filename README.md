# Seoul 놀먹

### 개발하게 된 계기와 간단한 소개
요즘 SNS에서 10대부터 30대까지 놀거리 및 먹거리를 많이 홍보하고 있습니다. 하지만 ***광고 기반에 따라 만들어진 홍보들이 많다 보니, 막상 기대를 안고 가보면 실망하는 사람들이 꽤나 많습니다.*** 그에 따라 사람들이 직접 가보고, 근처에 더 가볼만한 코스를 정해서 직접 추천해줄 수 있는 서비스를 개발하게 되었습니다.

`Seoul 놀먹`은 서울시 내에서 한정되어 있으며, 대중교통(지하철)으로 많이 이용하기 때문에 ***지하철역을 기준으로 하여 코스들을 보여줍니다.***

유저는 간단하게 작성되어 있는 옆으로 넘기는 슬라이드 형식의 카드일기 형식의 글을 볼 수 있습니다. 마지막 장에는 글의 장소 순서대로 경로를 추천하여 보여줄 수 있습니다.


![image](https://github.com/user-attachments/assets/086b8ddf-dad4-43b8-97e1-31db3385c387) <!-- 로그인 화면   -->
![image](https://github.com/user-attachments/assets/b2afda80-e524-45da-98a2-f7dd9ec60d8f) <!-- 글 상세 보기 화면 -->
![image](https://github.com/user-attachments/assets/17849ad2-ee12-47de-bbf8-d89ebe297964) <!-- 지하철역 검색 -->
![image](https://github.com/user-attachments/assets/1aeaa11e-667f-4935-91be-af39769c055a) <!-- 검색 & 위치기반 추천 -->
![image](https://github.com/user-attachments/assets/21101888-7e89-4d3b-950e-b1febfe1facc) <!-- 내 정보 -->
![image](https://github.com/user-attachments/assets/2fe0553f-900b-40bc-b6ac-c46301a1166f) <!-- 아키텍쳐 v1  -->

- Stack
  - Java 21, Spring Boot 3.3.9, JPA, Spring Security Crypto, MariaDB, AWS S3, Open API

- 기능 (v1)
  - User
    - 프로젝트 내의 회원가입, 로그인, 로그아웃 및 유저 프로필 사진 업로드
    - 카카오 소셜 로그인(회원가입) email, username 정보 수집
    - email을 통한 비밀번호 찾기
  - Subway
    - 지하철 역 정보 수집(역명, 위도, 경도, 상세정보)
  - Post
    - 글 작성, 수정, 삭제
    - 조회 (상세정보 확인 && no-offset 자신의 글 리스트 확인)
      - 지하철 태그된 글들 조회(검색) no-offset
      - 좋아요 누른 글들 조회 no-offset
      - 태그에 따른 글 조회 no-offset (subway / mood)
      - 좋아요 순 글 조회 no-offset
      - 역 검색 기반 추천 글 조회 (3) (예정)
      - 현재 위치 기반 추천 글 조회 (3) (예정)
    - Tag
      - 현재 태그 정보 조회(지하철 명 or 분위기 해시태그)
    - Pedestrian
      - 유저 보행자 경로 추천
      - 경로 최대 경유지 포함 5곳 추가
    - Likes
      - 글 좋아요 등록 및 취소
    - Place
      - 키워드 검색 기반 장소 상세정보 확인
