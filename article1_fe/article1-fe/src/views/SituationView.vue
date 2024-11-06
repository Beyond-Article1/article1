<template>
  <div class="situation-view">
    <h2 class="list-title">그날의 아웃핏 상황을 선택해주세요.</h2>
    <div class="situation-container">
      <div
          v-for="situation in situations"
          :key="situation.value"
          class="situation-option"
          :class="{ selected: selectedSituation === situation.value }"
          @click="selectSituation(situation.value)"
      >
        <img :src="situation.icon" :alt="situation.label" />
        <p>{{ situation.label }}</p>
      </div>
    </div>
    <button class="recommend-button" @click="getRecommendation">추천 받기</button>
    <div class="info-box">
      <p><strong>아웃핏 상황 안내</strong></p>
      <ul>
        <li>일상: 등교, 출근 등 일상 활동에 어울리는 아웃핏을 추천합니다.</li>
        <li>여행: 여행지의 날씨에 맞춰서 활동하기 좋은 아웃핏을 추천합니다.</li>
        <li>데이트: 데이트 분위기에 어울리는 세련된 아웃핏을 추천합니다.</li>
        <li>운동: 운동이나 야외 활동에 적합한 편안한 아웃핏을 추천합니다.</li>
        <li>격식있는자리: 비즈니스 미팅, 발표 등 격식을 차려야 하는 자리에 어울리는 단정한 아웃핏을 추천합니다.</li>
        <li>무관: 상황과 관계없이 날씨에 맞는 편안한 아웃핏을 추천합니다.</li>
      </ul>
    </div>
  </div>
</template>


<script>
import { useSelectedInfoStore } from '@/store/selectedInfoStore';

export default {
  setup() {
    const store = useSelectedInfoStore();

    return {
      selectedLatitude: store.selectedLatitude,
      selectedLongitude: store.selectedLongitude,
      selectedDate: store.selectedDate,
      selectedSituation: null,
      situations: [
        { label: "일상", value: "daily", icon: new URL('@/assets/images/situation/routine.png', import.meta.url).href },
        { label: "여행", value: "travel", icon: new URL('@/assets/images/situation/travel.png', import.meta.url).href },
        { label: "데이트", value: "date", icon: new URL('@/assets/images/situation/dating.png', import.meta.url).href },
        { label: "운동", value: "exercise", icon: new URL('@/assets/images/situation/sports.png', import.meta.url).href },
        { label: "격식있는자리", value: "formal", icon: new URL('@/assets/images/situation/formal.png', import.meta.url).href },
        { label: "무관", value: "none", icon: new URL('@/assets/images/situation/none.png', import.meta.url).href },
      ],
    };
  },
  methods: {
    selectSituation(value) {
      this.selectedSituation = value;
    },
    getRecommendation() {
      if (this.selectedSituation) {
        alert(`위도: ${this.selectedLatitude}, 경도: ${this.selectedLongitude}, 날짜: ${this.selectedDate.toLocaleString()}, 상황: ${this.selectedSituation}`);
      } else {
        alert("상황을 선택해 주세요.");
      }
    },
  },
};
</script>

<style scoped>
.situation-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 40px;
  background-size: cover;
  height: 100vh;
  color: #333;
}

.list-title {
  font-size: 1.8rem;
  margin-bottom: 20px;
}

.situation-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  max-width: 800px;
  gap: 20px;
}

.situation-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s, background-color 0.2s;
}

.situation-option.selected {
  background-color: #007bff;
  color: white;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.4);
  transform: scale(1.05);
}

.situation-option img {
  width: 80px;
  height: 80px;
  margin-bottom: 8px;
}

.situation-option p {
  font-size: 1rem;
  margin: 0;
}

.recommend-button {
  margin-top: 30px;
  padding: 12px 40px;
  font-size: 1.1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.recommend-button:hover {
  background-color: #0056b3;
}

.info-box {
  margin-top: 30px;
  padding: 20px;
  max-width: 600px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.info-box p {
  font-weight: bold;
}

.info-box ul {
  padding-left: 20px;
  list-style-type: disc;
  font-size: 0.9rem;
}
</style>
