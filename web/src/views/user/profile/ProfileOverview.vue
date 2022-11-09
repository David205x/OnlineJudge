<template>
  <ContentField>
    <div class="container" v-if="$store.state.user.pulling_info == false">
        <div class="row"> <!--   这他妈为啥是row  -->
<!--      他人主页左栏-->
          <div class="col-2" v-if="is_visit != -1">
              <div class="row">
                  <img :src="visitPhoto" class="rounded float-start" :alt="visitPhoto">  
              </div>
              <div class="row" style="margin-top: 5vh">
                  <p class="text-center">{{visitUsername}}</p>
                  <p class="fw-light" style="font-size: 15px; margin-top: 0px">Mys Institution<br><br></p>
                  <button type="button" class="btn btn-secondary">Edit Profile</button>
                  <button type="button" class="btn btn-secondary" @click="chat" v-if="$store.state.user.id != is_visit">Chat</button>
              </div>
          </div>
<!--      自己主页左栏-->
          <div class="col-2" v-if="is_visit == -1">
            <div class="row">
              <img src="../../../assets/logo.png" class="rounded float-start" alt="../../../assets/logo.png">
            </div>
            <div class="row" style="margin-top: 5vh">
              <p class="text-center">{{$store.state.user.username}}</p>
              <p class="fw-light" style="font-size: 15px; margin-top: 0px">Mys Institution<br><br></p>
              <button type="button" class="btn btn-secondary">Edit Profile</button>
            </div>
          </div>
          <div class="col-1"></div>
<!--      右栏-->
          <div class="col" style="font-size: 15px">
<!--          导航栏-->
              <nav>
                  <div class="nav nav-tabs" id="nav-tab" role="tablist">
                      <button class="nav-link active" id="nav-home-tab" data-bs-toggle="tab" data-bs-target="#nav-home" type="button" role="tab" aria-controls="nav-home" aria-selected="true">信息概览</button>
                      <button @click="tab(1)" class="nav-link" id="nav-profile-tab" data-bs-toggle="tab" data-bs-target="#nav-profile" type="button" role="tab" aria-controls="nav-profile" aria-selected="false">
                        <span v-if="is_visit == -1">我的提交</span>
                        <span v-else>ta的提交</span>
                      </button>
                      <button @click="tab(2)" class="nav-link" id="nav-contact-tab" data-bs-toggle="tab" data-bs-target="#nav-contact" type="button" role="tab" aria-controls="nav-contact" aria-selected="false">
                        <span v-if="is_visit == -1">我的题解</span>
                        <span v-else>ta的题解</span>
                    </button>
                  </div>
              </nav>
              <br>
<!--          导航栏内容-->
              <div class="tab-content" id="nav-tabContent">
<!--              概览-->
                  <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                    <content-field v-if="is_visit == -1" style="font-size: 10px">
                        <calendar-heatmap
                            :values="submissionList"
                            :end-date="endDate"
                            max="12"
                            round="3"
                            :range-color="['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39']"
                            locale="right"
                        />
                    </content-field>
                    <content-field v-else style="font-size: 10px">
                        <calendar-heatmap
                            :values="visitSubmissionList"
                            :end-date="endDate"
                            max="12"
                            round="3"
                            :range-color="['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39']"
                            locale="right"
                        />
                    </content-field>
                  </div>
<!--              做题情况-->
                  <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                      <div class="dropdown" style="margin: 2vh 0vh 0vh 2vh">
                          <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                              Dropdown button
                          </button>
                          <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                              <li><a class="dropdown-item" href="#">Action</a></li>
                              <li><a class="dropdown-item" href="#">Another action</a></li>
                              <li><a class="dropdown-item" href="#">Something else here</a></li>
                          </ul>
                      </div>
                      <nav aria-label="...">
                        <ul class="pagination" style="float: right;">
                          <li class="page-item" @click="click_page(-2)">
                            <a class="page-link" href="#">前一页</a>
                          </li>
                          <li :class="'page-item ' + page.is_active && !$store.state.user.isChatOpen ? page.is_active : ''" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                            <a class="page-link" href="#">{{ page.number }}</a>
                          </li>
                          <li class="page-item" @click="click_page(-1)">
                            <a class="page-link" href="#">下一页</a>
                          </li>
                        </ul>
                      </nav>
                      <table class="table table-striped table-hover" style="margin-top: 2vh">
                        <thead>
                        <tr>
                            <th scope="col">题目编号</th>
                            <th scope="col">题目名称</th>
                            <th scope="col">作答状态</th>
                            <th scope="col">提交次数</th>
                            <th scope="col">题目标签</th>

                        </tr>
                        </thead>
                        <tbody v-for="(item,i) in SubmissionListForOnes" :key="i">
                        <tr>
                            <th scope="row">{{item.problemKey}}</th>
                            <td>{{item.problemName}}</td>
                            <td style="color: #cf4e4e">{{item.result}}</td>
                            <td>{{item.submitTime}}</td>
                            <td >
                                <span v-for="tag in sliceList(item.label, 3)" :key="tag">
                                    <span class="badge bg-secondary"> {{tag}}</span>
                                    &nbsp;
                                  </span>
                            </td>
                        </tr>
                        </tbody>
                      </table>
                  </div>
