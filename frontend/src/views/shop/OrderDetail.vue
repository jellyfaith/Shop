<template>
  <div class="max-w-[1000px] mx-auto my-10 p-5 bg-white dark:bg-gray-800 rounded-2xl min-h-[400px] transition-colors duration-300" v-loading="loading">
    <div v-if="order">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-900 dark:text-white">Order Details</h1>
        <el-button @click="$router.back()">Back</el-button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <div class="bg-gray-50 dark:bg-gray-700/50 p-4 rounded-xl">
          <h3 class="font-bold mb-2 text-gray-900 dark:text-white">Order Info</h3>
          <p class="text-gray-600 dark:text-gray-300">No: {{ order.orderNo }}</p>
          <p class="text-gray-600 dark:text-gray-300">Time: {{ new Date(order.createTime).toLocaleString() }}</p>
          <p class="text-gray-600 dark:text-gray-300 mt-2">
            Status: <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
          </p>
        </div>
        <div class="bg-gray-50 dark:bg-gray-700/50 p-4 rounded-xl">
          <h3 class="font-bold mb-2 text-gray-900 dark:text-white">Receiver Info</h3>
          <p class="text-gray-600 dark:text-gray-300">Name: {{ order.receiverName || 'N/A' }}</p>
          <p class="text-gray-600 dark:text-gray-300">Phone: {{ order.receiverPhone || 'N/A' }}</p>
          <p class="text-gray-600 dark:text-gray-300">Address: {{ order.receiverAddress || 'N/A' }}</p>
        </div>
      </div>

      <h3 class="font-bold text-xl mb-4 text-gray-900 dark:text-white">Items</h3>
      <div class="space-y-4">
        <div v-for="item in order.items" :key="item.productId" class="flex items-center justify-between border-b border-gray-100 dark:border-gray-700 pb-4">
          <div class="flex items-center gap-4">
            <img :src="item.productIcon" class="w-16 h-16 object-cover rounded-lg bg-gray-200">
            <div>
              <p class="font-medium text-gray-900 dark:text-white">{{ item.productName }}</p>
              <p class="text-sm text-gray-500">¥{{ item.price }} x {{ item.quantity }}</p>
            </div>
          </div>
          <p class="font-bold text-gray-900 dark:text-white">¥{{ item.total }}</p>
        </div>
      </div>

      <div class="mt-8 text-right">
        <p class="text-2xl font-bold text-gray-900 dark:text-white">Total: <span class="text-red-500">¥{{ order.totalAmount }}</span></p>
        <div class="mt-4" v-if="order.status === 0">
          <el-button type="primary" size="large" @click="pay">Pay Now</el-button>
        </div>
      </div>
    </div>
    <el-empty v-else description="Order not found" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const order = ref(null)
const loading = ref(false)

const fetchOrder = async () => {
  loading.value = true
  const token = localStorage.getItem('token')
  try {
    const res = await axios.get(`/api/shop/order/${route.params.orderNo}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      order.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const pay = async () => {
  const token = localStorage.getItem('token')
  try {
    const res = await axios.post(`/api/shop/order/pay/${order.value.orderNo}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success('Payment Successful')
      fetchOrder()
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Payment Failed')
  }
}

const getStatusText = (status) => {
  const map = { 0: 'Unpaid', 1: 'Paid', 2: 'Shipped', 3: 'Completed', 4: 'Cancelled' }
  return map[status] || 'Unknown'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'primary', 3: 'success', 4: 'info' }
  return map[status] || 'info'
}

onMounted(() => {
  fetchOrder()
})
</script>
