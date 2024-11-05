// selectedInfoStore.js
import { defineStore } from 'pinia';

export const useSelectedInfoStore = defineStore('selectedInfoStore', {
    state: () => ({
        selectedLatitude: null,
        selectedLongitude: null,
        selectedDate: new Date(),
    }),
});
