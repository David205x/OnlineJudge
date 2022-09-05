<template>
    <ContentField>
         这是主界面 登录状态：{{ logged == true ? "登录" : "未登录" }}
        <div>
            <span>一段示例代码</span>
            <highlightjs language="c++" :code="md.context" />
        </div>
        
         <div >
            {{ $store.state.problem.problemDescription }}
         </div>
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { marked } from "marked";
import hljsVuePlugin from "@highlightjs/vue-plugin";
export default{
    components: { 
        ContentField,
        highlightjs: hljsVuePlugin.component
    },
    setup(){
        
        const store = useStore();
        const logged = store.state.user.is_login;
        const md = {
            context:
                "\nint public main(){\n    int t;\n    t = 1;\n    int cnt = 1;\n    while(t--){\n        solve(cnt++);\n    }\n    return 0;\n}\n"
        }
        store.dispatch("showProblem",{
                success(){
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
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
        }
    },
    computed:{
        compiledMarkdown(){
            
            return marked.parse(this.md.context);
        }
    }
}
 
 </script> 
 
 <style scoped> 
 </style>