import { createPinia } from 'pinia';
import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";

createApp(App).use(router).mount('#app')
const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
createApp(App).use(router).mount("#app");
