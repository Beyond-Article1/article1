<script setup>
import Header from "@/components/Header.vue";
</script>

<template>
  <Header />
  <div id="app">
    <RouterView key="$route.fullPath"/>
  </div>
</template>

<style>
  body{
    margin: 0;
    padding: 0;
    background-image: url('@/assets/images/back-main.png');
  }
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 0;
  }
</style>
