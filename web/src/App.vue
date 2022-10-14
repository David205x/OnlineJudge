<template>
  <NavBar/>
  <router-view></router-view>
  <ChattingRoom></ChattingRoom>
</template>

<script>

import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import 'highlight.js/styles/stackoverflow-light.css';// 可以切换其它样式风格，例如黑色主题
import 'highlight.js/lib/common';
import ChattingRoom from "@/components/chatting/ChattingRoom.vue"
import { useStore } from 'vuex'
export default {
    components: {
        NavBar,
        ChattingRoom,
    },
    setup() {
      const store = useStore();
        const jwt_token = localStorage.getItem("jwt_token");
        if(jwt_token){
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success(){
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        }else {
            store.commit("updatePullingInfo", false);
        }
    },
    
}

</script>
<style>
</style>

