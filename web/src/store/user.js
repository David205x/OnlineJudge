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
    },
    getters: {},
    mutations:{
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
            state.is_login = false
        },
    },
    actions:{
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: 'post',
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    console.log(data)
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
        getinfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/info/",
                type: 'get',
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateUser", {
                            ...resp,
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
        getinfoInMainPage(context, data) {
            console.log(context.state.token);
            console.log(data);
            $.ajax({
                url: "http://127.0.0.1:3000/index/info/",
                type: 'post',
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }
            });
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        },
        sendSubmission(context, data){
            console.log(context);
            $.ajax({
                url: "http://127.0.0.1:3000/user/submission/getinfo/",
                type: 'POST',
                data: {
                    userKey: data.userKey,
                    code: data.content,
                    language: data.language,
                    debugInfo: data.debugInfo,
                    targetProblem: data.targetProblem,
                },
                // headers: {
                //     Authorization: "Bearer " + context.state.token,
                // },
                success(resp) {
                    console.log(this.data)
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