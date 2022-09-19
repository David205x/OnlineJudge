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
            <ProblemDetails>
                
            </ProblemDetails>
        </div>
        <div v-else-if="f == 2">
            <ProblemEditorial>
            
            </ProblemEditorial>
        </div>
        
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import ProblemDetails from "@/components/problem/ProblemDetails.vue";
import ProblemEditorial from "@/components/problem/ProblemEditorial.vue";
import 'md-editor-v3/lib/style.css';
 export default{
    components: { ContentField, ProblemDetails, ProblemEditorial },
    setup(){  
        let f = ref(1);
        const onChange = (data) =>{
            f.value = data;
        }
         return {
            f,
            onChange,
         }
    }
 }
 
 </script> 
 
 <style scoped> 

 </style>