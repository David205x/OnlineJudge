import $ from "jquery"

export default {
    state: {
        socket: null,
        receiverId: "",
        receiverName:"",
        content: [],
        selected: -1,
    },
    getters: {},
    mutations: {
        chattingLogout(state){
            state.socket = null,
            state.receiverId = "",
            state.receiverName = "",
            state.content = [],
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
            state.receiverId = data.receiverId
            state.receiverName = data.receiverName
            $.ajax({
                url: "http://127.0.0.1:3000/chatting/chattingroom/chathistory/",
                type: 'post',
                data:{
                    senderKey : data.senderId,
                    receiverKey : state.receiverId,
                    page: 1
                },
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                },
                success(resp) {
                    state.content = resp.chattingList
                    data.success()
                },
                error(resp) {
                    console.log(resp)
                }
            })

        },
        
        updateSelected(state, selected){
            state.selected = selected
        },
        appendContent(state, content){
            state.content.push(JSON.parse(content));
        }
    },
    actions: {
        
    },
    modules: {}
}
