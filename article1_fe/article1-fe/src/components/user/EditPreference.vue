<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import Preference from "@/components/user/PreferenceForm.vue";

const router = useRouter();
const preferenceData = ref({
  condition: "", // 체질
  style: ""      // 스타일
});

const updatedPreference = ref({
  conditionSeq: null,
  styleSeq: null
})

const fetchPreferenceData = async () => {
  try {
    const response = await axios.get(`/user/preference`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      }
    });
    console.log("데이터:", response);
    preferenceData.value.condition = response.data.conditionSeq;
    preferenceData.value.style = response.data.styleSeq;
  } catch(error) {
    console.error("선호도 정보를 가져오는 중 에러 발생 ", error);
  }
}

// 자식에서 받은 선호도 데이터 업데이트
const handlePreferenceUpdate = (preferenceData) => {
  updatedPreference.value = preferenceData;
  console.log("업데이트된 선호도 데이터:", preferenceData);
};

// 선호도 수정 요청
const handlePreferenceEdit = async () => {
  const formData = {
    conditionSeq: updatedPreference.value.conditionSeq,
    styleSeq: updatedPreference.value.styleSeq
  };

  try {
    await axios.put(`/user/preference`, formData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        "Content-Type": "application/json"
      },
    });
    alert('선호도가 정상적으로 수정되었습니다.');
    router.push("/mypage/preference")
  } catch (error) {
    console.error("선호도 수정 중 오류 발생 ", error);
  }
}

onMounted(() => {
  fetchPreferenceData();
})
</script>

<template>
  <div class="preference_title">
    <h1 style="font-size: 40px">선호도</h1>
    <button @click="handlePreferenceEdit">수정</button>
  </div>

  <div class="card">
    <div>
      <Preference
          :conditionSeq="preferenceData.condition"
          :styleSeq="preferenceData.style"
          @updatePreference="handlePreferenceUpdate" />
    </div>
  </div>
</template>

<style scoped>
.card {
  background-color: #f0f2f5;
  border-radius: 16px;
  padding: 35px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.preference_title {
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
  border-radius: 16px;
  cursor: pointer;
  font-size: 17px;
}

button:hover {
  background-color: #0056b3;
}
</style>