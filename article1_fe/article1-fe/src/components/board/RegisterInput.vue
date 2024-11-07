<script setup>
import InputBox from "@/components/board/InputBox.vue";
import {ref, defineEmits} from "vue";

const props = defineProps({
  text: String
});

// emit 정의
const emit = defineEmits(['update:modelValue']);

// 입력값을 관리하는 ref
const inputValue = ref('');

// 입력값이 변경될 때 emit
function updateValue(newValue) {
  inputValue.value = newValue;
  emit('update:modelValue', inputValue.value); // 부모 컴포넌트로 emit
}
</script>

<template>
  <div>
    <div class="label">
      <span>{{ text }}</span>
    </div>
    <div>
      <InputBox :label="text" v-model="inputValue" @input="updateValue"/>
    </div>
  </div>
</template>

<style scoped>
.label {
  font-size: 20px;
  font-weight: bold;
}
</style>