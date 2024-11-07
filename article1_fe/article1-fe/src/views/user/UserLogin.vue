<script setup>

import {useAuthStore} from "@/store/authStore.js";


const authStore = useAuthStore();
// 소셜 로그인 리디렉션 URL
const redirectToSocial = (platform) => {
  let url = '';

  // 각 소셜 로그인 플랫폼에 맞는 URL로 리디렉션
  switch (platform) {
    case 'kakao':
      url = 'http://localhost:8080/oauth2/authorization/kakao';
      break;
    case 'naver':
      url = 'http://localhost:8080/oauth2/authorization/naver';
      break;
    case 'google':
      url = 'http://localhost:8080/oauth2/authorization/google';
      break;
    default:
      break;
  }
  // 외부 소셜 로그인 페이지로 리디렉션
  window.location.href = url;
}
</script>

<template>
  <section class="css-sns-login">
    <div class="sns-login">
        <div class="login-button">
          <!-- 소셜 로그인 버튼 -->
          <button @click="redirectToSocial('kakao')" class="BODY_15 BOLD css-kakao">
            <img src="@/assets/images/user/kakao-logo.png">
            <span>카카오로 시작하기</span>
          </button>
          <button @click="redirectToSocial('naver')" class="BODY_15 BOLD css-naver">
            <img src="@/assets/images/user/naver-logo.png">
            <span>네이버로 시작하기</span>
          </button>
          <button @click="redirectToSocial('google')" class="BODY_15 BOLD css-google">
            <img src="@/assets/images/user/google-logo.png">
            <span>구글로 시작하기</span>
          </button>
        </div>
    </div>
  </section>

</template>

<style scoped>
.login-button {
  box-sizing: border-box;
  max-width: 500px;
  text-align: center;
}

.css-sns-login {
  display: flex;
  align-items: center;      /* 세로 중앙 정렬 */
  justify-content: center;  /* 가로 중앙 정렬 */
  height: 70vh;            /* 화면의 전체 높이 */
  padding: 0px 20px;
}

button {
  font-family: inherit;
  font-size: 100%;
  line-height: 1.15;
  margin: 0;
  padding: 0;
  border: none;
  display: flex;
  align-items: center; /* 이미지와 텍스트를 세로 중앙에 배치 */
  justify-content: flex-start; /* 텍스트를 로고 오른쪽에 배치 */
}


.css-kakao, .css-naver, .css-google {
  width: 100%;
  max-width: 340px;
  margin: 0px 32px 10px;
  padding: 17px 55px 12px 40px;
  display: flex;
  align-items: center;
  border-radius: 24px;
  position: relative;
  overflow: hidden;
}

.css-kakao {
  background-color: rgb(254, 229, 0);
}

.css-naver {
  background-color: #03C75A;
  color: rgb(255, 255, 255);
}

.css-google {
  background-color: white;
  border: 1px solid rgb(210, 213, 214);
}

.css-kakao span,
.css-naver span,
.css-google span {
  font-weight: 600;  /* 글자 굵기를 더 굵게 설정 */
  font-size: 18px;    /* 글자 크기 */
  line-height: 18px;  /* 라인 높이 */
  color: inherit;     /* 기본 글자색을 유지 */
  display: inline-block;
  width: 100%;
  text-align: center;
}

.BODY_15 {
  font-size: 15px;
  line-height: 18px;
}

a, button {
  cursor: pointer;
  transition: background 0.25s cubic-bezier(0.33, 1, 0.68, 1), color 0.25s cubic-bezier(0.33, 1, 0.68, 1);
}


</style>
