<script>
import axios from 'axios';
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/store/authStore.js';
import Pagination from '@/components/common/Pagination.vue';

export default {
  components: {
    Pagination
  },
  setup() {
    const authStore = useAuthStore();
    const selectedRecords = ref([]);
    const myReview = ref([]);
    const startDate = ref('');
    const endDate = ref('');
    const currentPage = ref(1);
    const itemsPerPage = 4;
    const currentIndexes = ref([]);

    const fetchDataSelectedRecords = async () => {
      try {
        const response = await axios.get("/user/selectedRecords", {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        });

        if (response.status === 200) {
          selectedRecords.value = response.data;
        } else console.error("아웃핏 이력 조회 실패", response.status);
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

          currentIndexes.value = Array(myReview.value.length).fill(0);
        } else console.error("리뷰 조회 실패", response.status);
      } catch (error) {
        console.error("데이터 fetching 중 에러 발생:", error);

        alert("데이터를 가져오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
      }
    };

    const filteredReviews = computed(() => {
      return myReview.value.filter(item => {
        const date = new Date(item.regDate);
        const start = new Date(startDate.value);
        const end = new Date(endDate.value);

        return (!startDate.value || date >= start) && (!endDate.value || date < end);
      });
    });

    const paginatedReviews = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;

      return filteredReviews.value.slice(start, start + itemsPerPage);
    });

    const totalPages = computed(() => Math.ceil(filteredReviews.value.length / itemsPerPage));

    const goToPage = (page) => {
      if (page > 0 && page <= totalPages.value) currentPage.value = page;
    };

    const getVisibleOutfits = (outfits, reviewIndex) => {
      const startIndex = currentIndexes.value[reviewIndex];

      return outfits.slice(startIndex, startIndex + 2);
    };

    const scrollOutfits = (reviewIndex, direction) => {
      const maxIndex = Math.max(0, myReview.value[reviewIndex].outfits.length - 2);

      currentIndexes.value[reviewIndex] += direction;

      if (currentIndexes.value[reviewIndex] < 0) {
        currentIndexes.value[reviewIndex] = maxIndex;
      } else if (currentIndexes.value[reviewIndex] > maxIndex) currentIndexes.value[reviewIndex] = 0;
    };

    onMounted(() => {
      fetchDataSelectedRecords();
      fetchDataMyReview();
    });

    return {
      startDate,
      endDate,
      selectedRecords,
      myReview,
      filteredReviews,
      paginatedReviews,
      currentPage,
      totalPages,
      goToPage,
      getVisibleOutfits,
      scrollOutfits,
      getImageUrl(outfitSeq) {
        return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
      },
    };
  },
};
</script>

<template>
  <div>
    <div class="header">
      <h1>내가 작성한 리뷰</h1>
      <div class="search-period">
        <label for="startDate">검색 기간</label>
        <input type="date" id="startDate" v-model="startDate" />
        <span>~</span>
        <input type="date" id="endDate" v-model="endDate"/>
      </div>
    </div>

    <div class="review-table">
      <div class="table-header">
        <span class="header-cell">선택일</span>
        <span class="header-cell">위치</span>
        <span class="header-cell">날씨</span>
        <span class="header-cell">리뷰 내용</span>
        <span class="header-cell">작성일</span>
      </div>

      <div class="table-row" v-for="(item, index) in paginatedReviews" :key="item.reviewSeq">
        <div class="table-cell">{{ selectedRecords.find(user => user.selectSeq === item.selectSeq)?.selectDate.slice(0, 10) }}</div>
        <div class="table-cell">{{ item.location }}</div>
        <div class="table-cell">{{ item.weather }}°C</div>

        <div class="table-cell review-content">
          <p>{{ item.reviewContent }}</p>

          <!-- 옷 정보 슬라이더 추가 -->
          <div class="outfit-slider" v-if="item.outfits && item.outfits.length > 0">
            <button class="slider-btn" @click="scrollOutfits(index, -1)">←</button>
            <div class="outfit-images">
              <img
                  v-for="outfit in getVisibleOutfits(item.outfits, index)"
                  :key="outfit.outfitSeq"
                  :src="getImageUrl(outfit.outfitSeq)"
                  :alt="outfit.outfitName"
                  class="outfit-image"
              />
            </div>
            <button class="slider-btn" @click="scrollOutfits(index, 1)">→</button>
          </div>
        </div>

        <div class="table-cell date-time">
          {{ item.regDate.slice(0, 10) }}
        </div>
      </div>
    </div>

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        :goToPage="goToPage"
    />
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

.review-table {
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

.review-content {
  font-size: 16px;
  color: #444;
}

.date-time {
  font-size: 16px;
}

.outfit-slider {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
  justify-content: center;
}

.outfit-images {
  display: flex;
  overflow-x: hidden;
  gap: 10px;
  max-width: 150px;
}

.outfit-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.slider-btn {
  background-color: #ddd;
  border: none;
  padding: 4px 8px;
  cursor: pointer;
}
</style>
