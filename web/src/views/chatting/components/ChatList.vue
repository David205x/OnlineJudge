<template>
  <ContentField>
    <div>
      <!-- {{ searchedChatlist }} -->
      <ul style="overflow: auto;height: 55vh">
        <li
            v-for="(item, index) in store.state.user.friends"
            :key="index"
            :class="index == 0 ? 'active' : ''"
            @click="redSession(item)"

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
        </li>
      </ul>
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
    const redSession = (item) =>{
        store.commit("updateReceiver",{
          receiverId: item.userKey,
          receiverName: item.userName
      })
      console.log("??")
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