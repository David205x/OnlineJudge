<template>
  <ContentField>
    <div class="mb-3">
      <label for="exampleFormControlInput1" class="form-label">Search Bar</label>
      <input type="email" class="form-control" id="exampleFormControlInput1" placeholder="请根据题目编号，题目名称，题目来源搜索">
    </div>
    <div class="container">
      <div class="row align-items-start">

        <div class="col-9">
          <table class="table table-striped table-hover">
            <thead>
            <tr>
              <th scope="col">题目编号</th>
              <th scope="col">题目名称</th>
              <th scope="col">题目来源</th>
              <th scope="col">通过率</th>
              <th scope="col">题目标签</th>
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
                <span v-for="problemTag in problemOverview.problemTags" :key = "problemTag">
                  <span class="badge bg-primary">{{problemTag}}</span>
                    &nbsp;
                </span>
              
              </td>
            </tr>
            </tbody>
          </table>
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
        <div class="col-3">
          <div class="list-group">
            <button type="button" class="list-group-item list-group-item-action active" aria-current="true">
              ALL
            </button>
            <button type="button" class="list-group-item list-group-item-action">Solved</button>
            <button type="button" class="list-group-item list-group-item-action">Attempted</button>
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
import { ref } from 'vue';
export default{
  components: { ContentField },
  setup(){
    const todetails = (index) =>{
      router.push({name: "problem_details", params: {id : index}});
    }
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
    const problemOverviews = ref([{}])
    let current_page = 1;
    let total_problems = 0;
    let per_num = 1;
    let pages = ref([]);
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
          pageNum: page,
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
      pages,
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