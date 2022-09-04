<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <router-link  class="navbar-brand" :to="{name: 'home'}">BJUT OnlineJudge</router-link>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <router-link :class="route_name == 'home' ? 'nav-link active' : 'nav-link'"
                                     :to="{name: 'home'}"> 主界面
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">界面2</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">界面3</a>
                    </li>
                    
                </ul>
                <ul class="navbar-nav" v-if="$store.state.user.is_login">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            {{ $store.state.user.username }}
                        </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="#">功能1</a></li>
                        <li><a class="dropdown-item" href="#">功能2</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
                    </ul>
                    </li>
                </ul>
                <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                    <li class="nav-item">
                        <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button">
                            登录 
                        </router-link>  
                    </li>
                    <li class="nav-item">
                        <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
                            注册
                        </router-link>  
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex';
import router from '@/router';
export default {
    setup() {
        const route = useRoute();
        const store = useStore();
        
        let route_name = computed(() => route.name)
        const logout = () => {
            store.dispatch("logout");
            store.commit("updatePullingInfo", false);
            router.push({name: "user_account_login"});
        }
        return {
            route_name,
            logout,
        }
    }
}
</script>

<style scoped>

</style>