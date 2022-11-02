<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <SearcH>

        </SearcH>
        <ContentField>
          <div v-if="!$store.state.user.pulling_info">
            <div class="list-group" style="overflow: auto;height: 55vh">
              <div
                  v-for="(item, index) in store.state.user.friends"
                  :key="index"
                  :class="index == store.state.chatting.selected ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'"
                  @click="redSession(item, index)"
              >
              <div>
                <img src="../../assets/testp1.jpg" class="img-thumbnail" style="width:50px; height:50px; border-radius: 100px; webkit-border-radius: 100px; moz-border-radius: 100px;">
                &nbsp;
                <!-- friends[index].userName 对方用户名
                     item.unreadNum 未读消息数
                     item.chattingList[item.chattingList.length - 1].content 最后一条消息  -->
                <span>{{item.userName}}</span>
    
                <div v-if="$store.state.chatting.allContent.length > index && $store.state.chatting.allContent[index] != undefined && $store.state.chatting.allContent[index].chattingList.length > 0">

                  <span style="border-radius: 100%" 
                          v-if="$store.state.chatting.allContent[index].unreadNum != 0 
                          && $store.state.chatting.allContent[index].chattingList[store.state.chatting.allContent[index].chattingList.length - 1].senderkey != $store.state.user.id"
                          >{{store.state.chatting.allContent[index].unreadNum}}
                  </span>
                  <span v-if="$store.state.chatting.allContent[index].chattingList
                          [store.state.chatting.allContent[index].chattingList.length - 1].content"
                          >{{
                            store.state.chatting.allContent[index].chattingList
                            [store.state.chatting.allContent[index].chattingList.length - 1]
                            .content
                          }}
                  </span>
                </div>
              </div>
            </div>
            </div>
          </div>
        </ContentField>
      </div>
        <div class="col-9">
          <div class="message">
            <header class="header">
              <div class="friendname">{{store.state.chatting.receiverName}}</div>
            </header>
            <div class="message-wrapper" ref="list" id="list">
              <div class="container">  
                <div v-for="(item,i) in store.state.chatting.content" class="message-item" :key="i"  id="list">
                  <div class="row">
                    <div class="col"></div>
                    <div class="col">
                      <div class="time"><span>{{ time(item.time) }}</span></div>
                    </div>
                    <div class="col"></div>
                  </div>
                  <div  v-if="store.state.user.id == item.receiverkey">
                    <div class="row">
                      <div class="col" style="text-align: left">
                        <img src="../../assets/testp1.jpg" class="img-thumbnail" style="width:50px; height:50px; border-radius: 100px; webkit-border-radius: 100px; moz-border-radius: 100px;">
                        {{item.content}}
                      </div>
                      <div class="col"></div>
                      <div class="col"></div>
                    </div> 
                  </div>
                  <div v-else>
                    <div class="row">
                      <div class="col"></div>
                      <div class="col"></div>
                      <div class="col" style="text-align: right">
                        {{item.content}}
                        <img src="../../assets/testp1.jpg" class="img-thumbnail" style="width:50px; height:50px; border-radius: 100px; webkit-border-radius: 100px; moz-border-radius: 100px;">
                      </div>
                    </div>  
                  </div>
                </div>
              </div>
            </div>
          </div>
        <div>
          <textarea class="form-control" id="validationTextarea" ref="text" v-model="content" @keyup="onKeyup"/>
          <div class="send" >
            <button class="btn btn-primary" @click="send">发送(enter)</button>
          </div>
        </div>
        </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import SearcH from "@/views/chatting/components/SearcH.vue";
import { useStore } from 'vuex'
import { nextTick, onUnmounted, onMounted } from 'vue'
// import $ from 'jquery'
import { ref } from 'vue'



