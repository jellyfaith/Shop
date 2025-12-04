import { defineStore } from 'pinia'
import { ref } from 'vue'
import i18n from '../locales'

export const useLocaleStore = defineStore('locale', () => {
  const currentLocale = ref(i18n.global.locale.value)
  const currency = ref(localStorage.getItem('currency') || 'USD')

  const setLocale = (lang) => {
    currentLocale.value = lang
    i18n.global.locale.value = lang
    localStorage.setItem('language', lang) // Changed key to match i18n config
  }

  const setCurrency = (curr) => {
    currency.value = curr
    localStorage.setItem('currency', curr)
  }

  return {
    currentLocale,
    currency,
    setLocale,
    setCurrency
  }
})
