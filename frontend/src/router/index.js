import { createRouter, createWebHistory } from 'vue-router'

// 定义路由规则数组
const routes = [
  // 根路径重定向：访问 / 时自动跳转到商城首页
  {
    path: '/',
    redirect: '/shop/home'
  },
  
  // ============================================================
  // 商城前台路由配置
  // ============================================================
  {
    path: '/shop',
    // 使用 ShopLayout 作为父级布局组件，包含顶部导航栏等公共部分
    // 路由懒加载：访问时才加载组件，优化首屏加载速度
    component: () => import('../layout/ShopLayout.vue'),
    children: [
      {
        // 商城首页：/shop/home
        path: 'home',
        component: () => import('../views/shop/Home.vue')
      },
      {
        // 买家登录页：/shop/login
        path: 'login',
        component: () => import('../views/shop/Login.vue')
      },
      {
        // 商品详情页：/shop/product/:id
        // :id 是动态参数，可以通过 route.params.id 获取
        path: 'product/:id',
        component: () => import('../views/shop/ProductDetail.vue')
      },
      {
        // 购物车页面：/shop/cart
        path: 'cart',
        component: () => import('../views/shop/Cart.vue'),
        meta: { requiresAuth: true }
      },
      {
        // 订单列表页：/shop/orders
        path: 'orders',
        component: () => import('../views/shop/OrderList.vue'),
        meta: { requiresAuth: true }
      },
      {
        // 订单详情页：/shop/order/:orderNo
        path: 'order/:orderNo',
        component: () => import('../views/shop/OrderDetail.vue'),
        meta: { requiresAuth: true }
      },
      {
        // 用户信息页：/shop/user/profile
        path: 'user/profile',
        component: () => import('../views/shop/UserProfile.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },

  // ============================================================
  // 后台管理路由配置
  // ============================================================
  {
    path: '/backend',
    // 使用 BackendLayout 作为父级布局组件，包含侧边栏菜单等
    component: () => import('../layout/BackendLayout.vue'),
    children: [
      {
        // 管理员登录页：/backend/login
        path: 'login',
        component: () => import('../views/backend/Login.vue')
      },
      {
        // 数据看板：/backend/dashboard
        path: 'dashboard',
        component: () => import('../views/backend/Dashboard.vue'),
        // meta 元数据：用于权限控制
        // requiresAuth: true 表示需要登录才能访问
        // role: 'ADMIN' 表示需要管理员角色
        meta: { requiresAuth: true, role: 'ADMIN' }
      },
      {
        // 商品管理：/backend/product
        path: 'product',
        component: () => import('../views/backend/ProductManage.vue'),
        meta: { requiresAuth: true, role: 'ADMIN' }
      }
    ]
  }
]

// 创建路由实例
const router = createRouter({
  // 使用 HTML5 History 模式 (不带 # 号的 URL)
  // 如果部署在非根目录，可以在 createWebHistory() 中传入基础路径
  history: createWebHistory(),
  // 传入上面定义的路由规则
  routes
})

// 全局前置守卫：在路由跳转前执行
router.beforeEach((to, from, next) => {
  // 从本地存储获取 token (JWT)
  const token = localStorage.getItem('token')
  
  // 检查目标路由是否需要认证 (meta.requiresAuth)
  // 并且当前用户没有 token (未登录)
  if (to.meta.requiresAuth && !token) {
    // 如果是访问后台管理页面，重定向到后台登录页
    if (to.path.startsWith('/backend')) {
      next('/backend/login')
    } else {
      // 否则重定向到商城登录页，并携带 redirect 参数以便登录后跳转回来
      next({
        path: '/shop/login',
        query: { redirect: to.fullPath }
      })
    }
  } else {
    // 不需要认证，或者已登录，直接放行
    next()
  }
})

// 导出路由实例，供 main.js 挂载
export default router
