
import $ from "jquery";
export default {
    state: {
        socket: null,
        receiverId: "",
        receiverName:"",
        content: [],
        allContent :[],
        selected: -1,
        unread : false,
    },
    getters: {},
    mutations: {
        chattingLogout(state){
            if(state.socket != null)
                state.socket.close()
            state.socket = null,
            state.receiverId = "",
            state.receiverName = "",
            state.content = [],
            state.allContent = [],
            state.selected = -1
        },
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateContent(state, content){
            state.content = content;
        },
        updateReceiver(state, data){   //获取好友的聊天记录，点击某个好友时执行
            state.content = state.allContent[data.index].chattingList == null ? [] : state.allContent[data.index].chattingList
            state.receiverId = data.receiverId,
            state.receiverName = data.receiverName,
            data.success() 
        },
        updateSelected(state, selected){
            state.selected = selected
        },
        updateUnread(state, unread){
            state.unread = unread
        },
        appendContent(state, content){
            state.unread = false
            //如果不是在聊天界面的话就要额外执行未读信息功能
            if(content.url != "http://localhost:8080/chatting/chattingroom/"){
                console.log(content.url)
            }
            let cmp = JSON.parse(content.data)
            let data = new Date(cmp.time)
            cmp.time = data.getFullYear() + "-" + 
            (data.getMonth() + 1) + "-" + data.getDate()
             + " " + (data.getHours() < 10 ? "0" + data.getHours() : data.getHours())
              + ":" + (data.getMinutes() < 10 ? "0" + data.getMinutes() : data.getMinutes())  + ":";
            if(data.getSeconds() < 10){
                cmp.time += '0'   
            }
            cmp.time += data.getSeconds()  
            for(let j = 0; j < state.allContent.length; j++){
                if(state.allContent[j].chattingList.length == 0){
                    
                    if(content.friends[j].userKey == cmp.receiverkey
                    || content.friends[j].userKey == cmp.senderkey){
                        if(j == state.selected){
                            if(cmp.state == "unread" && content.userKey == cmp.receiverkey)
                                cmp.state = "read"
                            state.content.push(cmp);
                            state.allContent[j].chattingList = state.content
                            state.allContent[j].unreadNum = 0;
                            content.success()
                        }
                        else {
                            state.allContent[j].chattingList.push(cmp)
                            if(cmp.state == "unread"){
                                if(content.url != "http://localhost:8080/chatting/chattingroom/"){
                                    state.unread = true     
                                }
                                state.allContent[j].unreadNum++;
                            }
                            
                        }
                        return
                    }
                    
                }
                for(let i = 0; i < state.allContent[j].chattingList.length; i++){
                    if(state.allContent[j].chattingList[i].receiverkey == cmp.receiverkey
                        && state.allContent[j].chattingList[i].senderkey == cmp.senderkey
                        || state.allContent[j].chattingList[i].receiverkey == cmp.senderkey
                        && state.allContent[j].chattingList[i].senderkey == cmp.receiverkey){
                        if(j == state.selected){
                            if(cmp.state == "unread" && content.userKey == cmp.receiverkey)
                                cmp.state = "read"
                            state.content.push(cmp);
                            state.allContent[j].chattingList = state.content
                            state.allContent[j].unreadNum = 0;
                            content.success()
                        } 
                        else { 
                            state.allContent[j].chattingList.push(cmp)
                            if(cmp.state == "unread"){
                                if(content.url != "http://localhost:8080/chatting/chattingroom/"){
                                    state.unread = true
                                }
                                state.allContent[j].unreadNum++;
                            }
                                
                        }
                        return
                    }
                }
            }
           
        },
        addEmptyContent(state, selected){
            let chattingList = []
            let newAllContent =  state.allContent         
            let newContent = {
                "chattingList" : chattingList, 
                "unreadNum" : 0, 
                "chattingCount" : -1,
                "totalPages" : 0, 
                "perPage" : 10,
            }
            
            
            if(state.allContent.length == 0){
                state.allContent.push(newContent)
                return
            }
            if(selected == -1){
                for(let i = newAllContent.length; i >= 1; i--){
                    newAllContent[i] = newAllContent[i - 1]
                }
                newAllContent[0] = newContent
            }else {
                for(let i = newAllContent.length; i >= 2; i--){
                    newAllContent[i] = newAllContent[i - 1]
                }
                newAllContent[1] = newContent
            }
                
            if(newAllContent.length > 10) newAllContent = newAllContent.slice(0, 10)
            state.allContent = newAllContent
        },
        appendAllContent(state, data){
            let friends = data.friends;
            for(let i = 0; i < friends.length; i++){
                if(data.userKey == friends[i].userKey){
                    state.allContent[i] = data.content;
                }
            }
        },
        changeContentSequence(state, index){
            let Content = state.allContent[index]
            
            for(let i = index; i >= 1; i--){
                state.allContent[i] = state.allContent[i - 1]
            }
            state.allContent[0] = Content
        },
        updateState(state){
            for(let i = 0; i < state.allContent[0].chattingList.length; i++){
                if(state.allContent[0].chattingList[i].state == "unread"){
                    state.allContent[0].chattingList[i].state = "read";
                    state.allContent[0].unreadNum = state.allContent[0].unreadNum > 0 ? state.allContent[0].unreadNum - 1 : 0;
                }      
            }
        }
    },
    actions: {
        updateAllReceiver(context, data){
            console.log(context)
            //console.log(data.friends.length)
            if(data.friends == undefined || data.friends.length == 0){
                data.error()
                return
            }
            for(let i = 0; i < data.friends.length; i++){
                console.log(data.friends[i].userKey)
                $.ajax({
                    url: "http://127.0.0.1:3000/chatting/chattingroom/chathistory/",
                    type: 'post',
                    data:{
                        senderKey : data.senderKey,
                        receiverKey : data.friends[i].userKey,
                        page: 1
                    },
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                    },
                    success(resp) {
                        data.success({"resp" : resp, "userKey" : context.rootState.user.friends[i].userKey})
                        
                    },
                    error() {
                        data.error()
                    }
                })
            }
        },

    },
    modules: {}
}
