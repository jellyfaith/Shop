<template>
  <div class="home-container min-h-screen pb-20 transition-colors duration-300">
    <!-- 英雄横幅：根据主题色生成渐变背景，强调品牌氛围 -->
    <div 
      class="relative pt-24 pb-32 px-6 rounded-b-[3rem] shadow-lg overflow-hidden transition-all duration-500"
      :style="bannerStyle"
    >
      <!-- 装饰性图形：叠加网格和虚化圆形，制造层次感 -->
      <div class="absolute inset-0 opacity-10 pointer-events-none" 
           style="background-image: radial-gradient(circle at 2px 2px, white 1px, transparent 0); background-size: 32px 32px;">
      </div>
      <div class="absolute top-0 right-0 -mt-20 -mr-20 w-96 h-96 bg-white opacity-5 rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 -mb-20 -ml-20 w-72 h-72 bg-yellow-300 opacity-10 rounded-full blur-3xl"></div>

      <div class="relative max-w-4xl mx-auto text-center z-10">
        <h1 class="text-5xl md:text-7xl font-bold tracking-tight text-white">
          {{ $t('shop.bannerTitle') }}
        </h1>
        <br>
        <p class="text-xl md:text-2xl text-white font-medium max-w-2xl mx-auto">
          {{ $t('shop.bannerSubtitle') }}
        </p>
      </div>
    </div>

    <!-- 浮动搜索卡片：承载搜索、分类筛选与按钮 -->
    <div class="relative z-20 -mt-12 px-4 md:px-0 flex justify-center">
      <div class="w-full md:w-3/4 md:max-w-4xl bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-2 md:p-4 flex flex-col md:flex-row gap-4 items-center border border-gray-100 dark:border-gray-700 transition-colors duration-300 backdrop-blur-sm bg-opacity-95 dark:bg-opacity-95">
        
        <!-- 搜索输入框：回车或按钮均会触发 fetchProducts -->
        <div class="flex-1 w-full">
          <div class="relative group">
            <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
              <el-icon class="text-gray-400 group-focus-within:text-violet-500 transition-colors"><Search /></el-icon>
            </div>
            <input 
              v-model="searchKeyword" 
              type="text" 
              :placeholder="$t('shop.searchPlaceholder')" 
              class="w-full pl-11 pr-4 py-3 bg-gray-50 dark:bg-gray-900 border-none rounded-xl text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-violet-500 focus:bg-white dark:focus:bg-black transition-all duration-200"
              @keyup.enter="fetchProducts"
            />
          </div>
        </div>

        <!-- 分类筛选下拉框：演示 Element Plus 与主题色兼容 -->
        <div class="w-full md:w-48">
          <el-select 
            v-model="selectedCategory" 
            :placeholder="$t('shop.category')" 
            @change="fetchProducts" 
            size="large" 
            class="w-full !rounded-xl"
            effect="dark"
          >
            <el-option :label="$t('shop.all')" value="" />
            <el-option :label="$t('shop.electronics')" value="1" />
            <el-option :label="$t('shop.clothing')" value="2" />
          </el-select>
        </div>

        <!-- 搜索按钮：直接使用动态主题色 -->
        <button 
          @click="fetchProducts"
          class="w-full md:w-auto px-8 py-3 text-white font-semibold rounded-xl transition-all shadow-md hover:shadow-lg active:scale-95 transform duration-150 !w-28"
          :style="{ backgroundColor: themeColor }"
        >
          {{ $t('shop.search') }}
        </button>
      </div>
    </div>

    <!-- 主内容区：列表标题 + 商品宫格 -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-12">
      
      <!-- 模块标题：左侧彩条与右侧链接均沿用主题色 -->
      <div class="flex items-center justify-between mb-8">
        <h2 class="text-2xl font-bold text-gray-900 dark:text-white flex items-center gap-2">
          <span class="w-1 h-8 rounded-full" :style="{ backgroundColor: themeColor }"></span>
          {{ $t('shop.hot') }}
        </h2>
        <span 
          class="text-sm text-gray-500 dark:text-gray-400 cursor-pointer transition-colors"
          :style="{ color: themeColor }"
        >
          {{ $t('shop.more') }} &rarr;
        </span>
      </div>

      <!-- 商品宫格：响应式列数 + 悬浮互动 -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8" v-loading="loading">
        <div 
          v-for="product in products" 
          :key="product.id" 
          class="group bg-white dark:bg-gray-800 rounded-2xl overflow-hidden shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-300 border border-gray-100 dark:border-gray-700 flex flex-col"
          @click="goToDetail(product.id)"
        >
          <!-- 商品图：进入 hover 时显示加入购物车按钮 -->
          <div class="relative h-64 overflow-hidden bg-gray-100 dark:bg-gray-700">
            <img 
              :src="product.mainImage" 
              :alt="product.name" 
              loading="lazy" 
              class="w-full h-full object-cover object-center transform group-hover:scale-110 transition-transform duration-500"
            />
            <!-- Overlay Action -->
            <div v-if="product.status === 1" class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center">
              <button 
                @click.stop="goToDetail(product.id)"
                class="bg-white text-gray-900 px-6 py-2 rounded-full font-medium shadow-lg transform translate-y-4 group-hover:translate-y-0 transition-all duration-300 hover:bg-gray-50"
              >
                {{ $t('shop.details') }}
              </button>
            </div>
            <!-- Out of Stock Overlay -->
            <div v-else class="absolute inset-0 bg-gray-900/50 flex items-center justify-center backdrop-blur-[2px]">
              <span class="text-white font-bold text-lg tracking-wider border-2 border-white px-4 py-1 rounded-md transform -rotate-12">
                {{ $t('shop.outOfStock') }}
              </span>
            </div>
          </div>

          <!-- 商品信息 -->
          <div class="p-5 flex-1 flex flex-col">
            <div class="mb-2">
              <span 
                class="text-xs font-semibold px-2 py-1 rounded-md bg-opacity-10"
                :style="{ color: themeColor, backgroundColor: themeColor + '20' }"
              >
                {{ $t('shop.new') }}
              </span>
            </div>
            <h3 class="text-lg font-bold text-gray-900 dark:text-white mb-1 line-clamp-1 transition-colors group-hover:text-opacity-80">
              {{ product.name }}
            </h3>
            <p class="text-sm text-gray-500 dark:text-gray-400 mb-4 line-clamp-2 flex-1">
              {{ product.description || 'Experience premium quality with our latest collection.' }}
            </p>
            <div class="flex items-center justify-between mt-auto pt-4 border-t border-gray-100 dark:border-gray-700">
              <p class="text-xl font-bold text-gray-900 dark:text-white">
                ¥{{ product.minPrice }} {{ $t('shop.startingFrom') }}
              </p>
              <div class="flex text-yellow-400 text-sm">
                ★★★★☆ <span class="text-gray-400 ml-1">(4.5)</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Search, Shop } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useThemeStore } from '../../stores/theme'
