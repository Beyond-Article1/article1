<script setup>
import {onMounted, ref} from "vue";
import {useAuthStore} from "@/store/authStore.js";
import router from "@/router/index.js";

const termsRef = ref(null);
const showTerms = ref(false);
// 로그인 여부
const isLogin = ref();

// store 생성
const store = useAuthStore();

// 로그인 관련 이벤트 Mounted
onMounted(() => {
  const token = getCookie('token');
  const isNewMember = getCookie('newMember');

  // Store에 토큰이 없는 경우에만 쿠키에서 로그인 시도
  if (!store.accessToken && token) {
    console.log('쿠키에서 토큰을 가져왔습니다');
    store.login(token);
    isLogin.value = true;
    // 닉네임 등 입력이 안되어있으면 이동
    if (isNewMember === 'Y'){
      router.push('/user/data');
    }
  } else {
    console.log('쿠키에 토큰이 없습니다 또는 이미 로그아웃 상태입니다.');
    isLogin.value = false;
  }
});

// 쿠키에서 값을 가져오는 함수
function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

function scrollToTerms() {
  showTerms.value = true; // 약관 동의 섹션 보이기
  if (termsRef.value) {
    termsRef.value.scrollIntoView({ behavior: 'smooth' });
  }
}

// 로그인 여부에 따라 이동
function moveTo(){
    router.push('/map');
}
</script>

<template>
  <div class="main">

    <div class="main-content">

      <div class="main-title">
        <h1>입기 좋은 날</h1>
      </div>

      <div class="main-sub_title">
        <h3>날씨와 대기상황에 맞게 옷을 골라보세요!</h3>
      </div>



      <!-- 약관 동의 섹션: showTerms가 true일 때만 보임 -->
      <div v-show="showTerms" ref="termsRef" class="terms-section">
        <div class="term-title">
          <h2>위치 정보 조회 동의</h2>
        </div>
        <div class="term-info">
          <p>이용자가 "입기 좋은 날" 계정에 로그인한 상태이고 웹 및 앱 활동 또는 위치기록 설정을 활성화하였거나 위치정보를 저장하도록 동의한 경우,
            이용자의 위치정보는 "입기 좋은 날" 의 서비스에 저장 및 사용될 수 있습니다. <br> <br>
            "입기 좋은 날" 은 이를 통하여 이용자에게 보다 개인화된 경험 등을 제공할 수 있습니다. <br> <br>
            "입기 좋은 날" 은 신규로 가입한 이용자의 경우 웹 및 앱 활동 및 위치기록의 기본 보관 기간을 모두 18 개월로 설정해 두고 있습니다. <br> <br>
            "입기 좋은 날"은 이용자에 대한 위치정보의 수집∙이용∙제공사실을 위치정보시스템에 자동으로 기록, 보존하며, 6개월 이상 보관합니다.
          </p>
        </div>
        <button class="agree" @click="moveTo">동의</button>
        <button class="refusal">거절</button>
      </div>


    </div>
    <div class="main-move_bar" @click="scrollToTerms">
      <span class="more-content">더 알아보기</span>
      <div class="under">
        <img src="https://img.icons8.com/?size=100&id=26195&format=png&color=000000" width="30px">
      </div>
    </div>
  </div>
</template>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 90vh;
  background-position: center center;
  text-align: center;
}
.main-title h1 {
  margin-top: 0;
  font-weight: bolder;
  font-size: 3em;
  margin-bottom: 5%;
}
.main-sub_title {
  margin-bottom: 10%;
}
.main-content {
  background-color: rgb(255, 255, 255, 0.5);
  width: 70%;
  height: 90%;
  border-radius: 50px;
  position: relative;
  align-content: center;
  overflow: scroll;
  -ms-overflow-style: none;
  padding-bottom: 25px;
}
.main-content::-webkit-scrollbar{
  display:none;
}
.main-move_bar {
  width: 70%;
  height: 10%;
  position: absolute;
  bottom: 0;
  background: linear-gradient(rgba(100, 156, 203, 0.4), rgba(0, 0, 0, 0.4));
  border-radius: 0 0 50px 50px;
  align-content: center;
  cursor: pointer;
  margin-bottom: 6px;
}
.terms-section {
  text-align: center;
  align-content: center;
  height: 90%;
  padding: 20px;
  //background-color: rgba(255, 255, 255, 0.5);
  border-radius: 10px;
  width: 70%;
  margin: 10% auto;
}
.term-title{

}
.term-title h2{
  margin-bottom: 10%;
}
.term-info{
  padding: 20px;
  background-color: rgb(255, 255, 255, 0.5);
  margin-bottom: 8px;
  border-radius: 10px;
}
.terms-section button{
  width: 15%;
  border: none;
  border-radius: 10px;
  margin: 10px;
  height: 35px;
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
}
.terms-section button:hover{
  scale: 110%;
}
.agree{
  background-color: #24C7FF;
}
.refusal{
  background-color: #72BCD5;
}
</style>