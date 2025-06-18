<template>
  <div
      id="editProfileModal"
      ref="editProfileModal"
      class="modal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Редактировать профиль</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="form-group mb-3">
            <div class="d-flex justify-content-center mb-3">
              <div class="position-relative">
                <div
                    class="upload-icon-wrapper"
                    @click="triggerFileInput"
                >
                  <i class="bi bi-camera"></i>
                </div>
                <div v-if="uploadProgress > 0 && uploadProgress < 100" class="upload-progress">
                  <div class="progress">
                    <div
                        class="progress-bar"
                        role="progressbar"
                        :style="{width: uploadProgress + '%'}"
                        :aria-valuenow="uploadProgress"
                        aria-valuemin="0"
                        aria-valuemax="100">
                      {{ uploadProgress }}%
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <input
                ref="fileInput"
                type="file"
                accept="image/*"
                class="d-none"
                @change="handleFileUpload"
            />
            <div class="text-center text-muted small mb-3" v-if="!uploadError">
              Нажмите на изображение, чтобы загрузить новый аватар
              <p class="mt-1">Максимальный размер: 5MB</p>
            </div>
            <div class="text-center text-danger small mb-3" v-if="uploadError">
              {{ uploadError }}
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
          <button
              type="button"
              class="btn btn-primary"
              @click="saveChanges"
              :disabled="isUploading"
          >
            <span v-if="isUploading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
            Применить изменения
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue"
import {Modal} from 'bootstrap'
import {useUser} from '@/service/user'
import axios from "axios";

export default defineComponent({
  setup() {
    const {
      userInfo,
      isAuthenticated,
      tokenInfoFormatted,
      userRole,
      logout
    } = useUser();

    const fileInput = ref<HTMLInputElement | null>(null);
    const imagePreview = ref<string | null>(null);
    const imageFile = ref<File | null>(null);
    const uploadProgress = ref(0);
    const uploadError = ref<string | null>(null);
    const isUploading = ref(false);

    return {
      userInfo,
      isAuthenticated,
      tokenInfoFormatted,
      userRole,
      logout,
      fileInput,
      imagePreview,
      imageFile,
      uploadProgress,
      uploadError,
      isUploading,
    };
  },
  methods: {
    showModal() {
      window.setTimeout(() => Modal.getOrCreateInstance('#editProfileModal')?.show(), 50)
    },
    triggerFileInput() {
      if (this.fileInput) {
        this.fileInput.click();
      }
    },

    handleFileUpload(event: Event) {
      const target = event.target as HTMLInputElement;
      if (!target.files || target.files.length === 0) {
        return;
      }

      const file = target.files[0];

      if (!file.type.match('image.*')) {
        this.uploadError = 'Выбранный файл не является изображением';
        return;
      }

      if (file.size > 5 * 1024 * 1024) {
        this.uploadError = 'Размер файла превышает 5MB';
        return;
      }

      this.uploadError = null;
      this.imageFile = file;

      const reader = new FileReader();
      reader.onload = (e) => {
        this.imagePreview = e.target?.result as string;
      };
      reader.readAsDataURL(file);
    },


    async saveChanges() {
      if (!this.imageFile) {
        Modal.getInstance('#editProfileModal')?.hide();
        return;
      }

      try {
        axios.defaults.baseURL = process.env.VUE_APP_BACKEND_HOST;
        this.isUploading = true;
        this.uploadProgress = 0;

        const formData = new FormData();
        formData.append('file', this.imageFile);
        formData.append('userId', this.userInfo?.user_id?.toString() || '');

        const response = await axios.post('/users/profile/picture/upload/', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          onUploadProgress: (progressEvent) => {
            if (progressEvent.total) {
              this.uploadProgress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
            }
          }
        });

        this.uploadProgress = 100;
        setTimeout(() => {
          this.isUploading = false;
          Modal.getInstance('#editProfileModal')?.hide();
        }, 500);
      } catch (error) {
        console.error('Ошибка при загрузке изображения:', error);
        this.uploadError = 'Произошла ошибка при загрузке изображения';
        this.isUploading = false;
      }
    }

  }
})
</script>

<style scoped>

</style>
