<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="proName" style="margin-left: 1vw" >
            <p class="fw-light" style="font-size: 45px">{{problemName}}</p>
        </div>
        <ul class="nav nav-tabs">
            <li class="nav-item" @click="onChange(1)">
                <button  :class="f == 1 ? 'nav-link active' : 'nav-link'" >题目</button>       
            </li>
            <li class="nav-item" @click="onChange(2)">
                <button  :class="f == 2 ? 'nav-link active' : 'nav-link'" >题解</button>
            </li>
            <li class="nav-item" @click="onChange(3)">
                <button  :class="f == 3 ? 'nav-link active' : 'nav-link'" >写题解</button>
            </li>
          <li class="nav-item" @click="onChange(4)">
            <button  :class="f == 4 ? 'nav-link active' : 'nav-link'" >提交记录</button>
          </li>
            <div class="form-check form-switch" style="margin-top: 1.5vh">
                <input class="form-check-input" @click="switchLayout" type="checkbox" role="switch" id="flexSwitchCheckDefault">
                <label class="form-check-label" for="flexSwitchCheckDefault">布局切换</label>
            </div>
        </ul>
        <div v-if="f == 1">
            <div v-if="layout === 1">
                <ProblemDetailHorizontalTemplate>
                </ProblemDetailHorizontalTemplate>
            </div>
            <div v-else-if="layout === -1">
                <ProblemDetailVerticalTemplate>
                </ProblemDetailVerticalTemplate>
            </div>
        </div>
        <div v-else-if="f == 2">
            <ProblemEditorial>
            </ProblemEditorial>
        </div>
        <div v-else-if="f == 3">
            <div v-if="layout === 1">
                <ProblemEditorialEditorHorizontal>
                </ProblemEditorialEditorHorizontal>
            </div>
            <div v-if="layout === -1">
                <ProblemEditorialEditor>
                </ProblemEditorialEditor>
            </div>

        </div>
        <div v-else-if="f == 4">
            <ProblemResultView>

            </ProblemResultView>
        </div>
     </ContentField>
 </template>


 <script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import { useStore } from "vuex";
import ProblemEditorial from "@/components/problem/ProblemEditorial.vue";
import 'md-editor-v3/lib/style.css';
import ProblemDetailHorizontalTemplate from "@/components/problem/ProblemDetailHorizontalTemplate";
import ProblemDetailVerticalTemplate from "@/components/problem/ProblemDetailVerticalTemplate";
import ProblemResultView from "@/components/problem/ProblemResultView";
import ProblemEditorialEditor from "@/components/problem/ProblemEditorialEditor";
import 'md-editor-v3/lib/style.css';
import "../../assets/font/font.css"
import ProblemEditorialEditorHorizontal from "@/components/problem/ProblemEditorialEditorHorizontal";
    export default{
        components: {
            ProblemEditorialEditorHorizontal,
            ProblemDetailVerticalTemplate,
            ProblemDetailHorizontalTemplate,
            ContentField,
            ProblemResultView,
            ProblemEditorial,
            ProblemEditorialEditor,
        },
        setup(){

            const store = useStore();
            const jwt_token = localStorage.getItem("jwt_token");
            let layout = ref(-1);
            let problemName = ref("");
            let beforeTabId = window.location.href.match("tabId=.*/");
            let TabId = beforeTabId[0].split("=")[1].split("/")[0]
            let f = ref(TabId == '' ? 1 : TabId);
            let beforeProblemKey = window.location.href.match("problemId=.*/");
            let problemKey = beforeProblemKey[0].split("=")[1].split("/")[0]
            let beforeSolutionKey = window.location.href.match("solutionKey=.*/");
            let solutionKey = ref(beforeSolutionKey[0].split("=")[1].split("/")[0])

            const onChange = (data) =>{
                f.value = data;
                // if(f.value == 1){
                //   setTimeout(() =>{
                //     location.reload()
                //   }, 100)
                // }
            }
            const switchLayout = () => {
                layout.value = -layout.value;
            }


            store.commit("updatePullingInfo", true);
            if(jwt_token){
                store.commit("updateToken", jwt_token);
                store.dispatch("getInfo", {
                    success(){
                        if(solutionKey.value != ''){
                            store.dispatch("showProblemSolutionDetails", {
                                problemKey : this.problemKey,
                                solutionKey : this.solutionKey,
                                success(){
                                    store.commit("updatePullingInfo", false);
                                }
                            })
                        }
                        store.commit("updatePullingInfo", false);
                    },
                    error() {
                        store.commit("updatePullingInfo", false);

                    }
                })
            }else {
                //store.commit("updatePullingInfo", false);
            }
            store.dispatch("showProblem", {
                problemKey : problemKey,
                success(){
                    problemName.value = store.state.problem.problemKey + ". " + store.state.problem.problemName;
                    store.commit("updatePullingInfo", true);
                },
                error() {
                    store.commit("updatePullingInfo", true);
                }
            })

            return {
                f,
                onChange,
                switchLayout,
                layout,
                problemName,
                solutionKey,
                store,
            }
        }
    }
 </script> 
 
 <style scoped>
div.proName{
  font-family:XW;
}
 </style>