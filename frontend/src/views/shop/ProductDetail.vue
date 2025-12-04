<template>
  <div class="product-detail-container bg-white dark:bg-gray-800" v-loading="loading">
    <div v-if="product" class="product-content">
      <div class="product-image bg-gray-50 dark:bg-gray-700">
        <img :src="product.image" :alt="product.name" />
      </div>
      <div class="product-info">
        <h1 class="text-gray-900 dark:text-white">{{ product.name }}</h1>
        <p class="price">¥{{ product.price }}</p>
        <div class="description">
          <h3 class="text-gray-900 dark:text-gray-200">{{ $t('shop.description') }}</h3>
          <p class="text-gray-600 dark:text-gray-400">{{ product.description || 'No description available.' }}</p>
        </div>
        <div class="actions">
          <el-input-number v-model="quantity" :min="1" :max="99" />
          <el-button type="primary" size="large" @click="addToCart">{{ $t('shop.addToCart') }}</el-button>
        </div>
      </div>
    </div>
    <el-empty v-else description="Product not found" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const product = ref(null)
const loading = ref(false)
const quantity = ref(1)

const fetchProduct = async () => {
  loading.value = true
  try {
    const res = await axios.get(`/api/shop/product/${route.params.id}`)
    if (res.data.code === 200) {
      product.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const addToCart = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('Please login first')
    // router.push('/shop/login') // Need to import router first if I want to redirect
    return
  }
  try {
    const res = await axios.post('/api/shop/cart', {
      productId: product.value.id,
      quantity: quantity.value
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success(`Added ${quantity.value} ${product.value.name} to cart`)
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Failed to add to cart')
  }
}

onMounted(() => {
  fetchProduct()
})
</script>

<style scoped>
.product-detail-container {
  max-width: 1000px;
  margin: 40px auto;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.product-content {
  display: flex;
  gap: 40px;
}

.product-image {
  flex: 1;
  border-radius: 12px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: auto;
  display: block;
}

.product-info {
  flex: 1;
}

.product-info h1 {
  font-size: 2rem;
  margin-bottom: 10px;
}

.price {
  font-size: 1.5rem;
  color: #e53e3e;
  font-weight: bold;
  margin-bottom: 20px;
}

.description {
  margin-bottom: 30px;
  line-height: 1.6;
}

.actions {
  display: flex;
  gap: 20px;
  align-items: center;
}
</style>