<!--              题解贡献-->
                  <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                    <nav aria-label="...">
                      <ul class="pagination" style="float: right;">
                        <li class="page-item" @click="click_page(-2)">
                          <a class="page-link" href="#">前一页</a>
                        </li>
                        <li :class="'page-item ' + page.is_active && !$store.state.user.isChatOpen ? page.is_active : ''" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                          <a class="page-link" href="#">{{ page.number }}</a>
                        </li>
                        <li class="page-item" @click="click_page(-1)">
                          <a class="page-link" href="#">下一页</a>
                        </li>
                      </ul>
                    </nav>
                    <table class="table table-striped table-hover" style="margin-top: 2vh">
                      <thead>
                      <tr style="font-size: 17px">
                        <th scope="col">题目编号</th>
                        <th scope="col">题目名称</th>
                        <th scope="col">题解语言</th>
                        <th scope="col">题解时间</th>
                        <th scope="col" style="text-align: center">操作</th>

                      </tr>
                      </thead>
                      <tbody v-for="(item,i) in SubmissionListForOnes" :key="i">
                      <tr style="font-size: 15px">

                        <th scope="row">{{item.problemKey}}</th>
                        <th scope="row">{{item.problemName}}</th>
                        <td style="color: #cf4e4e">{{item.language}}</td>
                        <td>{{item.date}}</td>
                        <td>
                          <div class="col" style="text-align:center;font-size: 15px">
                            <button @click="ToSolution(item)" class="btn btn-primary">
                                查看
                            </button>
                            &nbsp;
                            <button @click="UpdateSolution(item)" class="btn btn-primary" >
                                修改
                            </button>
                            &nbsp;
                            <button @click="DeleteSolution(item)" class="btn btn-primary">
                                删除
                            </button>

                          </div>
                        </td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
              </div>

          </div>
        </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { CalendarHeatmap } from 'vue3-calendar-heatmap'
import { useStore } from 'vuex'
import { ref } from 'vue'
import $ from 'jquery'
import router from "@/router"

