<template>
    <ContentField>
         这是主界面 登录状态：{{ store.state.user.is_login == true ? "登录" : "未登录" }}
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
                style="height: 300px" 
                :options="{
                    enableBasicAutocompletion: true,
                    fontSize:14,
                    showPrintMargin:false,
                }"
                />
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
 export default{
    components: { ContentField },
    setup(){
       
        const store = useStore();
        const jwt_token = localStorage.getItem("jwt_token");
        if(jwt_token){
            store.commit("updateToken", jwt_token);
            store.dispatch("getInfo", {
                success(){
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        }else {
            store.commit("updatePullingInfo", false);
        }
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
    }
 }
 
 </script> 
 
 <style scoped> 
 </style>