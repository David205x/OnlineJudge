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
            state.content.push(JSON.parse(content));
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
    },
    actions: {
        updateAllReceiver(context, data){
            
            for(let i = 0; i < context.rootState.user.friends.length; i++){
                console.log(context.rootState.user.friends[i])
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
                        console.log(resp)
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