export default {
  components: {
    ContentField,
    SearcH,
  },
  setup(){
    const store = useStore();
    let selectedChat = ref([]);
    let user = ref([]);
    let content = ref('');
    // window.addEventListener('beforeunload', () =>{
    //   store.dispatch("submitFriends", {
    //     userKey : store.state.user.id,
    //     userName : store.state.user.username,
    //     friends : store.state.user.friends,
    //   })
    //   /**
    //    * 新接收者更新数据
    //    */
    //   store.dispatch("addFriends", {
    //     senderKey : store.state.user.id,
    //     senderName : store.state.user.username
    //   })
    //   store.dispatch("updateHis",{
    //       ChattingInfo : store.state.chatting.allContent,
    //       userkey : store.state.user.id,
    //       success(){
    //         store.commit("chattingLogout")
    //       }
    //   })
    // });
    onMounted(() =>{
      store.commit("updateUnread", false)
      store.commit("updateSelected", -1)
      console.log(store.state.chatting.allContent)
    })
    onUnmounted(() =>{
      store.commit("updateSelected", -1)
      store.commit("updateContent", [])
    })
    const messages = () =>{
      nextTick(() =>{
        let list = document.getElementById("list")
        list.scrollTop =  list.scrollHeight
      })
    }
    const redSession = (item, index) =>{
      console.log("?")
      store.commit("updateReceiver",{
        index : index,
        receiverId : item.userKey,
        receiverName : item.userName,
        success(){
          store.commit("changeSequence", index)
          store.commit("changeContentSequence", index);
          messages()
          store.commit("updateSelected", 0)
          store.commit("updateState")

          // store.dispatch("updateHis",{
          //   ChattingInfo : {"userkey" : store.state.user.id, "content" : store.state.chatting.allContent},
          //   success(){
          //   }
          // })
        }
      })
      
    }
    
    
    // 入参 fmt-格式 date-日期
    const dateFormat = (fmt, date) => {
      let ret;
      const opt = {
          "Y+": date.getFullYear().toString(),        // 年
          "m+": (date.getMonth() + 1).toString(),     // 月
          "d+": date.getDate().toString(),            // 日
          "H+": date.getHours().toString(),           // 时
          "M+": date.getMinutes().toString(),         // 分
          "S+": date.getSeconds().toString()          // 秒
          // 有其他格式化字符需求可以继续添加，必须转化成字符串
      };
      for (let k in opt) {
          ret = new RegExp("(" + k + ")").exec(fmt);
          if (ret) {
              fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
          }
      }
      return fmt;
    }
    const time = (date) => {
      
      date = new Date(date);
  
      return dateFormat("YYYY-mm-dd HH:MM", date)
    }

    const send = () => {
    
      if(store.state.chatting.receiverId == -1){
        
        return
      }
      if(content.value == null || content.value == undefined || content.value == ""){
        //提示不能为空
        return;
      }
      
      store.state.chatting.socket.send(JSON.stringify({
            event: "singleMessage",
            senderKey: store.state.user.id,
            receiverKey: store.state.chatting.receiverId,
            sendername: store.state.user.username,
            receivername: store.state.chatting.receiverName,
            content: content.value
      }));
      store.commit("updateFriends", {
        userKey : store.state.chatting.receiverId,
      })
      /**
       * 新接收者更新数据 
       */
      store.dispatch("addFriends", {
          senderKey : store.state.user.id,
          senderName : store.state.user.username
      })
      
      nextTick(() =>{
        content.value = ''
      }) 
    }
    return{
      store,
      selectedChat,
      user,
      time,
      redSession,
      messages,
      content,
      send,
    }
  },
  methods:{
    
  }
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
  text-align: center;
}
span{
  display: inline-block;
  padding: 4px 6px;
  color: rgb(73, 66, 66);
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
  /*.text {
  position: relative;
  height: 25%;
  background: #fff;

  /deep/ .btn-wrapper-simple {
    height: 100%;
    line-height: 20px;
  }

  /deep/ .child-ul-wrapper {
    max-height: 150px;
    overflow: scroll;
  }

  /deep/ .nav-name-right {
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 200px;
  }

  /deep/ .el-drawer__header {
    margin-bottom: 0px;
  }

  .emoji {
    position: relative;
    width: 100%;
    height: 40px;
    line-height: 40px;
    font-size: 12px;
    padding: 0 30px;
    box-sizing: border-box;
    color: #7c7c7c;

    .icon-look {
      cursor: pointer;

      &:hover {
        color: #1aad19;
      }
    }

    .elround {
      font-size: 20px;
      font-weight: 600;
      padding-right: 10px;
    }

    input.pic {
      position: absolute;
      height: 3vh;
      display: inline-block;
      width: 20px;
      border-radius: 10px;
      opacity: 0;
      left: 59px;
    }

    .emojiBox {
      position: absolute;
      display: flex;
      flex-wrap: wrap;
      top: -210px;
      left: -100px;
      width: 300px;
      height: 200px;
      padding: 5px;
      background-color: #fff;
      border: 1px solid #d1d1d1;
      border-radius: 2px;
      box-shadow: 0 1px 2px 1px #d1d1d1;

      &.showbox-enter-active, &.showbox-leave-active {
        transition: all 0.5s;
      }

      &.showbox-enter, &.showbox-leave-active {
        opacity: 0;
      }
    }

    .emojiBox1 {
      position: absolute;
      display: flex;
      flex-wrap: wrap;
      top: -210px;
      left: -100px;
      width: 300px;
      height: 200px;
      padding: 5px;
      border-radius: 2px;

      &.showbox-enter-active, &.showbox-leave-active {
        transition: all 0.5s;
      }

      &.showbox-enter, &.showbox-leave-active {
        opacity: 0;
      }
    }
  }

  textarea {
    box-sizing: border-box;
    padding: 0 30px;
    height: 110px;
    width: 100%;
    border: none;
    outline: none;
    font-family: 'Micrsofot Yahei';
    resize: none;
  }

  .send {
    position: fixed;
    bottom: 30px;
    right: 30px;
    width: 75px;
    height: 28px;
    line-height: 28px;
    box-sizing: border-box;
    text-align: center;
    border: 1px solid #e5e5e5;
    border-radius: 3px;
    background: #f5f5f5;
    font-size: 14px;
    color: #7c7c7c;

    &:hover {
      background: rgb(18, 150, 17);
      color: #fff;
    }
  }

  .warn {
    position: absolute;
    bottom: 50px;
    right: 10px;
    width: 110px;
    height: 30px;
    line-height: 30px;
    font-size: 12px;
    text-align: center;
    border: 1px solid #bdbdbd;
    border-radius: 4px;
    box-shadow: 0 1px 5px 1px #bdbdbd;

    &.appear-enter-active, &.appear-leave-active {
      transition: all 1s;
    }

    &.appear-enter, &.appear-leave-active {
      opacity: 0;
    }

    &:before {
      content: ' ';
      position: absolute;
      top: 100%;
      right: 20px;
      border: 7px solid transparent;
      border-top-color: #fff;
      filter: drop-shadow(1px 3px 2px #bdbdbd);
    }
  }
}*/
</style>

