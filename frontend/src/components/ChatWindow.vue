<template>
  <div class="chat-window">
    <div v-if="!selectedUser" class="no-chat-selected">
      Выберите пользователя для начала общения
    </div>
    <div v-else class="chat-container">
      <div class="chat-header">
        <h3>{{ selectedUser.name }}</h3>
      </div>
      <div class="messages-container" ref="messagesContainer">
        <div v-if="loading" class="loading-messages">
          Загрузка сообщений...
        </div>
        <div v-else-if="messages.length === 0" class="no-messages">
          Нет сообщений. Начните общение!
        </div>
        <message-item
            v-else
            v-for="message in messages"
            :key="message.id"
            :message="message"
        />
      </div>
      <chat-input :receiver-username="selectedUser.name" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, nextTick, onMounted, inject } from 'vue';
import MessageItem from './MessageItem.vue';
import ChatInput from './ChatInput.vue';
import { User } from '@/types/User';
import { useChat } from '@/service/chat';

export default defineComponent({
  name: 'ChatWindow',
  components: {
    MessageItem,
    ChatInput
  },
  setup() {
    const loading = ref(false);
    const messagesContainer = ref<HTMLElement | null>(null);
    const messages = computed(() => useChat().messages.value);
    const selectedChatRoomId = computed(() => useChat().selectedChatRoom.value);
    const selectedUser = inject('selectedUser') as User;

    watch(messages, () => {
      nextTick(() => {
        scrollToBottom();
      });
    });

    watch(selectedUser, (newUser) => {
      selectedUser.name = newUser.name
      selectedUser.id = newUser.id
      selectedUser.user_avatar_url = newUser.user_avatar_url
    });

    watch(selectedChatRoomId, (newChatRoomId) => {
      if (newChatRoomId) {
        loading.value = true;
        useChat().loadChatHistory(newChatRoomId)
            .then(() => {
              loading.value = false;
              nextTick(() => {
                scrollToBottom();
              });
            });
      }
    });
    const scrollToBottom = () => {
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
      }
    };

    onMounted(() => {
      if (selectedChatRoomId.value) {
        loading.value = true;
        useChat().loadChatHistory(selectedChatRoomId.value)
            .then(() => {
              loading.value = false;
              nextTick(() => {
                scrollToBottom();
              });
            });
      }
    });

    return {
      messages,
      selectedUser,
      loading,
      messagesContainer
    };
  }
});
</script>

<style scoped>
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.no-chat-selected {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
  font-size: 18px;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  padding: 15px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.chat-header h3 {
  margin: 0;
  font-size: 16px;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  background-color: #f8f8f8;
}

.loading-messages,
.no-messages {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
}
</style>