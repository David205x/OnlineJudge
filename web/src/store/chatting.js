export default {
    state: {
        status: "matching", //matching 匹配 playing 对战
        socket: null,
        receiverId: "",
        receiver_name: "",
        receiver_photo: "",
        content: [],
    },
    getters: {},
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateReceiver(state, receiver) {
            state.receiverId = receiver.receiverId;
            state.receiver_photo = receiver.receiver_photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateContent(state, content){
            state.content = content;
        },
        appendContent(state, content){
            console.log(content)
            state.content.push(content);
        }
    },
    actions: {},
    modules: {}
}
