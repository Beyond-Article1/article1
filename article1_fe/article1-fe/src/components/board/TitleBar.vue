<script setup>
const props = defineProps({
  userSeq: Number,
  regData: {
    type: String, // regData는 문자열로 받도록 변경
    required: true
  }
});

// 날짜를 'yyyy-MM-dd HH:mm' 형식으로 포맷하는 함수
const formatDate = (dateString) => {
  const date = new Date(dateString);

  // 유효한 날짜인지 확인
  if (isNaN(date.getTime())) {
    return '';
  }

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
    <span class="user-seq">{{ props.userSeq }}</span>
    <span class="reg-data">{{ formatDate(props.regData) }}</span>
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
