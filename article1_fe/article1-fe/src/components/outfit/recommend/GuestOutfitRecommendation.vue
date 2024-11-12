<template>
  <div class="recommendation-container">
    <div v-if="isLoading" class="loading-overlay">
      <div class="spinner"></div>
      <p>복장을 추천 중입니다. 잠시만 기다려주세요.</p>
    </div>
    <div class="title">{{ title }}</div>
    <div class="content-wrapper">
      <!-- 왼쪽 섹션 -->
      <div class="left-section">
        <div v-for="(items, category) in leftCategories" :key="category" class="category-section">
          <h3>{{ getCategoryName(category) }}</h3>
          <div class="carousel-container">
            <div class="carousel" :style="{ transform: `translateX(-${carouselOffsets[category]}px)` }">
              <div
                  v-for="(item, index) in items"
                  :key="item.outfitSeq"
                  class="item-card"
                  :class="{
                    'first-item': index === 0}"
                  @click="alertGuest"
              >
                <img :src="getImageSrc(item.outfitSeq)" alt="Outfit Image" />
                <p>{{ item.outfitName }}</p>
              </div>
            </div>
            <button class="nav-button left" @click="moveCarousel(category, -1)">&#8249;</button>
            <button class="nav-button right" @click="moveCarousel(category, 1)">&#8250;</button>
          </div>
        </div>
      </div>

      <!-- 오른쪽 섹션 -->
      <div class="right-section">
        <div v-for="(items, category) in rightCategories" :key="category" class="category-section">
          <h3>{{ getCategoryName(category) }}</h3>
          <div class="carousel-container">
            <div class="carousel" :style="{ transform: `translateX(-${carouselOffsets[category]}px)` }">
              <div
                  v-for="(item, index) in items"
                  :key="item.outfitSeq"
                  class="item-card"
                  :class="{
                    'first-item': index === 0}"
                  @click="alertGuest"
              >
                <img :src="getImageSrc(item.outfitSeq)" alt="Outfit Image" />
                <p>{{ item.outfitName }}</p>
              </div>
            </div>
            <button class="nav-button left" @click="moveCarousel(category, -1)">&#8249;</button>
            <button class="nav-button right" @click="moveCarousel(category, 1)">&#8250;</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useSelectedInfoStore } from "@/store/selectedInfoStore.js";
import { useRouter } from "vue-router";

export default {
  data() {
    return {
      isLoading: true,
      outfits: {
        TOP: [],
        BOTTOM: [],
        OUTERWEAR: [],
        SHOES: [],
        ACCESSORY: [],
      },
      carouselOffsets: {
        TOP: 0,
        BOTTOM: 0,
        OUTERWEAR: 0,
        SHOES: 0,
        ACCESSORY: 0,
      },
      itemWidth: 150, // 각 아이템의 너비
      title: "추천 복장 리스트",
    };
  },
  computed: {
    leftCategories() {
      return {
        TOP: this.outfits.TOP,
        BOTTOM: this.outfits.BOTTOM,
        OUTERWEAR: this.outfits.OUTERWEAR,
      };
    },
    rightCategories() {
      return {
        SHOES: this.outfits.SHOES,
        ACCESSORY: this.outfits.ACCESSORY,
      };
    },
  },
  async created() {
    await this.fetchOutfitRecommendations();
  },
  setup() {
    const router = useRouter(); // Vue Router 인스턴스 가져오기
    return {router};
  },
  methods: {
    async fetchOutfitRecommendations() {
      const store = useSelectedInfoStore();
      try {
        this.isLoading = true;
        let date = new Date(store.selectedDate);
        date.setHours(date.getHours() + 9);
        const response = await axios.post("/guest/outfit/recommendations", {
          situationSeq: store.selectedSituation,
          requestedAt: date.toISOString().split(".")[0],
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        });
        this.outfits = response.data;
      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      } finally {
        this.isLoading = false; // 로딩 상태 종료
      }
    },
    getCategoryName(category) {
      const categoryNames = {
        TOP: "상의",
        BOTTOM: "하의",
        OUTERWEAR: "아우터",
        SHOES: "신발",
        ACCESSORY: "악세사리",
      };
      return categoryNames[category] || category;
    },
    getImageSrc(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    },
    alertGuest() {
      alert("로그인이 필요한 서비스입니다.");
      this.router.push("/login");
    },
    moveCarousel(category, direction) {
      const maxOffset = this.outfits[category].length * this.itemWidth - 3 * this.itemWidth;
      const newOffset = this.carouselOffsets[category] + direction * this.itemWidth;
      this.carouselOffsets[category] = Math.min(Math.max(newOffset, 0), maxOffset);
    },
  },
};
</script>

<style scoped>
.recommendation-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0px; /* 패딩 줄이기 */
}

.title {
  font-size: 0.8rem; /* 제목 크기 축소 */
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 1000px; /* 전체 컨테이너 너비 축소 */
}

.left-section,
.right-section {
  display: flex;
  flex-direction: column;
  width: 48%;
}

.category-section {
  margin-bottom: 0px; /* 각 섹션 간격 축소 */
}

.carousel-container {
  position: relative;
  overflow: hidden;
  height: 120px; /* 높이 줄이기 */
  width: calc(90px * 3 + 85px); /* 아이템 3개 + 간격 */
}

.carousel {
  display: flex;
  transition: transform 0.3s ease-in-out;
}

.item-card {
  flex-shrink: 0;
  width: 90px; /* 너비 축소 */
  height: 90px; /* 높이 축소 */
  border: 1px solid #ccc;
  border-radius: 10px;
  margin-right: 40px;
  text-align: center;
  background-color: #f9f9f9;
}

.item-card.first-item {
  border: 2px solid #ff9800; /* 가장 앞 복장의 테두리를 강조 */
  background-color: #fff5e6; /* 강조된 배경색 */
}

.item-card img {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 이미지 비율 유지 */
}

.item-card p {
  margin-top: 5px;
  font-size: 0.8rem; /* 글자 크기 축소 */
}

.item-card.selected {
  border-color: #007bff; /* 선택된 의상에 파란색 테두리 추가 */
  background-color: #e7f0ff; /* 선택된 의상 배경색 변경 */
}
.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
  width: 25px;
  height: 25px; /* 버튼 크기 축소 */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button.left {
  left: -2px;
}

.nav-button.right {
  right: 5px;
}

.save-button-container {
  margin-top: 70px;
}

.save-button-container button {
  padding: 5px 10px;
  font-size: 0.9rem;
}
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 10;
}

.spinner {
  border: 5px solid rgba(0, 0, 0, 0.1);
  border-top: 5px solid #007bff;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-overlay p {
  margin-top: 15px;
  font-size: 1rem;
  color: #555;
  font-weight: bold;
}


</style>
