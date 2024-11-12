<template>
  <div class="comment-input">
    <textarea v-model="commentText" placeholder="댓글을 입력하세요..." rows="3"></textarea>
    <div class="button-container">
      <NormalButton text="등록" @click="submitComment" />
    </div>
  </div>
</template>

<script setup>
// vue
import {ref} from 'vue';

// router
import {useRouter, useRoute} from "vue-router";

// axios
import axios from "axios";

// auth
import {useAuthStore} from "@/store/authStore.js";

// component
import NormalButton from "@/components/board/NormalButton.vue";

const route = useRoute();
const authStore = useAuthStore();
const emit = defineEmits(['submit']);
const commentText = ref('');

async function submitComment() {
  if (commentText.value) {
    // 댓글 등록 요청
    await registerReply(commentText.value);
    // 입력 필드를 초기화합니다.
    commentText.value = '';
    // 부모에게 댓글 등록 이벤트 발생
    emit('submit', commentText.value);
  }
}


async function registerReply(replyContent) {
  try {
    const boardSeq = route.params.boardSeq;
    const response = await axios.post(`/reply/${boardSeq}`, {
      replyContent: replyContent // 입력된 댓글 내용을 서버로 전송
    }, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`, // JWT 토큰을 헤더에 추가
      }
    });

    if (response.status === 200 || response.status === 201) {
      emit('submit', replyContent); // 부모 컴포넌트로 댓글 등록 이벤트 발생
    } else {
      alert("댓글 등록에 실패했습니다."); // 실패 메시지
    }
  } catch (error) {
    console.error("댓글 등록 중 오류가 발생했습니다.", error);
    alert("댓글 등록 중 오류가 발생했습니다."); // 오류 메시지
  }
}
</script>

<style scoped>
.comment-input {
  margin-top: 20px; /* 위쪽 여백 추가 */
  width: 100%; /* 부모 요소에 맞게 너비를 100%로 설정 */
  max-width: 600px; /* 최대 너비를 설정 (필요에 따라 조정) */
}

textarea {
  width: 95%; /* 너비를 100%로 설정 */
  padding: 10px;
  margin-bottom: 10px; /* 버튼과의 간격 */
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: none; /* 크기 조정 비활성화 */
}

.button-container {
  display: flex;
  justify-content: flex-end; /* 버튼을 오른쪽 끝으로 정렬 */
}
</style>
