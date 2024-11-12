<script setup>

// Component
import Container from "@/components/board/Container.vue";
import BoardNoticeLi from "@/components/board/BoardNoticeLi.vue";
import BoardLi from "@/components/board/BoardLi.vue";

// Vue
import {computed, onMounted, ref} from "vue";

// Axios
import axios from "axios";

// Router
import {useRouter} from "vue-router";

// Pinia
import {useAuthStore} from "@/store/authStore.js";
import NormalButton from "@/components/board/NormalButton.vue";

// Jwt 토근 정보 확인
const authStore = useAuthStore();

// 호출 데이터
const boardList = ref([]);

// 데이터 호출
const fetchData = async () => {
  try {
    const response = await axios.get("/board", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      boardList.value = response.data;
      // console.log(boardList.value);
    } else {
      console.log("게시글 목록 조회 실패");
      console.log(`코드: ` + response.status);
    }

  } catch (error) {
    console.error("어라라...?\n", error);
  }
};

// 라우터
const router = useRouter();

// 정렬된 게시물 목록 생성
const sortedBoardList = computed(() => {
  return boardList.value.sort((a, b) => {
    return new Date(b.regDate) - new Date(a.regDate); // regDate 기준으로 정렬
  });
});

// 공지사항 목록 생성
const noticeList = computed(() => {
  return sortedBoardList.value.filter(item => item.boardIsNotice); // 모든 공지사항 반환
});

// 일반 게시물 목록 생성 (공지사항 제외)
const regularPosts = computed(() => {
  return sortedBoardList.value.filter(item => !item.boardIsNotice);
});

// 날짜 형식 변환 함수
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const today = new Date();

  // 오늘 날짜와 비교하여 동일한 경우 HH:mm 형식으로 반환
  if (
      date.getFullYear() === today.getFullYear() &&
      date.getMonth() === today.getMonth() &&
      date.getDate() === today.getDate()
  ) {
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  } else {
    // yyyy-MM-dd 형식으로 반환
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
};

// 상세 조회 페이지 이동
function goToDetailPage(boardSeq) {
  router.push(`/board/${boardSeq}`);
}

// 게시들 등록 페이지 이동
function goToRegister() {
  router.push(`/board/register`);
}

onMounted(() => {
  fetchData();
})

const searchQuery = ref(""); // 검색어를 저장할 변수

// 검색어에 따라 필터링된 일반 게시물 목록
const filteredRegularPosts = computed(() => {
  return regularPosts.value.filter(item =>
      item.boardTitle.includes(searchQuery.value) // 제목에 검색어가 포함된 경우
  );
});

</script>

<template>
  <Container>
    <template v-if="noticeList.length > 0 || filteredRegularPosts.length > 0">
      <table>
        <tbody>
        <!-- 공지사항 표시 (최대 2개) -->
        <tr v-if="noticeList.length > 0" v-for="item in noticeList.slice(0, 2)" :key="item.boardSeq">
          <BoardNoticeLi
              v-if="item.boardPictureList && item.boardPictureList.length > 0"
              @click="goToDetailPage(item.boardSeq)"
              :title="item.boardTitle"
              :content="item.boardContent"
              :date="formatDate(item.regDate)"
              :writer="item.userSeq"
              :imageUrl="item.boardPictureList[0]?.pictureUrl"
          />
          <BoardNoticeLi
              v-else
              @click="goToDetailPage(item.boardSeq)"
              :title="item.boardTitle"
              :content="item.boardContent"
              :date="formatDate(item.regDate)"
              :writer="item.userSeq"
              :imageUrl="null"
          />
        </tr>

        <!-- 모든 일반 게시물 표시 (공지사항 제외) -->
        <tr v-if="filteredRegularPosts.length > 0" v-for="item in filteredRegularPosts" :key="item.boardSeq">
          <BoardLi
              v-if="item.boardPictureList && item.boardPictureList.length > 0"
              @click="goToDetailPage(item.boardSeq)"
              :title="item.boardTitle"
              :content="item.boardContent"
              :date="formatDate(item.regDate)"
              :writer="item.userSeq"
              :imageUrl="item.boardPictureList[0]?.pictureUrl"
          />
          <BoardLi
              v-else
              @click="goToDetailPage(item.boardSeq)"
              :title="item.boardTitle"
              :content="item.boardContent"
              :date="formatDate(item.regDate)"
              :writer="item.userSeq"
              :imageUrl="null"
          />
        </tr>
        </tbody>
      </table>
    </template>

    <template v-else>
      <td colspan="5" class="no-results">조회 결과가 없습니다.</td>
    </template>

    <div class="button-container">
      <NormalButton @click="goToRegister" text="글쓰기" />
    </div>

    <div class="search-container">
      <input type="text" v-model="searchQuery" placeholder="검색할 제목을 입력하세요." class="search-input" />
      <button class="search-button" @click="fetchData">검색</button>
    </div>
  </Container>
</template>


<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

tbody tr:hover {
  background-color: #f1f1f1;
}

.button-container {
  display: flex; /* Flexbox 사용 */
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-top: 10px; /* 버튼 위쪽 여백 추가 */
}

.no-results {
  text-align: center; /* 가운데 정렬 */
  font-size: 1.5em; /* 폰트 크기 조정 */
  font-weight: bold; /* 볼드체 */
  color: #555; /* 텍스트 색상 (선택 사항) */
}

.search-container {
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 가운데 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  margin-top: 20px; /* 위쪽 여백 추가 */
}

.search-input {
  width: 50%; /* 입력란의 너비를 절반으로 설정 */
  padding: 10px; /* 내부 여백 추가 */
  border: 1px solid #ccc; /* 테두리 색상 */
  border-radius: 5px; /* 모서리 둥글게 */
  font-size: 1em; /* 폰트 크기 */
  margin-right: 10px; /* 버튼과의 간격 추가 */
}

.search-button {
  padding: 10px 15px; /* 버튼 내부 여백 */
  background-color: #007bff; /* 버튼 배경색 */
  color: white; /* 텍스트 색상 */
  border: none; /* 테두리 없애기 */
  border-radius: 5px; /* 모서리 둥글게 */
  cursor: pointer; /* 커서 포인터로 변경 */
  transition: background-color 0.3s ease; /* 배경색 변화 효과 */
}

.search-button:hover {
  background-color: #0056b3; /* 호버 시 배경색 변화 */
}

</style>