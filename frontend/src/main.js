import { createApp } from 'vue'
import 'element-plus/theme-chalk/dark/css-vars.css' // Element Plus 暗黑模式变量
import 'element-plus/dist/index.css' // Element Plus 样式
import './style.css' // 全局样式（Tailwind CSS）
import App from './App.vue'
import router from './router' // 路由配置
import { createPinia } from 'pinia' // 状态管理
import i18n from './locales' // 国际化配置

const app = createApp(App)

app.use(router) // 注册路由
app.use(createPinia()) // 注册 Pinia
app.use(i18n) // 注册国际化

app.mount('#app') // 挂载应用
