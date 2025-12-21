import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.username || ''
  },
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    clearToken() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    },
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
    async logout() {
      this.clearToken()
    }
  }
})
