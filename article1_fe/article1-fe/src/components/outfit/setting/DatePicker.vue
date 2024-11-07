<template>
  <div class="date-picker-wrapper square-component">
    <Datepicker
        class="full-size-datepicker dp__menu_inner"
        v-model="internalSelectedDate"
        :min-date="minDate"
        :max-date="maxDate"
        :inline="true"
        :format="displayDateFormat"
        @update:model-value="onDateChange"
    />
    <div v-if="formattedSelectedDate" class="selected-date-display">
      선택된 날짜: {{ formattedSelectedDate }}
    </div>
  </div>
</template>

<script>
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import { useSelectedInfoStore } from '@/store/selectedInfoStore.js';

export default {
  components: { Datepicker },
  data() {
    const today = new Date();
    const maxDate = new Date(today);
    maxDate.setDate(today.getDate() + 5);

    return {
      internalSelectedDate: new Date(),
      minDate: today,
      maxDate: maxDate,
      displayDateFormat: "yyyy년 MM월 dd일",
    };
  },
  computed: {
    formattedSelectedDate() {
      if (this.internalSelectedDate) {
        return this.internalSelectedDate.toLocaleString("ko-KR", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
        });
      }
      return null;
    },
  },
  methods: {
    onDateChange(date) {
      this.internalSelectedDate = date;
      const store = useSelectedInfoStore();
      store.selectedDate = date;
    },
  },
};
</script>

<style scoped>
.date-picker-wrapper {
  width: 470px;
  height: 470px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.full-size-datepicker {
  width: 100% !important;
  height: 100% !important;
}

.selected-date-display {
  margin-top: 10px;
  font-size: 1rem;
  color: #333;
}
</style>
