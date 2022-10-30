import $ from 'jquery'


export default{
    state:{
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true,
        isChatOpen: false,
        friends: [],
        friends_update : [],
    },
    getters: {},
    mutations:{
        updateVisit(state, data){
            localStorage.setItem("is_visit", data.userKey)
            if(data.userKey == -1){
                localStorage.setItem("visitUsername", "")
                localStorage.setItem("visitPhoto", "")  
            }
        },
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info;
        },
        logout(state) {
            state.id = "",
            state.username = "",
            state.photo = "",
            state.token = "",
            state.is_login = false,
            state.pulling_info = true,
            state.isChatOpen = false,
            state.friends = [],
            state.friends_update = [],
            localStorage.setItem("is_visit", -1)
            localStorage.setItem("visitUsername", "")
            localStorage.setItem("visitPhoto", "")
        },
        changeSequence(state, index){
            let friend = state.friends[index]
            
            for(let i = index; i >= 1; i--){
                state.friends[i] = state.friends[i - 1]
            }
            state.friends[0] = friend
        },
        addFriend(state, friend){       //维护好友队列，从其他用户主页进入聊天时执行
            let newFriend = []
            let update = {}
            let f = false;
            for(let i = 0; i < state.friends.length; i++){
                if(state.friends[i].userKey != friend.userKey){
                    newFriend.push(state.friends[i])
                }else {
                    f = true;
                    update = state.friends[i];
                }
            }
            for(let i = 1; i <= newFriend.length; i++){
                newFriend[i] = newFriend[i - 1]
            }
            if(f) newFriend[0] = update
            else newFriend[0] = friend  
            state.friends = newFriend
            if(state.friends.length > 10) state.friends = state.friends.slice(0, 10)
        },
        updateFriends(state, friend){   //更新新的接收者 点击发送会触发
            for(let i = 0; i < state.friends_update.length; i++){
                if(state.friends_update[i].userKey == friend.userKey){
                    return;
                }
            }
            if(state.friends_update.length + state.friends.length  + 1 > 10) return
            for(let i = state.friends_update.length; i >= 1; i--){
                state.friends_update[i] = state.friends_update[i - 1];
            }
            state.friends_update[0] = friend
        }
    },
    actions:{
        updateHis(context, data){   //会话结束时更新所有的记录
            for(let i = 0; i < data.ChattingInfo.length; i++){
                for(let j = 0; j < data.ChattingInfo[i].chattingList.length; j++){
                    $.ajax({
                        url: "http://127.0.0.1:3000/chatting/update/history/",
                        type: 'post',
                        data:{
                            ChattingInfo : JSON.stringify(data.ChattingInfo[i].chattingList[j])
                        },
                        headers: {
                            Authorization: "Bearer " + context.state.token,
                        },
                        success() {
                            data.success()
                        },
                        error(resp) {
                            console.log(resp)
                        }
                    })
                }
            }
            
        },
        submitFriends(context, data){ //会话结束后上传数据，更新本用户信息
            $.ajax({
                url: "http://127.0.0.1:3000/chatting/final/friends/",
                type: 'post',
                data: {
                    userKey : data.userKey,
                    FriendsInfo : JSON.stringify({"userName" : data.userName, "Friends" : JSON.stringify(data.friends)})
                },
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success() {
                },
                error(resp) {
                   console.log(resp)
                }
            });
        },
        refreshFriends(context, data){ //获取后端的好友列表
            $.ajax({
                url: "http://127.0.0.1:3000/chatting/update/friends/",
                type: 'post',
                data: {
                    userKey : data.userKey
                },
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if(JSON.parse(resp)[0].error_message === "error"){
                        console.log(resp)
                        data.success() 
                        return
                    }
                    context.state.friends = JSON.parse(resp)
                    data.success()
                },
                error(resp) {
                    console.log(resp)
                }
            });
        },
        addFriends(context, data){ //会话结束后更新与本用户产生关联的用户
            for(let i = 0; i < context.state.friends_update.length; i++){
                
                $.ajax({
                    url: "http://127.0.0.1:3000/chatting/add/friend/",
                    type: 'post',
                    data: {
                        senderKey : data.senderKey,
                        senderName : data.senderName,
                        receiverKey : context.state.friends_update[i].userKey
                    },
                    headers: {
                        Authorization: "Bearer " + context.state.token,
                    },
                    success() {
                    },
                    error(resp) {
                       console.log(resp)
                    }
                });
                
            }
            
        },
        getOthers(context, data){                       //进入他人主页时获取他人的部分信息
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/visit/",
                type: 'get',
                data:{
                    userKey: data.userKey
                },
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    localStorage.setItem("is_visit", resp.id)
                    localStorage.setItem("visitUsername", resp.username)
                    localStorage.setItem("visitPhoto", resp.photo)  
                },
                error(resp) {
                    console.log(resp);
                }
            });
        },
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: 'post',
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                    
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getInfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/info/",
                type: 'get',
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateUser", {
                            id: resp.id,
                            photo: resp.photo,
                            username: resp.username,
                            is_login: true,
                        });
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getinfoInMainPage(context) {
            $.ajax({
                url: "http://127.0.0.1:3000/index/info/",
                type: 'post',
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success() {
                },
                error(resp) {
                    console.log(resp);
                }
            });
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            context.commit("logout");
            context.commit("chattingLogout");
        },
        sendSubmission(context, data){
            $.ajax({
                url: "http://127.0.0.1:3000/user/submission/offersub/",
                type: 'POST',
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                data: {
                    userKey: data.userKey,
                    code: data.content,
                    language: data.language,
                    debugInfo: data.debugInfo,
                    targetProblem: data.targetProblem,
                    SUUID: data.SUUID,
                },
                success(resp) {
                    data.success(resp)
                },
                error() {
                    console.log(this.data)
                }
            });
        },
        sendPollRequest(context, data){
            $.ajax({
                url: "http://127.0.0.1:3000/user/submission/pollret/",
                type: 'POST',
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                data: {
                    SUUID: data.SUUID,
                },
                success(resp) {
                    data.success(resp)
                },
                error() {
                    console.log(this.data)
                }
            });
        },
    },
    modules:{}
}