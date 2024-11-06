<template>
  <div class="file-upload" @dragover.prevent @drop.prevent="handleDrop">
    <div class="label">
      <span>이미지 업로드</span>
    </div>
    <input
        :id="inputId"
        type="file"
        @change="handleFileUpload"
        accept="image/*"
        multiple
        class="file-input"
    />
    <div class="drop-area" @click="triggerFileInput">
      <p>여기에 이미지를 드래그하거나 클릭하여 선택하세요.</p>
    </div>
    <div class="image-preview">
      <img v-for="(image, index) in images" :key="index" :src="image" alt="Uploaded image" />
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps } from 'vue';

const props = defineProps({
  label: {
    type: String,
    required: true
  }
});

const inputId = `file-upload-${Math.random().toString(36).substr(2, 9)}`; // 고유 ID 생성
const images = ref([]); // 업로드된 이미지 URL을 저장할 배열

function handleFileUpload(event) {
  const files = event.target.files; // 선택된 모든 파일
  handleFiles(files);
}

function handleDrop(event) {
  const files = event.dataTransfer.files; // 드롭된 파일
  handleFiles(files);
}

function handleFiles(files) {
  if (files.length > 0) {
    Array.from(files).forEach(file => {
      const reader = new FileReader();
      reader.onload = (e) => {
        images.value.push(e.target.result); // 이미지 URL 추가
      };
      reader.readAsDataURL(file); // 파일을 데이터 URL로 읽기
    });
  }
}

function triggerFileInput() {
  const fileInput = document.getElementById(inputId);
  fileInput.click(); // 클릭 시 파일 선택 대화상자 열기
}
</script>

<style scoped>
.file-upload {
  margin-top: 20px; /* 여백 추가 */
}

.label {
  font-size: 20px;
  font-weight: bold;
}

.file-input {
  display: none; /* 파일 입력 숨기기 */
}

.drop-area {
  border: 2px dashed #ccc; /* 테두리 스타일 */
  border-radius: 4px; /* 둥글게 */
  padding: 20px; /* 여백 추가 */
  text-align: center; /* 가운데 정렬 */
  cursor: pointer; /* 커서 스타일 변경 */
  margin-top: 10px; /* 상단 여백 */
}

.drop-area:hover {
  border-color: #888; /* 드래그 시 색상 변경 */
}

.image-preview {
  margin-top: 20px; /* 상단 여백 */
  display: flex; /* Flexbox 사용 */
  flex-wrap: wrap; /* 줄 바꿈 */
}

.image-preview img {
  max-width: 100px; /* 최대 너비 설정 */
  margin-right: 10px; /* 이미지 간의 간격 */
  margin-bottom: 10px; /* 이미지 하단 간격 */
}
</style>