import { ElMessage } from 'element-plus'

const router = useRouter()
const themeStore = useThemeStore()
const products = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const selectedCategory = ref('')

// 动态主题主色：直接读取 Pinia 中的 primaryColor
const themeColor = computed(() => themeStore.primaryColor)

// 渐变辅助函数：在原色基础上做明暗偏移
const adjustColor = (color, amount) => {
    const clamp = (val) => Math.min(255, Math.max(0, val))
    const hex = color.replace('#', '')
    const num = parseInt(hex, 16)
    let r = (num >> 16) + amount
    let g = ((num >> 8) & 0x00FF) + amount
    let b = (num & 0x0000FF) + amount
    return '#' + (
        (clamp(r) << 16) | 
        (clamp(g) << 8) | 
        clamp(b)
    ).toString(16).padStart(6, '0')
}

// 根据当前主题色实时生成 3 段渐变色，驱动横幅背景
const bannerStyle = computed(() => {
  const color1 = themeColor.value
  const color2 = adjustColor(themeColor.value, -40) // Darker variant
  const color3 = adjustColor(themeColor.value, 40)  // Lighter variant
  return {
    background: `linear-gradient(135deg, ${color1} 0%, ${color2} 50%, ${color3} 100%)`
  }
})

// 异步获取商品列表：带 loading 状态与关键字/分类查询
const fetchProducts = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/shop/product/list', {
      params: {
        keyword: searchKeyword.value,
        categoryId: selectedCategory.value
      }
    })
    if (res.data.code === 200) {
      products.value = res.data.data.records
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 跳转详情页
const goToDetail = (id) => {
  router.push(`/shop/product/${id}`)
}

// 接入购物车逻辑
const addToCart = async (product) => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('Please login first')
    router.push('/shop/login')
    return
  }
  try {
    const res = await axios.post('/api/shop/cart', {
      productId: product.id,
      quantity: 1
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success('Added to cart')
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('Failed to add to cart')
  }
}

// 首次挂载时自动拉取列表
onMounted(() => {
  fetchProducts()
})
</script>

<style scoped>
/* Custom styles if needed, but mostly using Tailwind */
.el-select {
  --el-select-border-color-hover: v-bind(themeColor);
  --el-input-focus-border-color: v-bind(themeColor);
}
</style>
