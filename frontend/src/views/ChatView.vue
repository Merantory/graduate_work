<template>
  <div class="app-container">
    <div class="chat-layout">
      <UserList @user-selected="updateSelectedUser" />
      <ChatWindow :selected-user="selectedUser" />
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref, computed, onBeforeUnmount, onMounted, provide} from 'vue';
import UserList from '@/components/UserList.vue';
import ChatWindow from '@/components/ChatWindow.vue';
import { useChat } from '@/service/chat';
import type { User } from '@/types/User';

export default defineComponent({
  name: 'ChatView',
  components: {
    UserList,
    ChatWindow
  },
  setup() {
    const selectedUser = ref<User | null>(null);

    // Предоставьте selectedUser для дочерних компонентов
    provide('selectedUser', selectedUser);

    function updateSelectedUser(user: User | null) {
      selectedUser.value = user;
    }

    onBeforeUnmount(() => {
      useChat().disconnect();
    });

    return {
      selectedUser,
      updateSelectedUser
    };
  }
});
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
  font-family: Arial, sans-serif;
}

.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #f5f5f5;
}

.login-form {
  display: flex;
  flex-direction: column;
  width: 300px;
  margin-top: 20px;
}

.login-form input {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.login-form button {
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.login-form button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.chat-layout {
  display: flex;
  height: 100%;
  overflow: hidden;
}
</style>