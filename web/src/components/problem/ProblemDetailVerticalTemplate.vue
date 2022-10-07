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
              <span class="badge bg-success">{{$store.state.problem.problemDifficulty}}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              总通过数
              <span class="badge bg-secondary">{{$store.state.problem.acceptedAttempts}}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              总提交数
              <span class="badge bg-secondary">{{$store.state.problem.totalAttempts}}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              时间限制
              <span class="badge bg-secondary">{{$store.state.problem.problemTimeLimit}}ms</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              空间限制
              <span class="badge bg-secondary">{{$store.state.problem.problemMemoryLimit}}KB</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              标签
              <span class="badge bg-primary" v-for ="tag in $store.state.problem.problemTags" :key = "tag">{{tag}}</span>
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
              <option value="cpp" selected>C++</option>
              <option value="c">C</option>
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
      <div id="Vace">
            <VAceEditor
            @init="editorInit"
            :lang="(language_selected == 'cpp' || language_selected == 'c') ? 'c_cpp' : language_selected"
            theme="textmate"
            style="height: 600px; margin-top: 1vh"
            v-model:value="code.content"
            :options="{
                      enableBasicAutocompletion: true,
                      fontSize:FS,
                      showPrintMargin:false,
                  }">
            
          </VAceEditor>
        </div>
            
      

      <div class="submit_debug">
        <button @click="submitcode(debugInfo, 1)" class="btn btn-primary">调试</button>
        &nbsp;
        <button @click="submitcode(null, 0)" class="btn btn-primary">提交</button>
      </div>
      <div :class="submission_status == 'Accepted' || submission_status == 'Finished' ? 'accepted' : 'wrong' " >
          <span style="color:black; font-weight: normal;" v-if="submission_status !== '?'">代码运行状态:  </span>
          &nbsp;
          <span style="margin-top: 3vh" v-if="submission_status !== '?' && submission_status !== 'Waiting'">{{ submission_status }}</span>
          <span class="loading">
              <span class="fa fa-circle-o-notch fa-spin fa-lg" v-if="submission_status == 'Waiting'"></span>
          </span>
      </div>
      <div class="mb-3">
        <label for="validationTextarea" class="form-label">输入</label>
        <textarea class="form-control" id="validationTextarea" v-model="debugInfo"></textarea>
      </div>
      <div class="mb-3">
        <label for="validationTextarea" class="form-label">输出</label>
        <textarea class="form-control" id="validationTextarea" v-model="$store.state.problem.debugOutcome"></textarea>
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
import { nextTick } from 'vue'
ace.config.set(
    "basePath",
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
export default{
  components: {
    ContentField,
    VAceEditor,
    MdEditor,
  },
  created() {
    
  },
  setup(){
    
    let language_selected = ref('cpp');
    let spinner_cog = ref(0);
    let submission_status = ref('?');
    let debugInfo = ref("");
    let FS = ref(13);

    const store = useStore();
    const code = reactive({
      content: "",
    });
    
    nextTick(()=>{
      
      document.getElementById("Vace").addEventListener('mousewheel', function(){
          if (event.ctrlKey === true || event.metaKey) {
              event.preventDefault();
              if(FS.value <= 13) FS.value = 13;
              if(FS.value >= 100) FS.value = 100;
              if(event.deltaY < 0 && FS.value < 100) FS.value++;
              if(event.deltaY > 0 && FS.value > 13) FS.value--;
          }
      },{ passive: false});
    })
    const spinnerChangeCog = (data) =>{
      spinner_cog.value = data;
    }
    const compiledMarkdown = () => {
      return marked.parse(store.state.problem.problemDescription);
    }
    
    
    const submitcode = (debugInfo_value, is_debug) =>{
      console.log(code.content)
      submission_status.value = "Waiting"
      if(is_debug && (debugInfo_value == null || debugInfo_value.length == 0 || debugInfo_value.replace(/\s*/g,"").length == 0)){
        submission_status.value = 'Finished'
      }
      else {
          
          store.dispatch("sendSubmission", {
            userKey: store.state.user.id,
            content: code.content,
            language: language_selected.value,
            debugInfo: debugInfo_value,
            targetProblem: store.state.problem.problemKey,
            success(resp) {
              console.log(resp);
              submission_status.value = resp.SubmissionStatus;
              store.commit("updataDebugOutcome", resp.debugOutcome)
            },
            error() {
              console.log("?");
            }
        });
      }
      
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
      FS,
      debugInfo,
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