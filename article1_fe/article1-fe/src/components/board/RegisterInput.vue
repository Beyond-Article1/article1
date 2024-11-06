<script setup>
import InputBox from "@/components/board/InputBox.vue";

const props = defineProps({
  text: String
})
</script>

<template>
  <div>
    <div class="label">
      <span>{{ text }}</span>
    </div>
    <div>
      <InputBox :label="text"/>
    </div>
  </div>
</template>

<style scoped>
.label {
  font-size: 20px;
  font-weight: bold;
}
</style>