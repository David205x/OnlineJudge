

import $ from 'jquery'

export default{
    state:{
        problemKey: "",
        problemDescription: "",
        debugOutcome:"",
        problemName:"",
        problemDifficulty:"",
        problemTimeLimit: "",
        problemMemoryLimit: "",
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
            state.problemTimeLimit = problem.problemTimeLimit;
            state.problemMemoryLimit = problem.problemMemoryLimit;
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
            $.ajax({
                url: "http://127.0.0.1:3000/problems/show/",
                type: 'post',
                data:{
                    probKey: data.problemKey
                },
                success(resp) {
                    if (resp.problemBody !== undefined) {
                        context.commit("updateProblem", {
                            problemDescription : resp.problemBody,
                            problemKey : resp.problemKey,
                            problemName : resp.problemName,
                            problemTimeLimit : resp.problemTimeLimit,
                            problemMemoryLimit : resp.problemMemoryLimit, 
                            problemDifficulty : resp.problemDifficulty,
                            acceptedAttempts : resp.acceptedAttempts,
                            totalAttempts : resp.totalAttempts,
                            problemTags : resp.problemTags,
                        })
                        data.success(resp)

                    }else {
                        console.log(resp)
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