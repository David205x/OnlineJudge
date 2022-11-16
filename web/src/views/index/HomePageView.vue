<template>
    <ContentField>
        <div class="row">
<!--        左侧-->
            <div class ="col-3" style="margin: -3vh 0vw 0vh -1vw;">
<!--            上次做到题目-->
                <ContentField>
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">上次做到</p>
                    <table class="table table-striped table-hover" v-if="store.state.user.is_login">
                        <thead>
                        <tr>
                            <th scope="col">编号</th>
                            <th scope="col"  style="text-align: center">题目名称</th>
                        </tr>
                        </thead>
                        <tbody v-for="(item,i) in impasseProblems" :key="i">
                        <tr @click="redirectToDetail('problem_details',item.problemKey)">
                            <th scope="row" style="font-size: 17px;text-align: center">{{item.problemKey}}</th>
                            <td style="font-size: 17px; text-align: justify">{{item.problemName}}</td>
                        </tr>
                        </tbody>
                    </table>
                    <div  v-else>
                        &nbsp;
                        <p class="text-center fw-light">请登陆后查看</p>
                    </div>
                </ContentField>
<!--            码间一句-->
                <ContentField>
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">码间一句</p>
                    <i class="fa fa-quote-left" style="margin-left: 1vh"></i>
                    <p style="margin-left: 1vh">&nbsp;&nbsp;&nbsp;&nbsp;{{todayWord}}
                        <i class="fa fa-quote-right" style="float: right; margin: 1.5vh 0.5vw 0 0"></i></p>
                    <p></p>
                    <p></p>
                    <div class="row" style="margin-top: 2vh">
                        <div class="col-5" @click="getTodayWord">
                            <i class="fa fa-newspaper-o" style="margin-left: 1vh; color: dimgray">
                                &nbsp;换一句</i>
                        </div>
                        <div class="col-7">
                            <span style="float: right"><code>via hitokoto</code></span>
                        </div>
                    </div>
                </ContentField>
<!--            一码归一码-->
                <ContentField>
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">一码归一码</p>
                    <p style="margin-left: 1vh">不知从何做起? 试试这个!</p>
                    <p class="fw-bold" style="margin-left: 1vh">题号: {{randomProblem}}</p>
                    <p class="fw-bold" style="margin-left: 1vh; margin-top: -0.5vh">语言: {{randomLanguage}}</p>
                    <div class="row">
                        <div class="col-6">
                            <button class="btn btn-light" style="width: 100%"
                                    @click="generateRandom">
                                抽个题写</button>
                        </div>
                        <div class="col-6">
                            <button class="btn btn-outline-primary" style="width: 100%"
                                    @click="redirectToDetail('problem_details',randomProblem)">
                                这就开码</button>
                        </div>
                    </div>

                </ContentField>
            </div>
<!--        中部-->
            <div class ="col-6" style="margin: -3vh 5vw 0vh -2vw">
<!--            又新又好题目-->
                <ContentField style="width: 114.5%">
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">又新又好</p>
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr >
                            <th scope="col" style="text-align: center">题目编号</th>
                            <th scope="col" style="text-align: center">题目名称</th>
                            <th scope="col" style="text-align: center">题目来源</th>
                            <th scope="col" style="text-align: center">题目标签</th>
                        </tr>
                        </thead>
                        <tbody v-for="(item, index) in brandNewProblems" :key="index">
                        <tr @click="redirectToDetail('problem_details',item.problemKey)">
                            <th scope="row" style="font-size: 17px;text-align: center">{{item.problemKey}}</th>
                            <td style="font-size: 17px; text-align: justify">{{item.problemName}}</td>
                            <td style="font-size: 17px; text-align: center">{{item.problemSource}}</td>
                            <td class="center">
                                <span v-for="tag in sliceList(item.problemTags, 2)" :key="tag">
                                    <span class="badge bg-secondary"> {{tag}}</span>
                                    &nbsp;
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </ContentField>
<!--            码的趋势-->
                <ContentField style="width: 114.5%">
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">码的趋势</p>
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th scope="col" style="text-align: center">题目编号</th>
                            <th scope="col" style="text-align: center">题目名称</th>
                            <th scope="col" style="text-align: center">题目来源</th>
                            <th scope="col" style="text-align: center">题目标签</th>
                        </tr>
                        </thead>
                        <tbody v-for="(item, index) in popularProblems" :key="index">
                        <tr @click="redirectToDetail('problem_details',item.problemKey)">
                            <th scope="row" style="font-size: 17px;text-align: center">{{item.problemKey}}</th>
                            <td style="font-size: 17px; text-align: justify">{{item.problemName}}</td>
                            <td style="font-size: 17px; text-align: center">{{item.problemSource}}</td>
                            <td class="center">
                                <span v-for="tag in sliceList(item.problemTags, 2)" :key="tag">
                                    <span class="badge bg-secondary"> {{tag}}</span>
                                    &nbsp;
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </ContentField>
            </div>
