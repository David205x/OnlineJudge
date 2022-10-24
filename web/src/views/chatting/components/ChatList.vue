<template>
  <ContentField>
    <div>
      <!-- {{ searchedChatlist }} -->
      <ul style="overflow: auto;height: 55vh">
        <li
            v-for="(item,index) in searchedChatlist"
            :key="index"
            class="sessionlist"
            :class="{ active: item.orderid === selectId }"
            @click="redSession(item)"
        >
          <div class="list-left">
            <!-- <img
              class="avatar"
              width="42"
              height="42"
              src="https://p1.ssl.qhimgs1.com/t01f1a752c5c6e75077.jpg"
            > -->
            <el-badge v-if="item.is_read == 0 " :key="index" is-dot class="item">
              <el-avatar icon="el-icon-user-solid" />
            </el-badge>
            <el-avatar v-if="item.is_read == 1 " icon="el-icon-user-solid" />

            <!-- <el-avatar v-if="item.id != selectId"> 患者 </el-avatar> -->
          </div>
          <div class="list-right">
            <p class="name">{{ item.content }}</p>
            <!-- <span class="time">{{ item.createtime | time }}</span> -->
            <p class="lastmsg">{{ item.createtime }}</p>
          </div>
        </li>
      </ul>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";

export default {
  components: {
    ContentField,
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