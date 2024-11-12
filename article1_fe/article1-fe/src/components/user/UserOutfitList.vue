<script setup>
import {ref, computed, onMounted} from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/authStore.js';
import Pagination from '@/components/common/Pagination.vue';

const authStore = useAuthStore();
const selectedRecords = ref([]);
const selectedOutfit = ref([]);
const detail = ref([]);
const myReview = ref([]);
const existingReview = ref(null);
const selectedOutfitId = ref(null);
const reviewText = ref('');
const feedback = ref('');
const startDate = ref('');
const endDate = ref('');
const currentPage = ref(1);
const itemsPerPage = 4;
const showModal = ref(false);

const fetchDataSelectedRecords = async () => {
  try {
    const response = await axios.get("/user/selectedRecords", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      selectedRecords.value = response.data;

      // 모든 선택된 기록에 대해 아웃핏 데이터 조회
      for (const record of selectedRecords.value) {
        await fetchDataSelectedOutfit(record.selectSeq);

        // 리뷰가 존재하는지 확인
        const reviewResponse = await axios.get("/review/myreview", {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });

        if (reviewResponse.status === 200) existingReview.value = reviewResponse.data;
      }
    } else console.error("아웃핏 이력 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const fetchDataSelectedOutfit = async (selectSeq) => {
  try {
    const response = await axios.get("/user/selectedOutfit", {
      params: { selectSeq: selectSeq },
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      selectedOutfit.value.push(response.data);
    } else console.error("아웃핏 이력 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("아웃핏 정보를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const getImageUrl = (seq) => {
  return new URL(`/src/assets/images/outfits/${seq}.png`, import.meta.url).href;
};

const filteredSelectedRecords = computed(() => {
  return selectedRecords.value.filter(item => {
    const date = new Date(item.selectDate);
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);

    return (!startDate.value || date >= start) && (!endDate.value || date < end);
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredSelectedRecords.value.length / itemsPerPage);
});

const paginatedSelectedRecords = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;

  return filteredSelectedRecords.value.slice(start, start + itemsPerPage);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;

  currentPage.value = page;
};

const openModal = (id) => {
  selectedOutfitId.value = id;
  showModal.value = true;

  // 선택된 아웃핏 이력에 대한 리뷰 확인
  const selectedRecordData = selectedRecords.value.find(record => record.selectSeq === id);
  const myReviewData = myReview.value.find(review => review.selectSeq === id);

  if (myReviewData) {
    existingReview.value = myReviewData; // 기존 리뷰 정보 저장
    feedback.value = myReviewData.reviewLikeYn ? '좋아요' : '싫어요'; // 기존 리뷰의 좋아요 여부 설정
    reviewText.value = myReviewData.reviewContent; // 기존 리뷰 내용 설정
  } else {
    existingReview.value = null;
    feedback.value = '';
    reviewText.value = '';
  }
};

const fetchDataDetail = async () => {
  try {
    const response = await axios.get("/user/detail", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      detail.value = Array.isArray(response.data) ? response.data : [response.data];
    } else console.error("회원 정보 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const fetchDataMyReview = async () => {
  try {
    const response = await axios.get("/review/myreview", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      myReview.value = response.data;
    } else console.error("리뷰 조회 실패", response.status);
  } catch (error) {
    console.error("데이터 fetching 중 에러 발생:", error);

    alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};

const submitReview = async () => {
  try {
    const selectedOutfitData = selectedOutfit.value.find(outfit => outfit.selectSeq === selectedOutfitId.value);

    if (!selectedOutfitData) {
      alert("선택된 아웃핏 정보가 없습니다.");

      return;
    }

    const userSeq = detail.value.length > 0 ? detail.value[0].userSeq : null;

    if (!userSeq) {
      alert("사용자 정보가 없습니다. ID가 null입니다.");

      return;
    }

    const selectedRecordData = selectedRecords.value.find(record => record.selectSeq === selectedOutfitId.value);

    if (!selectedRecordData) {
      alert("선택된 기록 정보가 없습니다.");

      return;
    }

    const customLocation = selectedRecordData.customLocation;

    if (!customLocation) {
      alert("위치 정보가 없습니다.");

      return;
    }

    const reviewData = {
      userSeq: userSeq,
      selectSeq: selectedOutfitId.value,
      reviewContent: reviewText.value,
      weather: selectedRecordData.curTemp,
      location: customLocation.split(',')[0],
      reviewLikeYn: feedback.value === '좋아요',
      reviewBlind: feedback.value === '좋아요' ? 0 : 1,
      outfits: selectedOutfit.value
          .filter(outfit => outfit.selectSeq === selectedOutfitId.value)
          .map(outfit => ({
            outfitSeq: outfit.outfitSeq,
            outfitName: outfit.outfitName
          }))
    };

    const response = await axios.post(`/review/user/${userSeq}`, reviewData, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      alert("리뷰가 성공적으로 제출되었습니다.");

      // 작성한 리뷰를 myReview 배열에 추가
      myReview.value.push({
        ...reviewData,
        reviewSeq: response.data.reviewSeq // 서버에서 받은 리뷰 ID
      });

      // 기존 리뷰 상태 업데이트
      existingReview.value = myReview.value.find(review => review.reviewSeq === response.data.reviewSeq);
    } else alert("리뷰 제출에 실패했습니다.");
  } catch (error) {
    console.error("리뷰 제출 중 에러 발생:", error);

    alert("리뷰 제출 중 오류가 발생했습니다. 다시 시도해 주세요.");
  } finally {
    reviewText.value = '';
    feedback.value = '';
    showModal.value = false;
  }
};

const updateReview = async () => {
  if (!existingReview.value) {
    alert("수정할 리뷰가 없습니다.");

    return;
  }

  const myReviewData = myReview.value.find(review => review.selectSeq === selectedOutfitId.value);

  if (!myReviewData) {
    alert("수정할 리뷰를 찾을 수 없습니다.");

    return;
  }

  try {
    const selectedRecordData = selectedRecords.value.find(record => record.selectSeq === selectedOutfitId.value);

    const updateData = {
      reviewContent: reviewText.value,
      weather: selectedRecordData.curTemp,
      location: selectedRecordData.customLocation.split(',')[0],
      reviewLikeYn: feedback.value === '좋아요',
      reviewBlind: feedback.value === '좋아요' ? 0 : 1,
    };

    const response = await axios.put(`/review/${myReviewData.reviewSeq}`, updateData, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      alert("리뷰가 성공적으로 수정되었습니다.");

      // myReview 배열 업데이트
      Object.assign(myReviewData, updateData); // 수정된 데이터로 업데이트

      existingReview.value = null; // 수정 후 초기화
    }
  } catch (error) {
    console.error("리뷰 수정 중 에러 발생:", error);

    alert("리뷰 수정 중 오류가 발생했습니다. 다시 시도해 주세요.");
  } finally {
    reviewText.value = '';
    feedback.value = '';
    showModal.value = false;
  }
};

const deleteReview = async () => {
  if (!existingReview.value) {
    alert("삭제할 리뷰가 없습니다.");

    return;
  }

  const myReviewData = myReview.value.find(review => review.selectSeq === selectedOutfitId.value);

  if (!myReviewData) {
    alert("삭제할 리뷰를 찾을 수 없습니다.");

    return;
  }

  try {
    const response = await axios.delete(`/review/${myReviewData.reviewSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`
      }
    });

    if (response.status === 200) {
      alert("리뷰가 성공적으로 삭제되었습니다.");

      // myReview 배열에서 삭제된 리뷰 제거
      myReview.value = myReview.value.filter(review => review.reviewSeq !== myReviewData.reviewSeq); // 배열에서 제거
      existingReview.value = null; // 삭제 후 초기화
    }
  } catch (error) {
    console.error("리뷰 삭제 중 에러 발생:", error);

    alert("리뷰 삭제 중 오류가 발생했습니다. 다시 시도해 주세요.");
  } finally {
    reviewText.value = '';
    feedback.value = '';
    showModal.value = false;
  }
};

onMounted(() => {
  fetchDataSelectedRecords();
  fetchDataDetail();
  fetchDataMyReview();
});
</script>

<template>
  <div>
    <div class="header">
      <h1>아웃핏 이력 조회</h1>
      <div class="search-period">
        <label for="startDate">검색 기간</label>
        <input type="date" id="startDate" v-model="startDate" />
        <span>~</span>
        <input type="date" id="endDate" v-model="endDate" />
      </div>
    </div>

    <div class="outfit-table">
      <div class="table-header">
        <span class="header-cell">선택일</span>
        <span class="header-cell">위치</span>
        <span class="header-cell">날씨</span>
        <span class="header-cell">아웃핏</span>
      </div>

      <div class="table-row" v-for="item in paginatedSelectedRecords" :key="item.selectSeq">
        <div class="table-cell">{{ item.selectDate.slice(0, 10) }}</div>
        <div class="table-cell">{{ item.customLocation }}</div>
        <div class="table-cell">{{ item.curTemp }}°C</div>
        <div class="outfit-images">
          <img
              class="outfit-image"
              v-if="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.topSeq"
              :src="getImageUrl(selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq).topSeq)"
              :alt="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)"
          />
          <img
              class="outfit-image"
              v-if="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.bottomSeq"
              :src="getImageUrl(selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq).bottomSeq)"
              :alt="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)"
          />
          <img
              class="outfit-image"
              v-if="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.shoesSeq"
              :src="getImageUrl(selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq).shoesSeq)"
              :alt="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)"
          />
          <img
              class="outfit-image"
              v-if="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.outerSeq"
              :src="getImageUrl(selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq).outerSeq)"
              :alt="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)"
          />
          <img
              class="outfit-image"
              v-if="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)?.accessorySeq"
              :src="getImageUrl(selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq).accessorySeq)"
              :alt="selectedOutfit.find(outfit => outfit.selectSeq === item.selectSeq)"
          />
        </div>
        <button class="review-button" @click="openModal(item.selectSeq)">리뷰 작성하기</button>
      </div>
    </div>

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        :goToPage="goToPage"
    />

    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2 class="modal-title">{{ existingReview ? '리뷰 수정' : '리뷰 작성' }}</h2>
          <button class="close-button" @click="showModal = false">X</button>
        </div>
        <div class="modal-body">
          <div class="feedback-options">
            <label class="feedback-label">
              <input type="radio" value="좋아요" v-model="feedback" />
              <span class="circle" :class="{ selected: feedback === '좋아요' }"></span>
              <span class="text" style="margin-left: 10px; margin-right: 10px;">좋아요</span>
              <span class="icon">👍</span>
            </label>
            <label class="feedback-label">
              <input type="radio" value="싫어요" v-model="feedback" />
              <span class="circle" :class="{ selected: feedback === '싫어요' }"></span>
              <span class="text" style="margin-left: 10px; margin-right: 10px;">싫어요</span>
              <span class="icon">👎</span>
            </label>
          </div>
          <textarea v-model="reviewText" placeholder="한 줄 리뷰를 작성하여 의견을 말씀해 주세요." class="review-textarea"></textarea>
        </div>
        <div class="modal-footer">
          <button class="modal-review-button" v-if="!existingReview" @click="submitReview">리뷰쓰기</button>
          <button class="modal-edit-button" v-if="existingReview" @click="updateReview">수정</button>
          <button class="modal-delete-button" v-if="existingReview" @click="deleteReview">삭제</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.search-period {
  display: flex;
  align-items: center;
}

.search-period label {
  margin-right: 20px;
  margin-left: 10px;
  font-size: 16px;
  font-weight: bold;
}

.outfit-table {
  background-color: #f9f9ff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 2fr 1fr;
  padding: 10px;
  background-color: #cce4ff;
  border-radius: 8px;
  font-weight: bold;
  text-align: center;
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 2fr 1fr;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.table-cell {
  padding: 10px 5px;
  text-align: center;
}

.outfit-images {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.outfit-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.review-button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.review-button:hover {
  background-color: #0056b3;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 800px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.modal-title {
  flex-grow: 1;
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.feedback-options {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.feedback-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 0.5rem;
}

.feedback-options input[type="radio"] {
  display: none;
}

.circle {
  border: 2px solid #007BFF;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  position: relative;
  transition: background-color 0.3s, border-color 0.3s;
}

.circle.selected {
  background-color: #007BFF;
  border: 2px solid #007BFF;
}

.icon {
  font-size: 30px;
}

.review-textarea {
  width: 100%;
  height: 80px;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: none;
  margin-bottom: 1rem;
}

.modal-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}

.modal-review-button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  width: 100%;
}

.modal-review-button:hover {
  background-color: #0056b3;
}

.modal-edit-button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  width: 48%;
}

.modal-edit-button:hover {
  background-color: #0056b3;
}

.modal-delete-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  width: 48%;
}

.modal-delete-button:hover {
  background-color: #c82333;
}
</style>