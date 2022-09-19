<template>
    <ContentField>
        <div>
            <md-editor 
                v-model = md
                @onSave="Save">
            </md-editor>
        </div>
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import MdEditor from 'md-editor-v3'
import 'font-awesome/css/font-awesome.min.css';
import 'md-editor-v3/lib/style.css';

 export default{
    components: { 
        ContentField, 
        MdEditor
    },
    setup(){  
        const md = ref();
        const Save = () =>{
            console.log(md.value);
        }
        return {
            md,
            Save,
        }
    },
    computed:{
        
        
    },
 }
 
 </script> 
 
 <style scoped> 

 </style>