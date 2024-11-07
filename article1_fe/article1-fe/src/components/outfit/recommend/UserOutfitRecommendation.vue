<template>
  <OutfitRecommendationList
      title="회원 복장 추천 리스트"
      :outfits="outfits"
      :isSelected="isSelected"
      :getImageSrc="getImageSrc"
      :showSaveButton="true"
      @itemClicked="toggleSelection"
      @saveSelection="saveSelection"
  />

</template>

<script>
import axios from 'axios';
import OutfitRecommendationList from '@/components/outfit/recommend/OutfitRecommendationList.vue';
import {useSelectedInfoStore} from '@/store/selectedInfoStore.js';

export default {

  components: {OutfitRecommendationList},
  data() {
    return {
      outfits: null,
      selectedOutfits: {
        topSeq: null,
        bottomSeq: null,
        shoesSeq: null,
        outerSeq: null,
        accessorySeq: []
      }
    };
  },

  async created() {
    await this.fetchOutfitRecommendations();
  },
  methods: {
    async fetchOutfitRecommendations() {
      const store = useSelectedInfoStore();
      try {
        const response = await axios.post('http://localhost:8080/user/outfit/recommendations', {
          situationSeq: store.selectedSituation,
          requestedAt: store.selectedDate,
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6WyJVU0VSIl0sImV4cCI6MTczMDk4MjE5N30.oWZFbYMYgn3t6snY6Zj9GCzE5QkSaCSR3xM0j_DpnfY-GwKlysydTsHwKxS35i4FDjzPUYamDFb9eX30Xs1T7w'
          }
        });
        this.outfits = response.data;
        console.log(this.outfits);

      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      }

      console.log("상황번호: "+store.selectedSituation);
      console.log("위도: "+store.selectedLatitude);
      console.log("경도: "+store.selectedLongitude);
      console.log("날짜: "+store.selectedDate);

      console.log("현재날짜"+store.selectedDate);
    },
    isSelected(category, outfitSeq) {
      if (category === 'ACCESSORY') {
        return this.selectedOutfits.accessorySeq.includes(outfitSeq);
      } else if (category === 'OUTERWEAR') {
        return this.selectedOutfits.outerSeq === outfitSeq;
      } else {
        return this.selectedOutfits[`${category.toLowerCase()}Seq`] === outfitSeq;
      }
    }
    ,
    getImageSrc(outfitSeq) {
      return new URL(`/src/assets/images/outfits/${outfitSeq}.png`, import.meta.url).href;
    },
    toggleSelection(category, outfitSeq) {
      if (category === 'ACCESSORY') {
        const index = this.selectedOutfits.accessorySeq.indexOf(outfitSeq);
        if (index >= 0) {
          this.selectedOutfits.accessorySeq.splice(index, 1);
        } else {
          this.selectedOutfits.accessorySeq.push(outfitSeq);
        }
      } else if (category === 'OUTERWEAR') {
        this.selectedOutfits.outerSeq = outfitSeq;
      } else {
        this.selectedOutfits[`${category.toLowerCase()}Seq`] = outfitSeq;
      }
      // 선택 상태가 업데이트된 것을 확인
      console.log("Updated selectedOutfits:", this.selectedOutfits);
    },

    async saveSelection() {
      const store = useSelectedInfoStore();
      try {
        const formattedDate = store.selectedDate.toISOString().split('.')[0]; // 'Z'와 밀리초 제거

        const weatherResponse = await axios.get('http://localhost:8080/weather', {
          params: {
            time: formattedDate,
            lat: store.selectedLatitude,
            lon: store.selectedLongitude,
          }
        });

        const weatherData = weatherResponse.data;
        const weatherCode = weatherData.nowWeatherCode;
        const highTemp = weatherData.highTemp;
        const lowTemp = weatherData.lowTemp;
        const dailyTemp = highTemp - lowTemp;
        const curTemp = weatherData.nowTemp;
        const precipitation = weatherData.list[0]?.rain?.['1h'] || 0;

        console.log("Sending selected outfits:", this.selectedOutfits);

        await axios.post('http://localhost:8080/user/outfit/select', {
          situationSeq: store.selectedSituation,
          customDate: store.selectedDate.toISOString(),
          customLocation: `${store.selectedLatitude}, ${store.selectedLongitude}`,
          weatherCode: weatherCode,
          highTemp: highTemp,
          lowTemp: lowTemp,
          dailyTemp: dailyTemp,
          curTemp: curTemp,
          precipitation: precipitation,
          topSeq: this.selectedOutfits.topSeq,
          bottomSeq: this.selectedOutfits.bottomSeq,
          shoesSeq: this.selectedOutfits.shoesSeq,
          outerSeq: this.selectedOutfits.outerSeq,
          accessorySeq: this.selectedOutfits.accessorySeq,
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6WyJVU0VSIl0sImV4cCI6MTczMDk4MjE5N30.oWZFbYMYgn3t6snY6Zj9GCzE5QkSaCSR3xM0j_DpnfY-GwKlysydTsHwKxS35i4FDjzPUYamDFb9eX30Xs1T7w'
          }
        });
        alert("선택한 복장이 저장되었습니다.");
      } catch (error) {
        console.error("선택한 복장을 저장하지 못했습니다:", error);
        alert("저장 중 오류가 발생했습니다.");
      }
    }
  }
};
</script>
