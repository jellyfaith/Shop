<template>
  <div class="max-w-[1000px] mx-auto my-10 p-5 bg-white dark:bg-gray-800 rounded-2xl min-h-[400px] transition-colors duration-300">
    <h1 class="text-2xl font-bold mb-6 text-gray-900 dark:text-white">{{ $t('shop.cart') }}</h1>
    <el-empty :description="$t('shop.cartEmpty')" />
  </div>
</template>

<script setup>
</script>
