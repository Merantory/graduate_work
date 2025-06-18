<template>
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center">
      <div class="col col-lg-9 col-xl-8">
        <div class="card">
          <div class="rounded-top text-white d-flex flex-row" style="background-color: #999; height:200px;">
            <div class="ms-4 mt-5 d-flex flex-column" style="width: 150px;">
              <img v-if="avatar" :src="avatar"
                   alt="Generic placeholder image" class="profile-img">
            </div>
            <div class="ms-3" style="margin-top: 130px;">
              <h5>{{ userInfo?.username }}</h5>
            </div>
          </div>
          <div class="p-4 text-black bg-body-tertiary">
            <div class="d-flex justify-content-between text-center py-1 text-body">
              <div>
                <button type="button" @click="editImageClick()" class="btn btn-outline-dark text-body ms-2">
                  Обновить аватар
                </button>
              </div>
              <div class="d-inline-flex">

              </div>
            </div>
          </div>
          <div class="card-body p-4 text-black">
            <div class="mb-5  text-body">
              <p class="lead fw-normal mb-1">О себе</p>
              <div class="p-4 bg-body-tertiary">
                <!--
                <p class="font-italic mb-1">Web Developer</p>
                <p class="font-italic mb-1">Lives in New York</p>
                <p class="font-italic mb-0">Photographer</p>
                !-->
                Американский предприниматель, инженер и миллиардер, политический и общественный деятель.
                Это Илон Маск. У меня же жизнь чуть проще.
              </div>
            </div>
            <div class="d-flex justify-content-between align-items-center mb-4 text-body">
              <p class="lead fw-normal mb-0">Недавние или самые популярные посты (Но пока фотки)</p>
            </div>
            <div class="row g-2">
              <div class="col mb-2">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(112).webp" alt="image 1"
                     class="w-100 rounded-3">
              </div>
              <div class="col mb-2">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(107).webp" alt="image 1"
                     class="w-100 rounded-3">
              </div>
            </div>
            <div class="row g-2">
              <div class="col">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(108).webp" alt="image 1"
                     class="w-100 rounded-3">
              </div>
              <div class="col">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(114).webp" alt="image 1"
                     class="w-100 rounded-3">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <EditProfileComponent ref="editProfileComponent"/>
  <EditImageComponent ref="editImageComponent"/>
</template>

<script lang="ts">
import {defineComponent, ref, onMounted, watch} from "vue";
import EditProfileComponent from "@/components/EditProfileComponent.vue";
import { useUser } from "@/service/user";
import EditImageComponent from "@/components/EditImageComponent.vue";

export default defineComponent({
  components: {EditProfileComponent, EditImageComponent},
  methods: {
    editProfileClick() {
      (this.$refs.editProfileComponent as typeof EditProfileComponent).showModal()
    },
    editImageClick() {
      (this.$refs.editImageComponent as typeof EditImageComponent).showModal()
    },
  },
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
        console.error("Error loading avatar:", error);
      }
    };

    // При монтировании компонента
    onMounted(loadAvatar);

    // Наблюдение за изменениями userInfo
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
})
</script>

<style scoped>
.user-info {
  margin-top: 20px;
}

.gradient-custom-2 {
  /* fallback for old browsers */
  background: #fbc2eb;

  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to right, rgba(251, 194, 235, 1), rgba(166, 193, 238, 1));

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to right, rgba(251, 194, 235, 1), rgba(166, 193, 238, 1))
}

</style>