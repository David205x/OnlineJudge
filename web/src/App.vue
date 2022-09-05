<template>
  <NavBar/>
  <router-view></router-view>
</template>

<script>

import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import 'highlight.js/styles/stackoverflow-light.css';// 可以切换其它样式风格，例如黑色主题
import 'highlight.js/lib/common';
export default {
    components: {
        NavBar,
        
    },
    setup() {
      
    },
    
}

</script>
<style>
</style>

