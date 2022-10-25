<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <SearcH>

        </SearcH>
        <ChatList>

        </ChatList>
      </div>
      <div class="col-9">
      <MessaGe>
        
        
      </MessaGe>
      <TexT>

      </TexT>
      {{His}}
      <button class="btn btn-primary" @click="begin">发送</button>
    </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import MessaGe from "@/views/chatting/components/MessaGe.vue";
import TexT from "@/views/chatting/components/TexT.vue";
import SearcH from "@/views/chatting/components/SearcH.vue";
import ChatList from "@/views/chatting/components/ChatList.vue";
import { useStore } from 'vuex'
import { onMounted, onUnmounted } from 'vue'
import { ref } from "vue"
import $ from 'jquery'

export default {
  components: {
    ContentField,
    MessaGe,
    TexT,
    SearcH,
    ChatList,
  },
  setup(){
    const store = useStore();
    
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
    let socket = null;
    let His = ref();
    onMounted(() => {
          socket = new WebSocket(socketUrl);
          socket.onopen = () => {
              console.log("connected");
              
              store.commit("updateSocket", socket);  
              console.log(store.state.chatting.socket)
              store.state.chatting.socket.send(JSON.stringify({
                    event: "chatting",
              }));
          }
          socket.onmessage = msg => {
              //const data = JSON.parse(msg.data);
              console.log(msg.data)
              His.value = msg.data
              // if (data.event === "start-matching") {
              //   console.log(data);
              // } else if (data.event === "move") {
              //     console.log(data);
              // } else if (data.event === "result") {
              //     console.log(data);
              // }
          }
          socket.onclose = () => {
              console.log("disconnected");
          }
      });

    onUnmounted(() => {
          socket.close();
      })
    const begin = () =>{
        $.ajax({
              url: "http://127.0.0.1:3000/chatting/start/",
              type: 'post',
              data:{
                  a_id: 1,
                  b_id: 2
              },
              success(resp) {
                  console.log(resp)
              },
              error(resp) {
                  console.log(resp)
              }
          });
      }
    return{
      begin,
      His
    }
  },
  methods:{

  },
  computed: {

  },
  watch: {
  }
}
</script>

<style scoped>
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
