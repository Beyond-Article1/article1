<script setup>
const props = defineProps({
  text: String
});
</script>

<template>
  <button class="styled-button">
    {{ text }}
  </button>
</template>

<style scoped>
.styled-button {
  background-color: #D8EDFF; /* 버튼 배경색 */
  color: black; /* 글자색 */
  padding: 5px 10px; /* 패딩 줄이기 */
  text-align: center; /* 텍스트 중앙 정렬 */
  text-decoration: none; /* 텍스트 장식 제거 */
  display: inline-block; /* 인라인 블록으로 설정 */
  font-size: 10px; /* 글자 크기 줄이기 */
  margin: 4px 2px; /* 여백 */
  cursor: pointer; /* 커서 모양 변경 */
  transition: background-color 0.3s; /* 배경색 전환 효과 */
  border: 1px solid #cccccc; /* 테두리 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 그림자 */
  width: 52px;
}

.styled-button:hover {
  background-color: #c0ddfa; /* hover 시 배경색 변화 */
}
</style>
