<template>
  <div class="pagination">
    <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">이전</button>

    <button v-for="page in pages"
            :key="page"
            @click="goToPage(page)"
            :class="{ active: page === currentPage }">
      {{ page }}
    </button>

    <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">다음</button>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { defineProps } from 'vue';

const props = defineProps({
  currentPage: Number,
  totalPages: Number,
  goToPage: Function
});

// 페이지 번호 생성
const pages = computed(() => {
  const pageCount = 10; // 표시할 페이지 수
  const total = props.totalPages;

  // 페이지 번호 배열 생성
  const pageArray = [];
  const startPage = Math.max(1, props.currentPage - Math.floor(pageCount / 2));
  const endPage = Math.min(total, startPage + pageCount - 1);

  for (let i = startPage; i <= endPage; i++) {
    if (i > 0) {
      pageArray.push(i);
    }
  }

  return pageArray;
});
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

button {
  margin: 0 5px;
  padding: 8px 12px;
  cursor: pointer;
}

button:disabled {
  cursor: not-allowed;
  background-color: #ccc;
}

button.active {
  font-weight: bold;
  background-color: #007BFF;
  color: white;
}
</style>
