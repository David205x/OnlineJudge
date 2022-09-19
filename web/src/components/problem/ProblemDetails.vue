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
                    <div class="col-6">
                    </div>
                    <div class="col-4">
                        <select class="form-select" aria-label="Default select example" >
                            <option selected>C++</option>
                            <option value="1">C</option>
                            <option value="2">Python</option>
                            <option value="3">Java</option>
                            
                        </select>
                    </div>
                    <div class="col-1">
                        <i v-on:mouseover="spinnerChangeCog(1)" v-on:mouseout="spinnerChangeCog(0)" :class="spinner_cog == 1 ? 'fa fa-refresh fa-spin fa-2x' : 'fa fa-refresh fa-2x'" ></i>
                    </div>
                    <div class="col-1">
                        <i class="fa fa-cog fa-2x active"></i>
                    </div>
                </div>
            </div>
            
            <span>这是IDE</span>
            <VAceEditor
                @init="editorInit"
                lang="c_cpp"
                theme="textmate"
                style="height: 300px" 
                :options="{
                    enableBasicAutocompletion: true,
                    fontSize:14,
                    showPrintMargin:false,
                }">
                
            </VAceEditor>
        </div>
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { ref } from 'vue'
import { marked } from "marked";
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import mathJax from '@/components/mathJax/index.vue';
import 'font-awesome/css/font-awesome.min.css';

import 'md-editor-v3/lib/style.css';
ace.config.set(
    "basePath", 
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
export default{
    components: { 
        ContentField, 
        VAceEditor,
        mathJax,
    },
    setup(){  
        let spinner_cog = ref(0);
        const store = useStore();
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
        return {
            md,
            flag,
            spinner_cog,
            compiledMarkdown,
            spinnerChangeCog
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
	font-size: 2em;
	transition: all 0.3s;
}
i:hover{
	color: #03e9f4;
}
i.fa {
    text-shadow : 0 0 0.05px ,
              0 0 2px #64867a,
              0 0 2px #64867a,
              0 0 10px #64867a;
}
 
</style>