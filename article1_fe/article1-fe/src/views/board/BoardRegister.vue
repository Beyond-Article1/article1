<script setup>
import Container from "@/components/board/Container.vue";
import RegisterInput from "@/components/board/RegisterInput.vue";
import FileUpload from "@/components/board/FileUpload.vue";
import NormalButton from "@/components/board/NormalButton.vue";
</script>

<template>

    <Container>
      <div>
        <RegisterInput
            text="제목"
        />
      </div>
      <div>
        <RegisterInput
            text="내용"
        />
      </div>
      <div>
        <FileUpload
            label="이미지 업로드"
        />
      </div>
      <div class="upload-notice">
        <p>이미지를 업로드 하신 후 클릭을 하셔야 본문에 첨부됩니다.</p>
        <p>이미지 업로드는 최대 15MB까지 가능합니다.</p>
        <p>총 30개까지 업로드 가능합니다.</p>
        <p>통신 환경에 따라 고용량 이미지의 업로드가 실패할 수 있습니다.</p>
        <p class="warning">※ 정보통신망에서 불법촬영물등을 유통할 경우 「전기통신사업법」 제22조의 5 제1항에 따른 삭제.접속차단 등 유통방지에 필요한 조치가 취해지며 「성폭력처벌법」 제14조 「청소년성보호법」 제11조에 따라 형사처벌을 받을수 있습니다.</p>
      </div>

      <div class="button-container">
        <NormalButton
            text="등록"
        />
      </div>
    </Container>

</template>

<style scoped>
#background {
  background: #E7F4FF;
  height: auto; /* 높이를 자동으로 설정 */
  overflow: auto; /* 스크롤이 가능하도록 설정 */
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
