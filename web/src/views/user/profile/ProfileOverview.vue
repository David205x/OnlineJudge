<template>
  <ContentField>
    <div class="container">
        <div class="row"> <!--   这他妈为啥是row  -->
          <div class="col-2">
              <div class="row">
                  <img src="../../../assets/logo.png" class="rounded float-start" alt="../../../assets/logo.png">
              </div>
              <div class="row" style="margin-top: 5vh">
                  <p class="text-center">Holy Username</p>
                  <p class="fw-light" style="font-size: 15px; margin-top: 0px">Mys Institution<br><br></p>
                  <button type="button" class="btn btn-secondary">Edit Profile</button>
              </div>
          </div>
          <div class="col-1"></div>
          <div class="col" style="font-size: 15px">
              <nav>
                  <div class="nav nav-tabs" id="nav-tab" role="tablist">
                      <button class="nav-link active" id="nav-home-tab" data-bs-toggle="tab" data-bs-target="#nav-home" type="button" role="tab" aria-controls="nav-home" aria-selected="true">信息概览</button>
                      <button class="nav-link" id="nav-profile-tab" data-bs-toggle="tab" data-bs-target="#nav-profile" type="button" role="tab" aria-controls="nav-profile" aria-selected="false">我的提交</button>
                      <button class="nav-link" id="nav-contact-tab" data-bs-toggle="tab" data-bs-target="#nav-contact" type="button" role="tab" aria-controls="nav-contact" aria-selected="false">我的题解</button>
                  </div>
              </nav>
              <br>
              <div class="tab-content" id="nav-tabContent">
                  <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                    <content-field style="font-size: 10px">
                        <calendar-heatmap
                            :values="[{ date: '2022-9-11', count: 10 }, { date: '2022-9-10', count: 6 }]"
                            end-date="2022-9-20"
                            max="12"
                            round="3"
                            :range-color="['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39']"
                            locale="right"
                        />
                    </content-field>
                  </div>
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
                        <tbody>
                        <tr>
                            <th scope="row">1</th>
                            <td>what's this</td>
                            <td style="color: #cf4e4e">Wrong Answer</td>
                            <td>99</td>
                            <td>#HARD</td>
                        </tr>
                        <tr>
                          <th scope="row">2</th>
                          <td>a+b?</td>
                          <td style="color: #42b983">Accepted</td>
                          <td>1</td>
                          <td>#NEWBIE</td>
                        </tr>
                        <tr>
                          <th scope="row">3</th>
                          <td>你妹只因</td>
                          <td style="color: blue">TimeLimitExceeded</td>
                          <td>12</td>
                          <td>#INFERNO</td>
                        </tr>
                        </tbody>
                      </table>
                  </div>
                  <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                      郭瑞不过日剧
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
export default {
  name: "ProfileDetailView",
  components: {
    ContentField,
    CalendarHeatmap
  },
  setup(){
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
  }
}
</script>

<style scoped>
</style>