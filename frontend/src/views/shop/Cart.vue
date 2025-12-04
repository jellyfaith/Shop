<template>
  <div class="max-w-[1000px] mx-auto my-10 p-5 bg-white dark:bg-gray-800 rounded-2xl min-h-[400px] transition-colors duration-300">
    <h1 class="text-2xl font-bold mb-6 text-gray-900 dark:text-white">{{ $t('shop.cart') }}</h1>
    
    <div v-if="cartItems && cartItems.length > 0">
      <div v-for="item in cartItems" :key="item.productId" class="flex items-center justify-between border-b border-gray-200 dark:border-gray-700 py-4">
        <div class="flex items-center gap-4">
          <img :src="item.productIcon" alt="" class="w-20 h-20 object-cover rounded-lg bg-gray-100">
          <div>
            <h3 class="text-lg font-semibold text-gray-900 dark:text-white">{{ item.productName }}</h3>
            <p class="text-gray-500 dark:text-gray-400">¥{{ item.price }}</p>
          </div>
        </div>
        <div class="flex items-center gap-4">
          <el-input-number v-model="item.quantity" :min="1" @change="(val) => updateQuantity(item, val)" />
          <p class="font-bold text-gray-900 dark:text-white w-24 text-right">¥{{ item.subTotal }}</p>
          <el-button type="danger" circle :icon="Delete" @click="removeItem(item.productId)" />
        </div>
      </div>
      
      <div class="mt-8 flex justify-end items-center gap-6">
        <div class="text-xl text-gray-900 dark:text-white">
          Total: <span class="font-bold text-red-500 text-2xl">¥{{ totalPrice }}</span>
        </div>
        <el-button type="primary" size="large" @click="checkout">Checkout</el-button>
      </div>
    </div>

    <el-empty v-else :description="$t('shop.cartEmpty')" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const cartItems = ref([])
const totalPrice = ref(0)

const fetchCart = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/shop/cart', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      cartItems.value = res.data.data.items
      totalPrice.value = res.data.data.totalPrice
    }
  } catch (e) {
    console.error(e)
  }
}

const updateQuantity = async (item, quantity) => {
  const token = localStorage.getItem('token')
  try {
    await axios.put('/api/shop/cart', {
      productId: item.productId,
      quantity: quantity
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    fetchCart() // Refresh to get correct subtotal/total
  } catch (e) {
    console.error(e)
  }
}

const removeItem = async (productId) => {
  const token = localStorage.getItem('token')
  try {
    await axios.delete(`/api/shop/cart/${productId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    ElMessage.success('Removed')
    fetchCart()
  } catch (e) {
    console.error(e)
  }
}

const checkout = async () => {
  const token = localStorage.getItem('token')
  try {
    // Simple checkout for now
    const res = await axios.post('/api/shop/order/create', {
        receiverName: "Test User", // Placeholder
        receiverPhone: "1234567890",
        receiverAddress: "Test Address",
        paymentMethod: "WECHAT"
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success('Order created! Order No: ' + res.data.data)
      fetchCart()
      // router.push('/shop/order/' + res.data.data)
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Checkout failed')
  }
}

onMounted(() => {
  fetchCart()
})
</script>
