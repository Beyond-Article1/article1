<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/authStore.js';
import Pagination from '@/components/common/Pagination.vue';

const authStore = useAuthStore();
const detail = ref([]);
const board = ref([]);
const searchQuery = ref(''); // 검색어를 저장할 ref
const currentPage = ref(1);
const itemsPerPage = 4;

const fetchDataDetail = async () => {
  try {
    const response = await axios.get("/user/detail", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      detail.value = Array.isArray(response.data) ? response.data : [response.data];
    } else console.error("게시글 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const fetchDataBoard = async () => {
  try {
    const response = await axios.get("/board", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      board.value = response.data;
    } else console.error("게시글 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

// userSeq가 같은 게시글 필터링
const filteredBoard = computed(() => {
  if (detail.value.length === 0) return [];

  const userSeqSet = new Set(detail.value.map(user => user.userSeq));

  // 검색어에 따라 게시글 필터링
  const searchTerm = searchQuery.value.toLowerCase();

  return board.value
      .filter(item => userSeqSet.has(item.userSeq))
      .filter(item => {
        return (
            item.boardTitle.toLowerCase().includes(searchTerm) || item.boardContent.toLowerCase().includes(searchTerm)
        );
      });
});

const totalPages = computed(() => {
  return Math.ceil(filteredBoard.value.length / itemsPerPage);
});

const paginatedBoard = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;

  return filteredBoard.value.slice(start, start + itemsPerPage);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;

  currentPage.value = page;
};

// 검색어 변경 시 페이지 리셋
watch(searchQuery, () => {
  currentPage.value = 1; // 검색 시 첫 페이지로 이동
});

onMounted(() => {
  fetchDataDetail();
  fetchDataBoard();
});
</script>

<template>
  <div>
    <div class="header">
      <h1>내가 작성한 게시글</h1>
      <div class="search-container">
        <input type="text" v-model="searchQuery" placeholder="검색어 입력" class="search-input"/>
        <button class="search-button" @click="currentPage = 1">검색</button>
      </div>
    </div>

    <div class="board-table">
      <div class="table-header">
        <span class="header-cell">등록일</span>
        <span class="header-cell">제목</span>
        <span class="header-cell">내용</span>
        <span class="header-cell">이름</span>
      </div>

      <div class="table-row" v-for="item in paginatedBoard" :key="item.boardSeq">
        <div class="table-cell">{{ item.regDate.slice(0, 10) }}</div>
        <div class="table-cell">{{ item.boardTitle }}</div>
        <div class="table-cell">{{ item.boardContent }}</div>
        <div class="table-cell">{{ detail.find(user => user.userSeq === item.userSeq)?.userName }}</div>
      </div>
    </div>

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        :goToPage="goToPage"
    />
  </div>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.search-container {
  display: flex;
  align-items: center;
}

.search-input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 200px;
  margin-right: 0.5rem;
}

.search-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  background-color: #007BFF;
  color: white;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0056b3;
}

.board-table {
  background-color: #f9f9ff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 1fr 2fr 1fr;
  padding: 10px;
  background-color: #cce4ff;
  border-radius: 8px;
  font-weight: bold;
  text-align: center; /* 가운데 정렬 추가 */
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 2fr 1fr;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.table-cell {
  padding: 10px 5px;
  text-align: center;
}
</style>