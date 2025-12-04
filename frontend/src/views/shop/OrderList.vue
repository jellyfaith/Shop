<template>
  <div class="max-w-[1000px] mx-auto my-10 p-5 bg-white dark:bg-gray-800 rounded-2xl min-h-[400px] transition-colors duration-300">
    <h1 class="text-2xl font-bold mb-6 text-gray-900 dark:text-white">{{ $t('shop.myOrders') }}</h1>
    
    <div v-if="orders && orders.length > 0" class="space-y-6">
      <div v-for="order in orders" :key="order.orderNo" class="border border-gray-200 dark:border-gray-700 rounded-xl p-4 hover:shadow-md transition-shadow">
        <div class="flex justify-between items-center mb-4 border-b border-gray-100 dark:border-gray-700 pb-2">
          <div>
            <span class="text-sm text-gray-500 dark:text-gray-400">Order No: </span>
            <span class="font-mono font-medium text-gray-900 dark:text-white">{{ order.orderNo }}</span>
          </div>
          <div class="text-sm text-gray-500 dark:text-gray-400">
            {{ new Date(order.createTime).toLocaleString() }}
          </div>
        </div>
        
        <div class="space-y-2">
          <div v-for="item in order.items" :key="item.productId" class="flex justify-between items-center">
            <div class="flex items-center gap-2">
              <span class="text-gray-900 dark:text-white">{{ item.productName }}</span>
              <span class="text-gray-500 dark:text-gray-400 text-sm">x{{ item.quantity }}</span>
            </div>
            <span class="text-gray-900 dark:text-white">¥{{ item.total }}</span>
          </div>
        </div>

        <div class="flex justify-between items-center mt-4 pt-2 border-t border-gray-100 dark:border-gray-700">
          <div class="flex items-center gap-4">
            <span class="font-bold text-lg text-gray-900 dark:text-white">Total: ¥{{ order.totalAmount }}</span>
            <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
          </div>
          <div class="flex gap-2">
            <el-button v-if="order.status === 0" type="primary" size="small" @click="pay(order.orderNo)">Pay Now</el-button>
            <el-button size="small" @click="$router.push(`/shop/order/${order.orderNo}`)">Details</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-else :description="$t('shop.noOrders')" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const orders = ref([])

const fetchOrders = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/shop/order/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      orders.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const pay = async (orderNo) => {
  const token = localStorage.getItem('token')
  try {
    const res = await axios.post(`/api/shop/order/pay/${orderNo}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success('Payment Successful')
      fetchOrders()
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
  fetchOrders()
})
</script>
