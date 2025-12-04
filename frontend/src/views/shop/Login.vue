<template>
  <div class="login-container bg-white dark:bg-gray-900 transition-colors duration-300">
    <div class="login-box dark:bg-gray-800">
      <h2 class="dark:text-white">{{ isRegister ? $t('common.register') : $t('common.login') }}</h2>
      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" :placeholder="$t('common.username')" prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" :placeholder="$t('common.password')" prefix-icon="Lock" />
        </el-form-item>
        <!-- Confirm Password for Register -->
        <el-form-item v-if="isRegister">
          <el-input v-model="form.confirmPassword" type="password" :placeholder="$t('common.confirmPassword')" prefix-icon="Lock" />
        </el-form-item>
        
        <el-button type="primary" class="login-btn" @click="handleSubmit" :loading="loading">
          {{ isRegister ? $t('common.register') : $t('common.login') }}
        </el-button>
        
        <div class="links">
          <a href="#" @click.prevent="toggleMode">
            {{ isRegister ? $t('common.hasAccount') : $t('common.noAccount') }}
          </a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const isRegister = ref(false)
const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const toggleMode = () => {
  isRegister.value = !isRegister.value
  form.value = { username: '', password: '', confirmPassword: '' }
}

const handleSubmit = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('Please fill in all fields')
    return
  }

  if (isRegister.value) {
    if (form.value.password !== form.value.confirmPassword) {
      ElMessage.warning('Passwords do not match')
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
      localStorage.setItem('token', res.data.data)
      ElMessage.success('Login successful')
      router.push('/shop/home')
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Login failed')
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
      ElMessage.success('Registration successful, please login')
      isRegister.value = false
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Registration failed')
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
