<template>
    <ContentField>
      <div class="row">
          <div class="col-4" v-for="(item, index) in store.state.solution.eachCard" :key="index">
            <div class="card" style="width: 18rem; max-height:500px">
              <div class="card-header">题目贡献者：{{ item.userName }}</div>
              <img src="../../assets/1.jpeg" class="card-img-middle" style="width: 18rem; height: 300px" >
              <div class="card-body" >
                <h5 class="card-title">{{store.state.problem.problemName}}</h5>
                <p class="card-text-2" style=" white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
                  {{ item.contentOverview }}
                </p>
                <a @click="solution(item)" class="btn btn-primary">查看题解</a>
              </div>
            </div>
            <br v-if="index % 3">
            <br v-if="index % 3">
          </div>

        </div>

<!--      <div class="card" style="width: 18rem;">-->
<!--        <img src="../../assets/testp1.jpg" class="card-img-top" >-->
<!--        <div class="card-body">-->
<!--          <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>-->
<!--        </div>-->
<!--      </div>-->

     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import 'font-awesome/css/font-awesome.min.css';
import router from '@/router/index'
import $ from 'jquery'
import { useStore } from "vuex";

 export default{
    components: { 
        ContentField,
    },
    setup(){  
        const md = ref();
        let r = window.location.href.match("problemId=.*/");
        let t = r[0].split("=")[1].split("/")[0]
        const jwt_token = localStorage.getItem("jwt_token");
        const store = useStore();
        if(jwt_token){
          store.commit("updateToken", jwt_token);
          store.dispatch("getInfo", {
            success(){
              store.dispatch("showProblemSolution", {
                problemKey : t
              })
              store.commit("updatePullingInfo", false);
            },
            error() {
              store.commit("updatePullingInfo", false);
            }
          })
        }else {
          store.commit("updatePullingInfo", false);
        }


        const showTutorial = (solutionKey) =>{
          $.ajax({    
            url: "http://127.0.0.1:3000/problem/details/" + solutionKey,
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
        const solution = (item) =>{

          store.dispatch("showProblemSolutionDetails", {
            problemKey : t,
            solutionKey : item. solutionKey,
            success(){
              router.push({name: "problem_solution"});
            }
          })
        }

        return {
            md,
            Save,
            showTutorial,
            solution,
            store
        }
    },
    computed:{
        
        
    },
 }
 
 </script> 
 
 <style scoped>
 div.card-text {

 }

 </style>