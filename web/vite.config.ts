import { defineConfig, loadEnv } from "vite";
import path from "path";
import vue from '@vitejs/plugin-vue2'
import AutoImport from 'unplugin-auto-import/vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      imports: ["vue", "vue-router", "pinia"]
    }),
  ],
  base: "./",
  build: {
    emptyOutDir: false,
    chunkSizeWarningLimit: 1024 * 5,
    rollupOptions: {
      // 确保外部化处理那些你不想打包进库的依赖
      //external: ["vue", "vue-router","vuex","@vue/composition-api", "element-ui", "echart"],
      output: {
        // 在 UMD 构建模式下为这些外部化的依赖提供一个全局变量
        globals: {
          vue: "Vue",
        },
      },
    },
  },
  resolve: {
    alias: [
      {
        find: "@",
        replacement: path.resolve(__dirname, "src"),
      },
    ],
  },
  define: {
    "process.env": process.env,
  },
  server: {
    port: 8081,
  },
});
