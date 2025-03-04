# 매장 원격줄서기 서비스
### 매장을 방문할 때 미리 방문 예약을 진행하는 서비스

## 💡기술 스택

#### Language: <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/></a> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/></a>
#### Database: <img src ="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white"/></a>   <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=flat-square&logo=redis&logoColor=white"/></a>
#### DevObs: <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=flat-square&logo=swagger&logoColor=white"/></a> <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/></a> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/> 
#### Cloud: <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>


## 공통
- [x] OAuth2를 이용한 카카오톡 소셜 로그인
  - 로그인 후 매장 이용시 인증에 필요한 핸드폰번호 추가요청
    - 카카오톡 로그인 후 JWT토큰 발급
    - kakao에서 인증된 정보 요청 후 회원 정보, 삭제, 수정 요청 
- [x]  원격줄서기 후 해당 매장 정보(줄서기정보)메일전송 (이메일)
- [x]  사용자 유형별 타입 선언 (USER,MANAGER,ADMIN)
- [x]  회원탈퇴 (회원 상태 (stop)상태 변경 -> 서비스이용 금지상태로 바뀜 )

## 상점 
- [x]  매장 등록/ 수정/ 삭제 /상세정보 기능
- [x] 상점 목록 (가나다순, 별점순, 거리순)
- [x] 점주 예약 정보 확인 
- [x] 예약 이용 후 리뷰 작성 기능 구현
    -  예약자인지 확인 및 작성, 수정, 삭제 구현
    - 수정은 리뷰 작성자만 / 삭제 권한은 리뷰작성자,매장의 관리자(점장)만 삭제 가능
- [x] 상점 상세보기 -> 카카오맵 API 활용하여 매장 위치 보기

## 예약
- [x] 예약 요청/ 취소 기능
- [x] 예약 확정
  - Queue를 활용하여 대기번호 순서를 관리
  - Lock으로 동시성 제어 구현
- [x] 상정 예약 체크
  - 예약 중복이 되면 원격 줄서기 x (하나의 매장만 원격줄서기 가능)
- [x] 내 예약 목록 기능

## 리뷰

  
