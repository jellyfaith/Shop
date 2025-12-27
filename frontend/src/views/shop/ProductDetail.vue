<template>
  <div class='product-detail-container bg-white dark:bg-gray-800' v-loading='loading'>
    <div v-if='product' class='product-content'>
      <div class='product-image bg-gray-50 dark:bg-gray-700'>
        <img :src='product.mainImage' :alt='product.name' />
      </div>
      <div class='product-info'>
        <h1 class='text-gray-900 dark:text-white'>{{ product.name }}</h1>
        
        <!-- 价格显示：根据是否选中 SKU 显示不同价格 -->
        <p class='price'>
          <span v-if='currentSku'>{{ currentSku.price }}</span>
          <span v-else-if="priceRange">{{ priceRange }}</span>
          <span v-else>{{ product.minPrice }} {{ $t('shop.startingFrom') }}</span>
        </p>

        <div class='description'>
          <h3 class='text-gray-900 dark:text-gray-200'>{{ $t('shop.description') }}</h3>
          <p class='text-gray-600 dark:text-gray-400'>{{ product.description || $t('shop.noDescription') }}</p>
        </div>

        <!-- 规格选择区域 -->
        <div v-if='specKeys.length > 0' class='specs-section'>
          <div v-for='key in specKeys' :key='key' class='spec-group mb-4'>
            <h4 class='text-sm font-medium text-gray-700 dark:text-gray-300 mb-2 capitalize'>{{ key }}</h4>
            <div class='flex flex-wrap gap-2'>
              <el-button 
                v-for='value in specOptions[key]' 
                :key='value'
                :type="selectedSpecs[key] === value ? 'primary' : 'default'"
                :disabled="!isSpecValid(key, value)"
                size='small'
                @click='selectSpec(key, value)'
              >
                {{ value }}
              </el-button>
            </div>
          </div>
        </div>

        <!-- 库存显示 -->
        <div class='stock-info mb-4 text-gray-500 text-sm'>
          <span v-if='currentSku'>{{ $t('shop.stock') }} {{ currentSku.stock }}</span>
          <span v-else>{{ $t('shop.selectSpecs') }}</span>
        </div>

        <div class='actions'>
          <el-input-number v-model='quantity' :min='1' :max='currentSku ? currentSku.stock : 1' :disabled='!currentSku || currentSku.stock <= 0' />
          <el-button type='primary' size='large' @click='addToCart' :disabled='!currentSku || currentSku.stock <= 0'>
            {{ $t('shop.addToCart') }}
          </el-button>
        </div>
      </div>
    </div>
    <el-empty v-else :description="$t('shop.productNotFound')" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../../utils/request'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const loading = ref(false)
const quantity = ref(1)
const selectedSpecs = ref({})

// 计算属性：获取所有 SKU 中的唯一规格键
const specKeys = computed(() => {
  if (!product.value || !product.value.skus) return []
  const keys = new Set()
  product.value.skus.forEach(sku => {
    if (sku.specs) {
      Object.keys(sku.specs).forEach(k => keys.add(k))
    }
  })
  return Array.from(keys)
})

// 计算属性：获取每个规格键的所有可选值
const specOptions = computed(() => {
  if (!product.value || !product.value.skus) return {}
  const options = {}
  specKeys.value.forEach(key => {
    const values = new Set()
    product.value.skus.forEach(sku => {
      if (sku.specs && sku.specs[key]) {
        values.add(sku.specs[key])
      }
    })
    options[key] = Array.from(values)
  })
  return options
})

// 计算属性：查找匹配当前选中规格的 SKU
const currentSku = computed(() => {
  if (!product.value || !product.value.skus) return null
  // Check if all keys are selected
  for (const key of specKeys.value) {
    if (!selectedSpecs.value[key]) return null
  }
  
  return product.value.skus.find(sku => {
    for (const key of specKeys.value) {
      if (sku.specs[key] !== selectedSpecs.value[key]) return false
    }
    return true
  })
})

const priceRange = computed(() => {
  if (!product.value || !product.value.skus || product.value.skus.length === 0) return null
  const prices = product.value.skus.map(s => s.price)
  const min = Math.min(...prices)
  const max = Math.max(...prices)
  if (min === max) return min
  return `${min} - ${max}`
})

const fetchProduct = async () => {
  loading.value = true
  try {
    const res = await request.get(`/shop/product/${route.params.id}`)
    if (res.data.code === 200) {
      const { product: productInfo, skus } = res.data.data
      product.value = { ...productInfo, skus }
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// Helper: Check if a spec value is valid given other selected specs
const isSpecValid = (key, value) => {
  // Create a temporary selection with the current key-value pair
  const tempSpecs = { ...selectedSpecs.value, [key]: value }
  
  // Check if there is any SKU that matches this combination (considering other selected specs)
  return product.value.skus.some(sku => {
    // Check if this SKU matches all currently selected specs (except the one we are checking)
    for (const k in tempSpecs) {
      // If the user hasn't selected a value for this key yet, we don't need to check it
      // But here tempSpecs contains the potential new selection, so we check against it.
      // However, we only care about keys that are actually in tempSpecs.
      // Wait, the logic should be:
      // For a given key (e.g. 'storage') and value (e.g. '512GB'),
      // is there a SKU that has storage='512GB' AND matches all OTHER selected specs?
      
      // If we are checking 'storage', we should ignore the current 'storage' selection in selectedSpecs
      // and use the 'value' passed to the function.
      // For other keys (e.g. 'color'), if a value is selected, the SKU must match it.
      
      if (k !== key && selectedSpecs.value[k] && sku.specs[k] !== selectedSpecs.value[k]) {
        return false
      }
    }
    // Also check if the SKU has this specific value for the key we are checking
    if (sku.specs[key] !== value) return false
    
    // Finally, check if stock > 0
    return sku.stock > 0
  })
}

const selectSpec = (key, value) => {
  if (!isSpecValid(key, value)) return // Prevent selection if invalid (though button should be disabled)
  
  // If clicking the already selected value, deselect it
  if (selectedSpecs.value[key] === value) {
    const newSpecs = { ...selectedSpecs.value }
    delete newSpecs[key]
    selectedSpecs.value = newSpecs
  } else {
    selectedSpecs.value = { ...selectedSpecs.value, [key]: value }
  }
}

const addToCart = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('Please login first')
    router.push('/shop/login')
    return
  }
  
  if (!currentSku.value) {
    ElMessage.warning('Please select all specifications')
    return
  }

  try {
    const res = await axios.post('/api/shop/cart', {
      productId: product.value.id,
      skuId: currentSku.value.id,
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

.specs-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9fafb;
  border-radius: 8px;
}

.dark .specs-section {
  background-color: #374151;
}
</style>
