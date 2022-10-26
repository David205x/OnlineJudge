export default {
    state: {
        status: "matching", //matching 匹配 playing 对战
        socket: null,
        receiverId: "",
        receiverName:"",
        content: [],
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
        },
        appendContent(state, content){

            state.content.push(JSON.parse(content));
            console.log(JSON.parse(content))
        }
    },
    actions: {},
    modules: {}
}
