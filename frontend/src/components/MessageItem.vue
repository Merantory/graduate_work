<template>
  <div :class="['message-item', { 'own-message': isOwnMessage }]">
    <div class="message-content">
      {{ message.content }}
    </div>
    <div class="message-time">
      {{ formattedTime }}
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, PropType } from 'vue';
import { Message } from '@/types/Message';
import { useChat } from '@/service/chat';

export default defineComponent({
  name: 'MessageItem',
  props: {
    message: {
      type: Object as PropType<Message>,
      required: true
    }
  },
  setup(props) {
    const currentUser = useChat().currentUser;

    const isOwnMessage = computed(() => {
      return props.message.sender.id == currentUser.value?.id;
    });

    const formattedTime = computed(() => {
      const date = props.message.sentAt;
      return new Intl.DateTimeFormat('default', {
        hour: '2-digit',
        minute: '2-digit'
      }).format(date);
    });

    return {
      isOwnMessage,
      formattedTime
    };
  }
});
</script>

<style scoped>
.message-item {
  margin: 8px 0;
  max-width: 45%;
  padding: 8px 12px;
  border-radius: 12px;
  position: relative;
  background-color: #46d3f2;
}

.own-message {
  margin-left: auto;
  background-color: #dcf8c6;
}

.message-content {
  word-break: break-word;
}

.message-time {
  font-size: 11px;
  color: #999;
  text-align: right;
  margin-top: 4px;
}
</style>