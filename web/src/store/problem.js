
// TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER

import $ from 'jquery'

export default{
    state:{
        problemKey: "",
        problemDescription: "",
    },
    getters: {},
    mutations:{
        updateProblem(state, problem) {
            state.problemKey = problem.problemKey;
            state.problemDescription = problem.problemDescription;
        }
    },
    actions:{
        showProblem(context, data) {
            console.log( context)
            $.ajax({
                url: "http://127.0.0.1:3000/problems/test/",
                type: 'post',
                success(resp) {
                    if (resp.problemBody !== undefined) {
                        console.log(resp.problemBody)
                        context.commit("updateProblem", {
                            problemDescription : resp.problemBody,
                            problemKey : resp.problemKey
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