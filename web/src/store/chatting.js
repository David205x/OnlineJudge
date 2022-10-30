import $ from "jquery";
export default {
    state: {
        socket: null,
        receiverId: "",
        receiverName:"",
        content: [],
        allContent :[],
        selected: -1,
    },
    getters: {},
    mutations: {
        chattingLogout(state){
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
            state.content = state.allContent[data.index].chattingList
            state.receiverId = data.receiverId,
            state.receiverName = data.receiverName,
            data.success() 
        },
        updateSelected(state, selected){
            state.selected = selected
        },
        appendContent(state, content){
            let cmp = JSON.parse(content.data)
            let data = new Date(cmp.time)
            cmp.time = data.getFullYear() + "-" + 
            (data.getMonth() + 1) + "-" + data.getDate()
             + "T" + (data.getHours() < 10 ? "0" + data.getHours() : data.getHours())
              + ":" + (data.getMinutes() < 10 ? "0" + data.getMinutes() : data.getMinutes())  + ":";
            if(data.getSeconds() < 10){
                cmp.time += '0'   
            }
            cmp.time += data.getSeconds()   
            for(let j = 0; j < state.allContent.length; j++){
                for(let i = 0; i < state.allContent[j].chattingList.length; i++){
                    if(state.allContent[j].chattingList[i].receiverkey == cmp.receiverkey
                        && state.allContent[j].chattingList[i].senderkey == cmp.senderkey
                        || state.allContent[j].chattingList[i].receiverkey == cmp.senderkey
                        && state.allContent[j].chattingList[i].senderkey == cmp.receiverkey){
                        if(j == state.selected){
                            
                            if(cmp.state == "unread")
                                cmp.state = "read"
                            state.content.push(cmp);
                            state.allContent[j].chattingList = state.content
                            state.allContent[j].unreadNum = 0;
                            content.success()
                        } 
                        else { 
                            state.allContent[j].chattingList.push(cmp)
                            if(cmp.state == "unread")
                                state.allContent[j].unreadNum++;
                        }
                        return
                    }
                }
            }
           
        },
        appendAllContent(state, data){
            let friends = data.friends;
            for(let i = 0; i < friends.length; i++){
                if(data.userKey == friends[i].userKey){
                    state.allContent[i] = data.content;
                    return
                }
            }
        },
        changeContentSequence(state, index){
            let Cotent = state.allContent[index]
            
            for(let i = index; i >= 1; i--){
                state.allContent[i] = state.allContent[i - 1]
            }
            state.allContent[0] = Cotent
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
            
            for(let i = 0; i < context.rootState.user.friends.length; i++){
                $.ajax({
                    url: "http://127.0.0.1:3000/chatting/chattingroom/chathistory/",
                    type: 'post',
                    data:{
                        senderKey : data.senderKey,
                        receiverKey : context.rootState.user.friends[i].userKey,
                        page: 1
                    },
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                    },
                    success(resp) {
                        data.success({"resp" : resp, "userKey" : context.rootState.user.friends[i].userKey})
                    },
                    error(resp) {
                        console.log(resp)
                    }
                })
            }
        },

    },
    modules: {}
}
