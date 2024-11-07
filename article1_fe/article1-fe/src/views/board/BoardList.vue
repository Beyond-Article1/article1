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

// Jwt 토근 정보 확인
const authStore = useAuthStore();

// 게시글 목록 변수
const boardList = ref([]);

// 데이터 호출
const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/board", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      boardList.value = response.data;
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
  router.push(`/board/BoardRegister`);
}

onMounted(() => {
  console.log("access token");
  console.log(authStore.accessToken);

  fetchData();
})

</script>

<template>
  <div id="background">
    <Container>
      <table>
        <tbody>
        <!-- 공지사항 표시 (최대 2개) -->
        <tr v-if="noticeList && noticeList.length > 0" v-for="item in noticeList.slice(0, 2)" :key="item.boardSeq">
          <BoardNoticeLi v-if="item.boardPictureList && item.boardPictureList.length > 0"
                         v-on:click="goToDetailPage(item.boardSeq)"
                         :title="item.boardTitle"
                         :content="item.boardContent"
                         :date="formatDate(item.regDate)"
                         :writer="item.userSeq"
                         :imageUrl="item.boardPictureList[0]?.pictureUrl"
          />
          <BoardNoticeLi v-else
                         v-on:click="goToDetailPage(item.boardSeq)"
                         :title="item.boardTitle"
                         :content="item.boardContent"
                         :date="formatDate(item.regDate)"
                         :writer="item.userSeq"
                         :imageUrl="null"
          />
        </tr>

        <!-- 모든 일반 게시물 표시 (공지사항 제외) -->
        <tr v-if="regularPosts && regularPosts.length > 0" v-for="item in regularPosts" :key="item.boardSeq">
          <BoardLi v-if="item.boardPictureList && item.boardPictureList.length > 0"
                   v-on:click="goToDetailPage(item.boardSeq)"
                   :title="item.boardTitle"
                   :content="item.boardContent"
                   :date="formatDate(item.regDate)"
                   :writer="item.userSeq"
                   :imageUrl="item.boardPictureList[0]?.pictureUrl"
          />
          <BoardLi v-else
                   v-on:click="goToDetailPage(item.boardSeq)"
                   :title="item.boardTitle"
                   :content="item.boardContent"
                   :date="formatDate(item.regDate)"
                   :writer="item.userSeq"
                   :imageUrl="null"
          />
        </tr>
        </tbody>
      </table>
    </Container>
  </div>
</template>

<style scoped>
#background {
  background: #E7F4FF;
  height: auto; /* 높이를 자동으로 설정 */
  overflow: auto; /* 스크롤이 가능하도록 설정 */
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

tbody tr:hover {
  background-color: #f1f1f1;
}
</style>