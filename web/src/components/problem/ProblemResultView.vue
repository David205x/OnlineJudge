<template>
  <ContentField>
    <div>
      <nav aria-label="...">
        <ul class="pagination" style="float: right;">
          <li class="page-item" @click="click_page(-2)">
            <a class="page-link" href="#">前一页</a>
          </li>
          <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
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
          <th scope="col">Num<br></th>
          <th class="username sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            Username<br>
            <input type="text" id="un" name="un" class="search_text" style="width:100%">
          </th>
<!--          <th class="prob sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            Prob<br>
            <input type="text" id="probunm" name="probunm" class="search_text" style="width:100%">
          </th>-->
          <th class="status sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            Result
            <br>
            <select id="res" name="res" class="custom-select">
              <option value="0">All</option>
              <option value="1">Accepted</option>
              <option value="2">Presentation Error</option>
              <option value="3">Wrong Answer</option>
              <option value="4">Time Limit Exceed</option>
              <option value="5">Memory Limit Exceed</option>
              <option value="6">Output Limit Exceed</option>
              <option value="7">Runtime Error</option>
              <option value="8">Compile Error</option>
              <option value="9">Judge Failed</option>
              <option value="10">Unknown Error</option>
              <option value="11">Submit Error</option>
              <option value="12">Queuing &amp;&amp; Judging</option>
            </select>
          </th>
          <th scope="col">Time<br>(ms)</th>
          <th class="language hidden-lg-down sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
            Lang
            <br>
            <select name="language" id="language" class="custom-select">
              <option value="">All</option>
              <option value="C++">C++</option>
              <option value="C">C</option>
              <option value="JAVA">Java</option>
              <option value="PYTHON">Python</option>
            </select>
          </th>
          <th scope="col">Submit Time<br></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for ="submissionOverview in SubmissionOverview" :key = "submissionOverview.submissionKey">
          <th scope="row">{{submissionOverview.submissionKey}}</th>
<!--          <td @click="todetails(submissionOverview.username)">-->
          <td>{{submissionOverview.userName}}</td>
          <td>{{submissionOverview.result}}</td>
          <td>{{submissionOverview.timeUsed}}</td>
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
import {ref} from 'vue'
import $ from "jquery";

export default{
  components: {ContentField },
  setup(){
    const initpage = () =>{
      router.push({name: "problem_result_2"});
    }
    let SubmissionOverview = ref([{}]);
    let r = window.location.href.match("problemId=.*/");
    let t = r[0].split("=")[1].split("/")[0];
    let pages = ref([]);
    const store = useStore();
    const logged = store.state.user.is_login;
    store.commit("updatePullingInfo", false);
    if(store.state.user.is_login){
      store.dispatch("getinfoInMainPage", {
        success(){
          console.log(store.state.user);
        }
      })
    }
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
          console.log(resp);
          update_pages()
        },
        error(resp) {
          console.log(resp)
        }
      })
    }
    pull_page(current_page)
    return {
      logged,
      initpage,
      SubmissionOverview,
      click_page,
    }
  },
  computed: {
    sliceList() {
      return function (data,count) {
        if (data != undefined) {
          let arrTemp = [];
          console.log(data)
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