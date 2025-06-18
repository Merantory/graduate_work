<template>
  <h1> hello </h1>
</template>

<script lang="ts">
import { defineComponent } from "vue"

export default defineComponent({
  components: {},
  data() {
    return {
      conversations: [
        {
          id: 1,
          name: 'Jane Smith',
          avatar: 'avatar.jpg',
          lastMessage: 'Hey, how are you?'
        }
      ],
      selectedConv: null,
      messages: [],
      newMessage: ''
    }
  }
})
</script>