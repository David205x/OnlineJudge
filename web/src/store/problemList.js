export default {
    state:{
        problemKey: "",
        problemName:"",
        problemSource: "",
        AcceptedPct: "",
        problemTags: "",
    },
    getters:{},
    mutations:{
        updateProblemList(state, problem) {
            state.problemKey = problem.problemKey;
            state.problemName = problem.problemName;
            state.problemSource = problem.problemSource;
            state.AcceptedPct = problem.AcceptedPct;
            state.problemTags = problem.problemTags;
   
        }
    },
    actions:{},
    modules:{

    }
}