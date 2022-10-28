<template>
  <ContentField>
    <span>
      这里是题解界面
    </span>
    <div class="row">
    <div class="col">
    <div class="card" style="width: 18rem;">
      <img src="@/assets/1.jpeg" class="card-img-top" alt="...">
      <div class="card-body">
        <h5 class="card-title">Card title</h5>
        <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
        <a href="#" class="btn btn-primary">Go somewhere</a>
      </div>
    </div>
    </div>
    <div class="col">
      <div class="card" style="width: 18rem;">
        <img src="@/assets/1.jpeg" class="card-img-top" alt="...">
        <div class="card-body">
          <h5 class="card-title">Card title</h5>
          <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
          <a href="#" class="btn btn-primary">Go somewhere</a>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card" style="width: 18rem;">
        <img src="@/assets/1.jpeg" class="card-img-top" alt="...">
        <div class="card-body">
          <h5 class="card-title">Card title</h5>
          <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
          <a href="#" class="btn btn-primary">Go somewhere</a>
        </div>
      </div>
    </div>
    </div>
  </ContentField>



</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
export default{
  components: { ContentField },
  setup(){

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
    const logged = store.state.user.is_login;
    store.commit("updatePullingInfo", false);
    if(store.state.user.is_login){
      store.dispatch("getinfoInMainPage", {
        success(){
        }
      })
    }
    return {
      logged,
    }
  }
}

</script>

<style scoped>

</style>