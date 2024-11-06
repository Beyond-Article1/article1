<template>
  <div class="container">
    <slot/> <!-- 부모 컴포넌트에서 내용을 전달받기 위한 슬롯 -->
  </div>
</template>

<style scoped>
.container {
  background-color: white;
  padding: 20px; /* 내부 여백 */
  width: 55%;
  margin: auto; /* 수평 중앙 정렬 */
  margin-top: 5%;
}
</style>