<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="container" style="font-size: 17px">
            <div class="row align-items-start">
<!--            左栏-->
                <div class="col-4 left">
<!--                题目描述-->
                    <md-editor
                        v-model = $store.state.problem.problemDescription
                        previewOnly = true>
                    </md-editor>
                </div>
<!--            右栏-->
                <div class="col-8 right">
<!--                工具栏-->
                    <div class="row" style="margin-top: 1vh">
                        <div class="col-8">
                            <div class="input-group mb-3">
                                <input type="text" class="form-control" placeholder="题解标题" v-model=solution_title>
                                <select class="form-select" aria-label="Default select example" v-model=language_selected>
                                    <option value="cpp" selected>C++</option>
                                    <option value="c">C</option>
                                    <option value="python">Python</option>
                                    <option value="java">Java</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-2" style="margin: 0vw -1vw 0vw 1vw">
                            <button type="button" class="btn btn-outline-primary pull-right" disabled style="width: 100px" >保存</button>
                        </div>
                        <div class="col-2">
                            <button type="button" class="btn btn-primary pull-right" style="width: 100px" @click="submit">提交</button>
                        </div>
                    </div>
<!--                题解编辑-->
                    <md-editor style="height: 72vh"
                        v-model = md_problem>
                    </md-editor>
                </div>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { ref, reactive, onMounted } from "vue"
import { marked } from "marked";
import ace from 'ace-builds';
import 'font-awesome/css/font-awesome.min.css';
import 'md-editor-v3/lib/style.css';
import MdEditor from 'md-editor-v3'
import $ from 'jquery'
import router from "@/router";

ace.config.set(
    "basePath",
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

export default{
    name: "ProblemEditorialEditorHorizontal",
    components: {
        ContentField,
        MdEditor,
    },

    setup(){

        onMounted(() =>{
        })
        const store = useStore();
        const code = reactive({
            content: "",
        });
        let spinner_cog = ref(0);
        let submission_status = ref('?');
        let solution_content = ref('');
        let language_selected = ref(store.state.solution.language);
        let solution_title = ref(store.state.solution.title);
        let md_problem = ref('');
        let beforeProblemKey = window.location.href.match("problemId=.*/");
        let beforeSolutionKey = window.location.href.match("solutionKey=.*/");

        const spinnerChangeCog = (data) =>{
            spinner_cog.value = data;
        }
        const compiledMarkdown = () => {
            return marked.parse(store.state.problem.problemDescription);
        }
        const refresh = () =>{
            code.content = ""
        }
        const submit = () =>{
            console.log(md_problem.value)
            console.log(language_selected.value)
            console.log(store.state.user)
            console.log(store.state.problem.problemKey)
            console.log(solution_title)
            if(beforeSolutionKey[0].split("=")[1].split("/")[0] == ''){
                console.log("111")
                $.ajax({
                    url: "http://127.0.0.1:3000/problem/details/" + store.state.problem.problemKey + "/addsolution",
                    type: 'post',
                    data: {
                        userKey : store.state.user.id,
                        title : solution_title.value,
                        content : md_problem.value,
                        language: language_selected.value,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp){

                        console.log(resp)
                        location.reload()
                    }
                })
            }
            else{
                console.log("222")
                store.dispatch("updateProblemSolutionDetails", {
                    problemKey :  beforeProblemKey[0].split("=")[1].split("/")[0],
                    solutionKey : beforeSolutionKey[0].split("=")[1].split("/")[0],
                    language: language_selected.value,
                    userKey: store.state.user.id,
                    content: md_problem.value,
                    success(){
                        console.log(md_problem.value)
                        console.log(language_selected.value)
                        language_selected = ref(store.state.solution.language);
                        md_problem = ref(store.state.solution.content);
                        location.reload()
                    }
                })
            }
        }
        const permissionCheck = () =>{
            console.log("problem: ","-",store.state.problem.problemKey,
                "\nsolution: ","-",store.state.solution.solutionKey,
                "\nuser: ","-",store.state.solution.userKey)
            if(store.state.user.id != store.state.solution.userKey){
                router.push({name:"problem_solution",params:{
                    problemKey : store.state.problem.problemKey,
                    solutionKey : store.state.solution.solutionKey,
                    tabId : 1
                }})
            }
        }

        store.commit("updatePullingInfo", true);
        md_problem.value = store.state.solution.content;
        store.dispatch("showProblemSolutionDetails", {
            problemKey :  beforeProblemKey[0].split("=")[1].split("/")[0],
            solutionKey : beforeSolutionKey[0].split("=")[1].split("/")[0],
            success(){
                // language_selected.value = store.state.solution.language;
                // md_problem = ref(store.state.solution.content);
                permissionCheck();
            }
        })
        store.dispatch("showProblem", {
            success(){
                store.commit("updatePullingInfo", false);
            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })
        store.commit("updatePullingInfo", false);

        return {
            spinner_cog,
            compiledMarkdown,
            spinnerChangeCog,
            solution_title,
            code,
            submission_status,
            refresh,
            md_problem,
            language_selected,
            submit,
            solution_content
        }
    },
    computed:{


    },
}
</script>


<style scoped>
.col-4.left{
    height: 80vh;
    overflow-y: scroll;
}
.col-8.right{
    height: 80vh;
    overflow-y: scroll;
}
.form-select{
    width: 5vw;
}
</style>