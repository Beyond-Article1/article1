<script setup>
const props = defineProps({
  userSeq: Number,
  regData: {
    type: String,
    required: true
  }
});

// regData를 Date 객체로 변환
const regDateObject = new Date(props.regData);

// 날짜를 'yyyy-MM-dd HH:mm' 형식으로 포맷하는 함수
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}`;
};

</script>

<template>
  <div id="title-bar">
    <span class="user-seq">{{ userSeq }}</span>
    <span class="reg-data">{{ formatDate(regDateObject) }}</span>
  </div>
</template>

<style scoped>
#title-bar {
  display: flex; /* Flexbox 사용 */
  justify-content: space-between; /* 좌우 공간 분배 */
  align-items: center; /* 세로 중앙 정렬 */
  background: #D8EDFF;
  height: 36px;
  border: 1px solid #cccccc;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0 10px; /* 좌우 패딩 추가 */
}

.user-seq {
  flex: 0 1 auto; /* 고정 너비 유지 */
}

.reg-data {
  flex: 1; /* 중앙 정렬을 위해 flex-grow 사용 */
  text-align: center; /* 중앙 정렬 */
}
</style>
