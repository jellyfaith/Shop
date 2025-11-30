<template>
  <div class="login-container bg-white dark:bg-gray-900 transition-colors duration-300">
    <div class="login-box">
      <h2>{{ $t('common.login') }}</h2>
      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" :placeholder="$t('common.username')" prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" :placeholder="$t('common.password')" prefix-icon="Lock" />
        </el-form-item>
        <el-button type="primary" class="login-btn" @click="login" :loading="loading">{{ $t('common.login') }}</el-button>
        <div class="links">
          <a href="#">Forgot Password?</a>
          <a href="#">Register</a>
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

const router = useRouter()
const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const login = async () => {
  loading.value = true
  try {
    const res = await axios.post('/api/user/login', form.value)
    if (res.data.code === 200) {
      localStorage.setItem('token', res.data.data)
      router.push('/shop/home')
    } else {
      alert(res.data.message)
    }
  } catch (e) {
    console.error(e)
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
