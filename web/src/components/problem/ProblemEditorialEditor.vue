<template>
    <ContentField v-if="!$store.state.user.pulling_info">
      <div class="col-6">
        <input type="text" class="form-control" placeholder="题解标题" v-model=solution_title>
      </div>
      <br>


      <div class="row align-items-start">
        <div class="col-1">
          <p class="fw-bold" style="text-align: left; margin: 0.5vh">代码语言: </p>
        </div>
        <div class="col-2">
        <select class="form-select form-select-sm" aria-label="Default select example" v-model=language_selected>
          <option value="cpp" selected>C++</option>
          <option value="c">C</option>
          <option value="python">Python</option>
          <option value="java">Java</option>
        </select>
        </div>
      </div>
      <br>
      <span class="fs-2">
        题目要求及描述如下：
      </span>
      <div>
        <md-editor
          v-model = $store.state.problem.problemDescription
          previewOnly = true>
        </md-editor>
      </div>
      <br>
      <span class="fs-4">
        提示：下面的题解编辑器使用的是Markdown语法，建议使用``` #等关键字辅助完成题解的撰写
      </span>

        <div>
          <md-editor
              v-model = md_problem>
          </md-editor>
        </div>

      <div class="row align-items-start">
        <div class="col-10">
          <button type="button" class="btn btn-primary pull-right" style="width: 100px">保存</button>
        </div>
        <div class="col">
          <button type="button" class="btn btn-primary pull-right" style="width: 100px" @click="submit">提交</button>
        </div>
      </div>


     </ContentField>
 </template>
   
<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { ref, reactive } from "vue"
import { marked } from "marked";
import ace from 'ace-builds';
import 'font-awesome/css/font-awesome.min.css';
import 'md-editor-v3/lib/style.css';
import MdEditor from 'md-editor-v3'
import $ from 'jquery'
import router from "@/router";
ace.config.set(
    "basePath",
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
export default{
    name: "ProblemEditorialEditor",
    components: {
        ContentField,
        MdEditor,
    },

    setup(){
        const store = useStore();
        const code = reactive({
            content: "",
        });

        let spinner_cog = ref(0);
        let submission_status = ref('?');
        let solution_content = ref('');
        let language_selected = ref(store.state.solution.language);
        let solution_title = ref(store.state.solution.title);
        let md_problem = ref(store.state.solution.content);
        let beforeProblemKey = window.location.href.match("problemId=.*/");
        let beforeSolutionKey = window.location.href.match("solutionKey=.*/");

        // setTimeout(() =>{
        //   language_selected = ref(store.state.solution.language);
        //   md_problem = ref(store.state.solution.content);
        // }, 1000)

// <<<<<<< HEAD
//     let beforeSolutionKey = window.location.href.match("solutionKey=.*/");

//     store.dispatch("showProblemSolutionDetails", {
//       problemKey :  beforeProblemKey[0].split("=")[1].split("/")[0],
//       solutionKey : beforeSolutionKey[0].split("=")[1].split("/")[0],
//       success(){
//         // language_selected.value = store.state.solution.language;
//         // md_problem = ref(store.state.solution.content);
//         md_problem.value = store.state.solution.content
//         language_selected.value = store.state.solution.language
//       }
//     })

//     // setTimeout(() =>{
//     //   language_selected = ref(store.state.solution.language);
//     //   md_problem = ref(store.state.solution.content);
//     // }, 1000)
//     const code = reactive({
//       content: "",
//     });
//     store.dispatch("showProblem", {
//       problemKey: beforeProblemKey[0].split("=")[1].split("/")[0],
//       success(){
//         store.commit("updatePullingInfo", false);
//       },
//       error() {
//         store.commit("updatePullingInfo", false);
//       }
//     })
//     const spinnerChangeCog = (data) =>{
//       spinner_cog.value = data;
//     }
//     const compiledMarkdown = () => {
//       return marked.parse(store.state.problem.problemDescription);
//     }
//     const refresh = () =>{
//       code.content = ""
//     }
//     const submit = () =>{
//       console.log(md_problem.value)
//       console.log(language_selected.value)
//       console.log(store.state.user)
//       console.log(store.state.problem.problemKey)
//       if(beforeSolutionKey[0].split("=")[1].split("/")[0] == ''){
//         console.log("111")
//       $.ajax({
//         url: "http://127.0.0.1:3000/problem/details/" + store.state.problem.problemKey + "/addsolution/",
//         type: 'post',
//         data: {
//           userKey : store.state.user.id,
//           content : md_problem.value,
//           language: language_selected.value,
//         },
//         headers: {
//           Authorization: "Bearer " + store.state.user.token,
//         },
//         success(resp){
//           console.log(resp)
// =======
        const spinnerChangeCog = (data) =>{
            spinner_cog.value = data;
        }
        const compiledMarkdown = () => {
            return marked.parse(store.state.problem.problemDescription);
        }
        const refresh = () =>{
            code.content = ""
        }
        const submit = () =>{
            console.log(md_problem.value)
            console.log(language_selected.value)
            console.log(store.state.user)
            console.log(store.state.problem.problemKey)
            if(beforeSolutionKey[0].split("=")[1].split("/")[0] == ''){
                console.log("111")
                $.ajax({
                    url: "http://127.0.0.1:3000/problem/details/" + store.state.problem.problemKey + "/addsolution",
                    type: 'post',
                    data: {
                        userKey : store.state.user.id,
                        title : solution_title.value,
                        content : md_problem.value,
                        language: language_selected.value,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp){

                        console.log(resp)
                        location.reload()
                    }
                })
            }
            else{
                console.log("222")
                store.dispatch("updateProblemSolutionDetails", {
                    problemKey :  beforeProblemKey[0].split("=")[1].split("/")[0],
                    solutionKey : beforeSolutionKey[0].split("=")[1].split("/")[0],
                    language: language_selected.value,
                    userKey: store.state.user.id,
                    content: md_problem.value,
                    success(){
                        console.log(md_problem.value)
                        console.log(language_selected.value)
                        language_selected = ref(store.state.solution.language);
                        md_problem = ref(store.state.solution.content);
                        location.reload()
                    }
                })
            }


        }
        const permissionCheck = () =>{
            console.log("problem: ","-",store.state.problem.problemKey,
                "\nsolution: ","-",store.state.solution.solutionKey,
                "\nuser: ","-",store.state.solution.userKey)
            if(store.state.user.id != store.state.solution.userKey){
                console.log("111 ",store.state.solution.solutionKey);
                router.push({name:"problem_solution",params:{
                        problemKey : store.state.problem.problemKey,
                        solutionKey : store.state.solution.solutionKey,
                        tabId : 3
                    }})
            }
        }

        store.dispatch("showProblemSolutionDetails", {
            problemKey :  beforeProblemKey[0].split("=")[1].split("/")[0],
            solutionKey : beforeSolutionKey[0].split("=")[1].split("/")[0],
            success(){
                language_selected.value = store.state.solution.language;
                md_problem.value = store.state.solution.content;
                solution_title.value = store.state.solution.title;
                permissionCheck();
            }
        })
        store.dispatch("showProblem", {
            success(){
                store.commit("updatePullingInfo", false);
            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })


        return {
          spinner_cog,
          compiledMarkdown,
          spinnerChangeCog,
          solution_title,
          code,
          submission_status,
          refresh,
          md_problem,
          language_selected,
          submit,
          solution_content
        }
    },
    computed:{

    },
}
 
</script>


<style scoped>

 </style>