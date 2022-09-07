<template>
    <ContentField>
         这是主界面 登录状态：{{ logged == true ? "登录" : "未登录" }}
        <div>
            <span>一段示例代码</span>
            <highlightjs language="c_cpp" :code="md.context" />
        </div>
        <div>
            <span>这是IDE</span>
            <VAceEditor
                @init="editorInit"
                lang="c_cpp"
                theme="textmate"
                style="height: 300px" />
        </div>
        <div>
            <span>这是一段markdown</span>
            <mathJax :latex="compiledMarkdown"></mathJax>
        </div>
        <div>
            <span>这是MdEditor</span>
            <md-editor 
                v-model = md3>
            </md-editor>
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
import hljsVuePlugin from "@highlightjs/vue-plugin";
import mathJax from '@/components/mathJax/index.vue';
import MdEditor from 'md-editor-v3'

import 'md-editor-v3/lib/style.css';
ace.config.set(
    "basePath", 
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
export default{
    components: { 
        ContentField,
        highlightjs: hljsVuePlugin.component,
        VAceEditor,
        mathJax,
        MdEditor
    },
    setup(){
        const store = useStore();
        const logged = store.state.user.is_login;
        let md3 = ref("$x_i \\leq y_i$ 并且 $e = {x_i}^{10}$");
        let md = {
            context:
                "\nint main(){\n    int t;\n    t = 1;\n    int cnt = 1;\n    while(t--){\n        solve(cnt++);\n    }\n    return 0;\n}\n",
        }
        const md2 = {
            context: "$x_i \\leq y_i$ 并且 $e = {x_i}^{10}$"
        }
        store.commit("updatePullingInfo", false);
        if(store.state.user.is_login){
            store.dispatch("getinfoInMainPage", {
                success(){
                    console.log(store.state.user);
                }
            })
        }
        return {
            logged,
            md,
            md2,
            md3
        }
    },
    computed:{
        compiledMarkdown(){
            let Ht = marked.parse(this.md2.context);
            let j = JSON.stringify(Ht);
            console.log(JSON.parse(j))
            return JSON.parse(j);
        }
        
    },
}
 
 </script> 
 
 <style scoped> 
 </style>