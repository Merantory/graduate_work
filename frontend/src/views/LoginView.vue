<template>
  <h1>
    LOGIN PAGE
  </h1>
  <button @click="login">
    LOGIN
  </button>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import login from "@/service/login";


export default defineComponent({
  methods: {
    login() {
      return login.login();
    },
  }
})
</script>