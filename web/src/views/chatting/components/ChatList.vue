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
<!--          </div>-->{{item.userName }}
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
    const redSession = (item, index) =>{
      console.log(item)
        store.commit("updateReceiver",{
          receiverId: item.userKey,
          receiverName: item.userName
        })
        store.commit("updateSelected", index)
      
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