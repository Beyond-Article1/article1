<script setup>
import axios from 'axios';
import {ref} from "vue";

const time = ref('');   // 시간
const lat = ref('');    // 위도
const lon = ref('');    // 경도
const nowWeatherCode = ref(); // 현재 날씨코드
const nowWeatherIcon = ref(''); // 현재 날씨 아이콘
const nowTemp = ref();
const nowFeelsLike = ref(); // 현재 체감온도
const highTemp = ref();
const lowTemp = ref();
const pm2_5 = ref();  // 초미세먼지
const pm10 = ref();   // 미세먼지

const openWeather = ref([]);  // 날씨 데이터를 저장할 객체

const description = '맑음';
const getWeather = async () => {
  time.value = '2024-11-06-14:00';
  lat.value = '37.4972160230992';
  lon.value = '126.927607240617';
  await axios.get(`/weather`, {
    params: {
      time: time.value,
      lat: lat.value,
      lon: lon.value
    }
  })
      .then(res => {
        openWeather.value = res.data;
        console.log(openWeather.value);
        if (openWeather.value != null) {
          nowWeatherCode.value = openWeather.value.nowWeatherCode;
          nowWeatherIcon.value = openWeather.value.nowWeatherIcon;
          nowTemp.value = Math.round(openWeather.value.nowTemp);
          nowFeelsLike.value = Math.round(openWeather.value.nowFeelsLike);
          highTemp.value = Math.round(openWeather.value.highTemp);
          lowTemp.value = Math.round(openWeather.value.lowTemp);
          pm2_5.value = openWeather.value.pm2_5;
          pm10.value = openWeather.value.pm10;
        }
      })
      .catch(err => {
        console.error("날씨 불러오기 오류", err);
      });
}

// 미세먼지 농도에 따른 스타일 반환
const getPm2_5Style = (pm2_5) => {
  if (pm2_5 <= 30) {
    return { backgroundColor: 'blue', color: 'white' }; // 좋음
  } else if (pm2_5 <= 80) {
    return { backgroundColor: 'green', color: 'white' }; // 보통
  } else if (pm2_5 <= 120) {
    return { backgroundColor: 'orange', color: 'white' }; // 약간 나쁨
  } else if (pm2_5 <= 200) {
    return { backgroundColor: 'darkorange', color: 'white' }; // 나쁨
  } else {
    return { backgroundColor: 'red', color: 'white' }; // 매우 나쁨
  }
};

// 초미세먼지 농도에 따른 스타일 반환
const getPm10Style = (pm10) => {
  if (pm10 <= 15) {
    return { backgroundColor: 'blue', color: 'white' }; // 좋음
  } else if (pm10 <= 35) {
    return { backgroundColor: 'green', color: 'white' }; // 보통
  } else if (pm10 <= 75) {
    return { backgroundColor: 'yellow', color: 'black' }; // 나쁨
  } else {
    return { backgroundColor: 'red', color: 'white' }; // 매우 나쁨
  }
};

// 미세먼지 농도에 따른 텍스트 반환
const getPm2_5Text = (pm2_5) => {
  if (pm2_5 <= 30) {
    return '좋음';
  } else if (pm2_5 <= 80) {
    return '보통';
  } else if (pm2_5 <= 120) {
    return '약간 나쁨';
  } else if (pm2_5 <= 200) {
    return '나쁨';
  } else {
    return '매우 나쁨';
  }
};

// 초미세먼지 농도에 따른 텍스트 반환
const getPm10Text = (pm10) => {
  if (pm10 <= 15) {
    return '좋음';
  } else if (pm10 <= 35) {
    return '보통';
  } else if (pm10 <= 75) {
    return '나쁨';
  } else {
    return '매우 나쁨';
  }
};
</script>

<template>
  <div class="weather-container">
    <button @click="getWeather">날씨 불러오기</button>
    <div class="weather-container_top">
      <div class="weather-icon">
        <!-- 옵셔널 체이닝을 사용하여 안전하게 접근 -->
        <img :src="`https://openweathermap.org/img/wn/${nowWeatherIcon}@2x.png`" alt="날씨 아이콘">
      </div>
      <div class="weather-temp">
        <div class="temp">현재온도: {{ nowTemp || '데이터 없음' }} ℃</div>
      </div>
      <div class="weather-code">{{ nowWeatherCode || '데이터 없음' }}</div>
      <div class="weather-feel">체감온도: {{ nowFeelsLike }} ℃</div>
    </div>
    <hr>
    <div class="weather-container_bottom">
      <div class="min_max-temp">
        <div class="min-temp">최저기온: {{ lowTemp || '온도 데이터 없음' }} ℃</div>
        <div class="max-temp">최고기온: {{ highTemp || '온도 데이터 없음' }} ℃</div>
      </div>
      <div class="pm-box">
        <div
            class="pm2_5"
            :style="getPm2_5Style(pm2_5)">
          <div class="pm2_5-title">
            <span>미세먼지</span>
          </div>
          <div class="pm_25-info">
            {{ pm2_5 }} µg/m³
            <span>{{ getPm2_5Text(pm2_5) }}</span>
          </div>
        </div>
        <div
            class="pm10"
            :style="getPm10Style(pm10)">
          <div class="pm10-title">
            <span>초미세먼지</span>
          </div>
          <div class="pm10-info">
            {{ pm10 }} µg/m³
            <span>{{ getPm10Text(pm10) }}</span>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>