
<template>
  <main>
    <HeaderComponent :tokenInfo="tokenInfo"/>
    <RouterView :tokenInfo="tokenInfo"/>
  </main>
</template>

<script lang="ts">
import {defineComponent, provide} from 'vue';
import HeaderComponent from '@/components/HeaderComponent.vue';
import axios, { AxiosResponse } from 'axios';
import login from "@/service/login";
import { useUser } from '@/service/user';
import { useChat } from '@/service/chat';

export default defineComponent({
  components: {
    HeaderComponent
  },
  data: () => {
    return {
      tokenInfo: {}
    }
  },
  methods: {
    getCurrentPrincipal() {
      login.getTokenInfo()
          .then((result: AxiosResponse) => {
            console.log("Result getting token info: ", result);
            if (!result.data.active) {
              return login.login();
            }
            this.tokenInfo = result.data;
            const { setTokenInfo } = useUser();
            setTokenInfo(this.tokenInfo)
            useChat().connect(useUser().username.value)
          })
          .catch((err: Error) => {
            login.login();
          })
    }
  },
  computed: {
    tokenInfoString() {
      if (!this.tokenInfo) {
        return null;
      }
      return JSON.stringify(this.tokenInfo, null, 8);
    }
  },
  mounted() {
    this.getCurrentPrincipal();
  },
  provide() {
    return {
      tokenInfo: () => this.tokenInfo
    }
  }
})
</script>
