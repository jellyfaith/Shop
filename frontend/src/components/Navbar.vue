<template>
  <!-- 
    顶部导航栏组件
    sticky top-0 z-50: 吸顶定位，层级最高
    dark:bg-gray-900: 深色模式下背景变黑
    transition-colors: 颜色切换时的过渡动画
  -->
  <nav class="bg-white dark:bg-gray-900 border-b border-gray-100 dark:border-gray-800 transition-colors duration-300 shadow-sm dark:shadow-none sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <!-- 左侧区域：Logo 与主导航菜单 -->
        <div class="flex items-center">
          <!-- Logo 区域：点击跳转回首页 -->
          <div class="flex-shrink-0 flex items-center cursor-pointer group" @click="$router.push('/shop/home')">
            <!-- Logo 图标：动态背景渐变，读取主题色 -->
            <div class="w-8 h-8 rounded-lg mr-2 flex items-center justify-center text-white font-bold text-lg shadow-md group-hover:shadow-lg transition-all" :style="{ background: `linear-gradient(135deg, ${themeStore.primaryColor}, ${themeStore.primaryColor}dd)` }">S</div>
            <!-- Logo 文字：渐变色文字效果 -->
            <span class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-gray-900 to-gray-600 dark:from-white dark:to-gray-300">Shop</span>
          </div>
          
          <!-- 桌面端导航链接 (移动端隐藏 hidden sm:flex) -->
          <div class="hidden sm:ml-8 sm:flex sm:space-x-8">
            <!-- 首页链接 -->
            <router-link to="/shop/home" class="text-gray-900 dark:text-gray-100 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-[var(--el-color-primary)] hover:text-[var(--el-color-primary)] dark:hover:text-[var(--el-color-primary)] text-sm font-medium transition-colors">
              {{ $t('shop.home') }}
            </router-link>
            <!-- 购物车链接 -->
            <router-link to="/shop/cart" class="text-gray-900 dark:text-gray-100 hover:text-gray-900 dark:hover:text-gray-100 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-[var(--el-color-primary)] text-sm font-medium transition-colors">
              {{ $t('shop.cart') }}
            </router-link>
          </div>
        </div>

        <!-- 右侧操作区：暗色切换 + 语言/货币 + 登录 -->
        <div class="flex items-center space-x-2 sm:space-x-4">
          <!-- 日/夜间模式切换按钮 -->
          <button
            @click="themeStore.toggleDarkMode"
            class="p-2 rounded-full text-gray-500 hover:bg-gray-100 dark:text-gray-400 dark:hover:bg-gray-800 focus:outline-none transition-all duration-200 hover:scale-110"
          >
            <!-- 根据当前模式显示月亮或太阳图标 -->
            <el-icon v-if="themeStore.isDark" :size="20" class="text-yellow-400"><Moon /></el-icon>
            <el-icon v-else :size="20" class="text-orange-500"><Sunny /></el-icon>
          </button>

          <!-- 语言/货币悬浮面板：使用 group 实现 hover 显示下拉菜单 -->
          <div class="relative group h-full flex items-center">
            <!-- 触发按钮：显示当前国旗和语言代码 -->
            <button class="flex items-center space-x-1 text-gray-700 dark:text-gray-200 hover:text-[var(--el-color-primary)] dark:hover:text-[var(--el-color-primary)] focus:outline-none py-2 px-2 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors !w-20">
              <span class="text-xl">{{ currentFlag }}</span>
              <span class="font-medium text-sm hidden sm:inline">{{ localeStore.currentLocale.toUpperCase() }}</span>
              <el-icon :size="12" class="text-gray-400 ml-1 transition-transform group-hover:rotate-180"><ArrowDown /></el-icon>
            </button>

            <!-- 下拉菜单内容：绝对定位，默认隐藏 (opacity-0 invisible)，hover 时显示 -->
            <div class="absolute right-0 top-14 w-64 bg-white dark:bg-gray-800 rounded-xl shadow-xl ring-1 ring-black ring-opacity-5 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50 transform origin-top-right scale-95 group-hover:scale-100 ">
              <div class="py-1">
                <!-- 语言选择区域 -->
                <div class="px-4 py-3 border-b border-gray-100 dark:border-gray-700">
                  <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">{{ $t('setting.language') }}</p>
                  <div class="space-y-2">
                    <!-- 英文选项 -->
                    <label class="flex items-center justify-between cursor-pointer p-2 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                      <div class="flex items-center space-x-3">
                        <span class="text-lg">🇺🇸</span>
                        <span class="text-sm text-gray-700 dark:text-gray-200">English</span>
                      </div>
                      <input type="radio" :checked="localeStore.currentLocale === 'en'" @change="localeStore.setLocale('en')" class="text-[var(--el-color-primary)] focus:ring-[var(--el-color-primary)]">
                    </label>
                    <!-- 中文选项 -->
                    <label class="flex items-center justify-between cursor-pointer p-2 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                      <div class="flex items-center space-x-3">
                        <span class="text-lg">🇨🇳</span>
                        <span class="text-sm text-gray-700 dark:text-gray-200">中文</span>
                      </div>
                      <input type="radio" :checked="localeStore.currentLocale === 'zh'" @change="localeStore.setLocale('zh')" class="text-[var(--el-color-primary)] focus:ring-[var(--el-color-primary)]">
                    </label>
                  </div>
                </div>
                
                <!-- 货币展示区域 -->
                <div class="px-4 py-3 border-b border-gray-100 dark:border-gray-700">
                  <div class="flex items-center justify-between">
                    <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">{{ $t('setting.currency') }}</p>
                    <span class="text-xs text-[var(--el-color-primary)] cursor-pointer hover:underline" @click="openSettings">{{ $t('setting.change') }}</span>
                  </div>
                  <div class="mt-2 text-sm font-medium text-gray-900 dark:text-white bg-gray-50 dark:bg-gray-700/50 p-2 rounded-lg text-center">
                    {{ localeStore.currency }}
                  </div>
                </div>

                <!-- 更多设置链接 -->
                <div class="px-4 py-2 bg-gray-50 dark:bg-gray-700/30 rounded-b-xl">
                  <a
                    href="#"
                    @click.prevent="openSettings"
                    class="block text-center text-sm text-[var(--el-color-primary)] dark:text-[var(--el-color-primary)] hover:text-[var(--el-color-primary)] dark:hover:text-[var(--el-color-primary)] font-medium opacity-90 hover:opacity-100"
                  >
                    {{ $t('setting.more') }}
                  </a>
                </div>
              </div>
            </div>
          </div>

          <!-- 登录入口：Element Plus 按钮，强制覆盖背景色为主题色 -->
          <div class="ml-3 relative">
             <el-button type="primary" 
             round 
             class="!bg-[var(--el-color-primary)] !border-[var(--el-color-primary)] hover:opacity-90 !font-medium !px-6 !w-24" 
             @click="$router.push('/shop/login')">
             {{ $t('common.login') }}
            </el-button>
          </div>
        </div> 
      </div>
    </div>

    <!-- 全局设置弹窗组件 -->
    <SettingsModal v-model="showSettings" />
  </nav>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Moon, Sunny, ArrowDown } from '@element-plus/icons-vue'
import { useThemeStore } from '../stores/theme'
import { useLocaleStore } from '../stores/locale'
import SettingsModal from './SettingsModal.vue'

// 引入状态管理 Store
const themeStore = useThemeStore()
const localeStore = useLocaleStore()

// 控制设置弹窗的显示/隐藏
const showSettings = ref(false)

// 计算属性：根据当前语言代码返回对应的国旗 Emoji
const currentFlag = computed(() => {
  switch (localeStore.currentLocale) {
    case 'en': return '🇺🇸'
    case 'zh': return '🇨🇳'
    case 'es': return '🇪🇸'
    default: return '🌐'
  }
})

// 打开更多设置弹窗的方法
const openSettings = () => {
  showSettings.value = true
}
</script>
