<template>
  <div class="users-container">
    <div class="contacts-header">
      <h3>Контакты</h3>
    </div>
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <span>Загрузка...</span>
    </div>
    <ul v-else class="user-list">
      <li
          v-for="user in contacts"
          :key="user.id"
          :class="{ 'active': isSelected(user) }"
          @click="selectUser(user)"
      >
        <div class="user-avatar">
          <img v-if="user && user.user_avatar_url"
               :src="user.user_avatar_url"
               alt="User profile img"
               class="header-profile-img">
          <img v-else
               src="../assets/default-profile-picture.png"
               alt="User profile img"
               class="header-profile-img">
        </div>
        <div class="user-name">{{ user.name }}</div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useChat } from '@/service/chat';
import { User } from '@/types/User';
import axios from "axios";

export default defineComponent({
  name: 'UserList',
  setup(props, {emit}) {
    const loading = ref(true);
    const selectedUser = ref<User | null>(null);
    const contacts = computed(() => useChat().contacts.value);
    const MESSANGER_API_URL = process.env.VUE_APP_MESSANGER_HOST;

    onMounted(() => {
      loading.value = false;
    });

    const selectUser = async (user: User) => {
      selectedUser.value = user;
      const currentUser = useChat().currentUser.value;
      if (!currentUser) return;
      let chatRoomId = await determineChatRoomId(currentUser.id, user.id);
      if (chatRoomId == null) {
        chatRoomId = 0;
      }
      useChat().loadChatHistory(chatRoomId);
      emit('user-selected', user);
    };

    const isSelected = (user: User): boolean => {
      return selectedUser.value?.id === user.id;
    };

    const determineChatRoomId = async (userId1: number, userId2: number): Promise<number | null> => {
      try {
        let responseChatRoom = await fetch(`${MESSANGER_API_URL}/api/chat?first=${userId1}&second=${userId2}`);

        if (!responseChatRoom.ok) {
          if (responseChatRoom.status === 404) {
            axios.defaults.baseURL = MESSANGER_API_URL
            axios.post(`/api/chat?first=${userId1}&second=${userId2}`)
                .then(function (response) {
                  return response.data
                }).catch(function (error) {
              console.log(error)
            })
          }
        }
        const chatRoomId = await responseChatRoom.json();
        return chatRoomId;
      } catch (error) {
        console.log("Chat room not specified")
        throw error;
      }
    };

    return {
      loading,
      contacts,
      selectedUser,
      selectUser,
      isSelected
    };
  }
});
</script>

<style scoped>
.users-container {
  width: 280px;
  border-right: 1px solid #e0e0e0;
  height: 100%;
  overflow-y: auto;
  background-color: #f9f9f9;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}

.contacts-header {
  padding: 20px 15px;
  background-color: #4a76a8;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 10;
}

.contacts-header h3 {
  margin: 0;
  font-size: 1.3rem;
  font-weight: 600;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.user-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.user-list li {
  padding: 12px 15px;
  cursor: pointer;
  border-bottom: 1px solid #eeeeee;
  display: flex;
  align-items: center;
  transition: all 0.2s ease;
}

.user-list li:hover {
  background-color: #f0f4f8;
  transform: translateX(3px);
}

.user-list li.active {
  background-color: #e7f0fd;
  border-left: 4px solid #4a76a8;
}

.user-avatar {
  margin-right: 12px;
}

.header-profile-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-name {
  font-weight: 500;
  color: #333;
}

.loading {
  padding: 30px;
  text-align: center;
  color: #777;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.loading-spinner {
  width: 30px;
  height: 30px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #4a76a8;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>