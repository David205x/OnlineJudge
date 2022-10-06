<template>
    <ContentField>
        <div class="proName" style="margin-left: 1vw">
            <md-editor
              v-model = "problemName"
              previewOnly = true>
            </md-editor>
            &nbsp;
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
        </ul>  
        <div v-if="f == 1">
            <div v-if="layout == 1">
                <ProblemDetailHorizontalTemplate>

                </ProblemDetailHorizontalTemplate>
            </div>
            <div v-else-if="layout == 2">
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
import MdEditor from 'md-editor-v3'
 export default{
    components: {
      ProblemDetailVerticalTemplate,
      ProblemDetailHorizontalTemplate, 
      ContentField, 
      ProblemResultView, 
      ProblemEditorial, 
      MdEditor
    },
    setup(){  
        const store = useStore();
        let f = ref(1);
        let layout = ref(2);
        let problemName = ref("");
        let r = window.location.href.match("problemId=.*/");
        let t = r[0].split("=")[1].split("/")[0]
        store.dispatch("showProblem", {
            problemKey : t,
            success(){
                problemName.value = "##### " + store.state.problem.problemKey + ". " + store.state.problem.problemName
                store.commit("updatePullingInfo", false);
            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })
        const onChange = (data) =>{
            f.value = data;
        }
       
        console.log(problemName.value)
         return {
            f,
            onChange,
            layout,
            problemName,
         }
    }
 }
 </script> 
 
 <style scoped> 

 </style>