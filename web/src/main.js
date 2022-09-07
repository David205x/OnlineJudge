import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
createApp(App).use(store).use(router).mount('#app')

// // 创建对象
// const app = createApp(App)
// // 必须使用 nextTick，不然会有加载顺序问题，导致绑定失败
// nextTick(() => {
//   // 代替 Vue.prototype.$xxx 绑定
//   app.config.globalProperties.$mavonEditor = mavonEditor
//   console.log(app.config.globalProperties);
// })

// // 使用并挂载
// app.use(store).use(router).mount('#app')
