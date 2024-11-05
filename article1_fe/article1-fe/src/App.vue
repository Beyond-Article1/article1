<script setup>
import Header from "@/components/Header.vue";
</script>

<template>
  <Header />
  <div class="main">
    <RouterView key="$route.fullPath"/>
  </div>
</template>

<style>
  body{
    margin: 0;
    padding: 0;
    background-image: url('@/assets/images/back-main.png');
  }
</style>
