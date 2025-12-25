<template>
  <div class="dashboard-container p-6">
    <!-- 统计卡片行 -->
    <el-row :gutter="20">
      <el-col :span="6" v-for="(stat, index) in stats" :key="index">
        <el-card class="stat-card !border-none !rounded-xl shadow-sm hover:shadow-md transition-shadow duration-300 dark:bg-gray-800" shadow="hover">
          <template #header>
            <div class="card-header flex justify-between items-center">
              <span class="text-gray-500 dark:text-gray-400 font-medium">{{ stat.title }}</span>
              <el-icon :class="stat.iconClass" :size="20"><component :is="stat.icon" /></el-icon>
            </div>
          </template>
          <div class="card-value text-3xl font-bold text-gray-900 dark:text-white my-2">{{ stat.value }}</div>
          <div class="card-trend text-sm" :class="stat.trend > 0 ? 'text-green-500' : 'text-red-500'">
            {{ stat.trend > 0 ? '+' : '' }}{{ stat.trend }}% {{ $t('admin.vsLastMonth') }}
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表和时间线行 -->
    <el-row :gutter="20" class="mt-6">
      <el-col :span="16">
        <el-card shadow="hover" class="!border-none !rounded-xl dark:bg-gray-800">
          <template #header>
            <div class="card-header">
              <span class="text-lg font-bold text-gray-900 dark:text-white">{{ $t('admin.salesOverview') }}</span>
            </div>
          </template>
          <!-- ECharts 图表容器 -->
          <div ref="chartRef" class="h-[350px] bg-white dark:bg-gray-800 rounded-lg"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="!border-none !rounded-xl dark:bg-gray-800">
          <template #header>
            <div class="card-header">
              <span class="text-lg font-bold text-gray-900 dark:text-white">{{ $t('admin.recentActivity') }}</span>
            </div>
          </template>
          <!-- 时间线组件 -->
          <el-timeline>
            <el-timeline-item v-for="(activity, index) in activities" :key="index" :timestamp="activity.time" placement="top" :type="activity.type">
              <span class="text-gray-700 dark:text-gray-300">{{ activity.content }}</span>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useI18n } from 'vue-i18n'
import { Money, Goods, User, Bell } from '@element-plus/icons-vue'
import request from '@/utils/request'
import * as echarts from 'echarts'
import { useThemeStore } from '@/stores/theme'

const { t } = useI18n()
const themeStore = useThemeStore()
const chartRef = ref(null)
let chartInstance = null

const statsData = ref({
  totalSales: 0,
  totalOrders: 0,
  totalUsers: 0,
  pendingOrders: 0,
  salesTrend: 0,
  ordersTrend: 0,
  usersTrend: 0,
  pendingOrdersTrend: 0,
  activities: [],
  salesChart: { dates: [], values: [] }
})

const activities = computed(() => statsData.value.activities || [])

const stats = computed(() => [
  { title: t('admin.totalSales'), value: '¥ ' + statsData.value.totalSales, trend: statsData.value.salesTrend, icon: Money, iconClass: 'text-blue-500' },
  { title: t('admin.orders'), value: statsData.value.totalOrders, trend: statsData.value.ordersTrend, icon: Goods, iconClass: 'text-purple-500' },
  { title: t('admin.newUsers'), value: statsData.value.totalUsers, trend: statsData.value.usersTrend, icon: User, iconClass: 'text-orange-500' },
  { title: t('admin.pendingOrders'), value: statsData.value.pendingOrders, trend: statsData.value.pendingOrdersTrend, icon: Bell, iconClass: 'text-red-500' }
])

const initChart = (data) => {
  if (!chartRef.value || !data) return
  
  if (chartInstance) {
    chartInstance.dispose()
  }
  
  const isDark = themeStore.backendDark
  chartInstance = echarts.init(chartRef.value, isDark ? 'dark' : undefined)
  
  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data.dates,
      axisLine: {
        lineStyle: {
          color: isDark ? '#eee' : '#333'
        }
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        lineStyle: {
          color: isDark ? '#eee' : '#333'
        }
      },
      splitLine: {
        lineStyle: {
          color: isDark ? '#444' : '#eee'
        }
      }
    },
    series: [
      {
        name: t('admin.totalSales'),
        type: 'line',
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        },
        data: data.values
      }
    ]
  }
  
  chartInstance.setOption(option)
}

watch(() => themeStore.backendDark, () => {
  if (statsData.value.salesChart) {
    initChart(statsData.value.salesChart)
  }
})

window.addEventListener('resize', () => {
  chartInstance && chartInstance.resize()
})

const fetchStats = async () => {
  try {
    const res = await request.get('/backend/dashboard/stats')
    if (res.data.code === 200) {
      statsData.value = res.data.data
      nextTick(() => {
        if (statsData.value.salesChart) {
          initChart(statsData.value.salesChart)
        }
      })
    }
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
/* Tailwind handles most styles */
</style>
