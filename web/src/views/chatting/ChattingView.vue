<template>
    <ContentField>
        <ChattingRoom>

        </ChattingRoom>
     </ContentField>
 </template>
   
 <script>
import ContentField from "@/components/ContentField.vue";
//import { useStore } from "vuex";
// import { ref } from 'vue'
import ChattingRoom from "@/components/chatting/ChattingRoom.vue"
export default{
    components: { 
        ContentField,
        ChattingRoom,
    },
    setup(){
        //const store = useStore();
        return{

        }
    }
}
 
 </script> 
 
 <style scoped> 
 </style>