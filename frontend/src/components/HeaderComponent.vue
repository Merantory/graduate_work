<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">StudSocial</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link" href="/discussion">Обсуждения</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/messages">Сообщения</a>
          </li>
        </ul>
        <div class="d-flex align-items-center" v-if="isAuthenticated">
          <div class="dropdown profile-dropdown">
            <div class="profile-img-container" id="profileDropdown" data-bs-toggle="dropdown" aria-expanded="false">
              <img  v-if="userInfo && userInfo.user_id && avatar"
                   :src="avatar"
                   alt="User profile img"
                   class="header-profile-img">
              <img  v-else
                    src="../assets/default-profile-picture.png"
                    alt="User profile img"
                    class="header-profile-img">
            </div>
            <ul class="dropdown-menu dropdown-menu-lg-end" aria-labelledby="profileDropdown">
              <li><a class="dropdown-item" :href="'/profile/' + userInfo?.user_id">Мой профиль</a></li>
              <li><a class="dropdown-item" href="#" @click.prevent="logout">Выход</a></li>
            </ul>
          </div>
          <span class="mx-3">{{ userInfo?.username }}</span>
        </div>
      </div>
    </div>
  </nav>
</template>


<script lang="ts">
import {defineComponent, ref, onMounted, watch} from "vue"
import { useUser } from '@/service/user'

export default defineComponent({
  setup() {
    const {
      userInfo,
      isAuthenticated,
      tokenInfoFormatted,
      userRole,
      getAvatarUrl,
      logout
    } = useUser();

    const avatar = ref<string>('')

    const loadAvatar = async () => {
      try {
        if (userInfo.value && userInfo.value.user_id) {
          const avatarUrl = await getAvatarUrl(userInfo.value.user_id)
          if (avatarUrl != null) {
            avatar.value = avatarUrl
          }
        }
      } catch (error) {
        console.log("Doesnt have avatar")
      }
    };

    onMounted(loadAvatar);

    watch(() => userInfo.value, (newUserInfo) => {
      if (newUserInfo && newUserInfo.user_id) {
        loadAvatar();
      }
    }, { immediate: true });

    return {
      userInfo,
      isAuthenticated,
      tokenInfoFormatted,
      userRole,
      avatar,
      logout
    };
  },
  components: {},
})
</script>


<style scoped>
.profile-dropdown {
  position: relative;
}

.profile-img-container {
  cursor: pointer;
}

.profile-dropdown:hover .dropdown-menu {
  display: block;
}
</style>
