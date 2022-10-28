<template>
  <ContentField>
    <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
      <li class="nav-item" role="presentation">
        <button class="nav-link active" id="pills-home-tab" data-bs-toggle="pill" data-bs-target="#pills-home" type="button" role="tab" aria-controls="pills-home" aria-selected="true">Previous</button>
      </li>
      <li  class="nav-item" role="presentation">
        <button class="nav-link" id="pills-profile-tab" data-bs-toggle="pill" data-bs-target="#pills-profile" type="button" role="tab" aria-controls="pills-profile" aria-selected="false">1</button>
      </li>
      <li class="nav-item" role="presentation">
        <button  class="nav-link" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false">2</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false">3</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false">4</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false">5</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false">...</button>
      </li>
    </ul>
    <table id="listStatus" class="table table-striped table-hover">
      <thead>
      <tr>
        <th scope="col">Num<br></th>
        <th class="username sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
          Username<br>
          <input type="text" id="un" name="un" class="search_text" style="width:100%">
        </th>
        <th class="prob sorting_disabled" rowspan="1" colspan="1" style="text-align: center">
          Prob<br>
          <input type="text" id="probunm" name="probunm" class="search_text" style="width:100%">
        </th>
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
        <th scope="col">Mem<br>(MB)</th>
        <th class="length hidden-lg-down sorting_disabled" rowspan="1" colspan="1" >Length<br/></th>
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
      <tr>
        <th scope="row">1</th>
        <td>wxx</td>
        <td>3306</td>
        <td style="color: #42b983">Accepted</td>
        <td>123</td>
        <td>4096</td>
        <td>20</td>
        <td>C++</td>
        <td>1 minutes ago</td>
      </tr>
      <tr>
        <th scope="row">2</th>
        <td>wxx</td>
        <td>3306</td>
        <td style="color: #42b983">Accepted</td>
        <td>123</td>
        <td>4096</td>
        <td>20</td>
        <td>C++</td>
        <td>1 minutes ago</td>
      </tr>
      </tbody>
    </table>
  </ContentField>

</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";

export default{
  components: { ContentField },
  setup(){
    const store = useStore();
    const logged = store.state.user.is_login;
    store.commit("updatePullingInfo", false);
    if(store.state.user.is_login){
      store.dispatch("getinfoInMainPage", {
        success(){
        }
      })
    }
    return {
      logged,
    }

  }
}

</script>

<style scoped>

</style>