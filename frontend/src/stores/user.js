import { defineStore } from 'pinia'
import axios from 'axios'

// 用户状态仓库
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '', // 用户 Token
    adminToken: localStorage.getItem('adminToken') || '', // 后台 Token
    userInfo: null, // 用户信息
    adminInfo: null // 后台用户信息
  }),
  getters: {
    // 判断是否已登录
    isLoggedIn: (state) => !!state.token,
    isAdminLoggedIn: (state) => !!state.adminToken,
    // 获取用户名
    username: (state) => state.userInfo?.username || ''
  },
  actions: {
    // 设置 Token
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    setAdminToken(token) {
      this.adminToken = token
      localStorage.setItem('adminToken', token)
    },
    // 清除 Token 和用户信息（退出登录）
    clearToken() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    },
    clearAdminToken() {
      this.adminToken = ''
      this.adminInfo = null
      localStorage.removeItem('adminToken')
    },
    // 获取用户信息
    async fetchUserInfo() {
      if (!this.token) return

      // 解析 Token 获取角色信息
      try {
        const base64Url = this.token.split('.')[1]
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        }).join(''))
        const payload = JSON.parse(jsonPayload)
        
        // 如果是管理员，不调用前台用户信息接口，直接设置用户信息
        if (payload.role === 'ADMIN') {
           this.userInfo = { username: payload.sub, role: 'ADMIN' }
           return
        }
      } catch (e) {
        console.error('Error parsing token:', e)
      }

      try {
        const res = await axios.get('/api/shop/user/info', {
          headers: { Authorization: `Bearer ${this.token}` }
        })
        if (res.data.code === 200) {
          this.userInfo = res.data.data
        } else {
          this.clearToken()
        }
      } catch (e) {
        console.error(e)
        this.clearToken()
      }
    },
    // 退出登录
    async logout() {
      this.clearToken()
    },
    async adminLogout() {
      this.clearAdminToken()
    }
  }
})
