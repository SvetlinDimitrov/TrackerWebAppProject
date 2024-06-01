import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  optimizeDeps: {
    exclude: ['vuex', 'primevue', 'vue', 'vue-router']
  },
  build: {
    rollupOptions: {
      external: ['vue'],
    },
  },
})
