<!-- DeleteModal.vue -->
<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <h2>정말 삭제하시겠습니까?</h2>
      <div class="modal-buttons">
        <button @click="confirmDelete">예</button>
        <button @click="cancelDelete">아니오</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  onConfirm: {
    type: Function,
    required: true
  },
  onCancel: {
    type: Function,
    required: true
  }
});

function confirmDelete() {
  props.onConfirm(); // 부모 컴포넌트의 confirmDelete 호출
}

function cancelDelete() {
  props.onCancel(); // 부모 컴포넌트의 cancelDelete 호출
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}

.modal-buttons {
  margin-top: 20px;
}

.modal-buttons button {
  margin: 0 10px;
}
</style>
