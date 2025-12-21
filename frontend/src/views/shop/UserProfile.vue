<template>
  <div class="max-w-[800px] mx-auto my-10 p-8 bg-white dark:bg-gray-800 rounded-2xl shadow-sm transition-colors duration-300">
    <h1 class="text-2xl font-bold mb-8 text-gray-900 dark:text-white">{{ $t('shop.userProfile') }}</h1>
    
    <el-form :model="form" label-width="120px" class="max-w-[600px]">
      <el-form-item :label="$t('common.username')">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      
      <el-form-item :label="$t('shop.email')">
        <el-input v-model="form.email" />
      </el-form-item>
      
      <el-form-item :label="$t('shop.phone')">
        <el-input v-model="form.phone" />
      </el-form-item>
      
      <el-form-item :label="$t('shop.address')">
        <el-input v-model="form.address" type="textarea" :rows="3" />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="save" :loading="loading">{{ $t('common.save') }}</el-button>
        <el-button @click="$router.back()">{{ $t('common.cancel') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const loading = ref(false)
const form = ref({
  username: '',
  email: '',
  phone: '',
  address: ''
})

const fetchUserInfo = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/shop/user/info', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      form.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const save = async () => {
  loading.value = true
  const token = localStorage.getItem('token')
  try {
    const res = await axios.put('/api/shop/user/update', form.value, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success(t('shop.updateSuccess'))
    } else {
      ElMessage.error(res.data.msg || t('shop.updateFailed'))
    }
  } catch (e) {
    console.error(e)
    ElMessage.error(t('shop.updateFailed'))
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>
