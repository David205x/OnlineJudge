import { createRouter, createWebHistory } from 'vue-router'
import UserLoginView from '@/views/user/account/UserLoginView'
import UserRegisterView from '@/views/user/account/UserRegisterView'
import HomePageView from '@/views/index/HomePageView'
import PageIndexView from '@/views/index/PageIndexView'
import store from '@/store/index'
import ProfileOverview from "@/views/user/profile/ProfileOverview";
import ProblemResultView from "@/components/problem/ProblemResultView";
import ProblemResultView2 from "@/components/problem/ProblemResultView2";
import ChattingView from "@/views/chatting/ChattingView";
import ProblemSolutionView from "@/components/problem/ProblemSolutionView";
import NotFoundView from "@/views/NotFoundView";
const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/index/",
    meta: {
        requestAuth: false
    }
  },
  {
    path: "/index/",
    name: "main_page",
    component: HomePageView,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/home/",
    name: "home_page",
    component: PageIndexView,
    meta: {
      requestAuth: false
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
  {
    path: "/problem/details/problemId=:id?/solutionKey=:solutionKey?/",
    name: "problem_details",
    component: ProblemDetailView,
    meta: {
        requestAuth: false
    }
  },
  {
    path: "/problem/editorial/editor/",
    name: "problem_editorial_editor",
    component: ProblemEditorialEditor,
    meta: {
        requestAuth: false
    }
  },
  {
    path: "/problem/page2",
    name: "problem_twoView",
    component: Page_twoView,
    meta: {
        requestAuth: false
    }
  },
  {
    path: "/profile/overview/",
    name: "profile_overview",
    component: ProfileOverview,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/problem/problem_result/",
    name: "problem_result",
    component: ProblemResultView,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/problem/problem_result_2/",
    name: "problem_result_2",
    component: ProblemResultView2,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/chatting/chattingroom/",
    name: "chatting_list",
    component: ChattingView,
    meta:{
      requestAuth: false
    }
  },
  {
    path: "/problem/problem_solution/problemKey=:problemKey?/solutionKey=:solutionKey?/",
    name: "problem_solution",
    component: ProblemSolutionView,
    meta:{
      requestAuth: false
    }
  },
  {
    path: "/NotFoundView",
    name: "404_page",
    component: NotFoundView,
    meta: {
      requestAuth: false
    }
  },
  {
    path: "/:pathMatch(.*)",
    redirect: "/NotFoundView"
  }
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
