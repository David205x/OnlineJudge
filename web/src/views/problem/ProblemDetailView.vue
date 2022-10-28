<template>
    <ContentField>
        <div class="proName" style="margin-left: 1vw">
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
                <button  :class="f == 3 ? 'nav-link active' : 'nav-link'" >提交记录</button>
            </li>
            <div class="form-check form-switch" style="margin-top: 1.5vh">
                <input class="form-check-input" @click="switchLayout" type="checkbox" role="switch" id="flexSwitchCheckDefault">
                <label class="form-check-label" for="flexSwitchCheckDefault">布局切换</label>
            </div>
        </ul>
        <div v-if="f == 1">
            <div v-if="layout == 1">
                <ProblemDetailHorizontalTemplate>

                </ProblemDetailHorizontalTemplate>
            </div>
            <div v-else-if="layout == -1">
                <ProblemDetailVerticalTemplate>

                </ProblemDetailVerticalTemplate>
            </div>
        </div>
        <div v-else-if="f == 2">
            <ProblemEditorial>
            
            </ProblemEditorial>
        </div>
        <div v-else-if="f == 3">
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
import 'md-editor-v3/lib/style.css';
import "../../assets/font/font.css"
 export default{
    components: {
      ProblemDetailVerticalTemplate,
      ProblemDetailHorizontalTemplate, 
      ContentField, 
      ProblemResultView, 
      ProblemEditorial,
    },
    setup(){  
        const store = useStore();
        let f = ref(1);
        let layout = ref(-1);
        let problemName = ref("");
        let r = window.location.href.match("problemId=.*/");
        let t = r[0].split("=")[1].split("/")[0]
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
        store.dispatch("showProblem", {
            problemKey : t,
            success(){
                problemName.value = store.state.problem.problemKey + ". " + store.state.problem.problemName
                store.commit("updatePullingInfo", false);
            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })
        const onChange = (data) =>{
            f.value = data;
        }
        const switchLayout = () => {
            layout.value = -layout.value;
        }
    
         return {
            f,
            onChange,
            switchLayout,
            layout,
            problemName,
         }
    }
 }
 </script> 
 
 <style scoped>
div.proName{
  font-family:XW;
}
 </style>