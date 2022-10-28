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
  
    if(friends == null || friends == undefined || friends.length == 0 || friends == "[object Object]"){
      localStorage.setItem("friends", JSON.stringify([{"userKey" : "1", "userName" : "test4", "lastMessage" : "hello"}, {"userKey" : "2", "userName" : "test1", "lastMessage" : "hello"}]))
    }
    store.commit("updateFriends")
    const redSession = (item, index) =>{
      
        store.commit("updateReceiver",{
          receiverId: item.userKey,
          receiverName: item.userName,
          senderId: store.state.user.id
        })
        store.dispatch("getOthers",{
          userKey : item.userKey,
        })
        store.commit("updateSelected", index)
      
    }
    return {
      store,
      redSession
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