import $ from "jquery"

export default {
    state: {
        status: "matching", //matching 匹配 playing 对战
        socket: null,
        receiverId: "",
        receiverName:"",
        content: [],
        selected: "",
    },
    getters: {},
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateContent(state, content){
            state.content = content;
        },
        updateReceiver(state, data){
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
    actions: {},
    modules: {}
}
