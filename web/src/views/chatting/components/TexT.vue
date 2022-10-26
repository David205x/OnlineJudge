<template>
  <ContentField>
    <div>
      <textarea class="form-control" id="validationTextarea" ref="text" v-model="content" @keyup="onKeyup"/>
      <div class="send" >
        <button class="btn btn-primary" @click="send()">发送(enter)</button>
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
  data() {
    return {
      applyHover: false,
      content: '',
      hint: true,
      reply: '未找到',
      frequency: 0,
      warn: false,
      showEmoji: false,
      showReply: false,
      // 菜单数据
      contextMenuData: {
        menuName: 'demo',
        // 菜单显示的位置
        axis: {
          x: null,
          y: null
        },
        // 菜单选项
        menulists: [
          {
            icoName: 'el-icon-edit', // icon图标
            btnName: '快捷回复', // 菜单名称
            children: [
              {
                fnHandler: 'addReply',
                btnName: '您好，请问有什么可以帮到您?'
              },
              {
                fnHandler: 'addReply',
                btnName: '我有事离开一下，马上回来，请稍候。'
              },
              {
                fnHandler: 'addReply',
                btnName: '还有其他伴随症状吗？'
              },
              {
                icoName: 'el-icon-circle-plus-outline',
                fnHandler: 'Drawer',
                btnName: '更多'
              }
            ]
          },
          {
            fnHandler: 'deletedata',
            icoName: 'el-icon-delete',
            btnName: '清空内容'
          }
        ]
      },
      // 话术
      docAutoAnswers: [],
      drawer: false,
      direction: 'ltr',
      input2: '',
      activeNames: ['1']
    }
  },
  setup(){
    const store = useStore();
    return{
      store
    }
  },
  methods:{
    send(){
      this.store.state.chatting.socket.send(JSON.stringify({
            event: "singleMessage",
            a_id: this.store.state.user.id,
            b_id: String(-(this.store.state.user.id - '3')),
            content: this.content
      }));
    }
  },
  computed: {

  },
  watch: {
  }
}
</script>

<style scoped>

</style>