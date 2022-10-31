<template>
    <ContentField>
      <span class="fs-2">
        欢迎Wxx贡献自己的解题方案<br>
      </span>
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
    let problem_selected = ref('one');
    let language_selected = ref('cpp');
    let spinner_cog = ref(0);
    let submission_status = ref('?');
    let md_problem = ref();
    let solution_content = ref('');
    const store = useStore();
    const code = reactive({
      content: "",
    });
    store.dispatch("showProblem", {
      success(){
        store.commit("updatePullingInfo", false);
      },
      error() {
        store.commit("updatePullingInfo", false);
      }
    })
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
      $.ajax({
        url: "http://127.0.0.1:3000/problem/details/" + store.state.problem.problemKey + "/addsolution/",
        type: 'post',
        data: {
          userKey : store.state.user.id,
          content : md_problem.value,
          language: language_selected.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          console.log(resp)
        }
      })
    }

    return {
      spinner_cog,
      compiledMarkdown,
      spinnerChangeCog,
      code,
      submission_status,
      refresh,
      md_problem,
      problem_selected,
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