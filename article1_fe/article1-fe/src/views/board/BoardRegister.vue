<script setup>
// vue
import {ref, watch} from "vue";
import {useRouter} from "vue-router";

// axios
import axios from "axios";

// store
import {useAuthStore} from "@/store/authStore.js";

// component
import Container from "@/components/board/Container.vue";
import RegisterInput from "@/components/board/RegisterInput.vue";
import FileUpload from "@/components/board/FileUpload.vue";
import NormalButton from "@/components/board/NormalButton.vue";

// Jwt token value
const authStore = useAuthStore();

// router
const router = useRouter();

// 입력값을 저장할 ref
const title = ref('');
const content = ref('');
const imageList = ref([]);

watch(imageList, (newList) => {
  console.log("업로드된 이미지 리스트:", newList);
});

async function sendData() {
  try {
    const formData = new FormData();

    // 제목과 내용을 FormData에 추가
    formData.append('boardTitle', title.value); // 제목
    formData.append('boardContent', content.value); // 내용

    // 이미지 리스트를 FormData에 추가
    imageList.value.forEach(({ name, src }, index) => {
      // src를 Blob으로 변환하는 로직
      const byteString = atob(src.split(',')[1]);
      const mimeString = src.split(',')[0].split(':')[1].split(';')[0];
      const ab = new ArrayBuffer(byteString.length);
      const ia = new Uint8Array(ab);
      for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
      }
      const blob = new Blob([ab], { type: mimeString });

      // Blob을 FormData에 추가 (원래 파일 이름 사용)
      formData.append('imageList', blob, name); // 원래 파일 이름으로 설정
    });

    // FormData의 내용을 로그로 출력하여 확인
    for (const [key, value] of formData.entries()) {
      console.log(key, value);
    }

    // 서버에 POST 요청
    const response = await axios.post(`/board`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      console.log("게시글 등록 성공", response.data);
      router.push(`/board`);
    }

  } catch (error) {
    console.error("게시글 등록 중 오류가 발생했습니다.", error);
  }
}
</script>

<template>
  <Container>
    <div>
      <RegisterInput
          text="제목"
          v-model="title"
      />
    </div>
    <div>
      <RegisterInput
          text="내용"
          v-model="content"
      />
    </div>
    <div>
      <FileUpload
          label="이미지 업로드"
          :imageList="imageList"
          @update:imageList="imageList = $event"
      />
    </div>
    <div class="upload-notice">
      <p>이미지를 업로드 하신 후 클릭을 하셔야 본문에 첨부됩니다.</p>
      <p>이미지 업로드는 최대 15MB까지 가능합니다.</p>
      <p>총 30개까지 업로드 가능합니다.</p>
      <p>통신 환경에 따라 고용량 이미지의 업로드가 실패할 수 있습니다.</p>
      <p class="warning">※ 정보통신망에서 불법촬영물등을 유통할 경우 「전기통신사업법」 제22조의 5 제1항에 따른 삭제.접속차단 등 유통방지에 필요한 조치가 취해지며 「성폭력처벌법」 제14조
        「청소년성보호법」 제11조에 따라 형사처벌을 받을 수 있습니다.</p>
    </div>

    <div class="button-container">
      <NormalButton
          text="등록"
          @click="sendData"
      />
    </div>
  </Container>
</template>

<style scoped>
#background {
  background: #E7F4FF;
  height: auto; /* 높이를 자동으로 설정 */
  overflow: auto;
}

.upload-notice {
  margin-top: 20px; /* 상단 여백 추가 */
  font-size: 8px; /* 글자 크기 설정 */
  color: #afafaf; /* 글자 색상 설정 */
}

.upload-notice p {
  margin: 5px 0; /* 상하 마진을 좁혀 간격 조정 */
}

.warning {
  color: black;
}

.button-container {
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 중앙 정렬 */
  margin-top: 20px; /* 버튼 상단 여백 */
}
</style>
