<template>
  <OutfitRecommendationList
      title="게스트 복장 추천 리스트"
      :outfits="outfits"
      :getImageSrc="getImageSrc"
      :showSaveButton="false"
      @itemClicked="alertGuest"
  />
</template>

<script>
import axios from 'axios';
import OutfitRecommendationList from '@/components/outfit/recommend/OutfitRecommendationList.vue';
import { useSelectedInfoStore } from '@/store/selectedInfoStore.js';

export default {
  components: { OutfitRecommendationList },
  data() {
    return {
      outfits: null
    };
  },
  async created() {
    await this.fetchOutfitRecommendations();
  },
  methods: {
    async fetchOutfitRecommendations() {
      const store = useSelectedInfoStore();
      try {
        const response = await axios.post('http://localhost:8080/guest/outfit/recommendations', {
          situationSeq: store.selectedSituation,
          requestedAt: store.selectedDate.toISOString(),
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        });
        this.outfits = response.data;
      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      }
    },
    getImageSrc(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    },
    alertGuest() {
      alert("회원 전용 기능입니다. 회원 가입 후 이용해주세요.");
    }
  }
};
</script>
