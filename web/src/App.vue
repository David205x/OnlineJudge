<template>
  <NavBar/>
  <router-view></router-view>
</template>

<script>

import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import 'highlight.js/styles/stackoverflow-light.css';// 可以切换其它样式风格，例如黑色主题
import 'highlight.js/lib/common';
import { useStore } from 'vuex'

export default {
    components: {
        NavBar,
    },
    setup() {
        const store = useStore();
        store.commit("updatePullingInfo", true)
        let socket = null   
        const jwt_token = localStorage.getItem("jwt_token");
        let socketUrl = null;
        setTimeout(() =>{
            let list = document.getElementById("list")
            if(list != null)
                list.scrollTop =  list.scrollHeight
        }, 200)
        const refreshFriends = () =>{
            //之后获取好友的信息
            store.dispatch("refreshFriends", {
                userKey : store.state.user.id,
                friends : store.state.user.friends,
                success(){
                    // console.log("进入主页，好友列表：")
                    // console.log(store.state.user.friends)
                    updateAllReceiver()
                }
            })
        }
        const updateAllReceiver = () =>{
            //获取所有的好友的对应记录
            store.dispatch("updateAllReceiver",{
                senderKey : store.state.user.id,
                friends: store.state.user.friends,
                success(resp){
                    // console.log("进入主页，历史信息：")
                    
                    appendAllContent(resp)
                },
                error(){
                }
            });
        }
        const appendAllContent = (resp) =>{
            //正确的将记录的顺序和好友列表对应
            //console.log(resp.resp)
            let info = resp.resp.chattingList[resp.resp.chattingList.length - 1]
            if(info != undefined && info.state == "unread" && info.senderkey != store.state.user.id){
                store.commit("updateUnread", true);
            }
            store.commit("appendAllContent", {
                userKey : resp.userKey,
                content : resp.resp,
                friends : store.state.user.friends
            }) 
        }

        const otherOp = () =>{
            
            socket.onopen = () => {
                socket.onmessage = msg => {
                    let data = JSON.parse(msg.data)
                    //检测该用户是否已经在自己的列表中
                    store.commit("isMyFriend", {
                        senderkey : data.senderkey,
                        success(resp){ //返回是否已经是朋友 
                            if(!resp && data.senderkey != store.state.user.id){
                                //console.log("new friends")
                                store.commit("addFriend", {
                                    userName : data.sendername,
                                    userKey :  data.senderkey,
                                    selected : store.state.chatting.selected
                                })
                                store.commit("addEmptyContent", store.state.chatting.selected)
                            }
                            store.commit("appendContent", {
                                data : msg.data,
                                url : window.location.href,
                                friends : store.state.user.friends,
                                userKey : store.state.user.id,
                                success(){
                                    console.log("success")
                                    console.log(store.state.chatting.allContent)
                                    setTimeout(() =>{
                                        let list = document.getElementById("list")
                                        list.scrollTop =  list.scrollHeight
                                    }, 10)
                                    // store.dispatch("updateHis",{
                                    //   ChattingInfo : [store.state.chatting.content],
                                    // })
                                }
                            });  
                             
                        }
                    })
                }   
                socket.onclose = () => {
                    // console.log("close")
                }
            }
            store.commit("updateSocket", socket);
        }
        const exit = (socket) =>{
            window.addEventListener('beforeunload', () =>{
                store.state.chatting.socket.send(JSON.stringify({
                    event: "sessionEnd",
                    senderKey : store.state.user.id,
                    senderName : store.state.user.username,
                    userKey : store.state.user.id,
                    userName : store.state.user.username,
                    friends : store.state.user.friends,
                    ChattingInfo : store.state.chatting.allContent
                }));
                store.dispatch("submitFriends", {
                    userKey : store.state.user.id,
                    userName : store.state.user.username,
                    friends : store.state.user.friends,
                    success(){
                        // console.log(store.state.chatting.allContent)
                    }
                })
                store.dispatch("updateHis",{
                    userkey : store.state.user.id,
                    ChattingInfo : store.state.chatting.allContent,
                    success(){
                        store.commit("chattingLogout")
                    }
                })
                socket.close();         
            })
        }
        
        if(jwt_token){
            store.commit("updateToken", jwt_token);
            store.dispatch("getInfo", {      
               
                success(){
                    socketUrl = `ws://localhost:3000/websocket/${jwt_token}/`;
                    //第一次无论进入那个页面都生成对应socket
                    socket = new WebSocket(socketUrl);
                    refreshFriends()
                    otherOp()
                    //页面关闭或者刷新，就更新所有数据
                    exit(socket)
                    store.commit("updateSocket", socket);
                    setTimeout(() =>{
                        store.commit("updatePullingInfo", false);
                    }, 400)             
                },
                error() {
                    store.commit("updatePullingInfo", true);
                }
            })
        }else {
            store.commit("updatePullingInfo", true);
        }
    },
    
}

</script>
<style>
</style>

