<template>
  <!-- 
    商城前台的主布局容器
    min-h-screen: 确保最小高度占满屏幕
    transition-colors: 主题切换时背景色平滑过渡
    :style: 动态绑定背景色，根据 Pinia 中的主题状态切换深色/浅色背景 改变主体的颜色**不包括最上方状态栏**
  -->
  <div class="shop-layout min-h-screen transition-colors duration-300">
    <!-- 公共导航栏组件：包含 Logo、菜单、登录入口等，所有商城页面都会显示 -->
    <Navbar />
    
    <!-- 主内容区域 -->
    <main>
      <!-- 路由出口：Vue Router 会根据当前 URL 将对应的页面组件渲染在这里 -->
      <!-- 例如访问 /shop/home 时，这里显示 Home.vue -->
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
// 引入导航栏组件
import Navbar from '../components/Navbar.vue'
// 引入主题状态管理 Store
import { useThemeStore } from '../stores/theme'

// 初始化主题 Store
// 布局组件需要监听 themeStore.isDark 的变化，以便动态调整最外层容器的背景色
const themeStore = useThemeStore()
</script>

<style scoped>
/* scoped 表示样式只在当前组件生效，防止污染全局 */
</style>
