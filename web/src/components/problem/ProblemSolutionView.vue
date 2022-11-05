<template>
  <ContentField>
    <span class="fs-3" style="font-weight: bold; color: deepskyblue">
      题解贡献者：{{$store.state.solution.userName}}
    </span>
    <br>
    <span class="fs-3" style="font-weight: bold; color: deepskyblue">
      贡献时间：{{$store.state.solution.date}}
    </span>
    <br>
    <br>
    <div class="fs-4" style="font-weight: bold; color: deepskyblue">
      题目详情如下：
    </div>

    <div>
      <md-editor
          v-model = $store.state.problem.problemDescription
          previewOnly = true>
      </md-editor>
    </div>
    <br>
    <span class="fs-4" style="font-weight: bold; color: deepskyblue">
      题解如下：
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
    let beforeProblemKey = window.location.href.match("problemKey=.*/");
    let problemKey = beforeProblemKey[0].split("=")[1].split("/")[0]
    let beforeSolutionKey = window.location.href.match("solutionKey=.*/");
    let solutionKey = beforeSolutionKey[0].split("=")[1].split("/")[0]
    store.dispatch("showProblemSolutionDetails", {
      problemKey : problemKey,
      solutionKey : solutionKey,
      success(){
        store.dispatch("showProblem", {
          problemKey : problemKey,
          success(resp){
            console.log(resp)
          }
        })

      }
    })
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