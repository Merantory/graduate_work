<template>
  <div class="container mt-4">
    <div class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Создать обсуждение</h5>
        <button type="button" class="btn-close" @click="goBack"></button>
      </div>
      <div class="card-body">
        <form @submit.prevent="submitDiscussion">
          <div class="mb-3">
            <label for="title" class="form-label">Заголовок</label>
            <input
                type="text"
                class="form-control"
                id="title"
                v-model="discussion.title"
                required
            >
          </div>
          <div class="mb-3">
            <label for="content" class="form-label">Содержимое</label>
            <textarea
                class="form-control"
                id="content"
                rows="5"
                v-model="discussion.content"
                required
            ></textarea>
          </div>
          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-secondary me-2" @click="goBack">Отмена</button>
            <button type="submit" class="btn btn-primary">Создать</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from "vue";
import { useUser } from '@/service/user';
import { useRouter } from "vue-router";
import axios from "axios";

export default defineComponent({
  setup() {
    axios.defaults.baseURL = process.env.VUE_APP_BACKEND_HOST;
    const router = useRouter();

    const discussion = reactive({
      user_id: 0,
      title: "",
      content: ""
    });

    const submitDiscussion = () => {
      discussion.user_id = useUser().userInfo.value!.user_id ?? 0
      axios.post('/discussion', discussion)
          .then(function (response) {
            console.log(response)
          }).catch(function (error) {
            console.log(error)
          })
      router.push('/discussion'); // или на страницу со списком дискуссий
    };

    const goBack = () => {
      router.back();
    };

    return {
      discussion,
      submitDiscussion,
      goBack,
    };
  }
});
</script>
