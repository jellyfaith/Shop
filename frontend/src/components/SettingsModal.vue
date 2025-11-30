<template>
  <!-- 设置模态框对话框 -->
  <el-dialog
    v-model="visible"
    :title="$t('settingsModal.settings')"
    width="800px"
    class="settings-modal"
    :before-close="handleClose"
    append-to-body
  >
    <div class="flex h-[500px]">
      <!-- 侧边栏导航 -->
      <div class="w-1/4 border-r border-gray-200 dark:border-gray-700 pr-4">
        <div
          v-for="tab in tabs"
          :key="tab.id"
          @click="currentTab = tab.id"
          class="cursor-pointer p-3 rounded-lg mb-2 transition-colors duration-200"
          :class="currentTab === tab.id ? 'bg-blue-100 text-blue-600 dark:bg-blue-900 dark:text-blue-300' : 'hover:bg-gray-100 dark:hover:bg-gray-800 text-gray-600 dark:text-gray-300'"
        >
          {{ tab.label }}
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="w-3/4 pl-6 overflow-y-auto">
        <!-- 语言和区域设置面板 -->
        <div v-if="currentTab === 'language'">
          <h3 class="text-xl font-bold mb-6 text-gray-800 dark:text-white">{{ $t('settingsModal.languageAndCurrency') }}</h3>
          
          <!-- 语言选择 -->
          <div class="mb-8">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-3">{{ $t('settingsModal.language') }}</label>
            <div class="space-y-3">
              <div v-for="lang in languages" :key="lang.code" class="flex items-center">
                <input
                  type="radio"
                  :id="lang.code"
                  :value="lang.code"
                  v-model="tempLocale"
                  class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                >
                <label :for="lang.code" class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">
                  {{ lang.name }} <span class="text-gray-500 text-xs ml-1">{{ lang.native }}</span>
                </label>
              </div>
            </div>
          </div>

          <!-- 货币选择 -->
          <div class="mb-8">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-3">{{ $t('settingsModal.currency') }}</label>
            <select
              v-model="tempCurrency"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            >
              <option value="USD">USD - US Dollar ($)</option>
              <option value="CNY">CNY - Chinese Yuan (¥)</option>
              <option value="EUR">EUR - Euro (€)</option>
            </select>
          </div>
        </div>

        <!-- 外观设置面板 -->
        <div v-if="currentTab === 'appearance'">
          <h3 class="text-xl font-bold mb-6 text-gray-800 dark:text-white">{{ $t('settingsModal.appearance') }}</h3>
          
          <!-- 深色模式切换 -->
          <div class="mb-8">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-3">{{ $t('settingsModal.thememode') }}</label>
            <div class="flex items-center justify-between p-4 border border-gray-200 dark:border-gray-700 rounded-lg">
              <span class="text-gray-900 dark:text-white">{{ $t('settingsModal.darkmode') }}</span>
              <el-switch v-model="themeStore.isDark" />
            </div>
          </div>

          <!-- 主题色选择 -->
          <div class="mb-8">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-3">{{ $t('settingsModal.primaryColor') }}</label>
            <div class="flex gap-4">
              <div
                v-for="color in colors"
                :key="color"
                @click="themeStore.setPrimaryColor(color)"
                class="w-10 h-10 rounded-full cursor-pointer ring-2 ring-offset-2 transition-all"
                :style="{ backgroundColor: color }"
                :class="themeStore.primaryColor === color ? 'ring-gray-400 dark:ring-gray-500' : 'ring-transparent'"
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部按钮区域 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">Cancel</el-button>
        <el-button type="primary" @click="saveSettings">Save Changes</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useThemeStore } from '../stores/theme'
import { useLocaleStore } from '../stores/locale'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

// 定义组件的 props，接收 modelValue 控制显示隐藏
const props = defineProps({
  modelValue: Boolean
})

// 定义组件的 emits，用于更新 modelValue
const emit = defineEmits(['update:modelValue'])

// 初始化主题和语言状态管理 store
const themeStore = useThemeStore()
const localeStore = useLocaleStore()

// 控制对话框显示的响应式变量
const visible = ref(props.modelValue)
// 当前选中的设置标签页，默认为 'language'
const currentTab = ref('language')

// 临时存储用户选择的语言和货币，点击保存时才生效
const tempLocale = ref(localeStore.currentLocale)
const tempCurrency = ref(localeStore.currency)

// 设置选项卡配置
const tabs = computed(() => [
  { id: 'language', label: t('settingsModal.languageAndCurrency') },
  { id: 'appearance', label: t('settingsModal.appearance') },
  { id: 'account', label: t('settingsModal.account') }
])

// 支持的语言列表
const languages = computed(() => [
  { code: 'en', name: t('settingsModal.english'), native: 'English' },
  { code: 'zh', name: t('settingsModal.chinese'), native: '中文' },
])

// 可选的主题颜色列表
const colors = ['#8B5CF6', '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']

// 监听 props.modelValue 的变化，同步 visible 状态
// 当对话框打开时，重置临时变量为当前 store 中的值
watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    tempLocale.value = localeStore.currentLocale
    tempCurrency.value = localeStore.currency
  }
})

// 关闭对话框的处理函数
const handleClose = () => {
  visible.value = false
  emit('update:modelValue', false)
}

// 保存设置的处理函数
// 将临时变量的值应用到 store 中，并关闭对话框
const saveSettings = () => {
  localeStore.setLocale(tempLocale.value)
  localeStore.setCurrency(tempCurrency.value)
  handleClose()
}
</script>

<style scoped>
/* 内容区域的自定义滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 4px;
}
::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}
</style>
