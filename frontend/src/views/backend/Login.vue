<template>
  <div class="admin-login-container min-h-screen flex items-center justify-center bg-gray-100 dark:bg-gray-900 transition-colors duration-300">
    <div class="login-card w-full max-w-md p-8 bg-white dark:bg-gray-800 rounded-2xl shadow-xl border border-gray-100 dark:border-gray-700">
      <div class="header text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">{{ $t('admin.title') }}</h1>
        <p class="text-gray-500 dark:text-gray-400">{{ $t('admin.secureAccess') }}</p>
      </div>
      <el-form :model="form" size="large" class="space-y-6">
        <el-form-item>
          <el-input 
            v-model="form.username" 
            :placeholder="$t('common.username')" 
            prefix-icon="User" 
            class="!rounded-lg"
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="form.password" 
            type="password" 
            :placeholder="$t('common.password')" 
            prefix-icon="Lock" 
            show-password 
            class="!rounded-lg"
            @keyup.enter="login"
          />
        </el-form-item>
        <el-button 
          type="primary" 
          class="w-full !h-12 !text-lg !rounded-lg !font-semibold shadow-md hover:shadow-lg transition-all duration-200" 
          @click="login" 
          :loading="loading"
        >
          {{ $t('common.login') }}
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { User, Lock } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user'

const { t } = useI18n()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const login = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning(t('common.fillAllFields'))
    return
  }
  loading.value = true
  try {
    const res = await axios.post('/api/backend/user/login', form.value)
    if (res.data.code === 200) {
      userStore.setAdminToken(res.data.data)
      ElMessage.success(t('common.loginSuccess'))
      router.push('/backend/dashboard')
    } else {
      ElMessage.error(res.data.message || t('common.loginFailed'))
    }
  } catch (e) {
    console.error(e)
    ElMessage.error(t('common.networkError'))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* Scoped styles can be removed if using Tailwind fully, but keeping for specific overrides if needed */
</style>
