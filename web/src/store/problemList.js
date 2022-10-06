export default {
    state:{
        problemKey: "",
        problemName:"",
    },
    getters:{},
    mutations:{
        updateProblemList(state, problem) {
            state.problemKey = problem.problemKey;
            state.problemName = problem.problemName;
        }
    },
    actions:{},
    modules:{

    }
}