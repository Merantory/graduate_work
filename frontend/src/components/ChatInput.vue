<template>
  <div class="chat-input-container">
    <input
        type="text"
        class="message-input"
        v-model="messageText"
        placeholder="Введите сообщение..."
        @keyup.enter="sendMessage"
    />
    <button class="send-button" @click="sendMessage" :disabled="!messageText.trim() || !receiver">
      Отправить
    </button>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue';
import { useChat } from '@/service/chat';

export default defineComponent({
  name: 'ChatInput',
  props: {
    receiverUsername: {
      type: String,
      default: ''
    }
  },
  setup(props) {
    const messageText = ref('');
    const receiver = computed(() => props.receiverUsername);

    const sendMessage = () => {
      if (messageText.value.trim() && receiver.value) {
        useChat().sendMessage(messageText.value, receiver.value);
        messageText.value = '';
      }
    };

    return {
      messageText,
      receiver,
      sendMessage
    };
  }
});
</script>

<style scoped>
.chat-input-container {
  display: flex;
  padding: 10px;
  border-top: 1px solid #e0e0e0;
}

.message-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 8px;
}

.send-button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.send-button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}
</style>