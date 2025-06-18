<template>
  <div class="d-flex justify-content-end me-5">
    <button type="button" class="btn btn-primary mt-4" @click="goToCreateDiscussion">Создать обсуждение</button>
  </div>
  <div class="container forum-list mt-4">


    <div v-for="discussion in discussionList" :key="discussion.id"
         :class="['row', 'py-2', discussion.pinned_at ? 'bg-light border-top' : 'border-bottom']">
      <div class="col-1 d-block text-center align-content-center">
        <div class="icon-placeholder">
          <img v-if="discussion.user_avatar_url != null" :src="discussion.user_avatar_url"
               alt="User profile img"
               style="width: 48px; height: 48px; border-radius: 50%">
        </div>
      </div>
      <div class="col-6" @click="openDiscussion(discussion.id)">
        <strong>{{ discussion.title }}</strong>
        <span v-if="discussion.pinned_at" class="pinned">Закреплено</span><br>
        <small>{{ formatDate(discussion.created_at) }}</small>
      </div>
      <div class="col-2">
        <small>Комментариев: {{ discussion.repliesCount || 0 }}</small>
      </div>
      <div class="col-3 text-end">
        <small>{{ formatDate(discussion.updated_at || discussion.created_at) }}<br>{{ discussion.lastReplier || '' }}</small>
      </div>
    </div>

    <!-- Сообщение, если список пуст -->
    <div v-if="discussionList.length === 0" class="row py-4 text-center">
      <div class="col-12">
        <p>Обсуждений пока нет</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref, onMounted} from "vue";
import {useRouter} from "vue-router";
import {useUser} from "@/service/user";
import {Discussion} from "@/types/Discussion";
import axios from "axios";

export default defineComponent({
  setup() {
    axios.defaults.baseURL = process.env.VUE_APP_BACKEND_HOST;
    const router = useRouter();
    const discussionList = ref<Discussion[]>([]);

    const {
      userInfo,
      isAuthenticated,
      userRole,
      getAvatarUrl
    } = useUser();

    const goToCreateDiscussion = () => {
      router.push('/discussion/create');
    };

    const openDiscussion = (discussionId: number) => {
      router.push(`/discussion/${discussionId}`);
    };

    const loadAvatarUrls = async (discussions: Discussion[]) => {
      // Создаем массив промисов для всех запросов аватаров
      const avatarPromises = discussions.map(async (discussion) => {
        if (discussion.user_id) {
          const avatarUrl = await getAvatarUrl(discussion!.user_id);
          return {
            ...discussion,
            user_avatar_url: avatarUrl
          };
        }
        return discussion;
      });

      // Ждем выполнения всех промисов и обновляем список дискуссий
      discussionList.value = await Promise.all(avatarPromises);
    };

    const loadDiscussions = async () => {
      try {
        const response = await axios.get('/discussion');
        const discussions = response.data as Array<Discussion>;
        discussionList.value = discussions; // Сначала загружаем список без аватаров

        // Затем асинхронно загружаем URL аватаров
        await loadAvatarUrls(discussions);
      } catch (error) {
        console.log("Avatar not loaded")
      }
    };

    const formatDate = (dateString: string | null | undefined) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'short', day: 'numeric' };
      return date.toLocaleDateString('en-US', options);
    };

    // Загружаем обсуждения при монтировании компонента
    onMounted(() => {
      loadDiscussions();
    });

    return {
      discussionList,
      goToCreateDiscussion,
      openDiscussion,
      formatDate,
      userInfo,
      isAuthenticated,
      userRole
    };
  }
})
</script>
