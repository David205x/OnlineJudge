<template>
  <ContentField>
    <div class="container" style="font-size: 17px">
      <div class="row align-items-start">
        <div class="col-9" style="height: auto;">
          <md-editor
              v-model = $store.state.problem.problemDescription
              previewOnly = true>
          </md-editor>
        </div>
        <div class="col-3">
          <ul class="list-group">
            <li class="list-group-item d-flex justify-content-between align-items-center">
              难度
              <span class="badge bg-success">Newbie</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              总通过数
              <span class="badge bg-secondary">114</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              总提交数
              <span class="badge bg-secondary">514</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              标签
              <span class="badge bg-primary">#策马</span>
              <span class="badge bg-primary">#看看你的</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div>
      <div class="container" style="margin-top: 2vh">
        <div class="row align-items-start">
          <div class="col-6">
          </div>
          <div class="col-2">
            <p class="fw-normal" style="text-align: right; margin: 0.5vh">代码语言 :</p>
          </div>
          <div class="col-2">
            <select class="form-select form-select-sm" aria-label="Default select example" v-model=language_selected>
              <option value="c_cpp" selected>C++</option>
              <option value="c_cpp">C</option>
              <option value="python">Python</option>
              <option value="java">Java</option>
            </select>
          </div>
          <div class="col-2" style="margin: 0vh 0vh 0vh">
            <i @click="refresh"
               v-on:mouseover="spinnerChangeCog(1)"
               v-on:mouseout="spinnerChangeCog(0)"
               :class="spinner_cog == 1 ? 'fa fa-refresh fa-spin fa-2x' : 'fa fa-refresh fa-2x'" ></i>
            <i class="fa fa-cog fa-2x" style="margin-left: 1vh"></i>
          </div>

        </div>
      </div>
      <VAceEditor
          @init="editorInit"
          :lang="language_selected"
          theme="textmate"
          style="height: 600px; margin-top: 1vh"
          v-model:value="code.content"
          :options="{
                    enableBasicAutocompletion: true,
                    fontSize:17,
                    showPrintMargin:false,
                }">
      </VAceEditor>

      <div class="submit_debug">
        <button @click="submitcode" class="btn btn-primary">调试</button>
        &nbsp;
        <button @click="submitcode" class="btn btn-primary">提交</button>
      </div>
      <div :class="submission_status == 'Accepted' ? 'accepted' : 'wrong' " >
          <span style="color:black; font-weight: normal;" v-if="submission_status !== '?'">代码运行状态:  </span>
          &nbsp;
          <span style="margin-top: 3vh" v-if="submission_status !== '?' && submission_status !== 'Waiting'">{{ submission_status }}</span>
          <span class="loading">
              <span class="fa fa-circle-o-notch fa-spin fa-lg" v-if="submission_status == 'Waiting'"></span>
          </span>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { ref, reactive } from "vue"
import { marked } from "marked";
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'font-awesome/css/font-awesome.min.css';
import 'md-editor-v3/lib/style.css';
import MdEditor from 'md-editor-v3'
ace.config.set(
    "basePath",
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
export default{
  components: {
    ContentField,
    VAceEditor,
    MdEditor,
  },

  setup(){
    let language_selected = ref('c_cpp');
    let spinner_cog = ref(0);
    let submission_status = ref('?');
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
    const submitcode = () =>{
      submission_status.value = "Waiting"
      store.dispatch("sendSubmission", {
        userKey: "1",
        content: code.content,
        language: language_selected.value,
        success(resp) {
          console.log(resp);
          submission_status.value = resp.SubmissionStatus;
        },
        error() {
          console.log("?");
        }
      });
    }
    const refresh = () =>{
      code.content = ""
    }
    return {
      spinner_cog,
      compiledMarkdown,
      spinnerChangeCog,
      code,
      submitcode,
      submission_status,
      refresh,
      language_selected,
    }
  },
  computed:{


  },
}

</script>

<style scoped>
i{
  list-style: none;
  margin: 0 20px;
}
i{
  position: relative;
  display: inline-block;
  font-size: 1.9em;
  transition: all 0.3s;
  color: transparent
}
i.fa.fa-refresh.fa-spin:hover{
  color: #cf4e4e;
}
i.fa.fa-cog.fa-2x:hover{
  color: #535353;
}
i.fa {
  text-shadow : 0 0 0px #c2c2c2,
  0 0 0px #c2c2c2,
  0 0 0px #c2c2c2,
  0 0 0px #c2c2c2;
}
div.accepted{
  color: rgb(28, 193, 28);
  font-weight: bold;
  font-size:large;
  margin-left: 10px;
  margin-top: 25px;
}
div.wrong{
  color: red;
  font-weight: bold;
  margin-left: 10px;
  font-size:large;
  margin-top: 25px;
}
span.loading{
  color: rgb(58, 231, 124);
  margin-bottom: 5px;
}
button.btn.btn-primary{
  position: relative;
  align-items: center;
  justify-content: center;
  height: 5vh;
  width: 5vw;
}
div.submit_debug{
  font-weight: bold;
  font-size:large;
  height: 5vh;
  text-align: right;
}
</style>