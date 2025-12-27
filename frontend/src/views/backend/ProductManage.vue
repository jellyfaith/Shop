<template>
  <div class="product-manage p-6 bg-white dark:bg-gray-800 rounded-lg shadow-sm transition-colors duration-300">
    <el-tabs v-model="activeTab" class="demo-tabs">
      <el-tab-pane :label="$t('admin.productManage')" name="product">
        <div class="header flex flex-col md:flex-row justify-between items-center mb-6 gap-4">
          <h2 class="text-xl font-bold text-gray-900 dark:text-white hidden md:block">{{ $t('admin.productManage') }}</h2>
          <div class="flex gap-2 w-full md:w-auto justify-end">
            <el-button type="primary" @click="handleAdd">{{ $t('admin.addProduct') }}</el-button>
          </div>
        </div>

        <!-- Filters -->
        <div class="filters mb-6 flex flex-wrap gap-4">
          <el-input
            v-model="searchName"
            :placeholder="$t('admin.productName')"
            class="w-48"
            clearable
            @clear="fetchData"
            @keyup.enter="fetchData"
          />
          <el-select v-model="searchCategory" :placeholder="$t('shop.category')" clearable @change="fetchData" class="w-48">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
          <el-select v-model="sortField" :placeholder="$t('admin.sortBy')" @change="fetchData" class="w-40">
            <el-option :label="$t('admin.newest')" value="createTime" />
            <el-option :label="$t('admin.price')" value="price" />
          </el-select>
          <el-select v-model="sortOrder" :placeholder="$t('admin.sortOrder')" @change="fetchData" class="w-32">
            <el-option :label="$t('admin.desc')" value="desc" />
            <el-option :label="$t('admin.asc')" value="asc" />
          </el-select>
          <el-button type="primary" @click="fetchData">{{ $t('shop.search') }}</el-button>
        </div>
        
        <el-table :data="tableData" style="width: 100%" v-loading="loading" class="dark:bg-gray-800">
          <el-table-column prop="id" :label="$t('admin.id')" width="80" />
          <el-table-column prop="name" :label="$t('admin.productName')" />
          <el-table-column prop="minPrice" :label="$t('admin.price')" width="120">
            <template #default="scope">
              ¥{{ scope.row.minPrice }}
            </template>
          </el-table-column>
          <el-table-column prop="categoryId" :label="$t('shop.category')" width="120">
            <template #default="scope">
              {{ getCategoryName(scope.row.categoryId) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" :label="$t('admin.status')" width="100">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column :label="$t('admin.operations')" width="250">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="handleEdit(scope.row)">{{ $t('admin.edit') }}</el-button>
              <el-button link type="success" size="small" @click="handleStock(scope.row)">{{ $t('admin.stock') }}</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(scope.row)">{{ $t('admin.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination mt-4 flex justify-end">
          <el-pagination 
            background 
            layout="prev, pager, next" 
            :total="total" 
            @current-change="handlePageChange"
          />
        </div>
      </el-tab-pane>

      <el-tab-pane :label="$t('admin.categoryManage')" name="category">
        <CategoryManage />
      </el-tab-pane>
    </el-tabs>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
      <el-form :model="form" label-width="100px">
        <el-form-item :label="$t('admin.productName')">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item :label="$t('shop.category')">
          <el-select v-model="form.categoryId" placeholder="Select">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shop.description')">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item :label="$t('admin.price')">
          <el-input-number v-model="form.minPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item :label="$t('admin.image')">
          <el-input v-model="form.mainImage" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Stock Dialog -->
    <el-dialog v-model="stockDialogVisible" :title="$t('admin.manageStock')" width="60%">
      <div class="mb-4">
        <el-button type="primary" @click="openAddSkuDialog">{{ $t('common.add') }} SKU</el-button>
      </div>
      <el-table :data="skuList" style="width: 100%">
        <el-table-column prop="id" :label="$t('admin.id')" width="60" />
        <el-table-column prop="specs" :label="$t('admin.specs')">
            <template #default="scope">
                {{ scope.row.specs }}
            </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('admin.price')">
             <template #default="scope">
                <el-input-number v-model="scope.row.price" size="small" :min="0" :precision="2" />
            </template>
        </el-table-column>
        <el-table-column prop="stock" :label="$t('admin.stock')">
            <template #default="scope">
                <el-input-number v-model="scope.row.stock" size="small" :min="0" />
            </template>
        </el-table-column>
        <el-table-column :label="$t('admin.operations')">
            <template #default="scope">
                <el-button type="primary" size="small" @click="saveSku(scope.row)">{{ $t('common.save') }}</el-button>
                <el-button type="danger" size="small" @click="deleteSku(scope.row)">{{ $t('admin.delete') }}</el-button>
            </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- Add SKU Dialog -->
    <el-dialog v-model="addSkuDialogVisible" :title="$t('common.add') + ' SKU'" width="40%">
      <el-form :model="newSkuForm" label-width="80px">
        <el-form-item :label="$t('admin.price')">
          <el-input-number v-model="newSkuForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item :label="$t('admin.stock')">
          <el-input-number v-model="newSkuForm.stock" :min="0" />
        </el-form-item>
        <el-form-item :label="$t('admin.specs')">
          <div v-for="(item, index) in newSkuSpecs" :key="index" class="flex gap-2 mb-2">
            <el-input v-model="item.key" placeholder="Key (e.g. Color)" />
            <el-input v-model="item.value" placeholder="Value (e.g. Red)" />
            <el-button type="danger" size="small" @click="removeSpecRow(index)">Del</el-button>
          </div>
          <el-button type="default" class="w-full" @click="addSpecRow">+ Add Spec</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addSkuDialogVisible = false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="submitNewSku">{{ $t('common.save') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
import CategoryManage from './CategoryManage.vue'

const { t } = useI18n()
const activeTab = ref('product')
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const categories = ref([])

// Filters
const searchName = ref('')
const searchCategory = ref('')
const sortField = ref('createTime')
const sortOrder = ref('desc')

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  name: '',
  categoryId: null,
  description: '',
  minPrice: 0,
  mainImage: '',
  status: 1
})

const stockDialogVisible = ref(false)
const skuList = ref([])
const currentProductId = ref(null)

const dialogTitle = computed(() => isEdit.value ? t('admin.edit') : t('admin.addProduct'))

const fetchCategories = async () => {
  try {
    const res = await request.get('/backend/category/list')
    if (res.data.code === 200) {
      categories.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const getCategoryName = (id) => {
  const cat = categories.value.find(c => c.id === id)
  return cat ? cat.name : id
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/backend/product/list', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        name: searchName.value,
        categoryId: searchCategory.value,
        sortField: sortField.value,
        sortOrder: sortOrder.value
      }
    })
    if (res.data.code === 200) {
      tableData.value = res.data.data.records
      total.value = res.data.data.total
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchData()
}

// Removed handleCategoryManage as it is now integrated

const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, name: '', categoryId: null, description: '', minPrice: 0, mainImage: '', status: 1 }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(t('common.delete') + '?', t('common.warning'), {
    confirmButtonText: t('common.submit'),
    cancelButtonText: t('common.cancel'),
    type: 'warning',
  }).then(async () => {
    try {
      await request.delete(`/backend/product/${row.id}`)
      ElMessage.success(t('admin.deleted'))
      fetchData()
    } catch (e) {
      console.error(e)
    }
  })
}

const handleStatusChange = async (row) => {
  try {
    await request.put(`/backend/product/status/${row.id}/${row.status}`)
    ElMessage.success(t('admin.statusUpdated'))
  } catch (e) {
    console.error(e)
    row.status = row.status === 1 ? 0 : 1 // revert
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await request.put('/backend/product/update', form.value)
    } else {
      await request.post('/backend/product/add', form.value)
    }
    ElMessage.success(t('admin.saved'))
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    console.error(e)
  }
}