<!--        右侧-->
            <div class ="col-3" style="margin: -3vh -2vw 0vh -1vw">
<!--            今日精选题解-->
                <ContentField v-if="!$store.state.user.pulling_infp">
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">今日精选题解</p>
                    <div class="card" style="margin: -1vh -1vh -1vh -1vh">
                        <div class="card-header fw-bold" style="font-size: 20px">{{recommendSolution.solutionkey}}.{{ recommendSolution.title}}</div>
<!--                        <img src="" id="test1" class="card-img-middle" style="width: 16.9vw; height: 17vw" >-->
                        <img v-if="recommendSolution.language === 'c++'"         src="../../assets/code_c++.jpg"     class="card-img-middle" style="width: 16.9vw; height: 17vw"/>
                        <img v-else-if="recommendSolution.language === 'c'"      src="../../assets/code_c.jpg"       class="card-img-middle" style="width: 16.9vw; height: 17vw"/>
                        <img v-else-if="recommendSolution.language === 'java'"   src="../../assets/code_java.jpg"    class="card-img-middle" style="width: 16.9vw; height: 17vw"/>
                        <img v-else-if="recommendSolution.language === 'python'" src="../../assets/code_python.jpg"  class="card-img-middle" style="width: 16.9vw; height: 17vw"/>
                        <img v-else src="../../assets/plane.gif"  class="card-img-middle" style="width: 16.9vw; height: 17vw"/>
                        <div class="card-body" >
                            <h5 class="card-title"
                                style="margin:-1vh -1vh 0vh -1vh; font-size: 17px">
                                贡献者：{{ recommendSolution.username }}</h5>
                            <p class="card-text-2" style="margin:0vh 0vh 1vh -1vh; font-size:17px; white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
                                相关题：{{ recommendSolutionName }}
                            </p>
<!--                            <a @click="solution(recommendSolution)" class="btn btn-primary">查看题解</a>-->
                            <a class="btn btn-outline-primary" @click="redirectToDetail('problem_solution',recommendSolution.solutionkey, recommendSolution.problemkey)"
                               style="width: 110%; margin:0vh 1vh -1vh -1vh;">查看题解</a>
                        </div>
                    </div>
                </ContentField>
<!--            排名榜-->
                <ContentField v-if="!$store.state.user.pulling_infp">
                    <p class="fw-bold" style="font-size: 30px; margin-left: 0.5vw">超级码力榜</p>
                    <table class="table table-hover">
                        <thead>
                        <tr >
                            <th scope="col">码力排名</th>
                            <th scope="col" style="text-align: center">用户名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="table-primary" @click="redirectToDetail('profile_overview',rankingUser1.id)">
                            <th scope="row" style="font-size: 17px">
                                &nbsp;
                                <i class="fa fa-space-shuttle" v-if="rankingUser1.name !== ''"></i>
                                &nbsp;
                                1</th>
                            <td style="text-align: center">{{rankingUser1.name}}</td>
                        </tr>
                        <tr class="table-info" @click="redirectToDetail('profile_overview',rankingUser2.id)">
                            <th scope="row" style="font-size: 17px">
                                &nbsp;
                                <i class="fa fa-car" v-if="rankingUser2.name !== ''"></i>
                                &nbsp;
                                2</th>
                            <td style="text-align: center">{{rankingUser2.name}}</td>
                        </tr>
                        <tr class="table-light" @click="redirectToDetail('profile_overview',rankingUser3.id)">
                            <th scope="row" style="font-size: 17px">
                                &nbsp;
                                <i class="fa fa-motorcycle" v-if="rankingUser3.name !== ''"></i>
                                &nbsp;
                                3</th>
                            <td style="text-align: center">{{rankingUser3.name}}</td>
                        </tr>

                        </tbody>
                    </table>
                </ContentField>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import {useStore} from "vuex";
import {ref} from "vue";
import $ from "jquery";
import router from "@/router";

