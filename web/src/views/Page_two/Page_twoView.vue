<template>
  <ContentField>
    <span class="fs-1">
      题目列表
    </span>
    <br>
    <div class="row g-3 align-items-center">
      <div class="col-auto">
        <lbel for="inputProblemNum" class="col-form-label">题目编号</lbel>
      </div>
      <div class="col-auto">
        <input v-model="search_problem_key" type="problemNum" id="search_problem_num" class="form-control" aria-describedby="passwordHelpInline">
      </div>
      <div class="col-auto">
        <lbel for="inputProblemName" class="col-form-label">题目名称</lbel>
      </div>
      <div class="col-auto">
        <input v-model="search_problem_name" type="problemName" id="search_problem_name" class="form-control" aria-describedby="passwordHelpInline">
      </div>
      <div class="col-auto">
        <lbel for="inputProblemName" class="col-form-label">题目标签</lbel>
      </div>
      <div class="col-auto">
        <input v-model="search_problem_tag" type="problemTag" id="search_problem_tag" class="form-control" aria-describedby="passwordHelpInline">
      </div>
    </div>
    <div class="container">
      <div class="row align-items-start">

        <div class="col-9">
          <table class="table table-striped table-hover">
            <thead>
            <tr >
              <th scope="col">题目编号</th>
              <th scope="col">题目名称</th>
              <th scope="col">题目来源</th>
              <th scope="col">通过率</th>
              <th scope="col" style="text-align: center;">题目标签</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for ="problemOverview in problemOverviews" :key = "problemOverview.problemKey">
              <th scope="row">{{problemOverview.problemKey}}</th>
              <td @click="todetails(problemOverview.problemKey)">{{problemOverview.problemName}}</td>
              <td>{{problemOverview.problemSource}}</td>
              <td v-if="problemOverview.AcceptedPct < 30">
                <span class="badge bg-danger">{{problemOverview.AcceptedPct}}%</span>
              </td>
              <td v-if="problemOverview.AcceptedPct >= 30 && problemOverview.AcceptedPct < 70">
                <span class="badge bg-warning">{{problemOverview.AcceptedPct}}%</span>
              </td>
              <td v-if="problemOverview.AcceptedPct >= 70">
                <span class="badge bg-success">{{problemOverview.AcceptedPct}}%</span>
              </td>
              <td>
                <span v-for="tag in sliceList(problemOverview.problemTags, 5)" :key="tag">
                  <span class="badge bg-secondary"> {{tag}}</span>	
                  &nbsp;																																	
                </span>
              </td>
            </tr>
            </tbody>
          </table>
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
         
        </div>
        <div class="col-3">
          <div class="list-group">
            <button @click="ProblemChange(0)" type="button" :class="state == 0 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'" :aria-current="state == 0">
              全部题目
            </button>
            <button @click="ProblemChange(1)" type="button" :class="state == 1 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'" :aria-current="state == 1">
              已通过
            </button>
            <button @click="ProblemChange(2)" type="button" :class="state == 2 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'" :aria-current="state == 2">
              已尝试
            </button>
          </div>
          <div class="label2">
          <span>
            根据题目标签筛选题目
          </span>
          </div>
          <select
              style="  width: 30vh; "
              class="select_problem"
              onblur="size=0"
              onmousedown="if(options.length>6){size=7}"
              onchange="size=0"
          >
            <option value="volvo">文学集</option>
            <option value="saab">哲学</option>
            <option value="opel">雅思</option>
            <option value="audi">大学网奥数大学网奥数</option>
            <option value="volvo">文学集</option>
            <option value="saab">哲学</option>
            <option value="opel">雅思</option>
            <option value="audi">大学网奥数</option>
            <option value="audi">大学网奥数</option>
            <option value="audi">大学网奥数</option>
            <option value="audi">大学网奥数</option>
          </select>
        </div>
      </div>
    </div>


  </ContentField>



</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import router from "@/router";
import $ from "jquery";
import { ref,nextTick} from 'vue';
export default{
  components: { ContentField },
  setup(){
    let search_problem_key = ref('')
    let search_problem_name = ref('')
    let search_problem_tag = ref('')
    let state = ref(0)
    let pages = ref([]);

    const todetails = (index) =>{
      router.push({name: "problem_details", params: {id : index}});
    }
    const store = useStore();
    const jwt_token = localStorage.getItem("jwt_token");
    if(jwt_token){
        store.commit("updateToken", jwt_token);
        store.dispatch("getinfo", {
            success(){
                store.commit("updatePullingInfo", false);
            },
            error() {
                store.commit("updatePullingInfo", false);
            }
        })
    }else {
        store.commit("updatePullingInfo", false);
    }
    const logged = store.state.user.is_login;
    store.commit("updatePullingInfo", false);
    if(store.state.user.is_login){
      store.dispatch("getinfoInMainPage", {
        success(){
          console.log(store.state.user);
        }
      })
    }
    const ProblemChange = (data) =>{
      state.value = data;
      search()
    }
   
    const search = () =>{
      $.ajax({
        url: "http://127.0.0.1:3000/problems/overview/",
        data:{
          pageNum: "1",
          userKey: store.state.user.id,
          searchProblemKey: search_problem_key.value,
          searchProblemName: search_problem_name.value,
          searchProblemTag: search_problem_tag.value,
          problemState: state.value,
        },
        type: 'post',
        success(resp) {
          problemOverviews.value = resp.problemList
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
          console.log(search_problem_key.value)
          search()
          return false;
        }
      }
    })
    const problemOverviews = ref([{}])
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
        url: "http://127.0.0.1:3000/problems/overview/",
        data:{
          pageNum: JSON.stringify(page),
          searchPro: "",
          searchProblemKey:  "",
          searchProblemName: "",
          searchProblemTag: "",
          problemState: "",
        },
        type: 'post',
        success(resp) {
            problemOverviews.value = resp.problemList
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

    return {
      todetails,
      logged,
      problemOverviews,
      click_page,
      search,
      search_problem_key,
      search_problem_name,
      search_problem_tag,
      ProblemChange,
      state,
      pages,
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
div.list-group{
  margin-top: 2vh;
}
div.col-9{
  margin-top: 2vh;
}
.form-label{
  font-size: 20px;
  color: black;
}
div.label2{
  margin-top: 2vh;
}
</style>