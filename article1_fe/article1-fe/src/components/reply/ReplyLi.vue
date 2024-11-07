<script setup>
// component
import SmallButton from "@/components/board/SmallButton.vue";

// axios
import axios from "axios";

// router
import {useRoute} from "vue-router";

// pinia
import {useAuthStore} from "@/store/authStore.js";

// Jwt 토큰 정보 확인
const authStore = useAuthStore();
const route = useRoute();

// props
const props = defineProps({
  userSeq: Number,
  replyContent: String,
  regDate: String,
  replySeq: Number // replySeq를 props로 추가
});

// emit 정의
const emit = defineEmits(['delete']); // 'delete' 이벤트 정의

// 신고 이벤트
async function blame() {
  try {
    const response = await axios.post(`http://localhost:8003/blame/comment/${props.replySeq}`, {}, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      }
    });

    if (response.status === 200) {
      alert(`신고가 완료되었습니다.`);
    } else {
      alert(`신고에 실패했습니다.`);
    }
  } catch (error) {
    console.log("댓글 신고 중 오류가 발생했습니다.");
    console.log(error);
  }
}

// 삭제 이벤트
async function deleteReply() {
  try {
    const response = await axios.delete(`http://localhost:8080/reply/${props.replySeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      }
    });

    if (response.status === 200) {
      console.log("댓글 삭제 성공");
      // 댓글 삭제 후 부모 컴포넌트로 이벤트 emit
      emit('delete', props.replySeq); // 부모에게 삭제된 댓글의 ID를 전달
    } else {
      console.log("댓글 삭제 실패");
    }
  } catch (error) {
    console.log("댓글 삭제 중 오류가 발생했습니다.");
    console.log(error);
  }
}

// 날짜 형식을 변환하는 함수 (시간 제거)
function formatDate(dateString) {
  const date = new Date(dateString);

  // 연도, 월, 일 추출
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
  const day = String(date.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`; // 시간을 제외하고 원하는 형식으로 반환
}
</script>

<template>
  <div class="reply-list">
    <span class="user-seq">{{ userSeq }}</span>
    <span class="reply-content">{{ replyContent }}</span>
    <span class="reg-date">{{ formatDate(regDate) }}</span>
    <div class="buttons">
      <SmallButton v-on:click="blame" text="신고"/>
      <SmallButton v-on:click="deleteReply" text="삭제"/>
    </div>
  </div>
</template>

<style scoped>
.reply-list {
  display: flex; /* Flexbox 사용 */
  flex-direction: row; /* 수평 방향으로 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  margin-bottom: 15px; /* 각 댓글 간의 여백 */
  font-size: 10px; /* 글자 크기 설정 */
  border-bottom: 1px solid #cccccc;
}

.reply-list span {
  margin-right: 15px; /* 각 요소 간의 간격 추가 */
}

/* 모든 요소가 왼쪽 정렬되도록 설정 */
.user-seq, .reply-content {
  flex: 0; /* 기본 너비 유지 */
}

/* 댓글 내용이 가변적일 때도 왼쪽 정렬 유지 */
.reply-content {
  margin-left: 0; /* 댓글 내용 왼쪽 여백 제거 */
  flex-grow: 1; /* 댓글 내용이 필요한 공간을 차지하도록 설정 */
}

.reg-date {
  margin-left: 20px; /* 등록 날짜와 이전 요소 간의 간격 증가 */
  flex-shrink: 0; /* 등록 날짜가 줄어들지 않도록 설정 */
}

.buttons {
  display: flex; /* Flexbox 사용 */
  justify-content: flex-end; /* 버튼을 오른쪽 끝으로 정렬 */
}

/* 버튼 간의 간격 추가 */
small-button {
  margin-left: 10px; /* 버튼 간의 여백 추가 */
}
</style>
