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

    <!-- 썸네일 이미지 표시 영역 -->
    <div v-if="boardData.boardPictureList && boardData.boardPictureList.length > 0" class="thumbnail-container">
      <h3>업로드된 이미지</h3>
      <div class="thumbnail-gallery">
        <img
            v-for="(picture, index) in boardData.boardPictureList"
            :key="index"
            :src="picture.pictureUrl"
            :alt="picture.pictureOriginName"
            class="thumbnail-image"
        />
      </div>
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
          text="수정"
          @click="sendData"
      />
    </div>
  </Container>
</template>

<script setup>
// vue
import {onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";

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
const route = useRoute();

// 입력값을 저장할 ref
const title = ref('');
const content = ref('');
const imageList = ref([]);

// 게시글 데이터
const boardData = ref({});

// 게시글 데이터 조회
async function fetchData() {
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

      title.value = boardData.value.boardTitle; // 게시글 제목
      content.value = boardData.value.boardContent; // 게시글 내용

    } else {
      console.log("게시글 조회 실패", `코드: ${response.status}`);
    }
  } catch (error) {
    console.error("어라라...?\n", error);
  }
}

// 게시글 데이터 로드
onMounted(() => {
  fetchData();
});

// 이미지 리스트 변경 감지
watch(imageList, (newList) => {
  console.log("업로드된 이미지 리스트:", newList);
});

// 게시글 수정 데이터 전송
async function sendData() {
  try {
    const formData = new FormData();
    formData.append("boardTitle", title.value);
    formData.append("boardContent", content.value);

    // 이미지 리스트를 FormData에 추가 (Blob으로 변환)
    imageList.value.forEach(({name, src}) => {
      const byteString = atob(src.split(',')[1]);
      const mimeString = src.split(',')[0].split(':')[1].split(';')[0];
      const ab = new ArrayBuffer(byteString.length);
      const ia = new Uint8Array(ab);
      for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
      }
      const blob = new Blob([ab], {type: mimeString});

      // Blob을 FormData에 추가
      formData.append("imageList", blob, name);
    });

    // FormData의 내용을 로그로 출력하여 확인
    for (const [key, value] of formData.entries()) {
      console.log(key, value);
    }

    // 서버에 PUT 요청
    const response = await axios.put(`/board/${route.params.boardSeq}`, formData, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
        'Content-Type': 'multipart/form-data'
      }
    });

    if (response.status === 200 || response.status === 201) {
      console.log("게시글 수정 성공", response.data);
      router.push(`/board/${route.params.boardSeq}`);
    } else {
      console.log("수정 실패");
      console.log(response.status);
    }
  } catch (error) {
    console.error("게시글 수정 중 오류가 발생했습니다.", error);
  }
}
</script>

<style scoped>
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

.thumbnail-container {
  margin-top: 20px; /* 썸네일 상단 여백 */
}

.thumbnail-gallery {
  display: flex; /* Flexbox 사용 */
  flex-wrap: wrap; /* 줄 바꿈 */
}

.thumbnail-image {
  width: 100px; /* 썸네일 크기 조정 */
  height: auto; /* 비율 유지 */
  margin: 5px; /* 이미지 간격 */
}

.uploaded-image-title {
  margin-top: 20px; /* 제목 상단 여백 */
}
</style>