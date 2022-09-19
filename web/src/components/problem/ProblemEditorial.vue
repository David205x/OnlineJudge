<template>
    <ContentField>
        <div>
            <button @click="initEditorial" class="btn btn-primary">写题解</button>
        </div>
        <div class="container">
            <div class="row">
                <div class="col">
                    <div class="card">
                        <div class="card-body">
                            <slot></slot>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="card">
                        <div class="card-body">
                            <slot></slot>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="card">
                        <div class="card-body">
                            <slot></slot>
                        </div>
                    </div>
                </div>
            </div>
        </div>
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import 'font-awesome/css/font-awesome.min.css';
import router from '@/router/index'
 export default{
    components: { 
        ContentField, 
    },
    setup(){  
        const md = ref();
        const Save = () =>{
            console.log(md.value);
        }
        const initEditorial = () =>{
            router.push({name: "problem_editorial_editor"});
        }
        return {
            md,
            Save,
            initEditorial,
        }
    },
    computed:{
        
        
    },
 }
 
 </script> 
 
 <style scoped> 

 </style>