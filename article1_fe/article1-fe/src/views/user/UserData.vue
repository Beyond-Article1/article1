<script setup>
import { onMounted, ref, watchEffect } from 'vue';
import { useAuthStore } from '@/store/authStore.js';
import PreferenceForm from "@/components/user/PreferenceForm.vue";
import axios from 'axios';
import { useRouter } from "vue-router";

const router = useRouter();
const nickname = ref('');
const conditionSeq = ref(null);
const styleSeq = ref(null);

const handleUpdatePreference = (preferenceData) => {
  console.log('선호도 업데이트:', preferenceData);
  conditionSeq.value = preferenceData.conditionSeq;
  styleSeq.value = preferenceData.styleSeq;
};

const handleRegisterUser = async () => {
  if (!nickname.value || conditionSeq.value === null || styleSeq.value === null) {
    alert('모든 필드를 입력해주세요.');
    return;
  }

  try {
    const userData = {
      userNickname: nickname.value,
      conditionSeq: conditionSeq.value,
      styleSeq: styleSeq.value,
    };

    await axios.post('/user/data', userData); // Authorization 헤더는 기본 설정 사용
    alert('사용자 정보와 선호도가 등록되었습니다.');

      deleteCookie('token'); // 쿠키 삭제

      router.push("/");
  } catch (error) {
    console.error('등록 실패:', error);

    if (error.response && error.response.data) {
      const { status, code, message } = error.response.data;

      if (status === "CONFLICT" && code === "DUPLICATE_NICKNAME") {
        alert(message);
      } else {
        alert('닉네임 수정 중 오류가 발생했습니다.');
      }
    } else {
      alert('서버와의 통신에 실패했습니다.');
    }
  }
};
function deleteCookie(name) {
  document.cookie = `${name}=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT;`;
  if (document.domain) {
    document.cookie = `${name}=; Path=/; Domain=${document.domain}; Expires=Thu, 01 Jan 1970 00:00:00 GMT;`;
  }
}

const authStore = useAuthStore();

watchEffect(() => {
  if (authStore.token) {
    localStorage.setItem('accessToken', authStore.token);
    axios.defaults.headers.common['Authorization'] = `Bearer ${authStore.token}`;
    console.log('Authorization header updated with token:', authStore.token);
  }
});

onMounted(() => {
  const tokenCookie = document.cookie
      .split('; ')
      .find((row) => row.startsWith('token='));

  if (tokenCookie) {
    authStore.token = tokenCookie.split('=')[1];
    console.log(`[AuthStore] Loaded token: ${authStore.token}`);
  } else {
    console.log(`[AuthStore] Loaded token: null`);
  }
});
</script>


<template>
  <div class="user-registration-form">
    <div class="extra-data-title">
      <h1 style="font-size: 40px">추가 정보 입력</h1>
      <button @click="handleRegisterUser">등록</button>
    </div>

    <div class="form-content">
      <!-- 닉네임 입력 -->
      <div class="nickname">
        <label for="nickname"><h2>닉네임</h2></label>
        <input
            id="nickname"
            type="text"
            v-model="nickname"
            placeholder="닉네임을 입력하세요"
            required
        />
      </div>

      <div style="padding: 10px 35px 10px 35px">
        <hr>
      </div>

      <!-- 선호도 입력 폼 -->
      <PreferenceForm
          :conditionSeq="conditionSeq"
          :styleSeq="styleSeq"
          @updatePreference="handleUpdatePreference"
      />
    </div>
  </div>
</template>

<style scoped>
.user-registration-form {
  background-color: #f0f2f5;
  border-radius: 16px;
  padding: 20px 30px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  max-width: 1000px; /* 폼의 최대 너비를 제한 */
  margin: 0 auto;  /* 화면 중앙에 배치 */
}

.extra-data-title {
  padding: 0 20px;                  /* 좌우 패딩 추가 */
  display: flex;
  justify-content: space-between;   /* 왼쪽은 제목, 오른쪽은 수정 버튼 */
  align-items: center;              /* 세로 중앙 정렬 */
  margin-bottom: 20px;              /* 제목과 내용 사이의 간격 */
}

button {
  padding: 10px 25px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 17px;
}

button:hover {
  background-color: #0056b3;
}

.nickname {
  padding-left: 35px;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.nickname label {
  font-size: 18px;
  margin-right: 20px; /* 라벨과 입력 칸 사이의 간격 */
}

.nickname input {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border-radius: 8px;  /* 둥근 모서리 */
  border: 1px solid #ccc;
  box-sizing: border-box;
  max-width: 300px; /* 최대 너비를 설정하여 크기를 제한 */
}

.nickname input::placeholder {
  color: #aaa;
}

h2 {
  font-size: 1.3em;
  margin-bottom: 10px;
}
</style>
