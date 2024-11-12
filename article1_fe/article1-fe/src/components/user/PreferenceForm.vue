<script setup>
import { ref, defineProps, defineEmits, watch } from "vue";

const conditionOptions = ref([
  { value: 1, label: "더위를 많이 탄다", image: new URL('/assets/images/user/Condition-Hot.png', import.meta.url).href },
  { value: 2, label: "추위를 많이 탄다", image: new URL('/assets/images/user/Condition-Cold.png', import.meta.url).href },
  { value: 3, label: "무관", image: new URL('/assets/images/situation/none.png', import.meta.url).href }
]);

const styleOptions = ref([
  { value: 1, label: "캐주얼", image: new URL('/assets/images/user/Style-Casual.png', import.meta.url).href },
  { value: 2, label: "포멀", image: new URL('/assets/images/user/Style-Formal.png', import.meta.url).href },
  { value: 3, label: "스포티", image: new URL('/assets/images/user/Style-Sporty.png', import.meta.url).href },
  { value: 4, label: "무관", image: new URL('/assets/images/situation/none.png', import.meta.url).href }
]);

// props로 전달 받은 값
const props = defineProps({
  conditionSeq: {
    type: Number,
    default: null,
  },
  styleSeq: {
    type: Number,
    default: null,
  }
});

const emit = defineEmits(['updatePreference']);

// 선택된 값
const selectedConditionSeq = ref(props.conditionSeq || null);
const selectedStyleSeq = ref(props.styleSeq || null);

// 등록 모드, 수정 모드 구분 (watch를 통해 props 값이 바뀔 때마다 상태 확인)
const isEditMode = ref(props.conditionSeq !== null && props.styleSeq !== null);

// props 변경에 따라 값 업데이트
watch(() => props.conditionSeq, (newValue) => {
  selectedConditionSeq.value = newValue;
});

watch(() => props.styleSeq, (newValue) => {
  selectedStyleSeq.value = newValue;
});

// 선호도 저장 처리
watch([selectedConditionSeq, selectedStyleSeq], () => {
  // 값이 변경될 때마다 부모 컴포넌트에 값 전달
  emit('updatePreference', {
    conditionSeq: selectedConditionSeq.value,
    styleSeq: selectedStyleSeq.value
  })
})
</script>

<template>
  <div class="preference_form">

    <div class="preference-content">
      <div class="condition_title">
        <h2>체질</h2>
      </div>
      <div class="condition-content prefer-content">
        <div v-for="option in conditionOptions" :key="option.value" class="condition-option prefer-option" >
          <input
              type="radio"
              :id="'condition-' + option.value"
              :value="option.value"
              v-model="selectedConditionSeq"
          />
          <label :for="'condition-' + option.value">
            <img :src="option.image" :alt="option.label" />
            <div>{{ option.label }}</div>
          </label>
        </div>
      </div>

      <hr style="margin-top: 20px">

      <div class="style_title">
        <h2>스타일</h2>
      </div>
      <div class="style-content prefer-content">
        <div v-for="option in styleOptions" :key="option.value" class="style-option prefer-option" >
          <input
              type="radio"
              :id="'style-' + option.value"
              :value="option.value"
              v-model="selectedStyleSeq"
          />
          <label :for="option.value">
            <img :src="option.image" :alt="option.label" />
            <div>{{ option.label }}</div>
          </label>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.preference-content {
  padding: 0 35px;
}

.condition_title {
  margin-bottom: 30px;
}

.style_title {
  margin-top: 35px;
  margin-bottom: 30px;
}

.prefer-content {
  display: flex;
  justify-content: space-between;  /* 아이템 간 간격 조정 */
  align-items: center;             /* 세로 중앙 정렬 */
  flex-wrap: wrap;                 /* 아이템이 넘칠 경우 자동으로 줄바꿈 */
}

.prefer-option {
  display: flex;                  /* 내부 아이템들을 가로로 정렬 */
  align-items: center;            /* 아이템들을 세로로 가운데 정렬 */
  margin-right: 20px;
  margin-bottom: 25px;
}

.prefer-option img {
  width: 150px;                   /* 이미지 너비 */
  height: 120px;                  /* 이미지 높이 */
  object-fit: contain;
  margin-right: 10px;
}

label {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

input[type="radio"] {
  transform: scale(1.5);  /* 기본 크기에서 1.5배 키우기 */
  margin-right: 10px;     /* 이미지와 텍스트 간의 간격 */
}
</style>