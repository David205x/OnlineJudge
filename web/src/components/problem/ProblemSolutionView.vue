<template>
    <ContentField>
        <div class="row align-items-start">
<!--        题目详情-->
            <div class="col-6">
                <p class="font-monospace" style="font-size: 45px">{{$store.state.problem.problemKey}}.{{$store.state.problem.problemName}}</p>
                <md-editor style="margin-top: -1vh"
                    v-model = $store.state.problem.problemDescription
                    previewOnly = true>
                </md-editor>
            </div>
<!--        题解详情-->
            <div class="col-6" style="margin: 3vh -1vw 0 1vw">
                <p class="fw fw-bold" style="font-size: 30px">{{$store.state.solution.solutionKey}}.{{$store.state.solution.title}}</p>
                <ul class="list-group" style="width: 50%">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        题解贡献者: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{$store.state.solution.userName}}
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        发布时间: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{$store.state.solution.date}}
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        参考语言: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{$store.state.solution.language}}
                    </li>
                </ul>
                <p></p>
                <md-editor
                    v-model = $store.state.solution.content
                    previewOnly = true>
                </md-editor>
            </div>
        </div>
    </ContentField>
</template>


<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import MdEditor from 'md-editor-v3'
import "../../assets/font/font.css"

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
    console.log(store.state.solution)

    return {
      logged,
    }

  }
}

</script>

<style scoped>
.col-6{
    height: 80vh;
    overflow-y: scroll;
}
</style>