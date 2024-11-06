<template>
  <div class="comment-input">
    <textarea v-model="commentText" placeholder="댓글을 입력하세요..." rows="3"></textarea>
    <div class="button-container">
      <NormalButton text="등록" @click="submitComment" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import NormalButton from "@/components/board/NormalButton.vue";

const emit = defineEmits(['submit']);

const commentText = ref('');

function submitComment() {
  if (commentText.value.trim()) {
    emit('submit', commentText.value);
    commentText.value = ''; // 입력 필드를 초기화합니다.
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