export default {
  name: "ProfileDetailView",
  components: {
    ContentField,
    CalendarHeatmap
  },
  setup(){
    
    const store = useStore();
    const jwt_token = localStorage.getItem("jwt_token");
    
    let date = new Date()
    let endDate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
    let submissionList = ref([]);
    let visitSubmissionList = ref([]);
    let is_visit = ref(-1);
    let visitUsername = ref("");
    let visitPhoto = ref("");
    let pages = ref([]);
    let SubmissionListForOnes = ref([]);
    is_visit = ref(localStorage.getItem("is_visit"));
    let current_page = 1;
    let total_problems = 0;
    let per_num = 1;
    let whichTab = ref(1)
    const chat = () => {
      store.commit("isMyFriend",{
        senderkey : is_visit.value,
        success(resp){
          if(!resp){

            store.commit("addFriend", {
              userName : is_visit.value == -1 ? store.state.user.username : visitUsername.value,
              userKey : is_visit.value == -1 ? store.state.user.id : is_visit.value 
            })
            store.commit("addEmptyContent")
          }
          setTimeout(() =>{
              router.push({name : "chatting_list"})
          }, 10)
          store.dispatch("submitFriends", {
              userKey : store.state.user.id,
              userName : store.state.user.username,
              friends : store.state.user.friends,
              success(){

              }
          })
        }
      })
      
    }
    const ToSolution =(item) =>{
      console.log(item)

      store.dispatch("showProblemSolutionDetails", {
        problemKey : item.problemKey,
        solutionKey : item.solutionKey,
        success(){
          store.dispatch("showProblem", {
            problemKey : item.problemKey,
            success(resp){

              console.log(resp)
              setTimeout(() =>{
                router.push({name: "problem_solution", params:{solutionKey : item.solutionKey, problemKey : item.problemKey}});
              }, 10)
            }
          })

        }
      })
    }
    const UpdateSolution =(item) =>{
      store.dispatch("showProblemSolutionDetails", {
        problemKey : item.problemKey,
        solutionKey : item.solutionKey,
        success(){
          store.dispatch("showProblem", {
            problemKey : item.problemKey,
            success(resp){
              console.log(resp)
              setTimeout(() =>{
                router.push({name: "problem_details", params:{ id : item.problemKey, solutionKey : item.solutionKey, tabId : 3}});

              }, 100)
            }
          })

        }
      })

    }
    const DeleteSolution =(item) =>{
        store.dispatch("deleteProblemSolutionDetails", {
          solutionKey: item.solutionKey,
          success(){
            window.location.reload()
          }
        })
    }
    const click_page = (page) =>{
      if(page == -2) page = current_page - 1;
      else if(page == -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_problems / per_num));

      if(page >= 1 && page <= max_pages){
        pull_page(page);
      }
    }
    const tab = (num) =>{
      whichTab.value = num;
      pull_page(1)
    }
    const update_pages = () =>{
      let max_pages = parseInt(Math.ceil(total_problems / per_num));
      let new_pages = [];

      for(let i = current_page - 2; i <= current_page + 2; i++){
        if(i >= 1 && i <= max_pages){
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages
    }
    const pull_page = (page) =>{
      current_page = page;
      if(whichTab.value == 1){
        $.ajax({
          url: "http://127.0.0.1:3000/problem/details/sublist/",
          type: 'post',
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          data:{
            userKey: is_visit.value == -1 ? store.state.user.id : is_visit.value,
            page: JSON.stringify(page),
          },
          success(resp) {
            SubmissionListForOnes.value = resp.submissionList;
            total_problems = resp.totalPages;
            per_num = resp.perPage;
            update_pages()
          },
          error(resp) {
            console.log(resp)
          }
        })
      }else{
        $.ajax({
          url: "http://127.0.0.1:3000/problem/details/" + store.state.user.id + "/ones/solutionlist/",
          type: 'post',
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          data:{
            userKey: is_visit.value == -1 ? store.state.user.id : is_visit.value,
            page: JSON.stringify(page),
          },
          success(resp) {
            SubmissionListForOnes.value = resp.solutionList;
            total_problems = resp.totalPages;
            per_num = resp.perPage;
            update_pages()
          },
          error(resp) {
            console.log(resp)
          }
        })
      }


    }
    if(jwt_token){
        store.commit("updateToken", jwt_token);
        store.dispatch("getInfo", {
            success(){
                if(is_visit.value == -1){
                    $.ajax({
                        url: "http://127.0.0.1:3000/user/submission/heatmap/",
                        type: 'post',
                        data:{
                            userKey: store.state.user.id
                        },
                        headers: {
                            Authorization: "Bearer " + jwt_token,
                        },
                        success(resp) {
                            submissionList.value = resp.submissionList
                            pull_page(current_page)
                            store.commit("updatePullingInfo", false);
                        },
                        error(resp) {
                            console.log(resp);
                        }
                    });
                }
                else {
                    store.dispatch("getOthers", {
                        userKey : localStorage.getItem("is_visit")
                    })
                    $.ajax({
                        url: "http://127.0.0.1:3000/user/submission/heatmap/",
                        type: 'post',
                        data:{
                            userKey: localStorage.getItem("is_visit")
                        },
                        headers: {
                            Authorization: "Bearer " + jwt_token,
                        },
                        success(resp) { 
                            visitSubmissionList.value = resp.submissionList
                            visitUsername.value = localStorage.getItem("visitUsername");
                            visitPhoto.value = localStorage.getItem("visitPhoto");
                            pull_page(current_page)
                            store.commit("updatePullingInfo", false);
                        },
                        error(resp) {
                            console.log(resp);
                        }
                    });
                }

            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })

    }else {
        store.commit("updatePullingInfo", false);
        pull_page(current_page)
    }

    return{
        submissionList,
        visitSubmissionList,
        chat,
        endDate,
        is_visit,
        visitPhoto,
        visitUsername,
        click_page,
        pages,
        SubmissionListForOnes,
        whichTab,
        tab,
        UpdateSolution,
        ToSolution,
        DeleteSolution,
    }
  },
  computed: {
    sliceList() {
      return function (data, count) {
        if (data != undefined) {
          let arrTemp = [];
          let i = 0;
          for (i in data) {
            if (i >= count){
              break;
            }
            arrTemp.push(data[i])
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