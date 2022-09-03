<template>
    <ContentField>
         这是主界面 登录状态：{{ logged == true ? "登录" : "未登录" }}
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
 export default{
    components: { ContentField },
    setup(){
       
        const store = useStore();
        const logged = store.state.user.is_login;
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
        }
    }
 }
 
 </script> 
 
 <style scoped> 
 </style>