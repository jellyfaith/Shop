<template>
  <div>
    <h1>{{ $t('productList.title') }}</h1>
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in products" :key="item.id">
        <el-card>
          <img :src="item.image" class="image" style="width: 100%"/>
          <div style="padding: 14px">
            <span>{{ item.name }}</span>
            <div class="bottom">
              <span class="price">{{ $t('productList.price', { price: item.price }) }}</span>
              <el-button type="text" class="button" @click="addToCart(item)">{{ $t('productList.addToCart') }}</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import axios from 'axios'

const { t } = useI18n()
const products = ref([])

onMounted(async () => {
  // const res = await axios.get('/api/product/list')
  // products.value = res.data.data
  // Mock data
  products.value = [
      { id: 1, name: t('productList.product1'), price: 100, image: 'https://via.placeholder.com/150' },
      { id: 2, name: t('productList.product2'), price: 200, image: 'https://via.placeholder.com/150' }
  ]
})

const addToCart = (item) => {
    console.log('Add to cart', item)
}
</script>
