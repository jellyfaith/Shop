<template>
  <div class="max-w-[1000px] mx-auto my-10 p-5 bg-white dark:bg-gray-800 rounded-2xl min-h-[400px] transition-colors duration-300">
    <h1 class="text-2xl font-bold mb-6 text-gray-900 dark:text-white">{{ $t('shop.cart') }}</h1>
    
    <div v-if="cartItems && cartItems.length > 0">
      <!-- 购物车商品列表 -->
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
      
      <!-- 底部结算栏 -->
      <div class="mt-8 flex justify-end items-center gap-6">
        <div class="text-xl text-gray-900 dark:text-white">
          {{ $t('shop.total') }} <span class="font-bold text-red-500 text-2xl">¥{{ totalPrice }}</span>
        </div>
        <el-button type="primary" size="large" @click="checkout">{{ $t('shop.checkout') }}</el-button>
      </div>
    </div>

    <el-empty v-else :description="$t('shop.cartEmpty')" />

    <!-- 结算弹窗 -->
    <el-dialog v-model="checkoutVisible" :title="$t('shop.checkout')" width="600px">
      <el-form :model="checkoutForm" label-width="100px">
        <el-form-item :label="$t('shop.receiverInfo')">
          <div v-if="addressList.length > 0" class="w-full">
            <el-radio-group v-model="selectedAddressId" class="flex flex-col items-start w-full" @change="handleAddressSelect">
              <el-radio v-for="addr in addressList" :key="addr.id" :label="addr.id" class="!ml-0 w-full border p-3 rounded !h-auto !items-start mb-2 address-radio">
                <div class="flex flex-col text-left whitespace-normal leading-normal ml-2">
                  <span class="font-bold text-base">{{ addr.name }} <span class="ml-2 text-gray-500 text-sm">{{ addr.phone }}</span></span>
                  <span class="text-gray-500 text-sm mt-1 break-all">{{ addr.address }}</span>
                </div>
              </el-radio>
              <el-radio :label="-1" class="!ml-0 w-full border p-3 rounded !h-auto !items-start mb-2 address-radio">
                <div class="flex flex-col text-left whitespace-normal leading-normal ml-2">
                  <span class="font-bold text-base">
                    {{ $t('common.add') }} / {{ $t('shop.address') }}
                  </span>
                </div>
              </el-radio>
            </el-radio-group>
          </div>
          <div v-else class="text-gray-500 mb-2">{{ $t('settingsModal.noAddress') }}</div>
        </el-form-item>

        <!-- 新增地址表单 -->
        <div v-if="selectedAddressId === -1 || addressList.length === 0" class="border-t pt-4 mt-2">
          <el-form-item :label="$t('shop.name')">
            <el-input v-model="checkoutForm.receiverName" />
          </el-form-item>
          <el-form-item :label="$t('shop.phone')">
            <el-input v-model="checkoutForm.receiverPhone" />
          </el-form-item>
          <el-form-item :label="$t('shop.address')">
            <el-input v-model="checkoutForm.receiverAddress" type="textarea" />
          </el-form-item>
        </div>

        <el-form-item :label="$t('shop.paymentMethod')">
          <el-radio-group v-model="checkoutForm.paymentMethod">
            <el-radio label="WECHAT">{{ $t('shop.wechatPay') }}</el-radio>
            <el-radio label="ALIPAY">{{ $t('shop.alipay') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="checkoutVisible = false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="confirmCheckout" :loading="checkoutLoading">{{ $t('common.submit') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const router = useRouter()
const cartItems = ref([])
const totalPrice = ref(0)
const checkoutVisible = ref(false)
const checkoutLoading = ref(false)
const addressList = ref([])
const selectedAddressId = ref(-1)
const checkoutForm = ref({
  receiverId: null,
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  paymentMethod: 'WECHAT'
})

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
    ElMessage.success(t('shop.removed'))
    fetchCart()
  } catch (e) {
    console.error(e)
  }
}

const checkout = async () => {
  const token = localStorage.getItem('token')
  try {
    // Fetch addresses
    const res = await axios.get('/api/shop/receiver/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      addressList.value = res.data.data
      // Select default if exists
      const defaultAddr = addressList.value.find(a => a.isDefault)
      if (defaultAddr) {
        selectedAddressId.value = defaultAddr.id
        handleAddressSelect(defaultAddr.id)
      } else if (addressList.value.length > 0) {
        selectedAddressId.value = addressList.value[0].id
        handleAddressSelect(addressList.value[0].id)
      } else {
        selectedAddressId.value = -1
        handleAddressSelect(-1)
      }
    }
  } catch (e) {
    console.error(e)
    selectedAddressId.value = -1
  }
  checkoutVisible.value = true
}

const handleAddressSelect = (val) => {
  if (val === -1) {
    checkoutForm.value.receiverId = null
    checkoutForm.value.receiverName = ''
    checkoutForm.value.receiverPhone = ''
    checkoutForm.value.receiverAddress = ''
  } else {
    const addr = addressList.value.find(a => a.id === val)
    if (addr) {
      checkoutForm.value.receiverId = addr.id
      checkoutForm.value.receiverName = addr.name
      checkoutForm.value.receiverPhone = addr.phone
      checkoutForm.value.receiverAddress = addr.address
    }
  }
}

const confirmCheckout = async () => {
  if (!checkoutForm.value.receiverName || !checkoutForm.value.receiverPhone || !checkoutForm.value.receiverAddress) {
    ElMessage.warning(t('common.fillAllFields'))
    return
  }
  
  checkoutLoading.value = true
  const token = localStorage.getItem('token')
  try {
    const res = await axios.post('/api/shop/order/create', checkoutForm.value, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 200) {
      ElMessage.success(t('shop.orderCreated'))
      checkoutVisible.value = false
      fetchCart()
      router.push('/shop/order/' + res.data.data)
    } else {
      ElMessage.error(res.data.msg || t('shop.orderFailed'))
    }
  } catch (e) {
    console.error(e)
    ElMessage.error(t('shop.orderFailed'))
  } finally {
    checkoutLoading.value = false
  }
}

onMounted(() => {
  fetchCart()
})
</script>

<style scoped>
:deep(.address-radio) {
  display: flex !important;
  align-items: flex-start !important;
  height: auto !important;
}

:deep(.address-radio .el-radio__input) {
  margin-top: 3px !important; /* Fine-tuned for visual alignment */
}

:deep(.address-radio .el-radio__label) {
  width: 100%;
  white-space: normal;
  line-height: 1.5;
}
</style>
