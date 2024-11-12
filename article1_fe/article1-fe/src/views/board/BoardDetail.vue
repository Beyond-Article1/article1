<script setup>
// vue
import {onMounted, ref} from "vue";

// router
import {useRoute, useRouter} from "vue-router";

// axios
import axios from "axios";

// pinia
import {useAuthStore} from "@/store/authStore.js";

// component
import Container from "@/components/board/Container.vue";
import TitleBar from "@/components/board/TitleBar.vue";
import ContentArea from "@/components/board/ContentArea.vue";
import NormalButton from "@/components/board/NormalButton.vue";
import ReplyLi from "@/components/reply/ReplyLi.vue";
import ReplyInput from "@/components/reply/ReplyInput.vue";
import Modal from "@/components/board/DeleteModal.vue"; // 모달 컴포넌트 가져오기

// Jwt 토큰 정보 확인
const authStore = useAuthStore();

// 라우터
const router = useRouter();
const route = useRoute();

// 호출된 데이터
const boardData = ref({
  userSeq: null,
  regDate: '',
  boardTitle: '',
  boardContent: '',
  boardPictureList: []
}); // 게시글
const replyList = ref([]); // 댓글

// 모달 가시성 상태
const showModal = ref(false);

// 모달 활성화
function openModal() {
  showModal.value = true; // 모달 열기
}

// 게시글 조회
async function fetchBoardData() {
  try {
    const boardSeq = route.params.boardSeq;
    const response = await axios.get(`/board/${boardSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      boardData.value = response.data;
      console.log("조회된 게시글 데이터", boardData.value);

      // 데이터 구조 확인
      if (!boardData.value.userSeq || !boardData.value.regDate) {
        console.error("게시글 데이터의 userSeq 또는 regDate가 비어 있습니다.");
      }
    } else {
      console.log("게시글 조회 실패", `코드: ${response.status}`);
    }
  } catch (error) {
    console.error("어라라...?\n", error);
  }
}

// 댓글 목록 조회
async function fetchReplyList() {
  try {
    const boardSeq = route.params.boardSeq;
    const response = await axios.get(`/reply/${boardSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      replyList.value = response.data;
      console.log("조회된 댓글 데이터", replyList.value);
    } else if (response.status === 404) {
      console.log("게시글에 댓글이 없습니다.")
    } else {
      console.log("댓글 조회 실패", `코드: ${response.status}`);
    }
  } catch (error) {
    console.error("어라라...?\n", error);
  }
}

// 댓글 등록 후 목록 업데이트
async function handleCommentSubmit(newComment) {
  // 댓글 목록을 다시 가져옵니다.
  await fetchReplyList();
}

// 댓글 삭제 처리
function handleDeleteReply(replySeq) {
  // 댓글 목록에서 삭제된 댓글을 제거
  replyList.value = replyList.value.filter(reply => reply.replySeq !== replySeq);
}

// 삭제 요청
async function deleteBoard() {
  try {
    const boardSeq = route.params.boardSeq;
    const response = await axios.delete(`/board/${boardSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 204) {
      console.log("게시글 삭제에 성공하였습니다.");
      router.push(`/board`);
    } else if (response.status === 403) {
      console.error("권한이 없습니다.");
      alert("권한이 없습니다.");
    } else {
      console.log(`삭제에 실패하였습니다. 코드: ${response.status}`);
      alert(`삭제에 실패하였습니다.`);
    }
  } catch (error) {
    console.error("어라라...?\n", error);
  }
}

// 게시글 삭제 확인
function confirmDelete() {
  deleteBoard();
  showModal.value = false; // 모달 닫기
}

// 게시글 삭제 취소
function cancelDelete() {
  showModal.value = false; // 모달 닫기
}

// 목록 페이지 이동
function goToList() {
  router.push(`/board`);
}

// 게시글 등록 페이지 이동
function goToRegister() {
  router.push('/board/register');
}

// 컴포넌트가 마운트 될 때 데이터 가져오기
onMounted(() => {
  fetchBoardData();
  fetchReplyList();
});
</script>

<template>
  <Container>
    <TitleBar
        :user-seq="boardData.userSeq"
        :reg-data="boardData.regDate"
    />
    <ContentArea
        :board-title="boardData.boardTitle"
        :board-content="boardData.boardContent"
        :board-picture-list="boardData.boardPictureList"
        @delete="openModal"
    />
    <div class="buttons">
      <NormalButton @click="goToList" text="목록"/>
      <NormalButton @click="goToRegister" text="글쓰기"/>
    </div>
    <template v-if="replyList.length > 0">
      <hr class="divider"/>
      <div class="replies">
        <table class="reply-table">
          <tbody>
          <tr v-for="reply in replyList" :key="reply.replySeq">
            <td>
              <ReplyLi
                  :user-seq="reply.replyUserSeq"
                  :reply-content="reply.replyContent"
                  :reg-date="reply.regDate"
                  :reply-seq="reply.replySeq"
                  @delete="handleDeleteReply"
              />
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </template>
    <hr class="divider"/>
    <div class="reply-input-container">
      <ReplyInput @submit="handleCommentSubmit"/>
    </div>

    <!-- 모달 추가 -->
    <Modal
        v-if="showModal"
        :on-confirm="confirmDelete"
        :on-cancel="cancelDelete"
    />
  </Container>
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
  background-color: #f1f1f1
}

.buttons {
  display: flex; /* Flexbox 사용 */
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-top: 20px; /* 위쪽 여백 추가 (선택 사항) */
}

.divider {
  border: 1px solid #cccccc; /* 경계선 색상 및 두께 */
  margin: 10px 0; /* 위아래 여백 */
  width: 100%; /* 경계선의 너비를 100%로 설정 */
}

.replies {
  margin-top: 20px; /* 댓글 목록과 다른 요소 간의 여백 */
}

.reply-input-container {
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 가운데 정렬 */
  margin-top: 20px; /* 위쪽 여백 추가 */
}
</style>
