<template>
  <div class="login-container bg-white dark:bg-gray-900 transition-colors duration-300">
    <div class="login-box dark:bg-gray-800 relative">
      <!-- 错误提示框 -->
      <transition name="el-fade-in-linear">
        <el-alert
          v-if="errorMessage"
          :title="errorMessage"
          type="error"
          show-icon
          :closable="true"
          @close="errorMessage = ''"
          class="mb-6 !rounded-lg"
          effect="dark"
        />
      </transition>
      
      <!-- 成功提示框 -->
      <transition name="el-fade-in-linear">
        <el-alert
          v-if="successMessage"
          :title="successMessage"
          type="success"
          show-icon
          :closable="true"
          @close="successMessage = ''"
          class="mb-6 !rounded-lg"
          effect="dark"
        />
      </transition>

      <h2 class="dark:text-white">{{ isRegister ? $t('common.register') : $t('common.login') }}</h2>
      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" :placeholder="$t('common.username')" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" :placeholder="$t('common.password')" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <!-- 注册时的确认密码输入框 -->
        <el-form-item v-if="isRegister">
          <el-input v-model="form.confirmPassword" type="password" :placeholder="$t('common.confirmPassword')" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        
        <el-button type="primary" class="login-btn" @click="handleSubmit" :loading="loading" size="large">
          {{ isRegister ? $t('common.register') : $t('common.login') }}
        </el-button>
        
        <div class="links">
          <a href="#" @click.prevent="toggleMode" class="hover:text-blue-500 transition-colors">
            {{ isRegister ? $t('common.hasAccount') : $t('common.noAccount') }}
          </a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../../stores/user'

const { t } = useI18n()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const isRegister = ref(false) // 是否为注册模式
const errorMessage = ref('')
const successMessage = ref('')
const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

// 切换模式时清除错误信息
watch(isRegister, () => {
  errorMessage.value = ''
  successMessage.value = ''
})

// 切换登录/注册模式
const toggleMode = () => {
  isRegister.value = !isRegister.value
  form.value = { username: '', password: '', confirmPassword: '' }
}

const handleSubmit = async () => {
  errorMessage.value = '' // 清除之前的错误
  successMessage.value = ''
  
  if (!form.value.username || !form.value.password) {
    errorMessage.value = t('common.fillAllFields')
    return
  }

  if (isRegister.value) {
    if (form.value.password !== form.value.confirmPassword) {
      errorMessage.value = t('common.passwordMismatch')
      return
    }
    // Password validation: 8-20 chars, at least 2 types
    const password = form.value.password
    if (password.length < 8 || password.length > 20) {
      errorMessage.value = t('common.passwordLength')
      return
    }
    let types = 0
    if (/[a-zA-Z]/.test(password)) types++
    if (/\d/.test(password)) types++
    if (/[^a-zA-Z0-9]/.test(password)) types++
    
    if (types < 2) {
      errorMessage.value = t('common.passwordComplexity')
      return
    }

    await register()
  } else {
    await login()
  }
}

const login = async () => {
  loading.value = true
  try {
    const res = await axios.post('/api/shop/user/login', {
      username: form.value.username,
      password: form.value.password
    })
    if (res.data.code === 200) {
      const token = res.data.data
      userStore.setToken(token)
      await userStore.fetchUserInfo() // Fetch user info immediately
      
      ElMessage.success(t('common.loginSuccess'))
      // Check if there is a redirect query param
      const redirect = router.currentRoute.value.query.redirect || '/shop/home'
      router.push(redirect)
    } else {
      errorMessage.value = res.data.message || t('common.loginFailed')
    }
  } catch (e) {
    console.error(e)
    errorMessage.value = t('common.networkError')
  } finally {
    loading.value = false
  }
}

const register = async () => {
  loading.value = true
  try {
    const res = await axios.post('/api/shop/user/register', {
      username: form.value.username,
      password: form.value.password
    })
    if (res.data.code === 200) {
      successMessage.value = t('common.registerSuccess')
      // Switch to login mode
      isRegister.value = false
      // Clear password fields but keep username for convenience
      form.value.password = ''
      form.value.confirmPassword = ''
    } else {
      // Show specific error message from backend (e.g., "Username already exists")
      errorMessage.value = res.data.message || t('common.registerFailed')
    }
  } catch (e) {
    console.error(e)
    errorMessage.value = e.response?.data?.message || t('common.registerRetry')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-box {
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  width: 350px;
  text-align: center;
}
.login-box h2 {
  margin-bottom: 30px;
  color: #333;
}
.login-btn {
  width: 100%;
  margin-top: 20px;
  height: 40px;
  font-size: 16px;
}
.links {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}
.links a {
  color: #667eea;
  text-decoration: none;
}
</style>