export default {
    components:{
        ContentField,
    },
    setup(){

        const store = useStore();
        const jwt_token = localStorage.getItem("jwt_token");
        const impasseProblems = ref([{}]);
        const todayWord = ref([{}]);
        const randomProblem = ref([]);
        const randomLanguage = ref([]);
        const brandNewProblems = ref([{}]);
        const popularProblems = ref([{}]);
        const recommendSolution = ref({});
        const recommendSolutionName = ref({});
        const recommendSolutionPicPath = ref();
        const rankingUser1 = ref([{}]);
        const rankingUser2 = ref([{}]);
        const rankingUser3 = ref([{}]);

        const getImpasseProblems = () =>{
            $.ajax({
                url: "http://127.0.0.1:3000/problems/overview/",
                data: {
                    pageNum: "1",
                    userKey: store.state.user.id,
                    searchProblemKey: "",
                    searchProblemName: "",
                    searchProblemTag: "",
                    problemState: 2,
                },
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt_token"),
                },
                type: "post",
                success(resp) {
                    // console.log(resp)
                    impasseProblems.value = resp.problemList

                },
                error(resp) {
                    console.log(resp)
                }
            })
        }
        const getTodayWord = () =>{
            $.ajax({
                url: "https://v1.hitokoto.cn/?c=d&encode=text",
                dataType: "jsonp",
                type: "GET",
                jsonp: "callback",
                jsonpCallback: "hitokoto",

                success(data) {
                    $(todayWord).attr("herf", "https://hitokoto.cn/?uuid=" + data.uuid);
                    $(todayWord).text(data.hitokoto);
                    todayWord.value = data;
                    console.log("getTodayWord",data);
                },
                error(jqXHR, textStatus, errorThrown) {
                    console.error(textStatus, errorThrown);
                }
            })
        }
        const generateRandom = () =>{
            let languages = ["C","C++","Java","Python"];
            randomLanguage.value = languages[Math.floor(Math.random()*4)];
            randomProblem.value = Math.floor(Math.random()*15)+1;
        }
        const getBrandNewProblems = () =>{
            $.ajax({
                url: "http://127.0.0.1:3000/problems/brandnewproblem/",
                data: {
                    isAscending: "true",
                },
                type: "post",
                success(resp) {
                    console.log("getBrandNewProblems",resp.problemList);
                    brandNewProblems.value = resp.problemList;
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }
        const getPopularProblems = () =>{
            $.ajax({
                url: "http://127.0.0.1:3000/problems/brandnewproblem/",
                data: {
                    isAscending: "false",
                },
                type: "post",
                success(resp) {
                    console.log("getPopularProblems",resp.problemList);
                    popularProblems.value = resp.problemList;
                },
                error(resp) {
                    console.log("getPopularProblems",resp)
                }
            })
        }
        const getRecommendSolution = () =>{
            $.ajax({
                url: "http://127.0.0.1:3000/problem/details/randomsolution/",
                type: "post",
                success(resp) {
                    recommendSolution.value = resp.solution;
                    recommendSolutionName.value = resp.problemName;
                    if(recommendSolution.value.language === "c") recommendSolutionPicPath.value = "../../assets/code_c.jpg";
                    else if(recommendSolution.value.language === "cpp") recommendSolutionPicPath.value = "../../assets/code_c++.jpg";
                    else if(recommendSolution.value.language === "java") recommendSolutionPicPath.value = "../../assets/code_java.jpg";
                    else if(recommendSolution.value.language === "python") recommendSolutionPicPath.value = "../../assets/code_python.jpg";
                    console.log("getRecommendSolution:",resp.solution)
                    console.log(recommendSolutionPicPath.value)
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }
        const getRankingUsers = () =>{
            $.ajax({
                url: "http://127.0.0.1:3000/user/ranking/",
                data: {
                    count: 3
                },
                type: "post",
                success(resp) {
                    rankingUser1.value = resp.userList[0];
                    rankingUser2.value = resp.userList[1];
                    rankingUser3.value = resp.userList[2];
                    console.log(resp.userList)

                },
                error(resp) {
                    console.log(resp)
                }
            })
        }
        const redirectToDetail = (target, id, id2) =>{
            console.log("redirecToDetail",target, id, id2);
            if(target === "problem_details")
                router.push({name: target, params: {id : id}});
            else if(target === "problem_solution")
                router.push({name: target, params: {solutionKey : id, problemKey : id2}});
            else if(target === "profile_overview" && id !== 0){
                store.commit("updateVisit", {userKey : id});
                router.push({name: "profile_overview"});
            }
            setTimeout(() =>{
                location.reload()
            }, 0)
        }
        // console.log(document.getElementById("test1"))

        getTodayWord();
        generateRandom();
        getBrandNewProblems();
        getPopularProblems();
        getRecommendSolution();
        getRankingUsers();

        if(jwt_token){
            store.commit("updateToken", jwt_token);
            store.dispatch("getInfo", {
                success(){
                    getImpasseProblems()
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        }else {
            store.commit("updatePullingInfo", false);
        }



        return {
            store,
            impasseProblems,
            todayWord,
            randomProblem,
            randomLanguage,
            brandNewProblems,
            popularProblems,
            recommendSolution,
            recommendSolutionName,
            recommendSolutionPicPath,
            rankingUser1,
            rankingUser2,
            rankingUser3,

            redirectToDetail,
            getTodayWord,
            generateRandom
        }
    },

    computed: {
        sliceList() {
            return function (data,count) {
                if (data != undefined) {
                    let arrTemp = [];
                    let i = 0;
                    let tag = {};
                    for (tag in data) {
                        i++;
                        if (i > count){
                            break;
                        }
                        arrTemp.push(data[tag])
                    }
                    return arrTemp
                }
            }
        }
    }

}
</script>
<style scoped>

</style>