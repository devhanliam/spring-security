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
그리고 scr/http/request.http 파일에 http 요청테스트 있으니 이부분 참고바랍니다. 

## 구현하며 배운점

### 인증 과정
1. 정의된 SecurityFilter가 서블릿 컨테이너로 들어온 http요청을 가로챈다.
2. AuthenticationManager에게 인증책임이 위임된다.
3. AuthenticationProvider를 통해 인증논리를 구현한다.
4. UserDetails와 PasswordEncoder로 사용자를 검증한다.
5. 인증결과를 SecurityFilter에 반환한다.
6. 인증된 엔티티 세부정보가 SecurityContext에 저장된다.
7. 요청이 권한 부여 필터로 위임됨.
8. 필터가 요청을 허용할지 결정하고 결정되면 컨트롤러로 보낸다.

### 인증정보 저장 방법
세션을 사용하게 되면 세션에 인증정보가 저장된다.
따라서 이후 요청에는 세션에 저장된 인증객체를 읽을 수 있지만 Session Stateless 설정하는 Token방식은 토큰에 저장하는 방식으로 저장해야한다.  

### 인증 필터내 예외처리
``` 
Security filter chain: [
WebAsyncManagerIntegrationFilter
SecurityContextPersistenceFilter
HeaderWriterFilter
CsrfFilter
LogoutFilter
UsernamePasswordAuthenticationFilter
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
BasicAuthenticationFilter
RequestCacheAwareFilter
SecurityContextHolderAwareRequestFilter
AnonymousAuthenticationFilter
SessionManagementFilter
ExceptionTranslationFilter
FilterSecurityInterceptor
]
```
필터내 실제 예외 처리를 담당하는 필터는 ```ExceptionTranslationFilter```같지만 이전 필터에서 예외처리가 따로필요하다.
이번 프로젝트에서는 ```ExceptionHandlingFilter```라는 커스텀 필터를 인증필터들 선순위에 두고 예외핸들러들 주입받아 처리했다.

## 더 공부할것
1. 분산 시스템에서 인증처리
2. 세션 클러스터링