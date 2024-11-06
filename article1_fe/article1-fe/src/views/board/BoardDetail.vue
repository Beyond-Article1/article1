<script setup>
import { ref } from "vue";
import Container from "@/components/board/Container.vue";
import TitleBar from "@/components/board/TitleBar.vue";
import ContentArea from "@/components/board/ContentArea.vue";
import SmallButton from "@/components/board/NormalButton.vue";
import ReplyLi from "@/components/reply/ReplyLi.vue";
import ReplyInput from "@/components/reply/ReplyInput.vue";

const boardData = ref({
  boardSeq: 2,
  userSeq: 124,
  boardTitle: "제목 2",
  boardContent: "내용 2",
  boardPictureList: [
    {
      pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
      description: "이미지 설명 2"
    },
    {
      pictureUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNUPTGaKZ5ppYrFn0Lcg2w33ozjP3CoLydPA&s",
      description: "추가 이미지 설명 2"
    }
  ],
  regDate: "2023-11-01T11:00:00",
  upDate: null,
  delDate: null,
  boardIsNotice: false
});

const replyList = ref([
  {
    replySeq: 1,
    boardSeq: 1,
    replyUserSeq: 123,
    replyContent: "내용 1",
    regDate: "2023-11-01T11:00:00",
    delDate: null,
    replyIsBlind: false
  },
  {
    replySeq: 2,
    boardSeq: 1,
    replyUserSeq: 124,
    replyContent: "내용 2",
    regDate: "2023-11-01T11:00:00",
    delDate: null,
    replyIsBlind: false
  },
  {
    replySeq: 3,
    boardSeq: 1,
    replyUserSeq: 125,
    replyContent: "내ㄴㅁㄹㄴㅁㄹㅇㄴㄴㅁㄹㄴㄹㄹㄴ용 3",
    regDate: "2023-11-01T11:00:00",
    delDate: null,
    replyIsBlind: false
  }
]);
</script>

<template>
  <div id="background">
    <Container>
      <TitleBar
          :user-seq="boardData.userSeq"
          :reg-data="boardData.regDate"
      />
      <ContentArea
          :board-title="boardData.boardTitle"
          :board-content="boardData.boardContent"
          :board-picture-list="boardData.boardPictureList"
      />
      <div class="buttons">
        <SmallButton
            text="목록"
        />
        <SmallButton
            text="글쓰기"
        />
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
                />
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </template>
      <hr class="divider"/>
      <div class="reply-input-container">
        <ReplyInput/>
      </div>
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
