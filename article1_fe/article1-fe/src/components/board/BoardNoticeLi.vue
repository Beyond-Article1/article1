<template>
  <div>
    <!-- 헤더(제목) -->
    <div>
      <span class="list-title">{{ title }}</span>
    </div>
    <!-- 바디 -->
    <div class="body">
      <!-- 내용 -->
      <div class="content">
        <div class="text">
          <span>{{ content }}</span>
        </div>
        <!-- 시간 / 작성자 -->
        <div class="day-writer">
          <span>{{ date }} / {{ writer }}</span>
        </div>
      </div>
      <!-- 사진 -->
      <div class="thumbnail" v-if="imageUrl">
        <img :src="imageUrl" alt="이미지 로딩중"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  title: String,
  content: String,
  date: String,
  writer: Number,
  imageUrl: String
});
</script>

<style scoped>
.body {
  height: 140px;
  display: flex; /* 가로 배치를 위해 flexbox 사용 */
  justify-content: space-between; /* 양 끝으로 배치 */
  border-left: 3px solid red;
  border-bottom: 2px solid #ccc;
  padding-bottom: 10px;
  margin-bottom: 2%;
  padding-left: 10px;
}

.content {
  margin-top: 1%;
  display: flex; /* 세로 배치를 위해 flexbox 사용 */
  justify-content: space-between;
  flex-direction: column; /* 세로 방향으로 요소 배치 */
  flex: 1; /* 가용 공간을 차지하도록 설정 */
  margin-right: 20px; /* content와 thumbnail 사이의 간격 */
  overflow: hidden; /* 넘치는 내용 숨기기 */
}

.text {
  display: -webkit-box; /* Flexbox 사용 */
  -webkit-box-orient: vertical; /* 수직 방향으로 정렬 */
  -webkit-line-clamp: 4; /* 최대 2줄로 제한 */
  overflow: hidden; /* 넘치는 부분 숨기기 */
  text-overflow: ellipsis; /* 잘린 부분에 ... 표시 */
}

.thumbnail {
  display: flex; /* 가로 배치를 위해 flexbox 사용 */
  align-items: center; /* 세로 중앙 정렬 */
}

.thumbnail img {
  max-width: 200px;
  min-width: 0;
  max-height: 140px;
  min-height: 0;
}

.list-title {
  font-weight: bold;
  border-left: 3px solid red;
  font-size: 24px;
  color: red;
  padding-left: 10px;
}
</style>
