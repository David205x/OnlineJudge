
// TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER


import $ from 'jquery'

export default{
    state:{
        problemKey: "",
        problemDescription: "",
        debugOutcome:"",
        problemName:"",
        problemDifficulty:"",
        acceptedAttempts:"",
        totalAttempts:"",
        problemTags: "",
    },
    getters: {},
    mutations:{
        updateProblem(state, problem) {
            state.problemKey = problem.problemKey;
            state.problemDescription = problem.problemDescription;
            state.debugOutcome = null;
            state.problemName = problem.problemName;
            state.problemDifficulty = problem.problemDifficulty;
            state.acceptedAttempts = problem.acceptedAttempts;
            state.totalAttempts = problem.totalAttempts;
            state.problemTags = JSON.parse(problem.problemTags);
        },
        updataDebugOutcome(state, outcome){
            state.debugOutcome = outcome
        }
    },
    actions:{
        showProblem(context, data) {
            console.log( context)
            $.ajax({
                url: "http://127.0.0.1:3000/problems/test/",
                type: 'post',
                data:{
                    probKey: data.problemKey
                },
                success(resp) {
                    if (resp.problemBody !== undefined) {
                        console.log(resp)
                        context.commit("updateProblem", {
                            problemDescription : resp.problemBody,
                            problemKey : resp.problemKey,
                            problemName : resp.problemName,
                            problemDifficulty : resp.problemDifficulty,
                            acceptedAttempts : resp.acceptedAttempts,
                            totalAttempts : resp.totalAttempts,
                            problemTags : resp.problemTags,
                        })
                        data.success(resp)

                    }
                },
                error(resp) {
                    console.log(resp)
                }
            });
        },
    },
    modules:{}
}