<template>
  <NavBar/>
  <router-view></router-view>
</template>

<script>
  import NavBar from '@/components/NavBar.vue'
  import "bootstrap/dist/css/bootstrap.min.css"
  import "bootstrap/dist/js/bootstrap"

  export default {
      components: {
          NavBar
      },
      setup() {
      }
  }
</script>
<style>
</style>

