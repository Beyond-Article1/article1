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
        let date = new Date(store.selectedDate);
        date.setHours(date.getHours() + 9);
        console.log(date.toISOString().split('.')[0]);
        const response = await axios.post('/user/outfit/recommendations', {
          situationSeq: store.selectedSituation,
          requestedAt: date.toISOString().split('.')[0],
          latitude: store.selectedLatitude,
          longitude: store.selectedLongitude,
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMSIsImF1dGgiOlsiVVNFUiJdLCJleHAiOjE3MzA5OTk4MTN9.Hm8Qiy6mTlFbSin3SDxk3dyH6YpXkxwSybJaNkPN2SNiRVk5fyMewcaNuSWrnseVrSA_x2owkOfR0IiUZrkf2A'
          }
        });
        this.outfits = response.data;

      } catch (error) {
        console.error("추천 데이터를 불러오지 못했습니다:", error);
      }

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
        const weatherResponse = await axios.get('/weather', {
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

        await axios.post('/user/outfit/select', {
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
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMSIsImF1dGgiOlsiVVNFUiJdLCJleHAiOjE3MzA5OTk4MTN9.Hm8Qiy6mTlFbSin3SDxk3dyH6YpXkxwSybJaNkPN2SNiRVk5fyMewcaNuSWrnseVrSA_x2owkOfR0IiUZrkf2A'
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
