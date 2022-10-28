<template>
  <ContentField>
    <div class="message">
      <header class="header">
        <div class="friendname">{{store.state.chatting.receiverName}}</div>
      </header>
      <div class="message-wrapper" ref="list">

          <div v-for="(item,i) in store.state.chatting.content" class="message-item" :key="i"  id="list">
            <div class="time" ><span>{{ time(item.time) }}</span></div>
            &nbsp;
            <div style="float: left" v-if="store.state.user.id == item.receiverkey">{{item.content}}</div>
            <div style="float: right" v-else>{{item.content}}</div>

          </div>


      </div>
    </div>

  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { ref } from 'vue'
import { useStore } from "vuex";

export default {
  components: { ContentField },
  computed: {
  },
  setup(){
    let selectedChat = ref([]);
    let user = ref([]);
    const store = useStore();
    
    const time = (date) => {
      date = new Date(date);
      if(date.getMinutes() < 10){
        return date.getHours() + ':0' +date.getMinutes();
      }else{
        return date.getHours() + ':' + date.getMinutes();
      }
    }
    return{
      selectedChat,
      user,
      store,
      time,
    }
  },
}
</script>

<style scoped>
.message{
  width: 100%;
  height: 450px;}
.header{
  height: 60px;
  padding: 28px 0 0 30px;
  box-sizing: border-box;
  border-bottom: 1px solid #e7e7e7;}
.friendname{
  font-size: 18px}

.message-wrapper{
  min-height: 390px;
  max-height: 390px;
  padding: 10px 15px;
  box-sizing: border-box;
  overflow-y: auto;
  border-bottom: 1px solid #e7e7e7;}
.message{
  margin-bottom: 15px}
.time{
  width: 100%;
  font-size: 12px;
  margin: 7px auto;
  text-align: center;}
span{
  display: inline-block;
  padding: 4px 6px;
  color: #fff;
  border-radius: 3px;
  background-color: #dcdcdc;}
.main{}
.avatar{
  float: left;
  margin-left: 15px;
  border-radius: 3px;}
.content{
  display: inline-block;
  margin-left: 10px;
  position: relative;
  padding: 6px 10px;
  max-width: 330px;
  min-height: 36px;
  line-height: 24px;
  box-sizing: border-box;
  font-size: 14px;
  text-align: left;
  word-break: break-all;
  background-color: #fafafa;
  border-radius: 4px;}
.before{
  content: " ";
  position: absolute;
  top: 12px;
  right: 100%;
  border: 6px solid transparent;
  border-right-color: #fafafa;}
.self{
  text-align: right}
.avatar{
  float: right;
  margin:0 15px;}
.content{
  background-color: #b2e281}
before{
  right: -12px;
  vertical-align: middle;
  border-right-color: transparent;
  border-left-color: #b2e281;}
</style>