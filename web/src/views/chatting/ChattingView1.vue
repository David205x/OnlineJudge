<template>
  <ContentField>
    <div class="row">
      <div class="col-3" style="margin: -2vh 0vw -1vh -1vw">
          <p class="fw" style="font-size: 40px; margin: 2vh 5vh 0vh 2vw">私信</p>
        <ContentField>
          <div v-if="is_show">
            <div class="list-group" style="overflow: auto; height: 60vh; margin: -1.5vh -1vw 0vh -1vw">
              <div
                  v-for="(item, index) in store.state.chatting.allContent"
                  :key="index"
                  :class="index == store.state.chatting.selected ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'"
                  @click="redSession(friends[index], index)"
                  style="height: 20vh"
              >
              <!-- friends[index].userName 对方用户名
                   item.unreadNum 未读消息数
                   item.chattingList[item.chattingList.length - 1].content 最后一条消息  -->
              <div>
                  <div row align-items-start>
                      <div col-4>
                          <img src="../../assets/testp1.jpg" class="img-thumbnail"
                               style="width:50px; height:50px;
                               border-radius: 100px; webkit-border-radius: 100px; moz-border-radius: 100px;
                               margin-left: -0.5vw">
                          &nbsp;
                          {{ friends[index].userName }}
                          &nbsp;
                          <span class="badge rounded-pill bg-danger"
                                v-if="item.unreadNum != 0 && item.chattingList[item.chattingList.length - 1].senderkey != $store.state.user.id"
                          >{{item.unreadNum}}</span>
                      </div>
                  </div>
                  <p class="fw-light lh-sm" style="font-size: 15px; margin-top: 2vh">{{item.chattingList[item.chattingList.length - 1].content}}</p>
                &nbsp;
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
            <button class="btn btn-primary" @click="send()">发送(enter)</button>
          </div>
        </div>
        </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from 'vuex'
import { onMounted, onUnmounted, nextTick } from 'vue'
// import $ from 'jquery'
import { ref } from 'vue'


export default {
  components: {
    ContentField,
  },
  setup(){
    const store = useStore();
    let selectedChat = ref([]);
    let user = ref([]);
    let content = ref('');
    let friends = ref([]);
    let is_show = ref(false);
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
    let socket = null;
    const jwt_token = localStorage.getItem("jwt_token");
    window.addEventListener('beforeunload', () =>{
      store.dispatch("submitFriends", {
        userKey : store.state.user.id,
        userName : store.state.user.username,
        friends : store.state.user.friends,
      })
      /**
       * 新接收者更新数据
       */
      store.dispatch("addFriends", {
        senderKey : store.state.user.id,
        senderName : store.state.user.username
      })
      store.dispatch("updateHis",{
            ChattingInfo : store.state.chatting.allContent,
            userkey : store.state.user.id,
            success(){
              store.commit("chattingLogout")
            }
      })
    } );
    store.commit("updatePullingInfo", true); 
    if(jwt_token){
        store.commit("updateToken", jwt_token);
        store.dispatch("getInfo", {
            success(){
                   /**
                   * 读取用户好友列表
                   */
                   store.dispatch("refreshFriends", {
                      userKey : store.state.user.id,
                      friends: store.state.user.friends,
                      success(){

                        store.dispatch("updateAllReceiver",{
                          senderKey : store.state.user.id,
                          friends: store.state.user.friends,
                          success(resp){
                            console.log(store.state.user.friends)
                            store.commit("appendAllContent", {
                              userKey : resp.userKey,
                              content : resp.resp,
                              friends : store.state.user.friends
                            })
                            friends.value = store.state.user.friends   
                            setTimeout(() =>{
                              is_show.value = true;
                            }, 10) 
                          }
                        });
                      }
                    })
                store.commit("updatePullingInfo", false);         
            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })
    }else {
        store.commit("updatePullingInfo", false);
    }
    
    
    const messages = () =>{
      nextTick(() =>{
        let list = document.getElementById("list")
        list.scrollTop =  list.scrollHeight
      })
    }
    const redSession = (item, index) =>{
     
      store.commit("updateReceiver",{
        index : index,
        receiverId : item.userKey,
        receiverName : item.userName,
        success(){
          friends.value = store.state.user.friends
          store.commit("changeSequence", index)
          store.commit("changeContentSequence", index);
          messages()
          store.commit("updateSelected", 0)
          store.commit("updateState")
          console.log(store.state.chatting.allContent)
          store.dispatch("updateHis",{
            ChattingInfo : {"userkey" : store.state.user.id, "content" : store.state.chatting.allContent},
            success(){
            }
          })
        }
      })
      
    }
    
    onMounted(() => {
      socket = new WebSocket(socketUrl);
      socket.onopen = () => {
          store.commit("updateSocket", socket);
      }
      socket.onmessage = msg => {
        
        store.commit("appendContent", {
          data : msg.data,
          success(){
            console.log(store.state.chatting.content)
            // store.dispatch("updateHis",{
            //   ChattingInfo : [store.state.chatting.content],
            // })
          }
        });
          messages()    
      }
      socket.onclose = () => {
      }
    });

    onUnmounted(() => {
      store.dispatch("submitFriends", {
        userKey : store.state.user.id,
        userName : store.state.user.username,
        friends : store.state.user.friends,
      })
      /**
       * 新接收者更新数据 
       */
      store.dispatch("addFriends", {
        senderKey : store.state.user.id,
        senderName : store.state.user.username
      })
      store.dispatch("updateHis",{
        ChattingInfo : {"userkey" : store.state.user.id, "content" : store.state.chatting.allContent},
        success(){
          store.commit("chattingLogout")
        }
      })
      
      socket.close();
    })
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
      friends,
      is_show,
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

