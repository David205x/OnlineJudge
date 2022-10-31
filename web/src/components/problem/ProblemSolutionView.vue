<template>
  <ContentField>
    <span class="fs-4">
      题解贡献者：{{$store.state.solution.userName}}
    </span>
    <br>
    <span class="fs-4">
      贡献时间：{{$store.state.solution.date}}
    </span>
    <div style="height: auto;">
      <md-editor
          v-model = $store.state.solution.content
          previewOnly = true>
      </md-editor>
    </div>
  </ContentField>

</template>


<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import MdEditor from 'md-editor-v3'

export default{
  components: {
    ContentField ,
    MdEditor,
  },
  setup(){
    const store = useStore();
    const logged = store.state.user.is_login;
    store.commit("updatePullingInfo", false);

    const jwt_token = localStorage.getItem("jwt_token");
    if(jwt_token){
      store.commit("updateToken", jwt_token);
      store.dispatch("getInfo", {
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

    return {
      logged,
    }

  }
}

</script>

<style scoped>

</style>