const handleStock = async (row) => {
  currentProductId.value = row.id
  try {
    const res = await request.get(`/backend/product/skus/${row.id}`)
    if (res.data.code === 200) {
      skuList.value = res.data.data
      stockDialogVisible.value = true
    }
  } catch (e) {
    console.error(e)
    // Fallback if API fails or not implemented
    skuList.value = []
    stockDialogVisible.value = true
  }
}

const saveSku = async (sku) => {
    try {
        await request.put('/backend/product/sku/update', sku)
        ElMessage.success(t('admin.skuUpdated'))
    } catch (e) {
        console.error(e)
    }
}

// New SKU Logic
const addSkuDialogVisible = ref(false)
const newSkuSpecs = ref([{ key: '', value: '' }])
const newSkuForm = ref({
  price: 0,
  stock: 0,
  image: ''
})

const openAddSkuDialog = () => {
  newSkuSpecs.value = [{ key: '', value: '' }]
  newSkuForm.value = { price: 0, stock: 0, image: '' }
  addSkuDialogVisible.value = true
}

const addSpecRow = () => {
  newSkuSpecs.value.push({ key: '', value: '' })
}

const removeSpecRow = (index) => {
  newSkuSpecs.value.splice(index, 1)
}

const submitNewSku = async () => {
  const specsMap = {}
  newSkuSpecs.value.forEach(item => {
    if (item.key && item.value) {
      specsMap[item.key] = item.value
    }
  })

  const payload = {
    productId: currentProductId.value,
    price: newSkuForm.value.price,
    stock: newSkuForm.value.stock,
    image: newSkuForm.value.image,
    specs: specsMap
  }

  try {
    await request.post('/backend/product/sku/add', payload)
    ElMessage.success(t('common.addSuccess')) // Ensure translation exists or use hardcoded for now if key missing
    addSkuDialogVisible.value = false
    // Refresh SKU list
    const res = await request.get(`/backend/product/skus/${currentProductId.value}`)
    if (res.data.code === 200) {
      skuList.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const deleteSku = (sku) => {
    ElMessageBox.confirm(t('common.delete') + '?', t('common.warning'), {
    confirmButtonText: t('common.submit'),
    cancelButtonText: t('common.cancel'),
    type: 'warning',
  }).then(async () => {
    try {
      await request.delete(`/backend/product/sku/${sku.id}`)
      ElMessage.success(t('admin.deleted'))
      // Refresh SKU list
      const res = await request.get(`/backend/product/skus/${currentProductId.value}`)
      if (res.data.code === 200) {
        skuList.value = res.data.data
      }
    } catch (e) {
      console.error(e)
    }
  })
}

onMounted(() => {
  fetchCategories()
  fetchData()
})
</script>
