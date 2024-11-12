<script setup>
import axios from 'axios';
import {onMounted, ref} from "vue";
import {useWeatherStore} from "@/store/useWeatherStore.js";
import {useSelectedInfoStore} from "@/store/selectedInfoStore.js";

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
const nowDescription = ref('');

const openWeather = ref([]);  // 날씨 데이터를 저장할 객체

// 날씨관련 배경 변경
const weatherStore = useWeatherStore();

// 시간 선택 스토어에서 값 받아오기
const selectStore = useSelectedInfoStore();

onMounted(() => {
  getWeather();
})
async function getWeather () {
  let date = new Date(selectStore.selectedDate);
  date.setHours(date.getHours() + 9);
  time.value = date.toISOString().split('.')[0]
  lat.value = selectStore.selectedLatitude;
  lon.value = selectStore.selectedLongitude;
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
          nowWeatherCode.value = openWeather.value.realWeatherCode;
          nowWeatherIcon.value = openWeather.value.nowWeatherIcon;
          nowTemp.value = Math.round(openWeather.value.nowTemp);
          nowFeelsLike.value = Math.round(openWeather.value.nowFeelsLike);
          highTemp.value = Math.round(openWeather.value.highTemp);
          lowTemp.value = Math.round(openWeather.value.lowTemp);
          pm2_5.value = openWeather.value.pm2_5;
          pm10.value = openWeather.value.pm10;
          nowDescription.value = openWeather.value.nowWeatherDescription;

          console.log("nowWeatherIcon", nowWeatherIcon.value);
          console.log("pm2_5", pm2_5.value);
          console.log("pm10", pm10.value);
          weatherStore.setWeatherCode(nowWeatherCode.value);
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
    <div class="weather-container_top">
      <div class="weather-icon">
        <!-- 옵셔널 체이닝을 사용하여 안전하게 접근 -->
        <img :src="`https://openweathermap.org/img/wn/${nowWeatherIcon}@2x.png`" alt="날씨 아이콘" width="180px">
      </div>
      <div class="weather-temp">
        <div class="now-temp">{{ nowTemp || '데이터 없음' }} ℃</div>
      </div>
      <div class="weather-description">{{ nowDescription || '데이터 없음' }}</div>
      <div class="weather-feel">체감 {{ nowFeelsLike }} ℃</div>
      <hr>
    </div>

    <div class="weather-container_bottom">
      <div class="min_max-temp">
        <div class="min-temp">
          <div class="min-temp-title temp-title">
            <span>최저기온</span>
          </div>
          <div class="min-temp-main temp">
            <span>{{ lowTemp || '온도 데이터 없음' }} ℃</span>
          </div>
        </div>

        <div class="max-temp">
          <div class="max-temp-title temp-title">
            <span>최고기온</span>
          </div>
          <div class="max-temp-main temp">
            <span>{{ highTemp || '온도 데이터 없음' }} ℃</span>
          </div>
        </div>
      </div>
      <div class="pm-box">
        <div
            class="pm2_5 pm"
            :style="getPm2_5Style(pm2_5)">
          <div class="pm2_5-title pm-title">
            <span>미세먼지</span>
          </div>
          <div class="pm_25-info pm-info">
            <span>{{ getPm2_5Text(pm2_5) }}</span>
          </div>
        </div>
        <div
            class="pm10 pm"
            :style="getPm10Style(pm10)">
          <div class="pm10-title pm-title">
            <span>초미세먼지</span>
          </div>
          <div class="pm10-info pm-info">
            <span>{{ getPm10Text(pm10) }}</span>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
  .weather-container{
    width: 350px;
    text-align: center;
    background-color: rgb(0, 0, 0, 0.2);
    height: 480px;
    padding: 10px;
    border-radius: 15px;
    max-height: 700px;
  }
  .now-temp, .weather-description{
    font-size: 23px;
    font-weight: bold;
  }
  .weather-description{
    margin-top: 5px;
  }
  .weather-container_bottom{
    margin-top: 5%;
  }
  .weather-feel{
    margin-top: 15px;
    text-align: right;
    font-size: 14px;
  }
  hr{
    margin-top: 5%;
  }
  .min_max-temp{
    display: flex;
    justify-content: space-around;
    font-size: 17px;
    font-weight: bold;
  }
  .min_max-temp .temp{

  }
  .pm-box{
    display: flex;
    justify-content: space-around;
    margin-top: 5%;
  }
  .pm{
    width: 90px;
    border-radius: 10px;
  }
  .pm-title span{
    font-size: 12px;
  }
  .pm-info span{
    font-weight: bold;
  }
</style>