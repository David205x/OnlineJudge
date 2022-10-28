<template>
  <ContentField>
    <div>
      <!-- {{ searchedChatlist }} -->
      <div class="list-group" style="overflow: auto;height: 55vh">
        <button
            v-for="(item, index) in store.state.user.friends"
            :key="index"
            :class="index == store.state.chatting.selected ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'"
            @click="redSession(item, index)"

        >
        <div>
          <span>{{item.userName}} &nbsp;</span>
        </div>
        <div>
          <img src="../../../assets/testp1.jpg" class="img-thumbnail" style="width:50px; height:50px; border-radius: 100px; webkit-border-radius: 100px; moz-border-radius: 100px;">
        </div>
        
       


<!--          <div class="list-left">-->
<!--            &lt;!&ndash; <img-->
<!--              class="avatar"-->
<!--              width="42"-->
<!--              height="42"-->
<!--              src="https://p1.ssl.qhimgs1.com/t01f1a752c5c6e75077.jpg"-->
<!--            > &ndash;&gt;-->
<!--            <el-badge v-if="item.is_read == 0 " :key="index" is-dot class="item">-->
<!--              <el-avatar icon="el-icon-user-solid" />-->
<!--            </el-badge>-->
<!--            <el-avatar v-if="item.is_read == 1 " icon="el-icon-user-solid" />-->

<!--            &lt;!&ndash; <el-avatar v-if="item.id != selectId"> 患者 </el-avatar> &ndash;&gt;-->
<!--          </div>-->
<!--          <div class="list-right">-->
<!--            <p class="name">{{ item.content }}</p>-->
<!--            &lt;!&ndash; <span class="time">{{ item.createtime | time }}</span> &ndash;&gt;-->
<!--            <p class="lastmsg">{{ item.createtime }}</p>-->
<!--          </div>-->
        </button>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from 'vuex'

export default {
  components: {
    ContentField,
  },
  setup(){
    const store = useStore();
    let friends = localStorage.getItem("friends");
    if(friends == null || friends == undefined || friends.length == 0){
      localStorage.setItem("friends", JSON.stringify([{"userKey" : "1", "userName" : "test4"}, {"userKey" : "2", "userName" : "test1"}]))
      store.commit("updateFriends")
    }
    const redSession = (item, index) =>{
        store.commit("updateReceiver",{
          receiverId: item.userKey,
          receiverName: item.userName,
          senderId: store.state.user.id
        })
        store.commit("updateSelected", index)
        console.log(store.state.chatting.content)
      
    }
    return {
      store,
      redSession
    }
  },
  methods:{

  },
  computed: {

  },
  watch: {
  }
}
</script>

<style scoped>

</style>