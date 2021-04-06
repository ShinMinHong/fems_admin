# firealarm-admin

도메인 : admin.firealarm.com
프로젝트명 : firealarm-admin

환경: 
-jdk1.8.0_232 , 
-apache-tomcat-8.5.45,
-STS4.2.1.RELEASE
-Github URL: https://github.com/ablecoms/firealarm-admin.git

프로젝트 설정

1.workspace 경로:
C:\Java\2020\firealarm-admin

2. 깃허브 리파지토리 이클립스에 import: 
import project from git > clone URI >https://github.com/ablecoms/firealarm-admin.git ID, PW 입력 >Local Destination Directory 설정 : C:\Java\2020\firealarm-admin\git > import existing Eclipse projects 선택 

3. Project facets - dynamic web module, java, javascript 설정

4. java build path > source > default output folder : firealarm-admin/WebContent/WEB-INF/classes 로 변경

5. java build path > libraries > add Library > server Runtime > Apache tomcat 8.5 선택 (만약 추가 안되면 7번 먼저)

6. 5번과 동등하게 Junit 4 라이브러리 추가:

7. 톰켓 추가:
 server > new > server > Tomcat v8.5 server >
server name : firealarm-admin > add(fire-alarm) > Finish

8. 톰켓 운영 환경 설정 :
 firealarm-admin 서버 > open launch configuration > environment > spring.profiles.active // local 입력 

9. 톰켓 외부 웹 모듈 설정:
 Modules > add External Web Module에 아래사항 입력
document base:C:\Java\2020\firealarm-admin\git\WebContent
path : /

10. GS인증 Library교체(2021-01. 추가요청사항)
bootstrap
moment
jquery






