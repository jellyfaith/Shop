import { defineStore } from 'pinia'
import axios from 'axios'

// 用户状态仓库
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '', // 用户 Token
    userInfo: null // 用户信息
  }),
  getters: {
    // 判断是否已登录
    isLoggedIn: (state) => !!state.token,
    // 获取用户名
    username: (state) => state.userInfo?.username || ''
  },
  actions: {
    // 设置 Token
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    // 清除 Token 和用户信息（退出登录）
    clearToken() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    },
    // 获取用户信息
    async fetchUserInfo() {
      if (!this.token) return
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
    }
  }
})
