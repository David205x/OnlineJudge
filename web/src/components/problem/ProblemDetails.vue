<template>
    <ContentField>
        <div class="container">
            <div class="row align-items-start">
                <div class="col-10" style="height: auto;">
                        <mathJax :latex="compiledMarkdown()"></mathJax>       
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
                <div class="row align-items-start">
                    <div class="col-7">
                    </div>
                    <div class="col-3">
                        <select class="form-select" aria-label="Default select example" v-model=language_selected>
                            <option value="c_cpp" selected>C++</option>
                            <option value="c_cpp">C</option>
                            <option value="python">Python</option>
                            <option value="java">Java</option>         
                        </select>
                    </div>
                    <div class="col-2" style="margin-top: 4px; ">
                        <i @click="refresh" v-on:mouseover="spinnerChangeCog(1)" v-on:mouseout="spinnerChangeCog(0)" :class="spinner_cog == 1 ? 'fa fa-refresh fa-spin fa-2x' : 'fa fa-refresh fa-2x'" ></i>
                        <i class="fa fa-cog fa-2x" style="margin-left: 50px"></i>
                    </div>
                    
                </div>
            </div>
            <VAceEditor
                @init="editorInit"
                :lang="language_selected"
                theme="textmate"
                style="height: 600px; margin-top: 8px" 
                v-model:value="code.content"
                :options="{
                    enableBasicAutocompletion: true, 
                    fontSize:14,
                    showPrintMargin:false,
                }">
                
            </VAceEditor>
                
            <div :class="submission_status == 'Accepted' ? 'accepted' : 'wrong' " >
                <span style="color:black; font-weight: normal" v-if="submission_status !== '?'">代码运行状态:  </span>
                &nbsp;
                <span v-if="submission_status !== '?' && submission_status !== 'Waiting'">{{ submission_status }}</span>
                <span class="loading">
                    <span class="fa fa-circle-o-notch fa-spin fa-lg" v-if="submission_status == 'Waiting'"></span>
                </span>
                
                <button @click="submitcode" class="btn btn-primary" style="float: right; margin-top: -10px">提交</button>
                
            </div>
        </div>
        <div style="font-size:10px; ">
            <calendar-heatmap 
                    :values="[{ date: '2022-9-11', count: 10 }, { date: '2022-9-10', count: 6 }]" 
                    end-date="2022-9-20" 
                    max="12"
                    round="3"
                    :range-color="['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39']"
                    locale="right"
                    />
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
import mathJax from '@/components/mathJax/index.vue';
import 'font-awesome/css/font-awesome.min.css';
import { CalendarHeatmap } from 'vue3-calendar-heatmap'
import 'md-editor-v3/lib/style.css';
ace.config.set(
    "basePath", 
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
export default{
    components: { 
        ContentField, 
        VAceEditor,
        mathJax,
        CalendarHeatmap,
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
            md,
            flag,
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