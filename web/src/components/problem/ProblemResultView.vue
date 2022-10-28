<template>
  <ContentField>
    <div>
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
    </div>

      <table id="listStatus" class="table table-striped table-hover">
        <thead>
        <tr>
          <th scope="col">提交编号<br></th>
          <th class="username sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            用户名<br>
            <input v-model="searchUsername" type="text" id="un" name="un" class="search_text" style="width:100%" @input="search()">
          </th>
          <th class="status sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            运行结果
            <br>
            <select  v-model="searchResult" id="res" name="res" class="custom-select" @click="search()">
              <option value="">All</option>
              <option value="Accepted">Accepted</option>
              <option value="WrongAnswer">Wrong Answer</option>
              <option value="TimeLimitExceeded">Time Limit Exceeded</option>
              <option value="MemoryLimitExceeded">Memory Limit Exceeded</option>
              <option value="CompileError">Compile Error</option>
              <option value="Others">Others</option>
            </select>
          </th>
          <th scope="col">运行时间<br>(ms)</th>
          <th class="language hidden-lg-down sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            语言
            <br>
            <select v-model="searchLang" name="language" id="language" class="custom-select" @click="search()">
              <option value="">All</option>
              <option value="cpp">C++</option>
              <option value="c">C</option>
              <option value="java">Java</option>
              <option value="python">Python</option>
            </select>
          </th>
          <th scope="col">提交时间<br></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for ="submissionOverview in SubmissionOverview" :key = "submissionOverview.submissionKey">
          <th scope="row">{{submissionOverview.submissionKey}}</th>
          <td @click="visitOther(submissionOverview)">{{submissionOverview.userName}}</td>
          <td>{{submissionOverview.result}}</td>
          <td v-if="submissionOverview.timeUsed <= 0">---</td>
          <td v-if="submissionOverview.timeUsed > 0">{{submissionOverview.timeUsed}}</td>
          <td>{{submissionOverview.lang}}</td>
          <td>{{submissionOverview.date}}</td>
        </tr>
        </tbody>
      </table>
  </ContentField>

</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import router from "@/router";
import {nextTick, ref} from 'vue'
import $ from "jquery";

export default{
  components: { ContentField },
  setup(){
    const initpage = () =>{
      router.push({name: "problem_result_2"});
    }
    let SubmissionOverview = ref([{}]);
    let r = window.location.href.match("problemId=.*/");
    let t = r[0].split("=")[1].split("/")[0];
    let pages = ref([]);

    let searchResult = ref('');
    let searchLang = ref('');
    let searchUsername = ref('');
    const store = useStore();
    const logged = store.state.user.is_login;

    store.commit("updatePullingInfo", false);
    if(store.state.user.is_login){
      store.dispatch("getinfoInMainPage", {
        success(){
          
        }
      })
    }
    const search = () =>{
      
      $.ajax({
        url: "http://127.0.0.1:3000/problem/details/" + t + "/sublist/",
        type: 'post',
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data:{
          userName: searchUsername.value,
          result: searchResult.value,
          lang: searchLang.value,
          page: '1',
        },
        success(resp) {
          SubmissionOverview.value = resp.submissionList;
          console.log(SubmissionOverview.value[0].userKey)
          total_problems = resp.totalPages;
          per_num = resp.perPage;
          current_page = 1;
          update_pages()
        },
        error(resp) {
          console.log(resp)
        }
      })
    }
    nextTick(()=>{
      document.onkeydown = function(e){
        var ev = document.all ? window.event : e;
        if(ev.keyCode==13) {
          search()
          return false;
        }
      }
    })
    let current_page = 1;
    let total_problems = 0;
    let per_num = 1;

    const click_page = (page) =>{
      if(page == -2) page = current_page - 1;
      else if(page == -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_problems / per_num));

      if(page >= 1 && page <= max_pages){
        pull_page(page);
      }
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
      $.ajax({
        url: "http://127.0.0.1:3000/problem/details/" + t + "/sublist/",
        type: 'post',
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data:{
          userName: "",
          result: "",
          lang: "",
          page: JSON.stringify(page),
        },
        success(resp) {
          SubmissionOverview.value = resp.submissionList;
          total_problems = resp.totalPages;
          per_num = resp.perPage;
          update_pages()
        },
        error(resp) {
          console.log(resp)
        }
      })
    }
    pull_page(current_page)
    const visitOther = (user) =>{
        store.commit("updateVisit", {
            userKey : user.userKey,
        })
        router.push({name: "profile_overview"});
    }
    return {
      logged,
      initpage,
      SubmissionOverview,
      pages,
      searchResult,
      searchLang,
      searchUsername,
      click_page,
      search,
      visitOther,
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
          while(i < count){
            arrTemp.push('')
            i++;
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