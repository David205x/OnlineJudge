<template>
    <ContentField>
      <div class="row">
        <div class="col">
          <div class="card" style="width: 18rem;">
            <img src="@/assets/1.jpeg" class="card-img-top" alt="...">
            <div class="card-body">
              <h5 class="card-title">简单的加法运算</h5>
              <p class="card-text">由题干可以....</p>
              <!-- <a href="#" class="btn btn-primary">查看此题题解</a> -->
              <a @click="showTutorial(1)" class="btn btn-primary">查看此题题解</a>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card" style="width: 18rem;">
            <img src="@/assets/1.jpeg" class="card-img-top" alt="...">
            <div class="card-body">
              <h5 class="card-title">Card title</h5>
              <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
              <a href="#" class="btn btn-primary">Go somewhere</a>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card" style="width: 18rem;">
            <img src="@/assets/1.jpeg" class="card-img-top" alt="...">
            <div class="card-body">
              <h5 class="card-title">Card title</h5>
              <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
              <a href="#" class="btn btn-primary">Go somewhere</a>
            </div>
          </div>
        </div>
      </div>
      <div>
        <span>
          如果对某道题的解法有优化或有更好的方法，欢迎上交题解
        </span>
        <button @click="initEditorial" class="btn btn-primary">写题解</button>
      </div>
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import 'font-awesome/css/font-awesome.min.css';
import router from '@/router/index'
import $ from 'jquery'
//import { useStore } from 'vuex'
 export default{
    components: { 
        ContentField, 
    },
    setup(){  
        const md = ref();
        let r = window.location.href.match("problemId=.*/");
        let t = r[0].split("=")[1].split("/")[0]
        //const store = useStore();
        const jwt_token = localStorage.getItem("jwt_token");

        const showTutorial = (solutionKey) =>{
          $.ajax({    
            url: "http://127.0.0.1:3000/problem/details/" + t + "/" + solutionKey,
            type: 'post',
            headers: {
                Authorization: "Bearer " + jwt_token,
            },
            success(resp) {
              console.log(resp)
            },
            error(resp) {
              console.log(resp);
            }
          })
        }
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
            showTutorial,
        }
    },
    computed:{
        
        
    },
 }
 
 </script> 
 
 <style scoped> 

 </style>