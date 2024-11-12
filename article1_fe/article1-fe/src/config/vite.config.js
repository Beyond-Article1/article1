import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, '../'), // '@'를 'src' 디렉터리로 매핑
    },
  },
  define: {
    'import.meta.env': {
      VITE_KAKAO_API_KEY: JSON.stringify(process.env.VITE_KAKAO_API_KEY),
      VITE_KAKAO_REST_API_KEY: JSON.stringify(process.env.VITE_KAKAO_REST_API_KEY),
    }
  },
});
