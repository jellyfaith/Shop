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

        <!-- Address Management Panel -->
        <div v-if="currentTab === 'address'">
          <div class="flex justify-between items-center mb-6">
            <h3 class="text-xl font-bold text-gray-800 dark:text-white">{{ $t('settingsModal.address') }}</h3>
            <el-button type="primary" size="small" @click="openAddressDialog()">{{ $t('settingsModal.addAddress') }}</el-button>
          </div>

          <div v-if="addressList.length > 0" class="space-y-4">
            <div v-for="addr in addressList" :key="addr.id" class="p-4 border rounded-lg dark:border-gray-700 relative group">
              <div class="flex justify-between items-start">
                <div>
                  <div class="flex items-center gap-2">
                    <span class="font-bold text-gray-900 dark:text-white">{{ addr.name }}</span>
                    <span class="text-gray-500 dark:text-gray-400 text-sm">{{ addr.phone }}</span>
                    <el-tag v-if="addr.isDefault" size="small" type="success">{{ $t('settingsModal.default') }}</el-tag>
                  </div>
                  <p class="text-gray-600 dark:text-gray-300 mt-1">{{ addr.address }}</p>
                </div>
                <div class="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <el-button link type="primary" @click="openAddressDialog(addr)">{{ $t('common.edit') }}</el-button>
                  <el-button link type="danger" @click="deleteAddress(addr.id)">{{ $t('common.delete') }}</el-button>
                  <el-button v-if="!addr.isDefault" link type="warning" @click="setDefaultAddress(addr.id)">{{ $t('settingsModal.setDefault') }}</el-button>
                </div>
              </div>
            </div>
          </div>
          <el-empty v-else :description="$t('settingsModal.noAddress')" />
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

        <!-- Account Settings Panel -->
        <div v-if="currentTab === 'account'">
          <h3 class="text-xl font-bold mb-6 text-gray-800 dark:text-white">{{ $t('settingsModal.account') }}</h3>
          
          <div class="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-xl mb-6">
            <div class="flex items-center mb-4">
              <el-avatar :size="64" class="bg-[var(--el-color-primary)] text-2xl">{{ userStore.username ? userStore.username.charAt(0).toUpperCase() : 'U' }}</el-avatar>
              <div class="ml-4">
                <h4 class="text-lg font-bold text-gray-900 dark:text-white">{{ userStore.username }}</h4>
                <p class="text-gray-500 dark:text-gray-400 text-sm">ID: {{ userStore.userInfo?.id || 'Loading...' }}</p>
              </div>
            </div>
            <div class="grid grid-cols-1 gap-4">
              <div class="p-3 bg-white dark:bg-gray-800 rounded-lg border border-gray-100 dark:border-gray-700">
                <span class="text-gray-500 dark:text-gray-400 text-sm block mb-1">{{ $t('shop.email') }}</span>
                <span class="text-gray-900 dark:text-white">{{ userStore.userInfo?.email || 'Not set' }}</span>
              </div>
              <div class="p-3 bg-white dark:bg-gray-800 rounded-lg border border-gray-100 dark:border-gray-700">
                <span class="text-gray-500 dark:text-gray-400 text-sm block mb-1">{{ $t('shop.phone') }}</span>
                <span class="text-gray-900 dark:text-white">{{ userStore.userInfo?.phone || 'Not set' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部按钮区域 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="saveSettings">{{ $t('common.save') }}</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- Address Edit Dialog -->
  <el-dialog v-model="addressDialogVisible" :title="isEdit ? $t('settingsModal.editAddress') : $t('settingsModal.addAddress')" width="500px" append-to-body>
    <el-form :model="addressForm" label-width="80px">
      <el-form-item :label="$t('shop.name')">
        <el-input v-model="addressForm.name" />
      </el-form-item>
      <el-form-item :label="$t('shop.phone')">
        <el-input v-model="addressForm.phone" />
      </el-form-item>
      <el-form-item :label="$t('shop.address')">
        <el-input v-model="addressForm.address" type="textarea" />
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="addressForm.isDefault" :true-label="1" :false-label="0">{{ $t('settingsModal.setDefault') }}</el-checkbox>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addressDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="saveAddress">{{ $t('common.save') }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue'
import { useThemeStore } from '../stores/theme'
import { useLocaleStore } from '../stores/locale'
import { useUserStore } from '../stores/user'
import { useI18n } from 'vue-i18n'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

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
const userStore = useUserStore()

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
  { id: 'address', label: t('settingsModal.address') },
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

// Address Management Logic
const addressList = ref([])
const addressDialogVisible = ref(false)
const isEdit = ref(false)
const addressForm = ref({
  id: null,
  name: '',
  phone: '',
  address: '',
  isDefault: 0
})

const fetchAddresses = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/shop/receiver/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      addressList.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const openAddressDialog = (addr = null) => {
  if (addr) {
    isEdit.value = true
    addressForm.value = { ...addr }
  } else {
    isEdit.value = false
    addressForm.value = { id: null, name: '', phone: '', address: '', isDefault: 0 }
  }
  addressDialogVisible.value = true
}

const saveAddress = async () => {
  const token = localStorage.getItem('token')
  try {
    const url = isEdit.value ? '/api/shop/receiver/update' : '/api/shop/receiver/add'
    const method = isEdit.value ? 'put' : 'post'
    const res = await axios[method](url, addressForm.value, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success(res.data.data || t('common.save') + ' ' + t('common.success')) // Fix: use t('common.success') if available or just generic
      addressDialogVisible.value = false
      fetchAddresses()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error(t('shop.updateFailed'))
  }
}

const deleteAddress = (id) => {
  ElMessageBox.confirm(t('settingsModal.confirmDeleteAddress'), t('common.delete'), {
    confirmButtonText: t('common.submit'),
    cancelButtonText: t('common.cancel'),
    type: 'warning'
  }).then(async () => {
    const token = localStorage.getItem('token')
    try {
      const res = await axios.delete(`/api/shop/receiver/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      if (res.data.code === 200) {
        ElMessage.success(t('shop.removed'))
        fetchAddresses()
      }
    } catch (e) {
      ElMessage.error(t('shop.updateFailed'))
    }
  })
}

const setDefaultAddress = async (id) => {
  const token = localStorage.getItem('token')
  try {
    const res = await axios.post(`/api/shop/receiver/default/${id}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success(t('shop.updateSuccess'))
      fetchAddresses()
    }
  } catch (e) {
    ElMessage.error(t('shop.updateFailed'))
  }
}

watch(currentTab, (val) => {
  if (val === 'address') {
    fetchAddresses()
  }
})
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
