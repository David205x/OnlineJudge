<template>
    <ContentField>
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
<!--            <ProblemDetails>-->
<!--                -->
<!--            </ProblemDetails>-->


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
import ProblemEditorial from "@/components/problem/ProblemEditorial.vue";
import 'md-editor-v3/lib/style.css';
import ProblemDetailHorizontalTemplate from "@/components/problem/ProblemDetailHorizontalTemplate";
import ProblemDetailVerticalTemplate from "@/components/problem/ProblemDetailVerticalTemplate";
import ProblemResultView from "@/components/problem/ProblemResultView";
 export default{
    components: {
      ProblemDetailVerticalTemplate,
      ProblemDetailHorizontalTemplate, ContentField, ProblemResultView, ProblemEditorial },
    setup(){  
        let f = ref(1);
        let layout = ref(2);
        const onChange = (data) =>{
            f.value = data;
        }
         return {
            f,
            onChange,
            layout
         }
    }
 }
 
 </script> 
 
 <style scoped> 

 </style>