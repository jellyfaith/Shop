import { defineStore } from 'pinia'
import { ref } from 'vue'
import i18n from '../locales'

// 国际化状态仓库
export const useLocaleStore = defineStore('locale', () => {
  // 当前语言
  const currentLocale = ref(i18n.global.locale.value)
  // 当前货币单位
  const currency = ref('CNY')

  // 设置语言
  const setLocale = (lang) => {
    currentLocale.value = lang
    i18n.global.locale.value = lang
    localStorage.setItem('language', lang) // 保存到本地存储
  }

  return {
    currentLocale,
    currency,
    setLocale
  }
})
