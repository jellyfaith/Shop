<template>
  <div class="user-manage">
    <div class="mb-4">
      <el-input
        v-model="searchKeyword"
        :placeholder="$t('user.searchPlaceholder')"
        style="width: 200px;"
        clearable
        @clear="fetchUsers"
        @keyup.enter="fetchUsers"
      >
        <template #append>
          <el-button @click="fetchUsers">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
    </div>

    <el-table :data="userList" border stripe v-loading="loading">
      <el-table-column prop="id" :label="$t('user.id')" width="80" />
      <el-table-column prop="username" :label="$t('user.username')" />
      <el-table-column prop="email" :label="$t('user.email')" />
      <el-table-column prop="phone" :label="$t('user.phone')" />
      <el-table-column prop="createTime" :label="$t('user.registerTime')" />
      <el-table-column :label="$t('common.operations')" width="150">
        <template #default="scope">
          <el-button size="small" type="primary" @click="viewLogs(scope.row)">{{ $t('user.viewLogs') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt-4 flex justify-end">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        v-model:current-page="currentPage"
        @current-change="fetchUsers"
      />
    </div>

    <!-- 日志抽屉 -->
    <el-drawer v-model="drawerVisible" :title="$t('user.logs')" size="50%">
      <el-table :data="logList" border stripe v-loading="logLoading">
        <el-table-column prop="actionType" :label="$t('user.logType')" width="100">
            <template #default="scope">
                <el-tag :type="scope.row.actionType === 'BUY' ? 'success' : 'info'">{{ scope.row.actionType }}</el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="targetId" :label="$t('user.targetId')" width="100" />
        <el-table-column prop="details" :label="$t('user.details')" />
        <el-table-column prop="createTime" :label="$t('user.time')" width="180" />
      </el-table>
      <div class="mt-4 flex justify-end">
        <el-pagination
            background
            layout="prev, pager, next"
            :total="logTotal"
            :page-size="logPageSize"
            v-model:current-page="logCurrentPage"
            @current-change="fetchLogs"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { Search } from '@element-plus/icons-vue'

const userList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')

const drawerVisible = ref(false)
const logList = ref([])
const logLoading = ref(false)
const logTotal = ref(0)
const logCurrentPage = ref(1)
const logPageSize = ref(10)
const currentUserId = ref(null)

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await request.get('/backend/user/list', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        username: searchKeyword.value
      }
    })
    if (res.data.code === 200) {
      userList.value = res.data.data.records
      total.value = res.data.data.total
    }
  } finally {
    loading.value = false
  }
}

const viewLogs = (user) => {
  currentUserId.value = user.id
  logCurrentPage.value = 1
  drawerVisible.value = true
  fetchLogs()
}

const fetchLogs = async () => {
  if (!currentUserId.value) return
  logLoading.value = true
  try {
    const res = await request.get(`/backend/user/${currentUserId.value}/logs`, {
      params: {
        page: logCurrentPage.value,
        size: logPageSize.value
      }
    })
    if (res.data.code === 200) {
      logList.value = res.data.data.records
      logTotal.value = res.data.data.total
    }
  } finally {
    logLoading.value = false
  }
}

onMounted(() => {
  fetchUsers()
})
</script>
