
<p align="center"><img src = "https://github.com/seniors-project/backend/assets/117654450/72756334-6370-46d6-9f29-f94d0a63b0f6" height = "200" width = "700"></p>

</br>

# 프로젝트 개요


## 💡 배경 or 컨셉

<aside>
후반전 커리어 소셜 플랫폼 Next Career
</br>

프로젝트는 중장년층 취업 정보 공유의 부족 문제를 해결하고자 하는 것을 목적으로 한다. 현재 중장년층을 대상으로 한 창업 지원과 구직 지원 서비스가 여러 방면에서 제공되고 있지만, 
그 정보가 분산되어 있어 찾기 어려우며, 취업과 관련된 고민을 털어놓을 수 있는 커뮤니티가 부재한 상황이다.

</br>

따라서 프로젝트에서는 SNS 피드 기반의 커뮤니티를 만들어 양질의 정보와 노하우를 전달하는 콘텐츠를 제공하고, 유저들도 스스로 동참해 공유할 수 있도록 해 중장년층의 2번째 커리어를 돕는 역할을 한다. 쉽게 말해 시니어를 위한 링크드인 혹은 디스콰이엇의 역할을 하는 것이다.

</br>

즉, 국내 유일의 중장년층 커리어 소셜 네트워크 플랫폼이다.

</aside>

</br>

## 🧐 해결하고자 하는 문제
- 양질의 취업 및 창업 정보 습득의 어려움(정보의 분산)
- 은퇴 후 재취업, 창업에 대해 같이 공유할 수 있는 공간 부족

## 😄 솔루션
- 취업, 창업 정보 제공
- 은퇴자를 위한 커뮤니티 제공
- 빠른 피드백을 위한 채팅 기능


# 👐 Member

|[이상훈](https://github.com/strangehoon)|[차윤범](https://github.com/uiurihappy)|[최용석](https://github.com/regchoi)|
|:-:|:-:|:--:|
|<img src="https://avatars.githubusercontent.com/u/117654450?v=4" alt="daeun" width="170" height="170">|<img src="https://avatars.githubusercontent.com/u/68099546?v=4" alt="yb__char" width="170" height="170">|<img src="https://avatars.githubusercontent.com/u/103176657?v=4" alt="regchoi" width="170" height="170">|

</br>

# 🕹 Skills

![image](https://github.com/seniors-project/backend/assets/117654450/119cf4fa-2b71-4109-8004-c396fe4f2a49)



</br>


# 🛠 Infrastructure

<img src = "https://github.com/seniors-project/backend/assets/117654450/91df2e21-fee2-4ff8-adbc-bb537e91ea70" height = "500" width = "800" allign = "center">

</br>

# 🔧 CICD

<img src = "https://github.com/seniors-project/backend/assets/117654450/91630253-2114-4aee-a002-bd95dd65ac50" height = "450" width = "700" allign = "center">

**Developer Flow**
1. App Repository에 code 변경 사항 push
2. Github Actions가 변경 감지
3. 빌드 후, Docker Hub에 이미지 push
4. Slack에 빌드 성공 여부 알림 전송
5. Config Repository의 values.yaml의 image.tag 값 변경
6. ArgoCD가 Config Repository Sync
7. EKS에 배포
8. Slack에 배포 성공 여부 알림 전송

</br>

**Operator Flow**
1. Config Repository에 code 변경 사항 push
2. ArgoCD가 Config Repository Sync
3. EKS에 배포
4. Slack에 배포 성공 여부 알림 전송

</br>

# 📊 ERD
![image](https://github.com/seniors-project/backend/assets/117654450/590cc5c9-ec2a-429a-a85a-557fded3f41a)


</br>


