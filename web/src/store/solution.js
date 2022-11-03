import $ from 'jquery'

export default{
    state:{
        eachCard:[],
        userName:"",
        date:"",
        content:"",
        language:"",
    },
    getters: {},
    mutations:{

    },
    actions:{
        showProblemSolution(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/problem/details/" + data.problemKey +  "/solutionlist/",
                type: 'post',
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                },
                data:{
                    probKey: data.problemKey,
                    page : 1

                },
                success(resp) {
                    console.log(resp)
                    context.state.eachCard = resp.solutionList
                },
                error(resp) {
                    console.log(resp)
                }
            });
        },
        showProblemSolutionDetails(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/problem/details/" + data.problemKey +  "/" + data.solutionKey + "/",
                type: 'post',
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                },
                success(resp) {
                    console.log(resp)
                    context.state.content = resp.content
                    context.state.userName = resp.userName
                    context.state.date = resp.date
                    context.state.language = resp.language

                    data.success()
                },
                error(resp) {
                    console.log(resp)
                }
            });
        },
        updateProblemSolutionDetails(context,data){
            $.ajax({
                url: "http://127.0.0.1:3000/problem/details/" + data.problemKey + '/' + data.solutionKey + "/updatesolution",
                type: 'post',
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                },
                data: {
                    language: data.language,
                    userKey: data.userKey,
                    content: data.content,
                },
                success(resp) {
                    console.log(resp)
                    // context.state.content = resp.content
                    // context.state.userName = resp.userName
                    // context.state.date = resp.date
                    data.success()
                },
                error(resp) {
                    console.log(resp)
                    console.log(data)
                }
            });
        },
        deleteProblemSolutionDetails(context,data){
            $.ajax({
                url: "http://127.0.0.1:3000/problem/details/deletesolution",
                type: 'post',
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                },
                data: {
                    solutionKey:data.solutionKey
                },
                success(resp) {
                    console.log(resp)
                    data.success()
                },
                error(resp) {
                    console.log(resp)
                    console.log(data)
                }
            });
        },
    },
    modules:{}
}