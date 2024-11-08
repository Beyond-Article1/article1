<script setup>
import {computed, nextTick, ref} from 'vue';
import router from "@/router/index.js";
import {useAuthStore} from "@/store/authStore.js";

// 상태관리
const store = useAuthStore();

// 로그인 유무
const isLogIn = computed(() => store.accessToken !== null);

// 로그아웃 후 메인페이지로 이동
const logout = async () => {
  console.log("logout 호출");
  await store.logout();
  // 'token'이라는 이름의 쿠키를 모든 경로와 도메인에서 삭제
  deleteCookie('token');
  await nextTick();      // 상태가 반영된 후 페이지 이동

  router.push('/');
}

function deleteCookie(name) {
  // 기본적으로 Path를 '/'로 지정하여 모든 경로에서 쿠키 삭제 시도
  document.cookie = `${name}=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT;`;

  // 도메인이 존재하는 경우, 명시적으로 도메인을 지정하여 삭제
  if (document.domain) {
    document.cookie = `${name}=; Path=/; Domain=${document.domain}; Expires=Thu, 01 Jan 1970 00:00:00 GMT;`;
  }
}

</script>

<template>
  <div class="header-content">
    <div class="logo" @click="router.push('/')">
      <img src="@/assets/images/Logo.png" width="110px">
    </div>
    <!-- 로그인 상태일 때만 메뉴 항목 표시 -->
    <div class="menu-list" v-if="isLogIn">
      <ul>
        <li>OUTFIT</li>
        <li>REVIEW</li>
        <li>BOARD</li>
        <li>MYPAGE</li>
      </ul>
    </div>
    <!-- 로그인/로그아웃 버튼 -->
    <div class="login-logout">
      <template v-if="isLogIn">
        <!-- 로그아웃 버튼 -->
        <button @click="logout" class="logout-btn">LOGOUT</button>
      </template>
      <template v-else>
        <!-- 로그인 버튼 -->
        <button @click="router.push('/login')" class="login-btn">LOGIN</button>
      </template>
    </div>
  </div>
</template>

<style scoped>
  .header-content{
    display: flex;
    justify-content: space-between;
    font-weight: bold;
  }
  .logo{
    margin-left: 2%;
  }
  .logo:hover{
    cursor: pointer;
    scale: 120%;
  }
  .menu-list{
    width: 40%;
    align-content: center;
  }
  ul{
    padding: 0;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
  li{
    margin: 0 auto;
    align-content: center;
    text-align: center;
    list-style: none;
  }
  li:hover{
    cursor: pointer;
    scale: 120%;
  }
  .login-logout{
    margin-right: 2%;
    align-content: center;
    font-size: 1.2em;
  }
  .login-logout button{
    border: none;
    background-color: rgb(0, 0, 0, 0);
    font-size: 1.2em;
    font-weight: bold;
  }
  .login-logout button:hover{
    cursor: pointer;
    scale: 120%;
  }
  .signup-btn{
    margin-right: 20px;
  }
</style>