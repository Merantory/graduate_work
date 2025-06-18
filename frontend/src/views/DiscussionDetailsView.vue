<template>
  <div class="container mt-4">
    <!-- Навигация и кнопки обратно -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <button class="btn btn-outline-secondary" @click="goBack">
        <i class="bi bi-arrow-left"></i> Назад
      </button>
      <div v-if="true">
        <button class="btn btn-warning me-2" @click="editDiscussion" v-if="true">
          <i class="bi bi-pencil"></i> Редактировать
        </button>
        <button class="btn btn-danger" @click="confirmDelete">
          <i class="bi bi-trash"></i> Удалить
        </button>
      </div>
    </div>

    <!-- Загрузка и ошибки -->
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Загрузка...</span>
      </div>
    </div>

    <div v-else-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <!-- Детали дискуссии -->
    <div v-else-if="discussion" class="discussion-details">
      <div class="card mb-4">
        <div class="card-header d-flex justify-content-between align-items-center">
          <div>
            <h1 class="h3 mb-0">{{ discussion.title }}</h1>
            <span v-if="discussion.pinned_at" class="badge bg-info me-2">Закреплено</span>
            <small>Дата создания {{ formatDate(discussion.created_at) }}</small>
          </div>
          <div class="user-info text-end">
            <div class="d-flex align-items-center justify-content-end">
              <img v-if="authorAvatar" :src="authorAvatar" alt="User profile"
                   style="width: 40px; height: 40px; border-radius: 50%" class="me-2">
              <div>
                <div>Автор: <strong>Stanislav</strong></div>
              </div>
            </div>
          </div>
        </div>
        <div class="card-body">
          <div class="content-area p-3">
            {{ discussion.content }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Секция комментариев -->
  <div class="comments-section mt-4">
    <h3 class="mb-3">Комментарии {{  }}</h3>

    <!-- Форма добавления комментария -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">Оставить комментарий</h5>
        <form @submit.prevent="submitComment">
          <div class="mb-3">
          <textarea
              class="form-control"
              v-model="newComment"
              rows="3"
              placeholder="Напишите ваш комментарий..."
              required
          ></textarea>
          </div>
          <button type="submit" class="btn btn-primary" :disabled="submitLoading">
            <span v-if="submitLoading" class="spinner-border spinner-border-sm me-2" role="status"></span>
            Отправить
          </button>
        </form>
      </div>
    </div>

    <!-- Список комментариев -->
    <div v-if="1 > 0">
      <div class="card mb-3" v-for="comment in comments" :key="comment.id">
        <div class="card-header d-flex justify-content-between align-items-center">
          <div class="d-flex align-items-center">
            <img v-if="comment.avatar" :src="comment.avatar" alt="User profile"
                 style="width: 30px; height: 30px; border-radius: 50%" class="me-2">
            <strong>{{ comment.author.name || 'Пользователь' }}</strong>
          </div>
          <small>{{ formatDate(comment.created_at) }}</small>
        </div>
        <div class="card-body">
          <p class="card-text">{{ comment.content }}</p>
          <div class="d-flex justify-content-end" v-if="isCurrentUserAuthor(comment) || isAdmin">
            <button class="btn btn-sm btn-outline-warning me-2" @click="editComment(comment)">
              <i class="bi bi-pencil"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="deleteComment(comment.id)">
              <i class="bi bi-trash"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <div class="d-flex flex-start align-items-center">
            <img v-if="authorAvatar" :src="authorAvatar" alt="User profile"
                 style="width: 40px; height: 40px; border-radius: 50%" class="me-2">
            <div>
              <h6 class="fw-bold text-primary mb-1">Stanislav</h6>
            </div>
          </div>

          <p class="mt-3 mb-4 pb-2">
            Что такое Lorem Ipsum?
          </p>

          <div class="small d-flex justify-content-start">
            <button class="btn btn-sm btn-outline-warning me-2" @click="editComment(comment)">
              <i class="bi bi-pencil"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger me-4" @click="deleteComment(comment.id)">
              <i class="bi bi-trash"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- Пагинация -->
      <div class="d-flex justify-content-center mt-4" v-if="totalPages > 1">
        <nav aria-label="Навигация по комментариям">
          <ul class="pagination">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">Пред.</a>
            </li>
            <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: page === currentPage }">
              <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">След.</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>

    <div v-else class="alert alert-info">
      Пока нет комментариев. Будьте первым, кто оставит комментарий!
    </div>

    <!-- Модальное окно для редактирования комментария -->
    <div class="modal fade" id="editCommentModal" tabindex="-1" aria-labelledby="editCommentModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editCommentModalLabel">Редактировать комментарий</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <textarea class="form-control" v-model="editCommentText" rows="3"></textarea>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
            <button type="button" class="btn btn-primary" @click="saveCommentEdit">Сохранить</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUser } from '@/service/user';
import { Discussion } from '@/types/Discussion';
import axios from 'axios';

export default defineComponent({
  setup() {
    axios.defaults.baseURL = process.env.VUE_APP_BACKEND_HOST;
    const route = useRoute();
    const router = useRouter();
    const { userInfo, isAuthenticated, getAvatarUrl } = useUser();

    const discussionId = computed(() => Number(route.params.id));
    const discussion = ref<Discussion | null>(null);
    const loading = ref(true);
    const error = ref<string | null>(null);
    const authorAvatar = ref<string | null>('');

    // Флаги для разрешений
    const isOwner = computed(() => {
      return discussion.value?.user_id === userInfo.value?.id;
    });

    const isAdmin = computed(() => {
      return userInfo.value?.role === 'admin';
    });

    // Загрузка дискуссии
    const loadDiscussion = async () => {
      loading.value = true;
      error.value = null;

      try {
        const response = await axios.get(`/discussion/${discussionId.value}`);
        discussion.value = response.data
        const avatarUrl = await getAvatarUrl(response.data!.user_id);
        authorAvatar.value = avatarUrl
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Failed to load discussion';
      } finally {
        loading.value = false;
      }
    };

    // Форматирование даты
    const formatDate = (dateString: string | null | undefined) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    };

    // Навигация назад
    const goBack = () => {
      router.push('/discussions');
    };


  // Редактирование дискуссии
  const editDiscussion = () => {
    router.push(`/discussion/${discussionId.value}/edit`);
  };

  // Удаление дискуссии
  const confirmDelete = async () => {
    if (!confirm('Are you sure you want to delete this discussion?')) return;

    try {
      //await axios.delete('/discussion/${discussionId.value}');
      router.push('/discussions');
    } catch (err) {
      console.error('Error deleting discussion:', err);
    }
  };

  // Загрузка данных при монтировании
  onMounted(() => {
  loadDiscussion();
});

return {
  discussion,
  loading,
  error,
  isOwner,
  isAdmin,
  userInfo,
  isAuthenticated,
  formatDate,
  goBack,
  editDiscussion,
  confirmDelete,
  authorAvatar
};
}
});
</script>

<style scoped>
.content-area {
  min-height: 200px;
  background-color: #f9f9f9;
  border-radius: 5px;
}

.replies-list {
  max-height: 600px;
  overflow-y: auto;
}
</style>
