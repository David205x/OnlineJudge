<template>
    <ContentField>
        <div class="container" style="font-size: 17px">
            <div class="row align-items-start">
                <div class="col-5" style="height: auto;">
                    <md-editor
                        v-model = $store.state.problem.problemDescription
                        previewOnly = true>
                    </md-editor>
                </div>
                <div class="col-1"></div>
                <div class="col-6">
                    <VAceEditor
                        @init="editorInit"
                        :lang="language_selected"
                        theme="textmate"
                        style="height: 100vh; margin-top: 1vh"
                        v-model:value="code.content"
                        :options="{
                            enableBasicAutocompletion: true,
                            fontSize:17,
                            showPrintMargin:false,
                         }">
                    </VAceEditor>
                </div>
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
  name: "ProblemDetailHorizontalTemplate",
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
</style>