import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service = axios.create({
  baseURL: '/api', // 使用代理，指向后端接口
  timeout: 5000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 判断是否是后台接口
    if (config.url.includes('/backend/')) {
      const adminToken = localStorage.getItem('adminToken')
      if (adminToken) {
        config.headers['Authorization'] = 'Bearer ' + adminToken
      }
    } else {
      // 前台接口
      const token = localStorage.getItem('token')
      if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
      }
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    // 这里可以根据后端返回的状态码进行统一处理
    // 例如：if (res.code !== 200) { ... }
    return response
  },
  error => {
    console.log('err' + error)
    // 处理 401 未授权错误
    if (error.response && error.response.status === 401) {
        ElMessage.error('未授权，请重新登录')
        // 可选：重定向到登录页
    } else {
        // 显示错误信息
        ElMessage.error(error.message || '请求失败')
    }
    return Promise.reject(error)
  }
)

export default service
