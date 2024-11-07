// store/selectedInfoStore.js
import {defineStore} from 'pinia';

export const useSelectedInfoStore = defineStore('selectedInfoStore', {
    state: () => ({
        selectedLatitude: null,
        selectedLongitude: null,
        selectedDate: new Date(),
        selectedSituation: null, // 선택한 상황을 저장
    }),
    actions: {
        setLocation(latitude, longitude) {
            this.selectedLatitude = latitude;
            this.selectedLongitude = longitude;
        },
        setDate(date) {
            this.selectedDate = date;
        },
        setSituation(situation) {
            this.selectedSituation = situation;
        }
    },
});
