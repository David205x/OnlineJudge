import { createRouter, createWebHistory } from 'vue-router'
import UserLoginView from '@/views/user/account/UserLoginView'
import UserRegisterView from '@/views/user/account/UserRegisterView'
import store from '@/store/index'
const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/user/account/login/",
    meta: {
        requestAuth: true
    }
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserLoginView,
    meta: {
        requestAuth: false
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserRegisterView,
    meta: {
        requestAuth: false
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
      next({name: "user_account_login"});
  } else {
      next();
  }
})

export default router
