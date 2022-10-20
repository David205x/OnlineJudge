<template>
  <vue-advanced-chat
    :current-user-id="currentUserId"
    :rooms="JSON.stringify(rooms)"
    :messages="JSON.stringify(messages)"
    :room-actions="JSON.stringify(roomActions)"
  />
</template>

<script>
  import { register } from 'vue-advanced-chat'
  register()

  export default {
    data() {
      return {
        currentUserId: '4321',
        rooms:[
            {
              roomId: '1',
              roomName: 'Room 1',
              avatar: '',
              unreadCount: 0,
              index: 1,
              lastMessage: {
                _id: 'xyz',
                content: 'Last message received',
                senderId: '1234',
                username: 'John Doe',
                timestamp: '10:20',
                saved: true,
                distributed: false,
                seen: true,
                new: true
              },
              users: [
                {
                  _id: '1234',
                  username: 'John Doe',
                  avatar: '',
                  status: {
                    state: 'online',
                    lastChanged: 'today, 14:30'
                  }
                },
                {
                  _id: '4321',
                  username: 'John Snow',
                  avatar: '',
                  status: {
                    state: 'online',
                    lastChanged: '14 July, 20:00'
                  }
                }
              ],
              typingUsers: [ 4321 ]
            },
        ],
        messages:[],
        roomActions: [
          { name: 'inviteUser', title: 'Invite User' },
          { name: 'removeUser', title: 'Remove User' },
          { name: 'deleteRoom', title: 'Delete Room' }
        ]
      }
    }
  }
</script>
<style scoped>

</style>