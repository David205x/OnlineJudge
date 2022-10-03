<template>
    <ContentField>
        <div class="container">
            <div class="row align-items-start">
                <div class="col-10" style="height: auto;">
                        <md-editor 
                            v-model = $store.state.problem.problemDescription
                            previewOnly = true>
                        </md-editor>
                </div>
                <div class="col-2">
                    <ul class="list-group">
                        <li class="list-group-item">难度</li>
                        <li class="list-group-item">总通过数</li>
                        <li class="list-group-item">总提交数</li>
                        <li class="list-group-item">tag</li>
                    </ul>
                </div>
            </div>
        </div>
        <div>
            <div class="container">
                <div class="row align-items-center">
                    <div class="col">

                    </div>
                    <div class="col">

                    </div>
                    <div class="col-4">
                        <select class="form-select" aria-label="Default select example"  v-model=language_selected>
                            <option value="c_cpp" selected>C++</option>
                            <option value="c_cpp">C</option>
                            <option value="python">Python</option>
                            <option value="java">Java</option>         
                        </select>
                    </div>
                    <div class="col justify-content-end" >
                        <i @click="refresh" v-on:mouseover="spinnerChangeCog(1)" v-on:mouseout="spinnerChangeCog(0)" :class="spinner_cog == 1 ? 'fa fa-refresh fa-spin fa-2x' : 'fa fa-refresh fa-2x'"  style="margin-left: 5vw"></i>
                        <i class="fa fa-cog fa-2x" style="margin-left: 4vw"></i>
                    </div>
                    
                </div>
            </div>
            <VAceEditor
                @init="editorInit"
                :lang="language_selected"
                theme="textmate"
                style="height: 80vh; margin-top: 2vh" 
                v-model:value="code.content"
                :options="{
                    enableBasicAutocompletion: true, 
                    fontSize:14,
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
        const store = useStore();
        let submission_status = ref('?');
        
        const code = reactive({
            content: "",
        });
        const md = {
            context:
                "int main(){\n    int t;\n    t = 1;\n    int cnt = 1;\n    while(t--){\n        solve(cnt++);\n    }\n    return 0;\n}\n",
        }
        let flag = 0;
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
            md,
            flag,
            spinner_cog,
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
    margin-left: 2vw;
    font-size:large;
    margin-top: 2vh;
}
div.wrong{
    color: red;
    font-weight: bold;
    margin-left: 2vw;
    font-size:large;
    margin-top: 2vh;
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