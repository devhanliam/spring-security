# 간단한 인증모듈 구헌
유저의 이메일과 패스워드로 검증 후 JWT를 발급하고 리소스에 접근 권한을 주는 모듈입니다. 
예외 처리 필터를 두어 인증과정에서 발생하는 예외를 한곳에서 처리하도록 구현해봤습니다.

# 환경
- JDK : 11 
- IDE : IntelliJ IDEA 2022.2.5
- OS : AppleSilicon M1
- DB : H2
- SpringBoot : 2.5.1

# 시작하기
H2 데이터 베이스를 사용하여 별도 설정없이 바로 구동가능합니다.
resource/data/data.sql 경로에 최초 데이터를 삽입할 수 있도록 했으니 참고바랍니다.
그리고 scr/http/request.http 파일에 http 요청테스트 부분 참고바랍니다. 

## 구현하며 배운점
https://velog.io/@hhg1993/SpringSecurity-ExceptionHandling%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B0%9C%EC%9D%B8%EC%A0%81%EC%9D%B8-%EC%98%A4%ED%95%B4

