import { defineConfig } from 'vite' // 导入 Vite 的配置定义函数
import vue from '@vitejs/plugin-vue' // 导入 Vue 3 插件，用于处理 .vue 单文件组件
import AutoImport from 'unplugin-auto-import/vite' // 导入自动导入插件，用于自动导入 Vue 组合式 API 和组件
import Components from 'unplugin-vue-components/vite' // 导入组件自动注册插件，用于自动注册全局组件
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers' // 导入 Element Plus 解析器，用于自动导入 Element Plus 组件和样式
import path from 'path' // 导入 Node.js 的路径模块，用于处理文件路径

// 导出 Vite 配置
export default defineConfig({
  // 路径解析配置
  resolve: {
    // 别名配置，用于简化导入路径
    alias: {
      '@': path.resolve(__dirname, 'src') // 将 '@' 别名指向项目的 src 目录
    }
  },
  // 插件配置
  plugins: [
    vue(), // 使用 Vue 3 插件
    AutoImport({
      // 配置自动导入的解析器
      resolvers: [ElementPlusResolver()], // 自动导入 Element Plus 相关函数和组件
    }),
    Components({
      // 配置组件自动注册的解析器
      resolvers: [ElementPlusResolver()], // 自动注册 Element Plus 组件
    }),
  ],
  // 开发服务器配置
  server: {
    port: 3000, // 设置开发服务器端口为 3000
    // 代理配置，用于解决跨域问题
    proxy: {
      '/api': { // 匹配以 '/api' 开头的请求路径
        target: 'http://localhost:8080', // 代理目标服务器地址，通常是后端 API 服务器
        changeOrigin: true, // 允许跨域
        rewrite: (path) => path.replace(/^\/api/, '') // 重写请求路径，去掉 '/api' 前缀
      }
    },
    // 允许访问的主机列表
    allowedHosts: ['blocked-gertude-unpatronizable.ngrok-free.dev'] // 允许 ngrok 提供的外部访问地址
  }
})