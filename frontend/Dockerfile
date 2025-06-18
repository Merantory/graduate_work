
# Шаг 1: Сборка приложения
FROM node:20-alpine as build-stage

WORKDIR /app

# Копируем package.json и package-lock.json
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем исходный код приложения
COPY . .

# Создаем динамический .env файл для сборки
RUN echo "VUE_APP_OAUTH_REDIRECT_URI=$VUE_APP_OAUTH_REDIRECT_URI" > .env.production && \
    echo "VUE_APP_OAUTH_CLIENT_ID=$VUE_APP_OAUTH_CLIENT_ID" >> .env.production && \
    echo "VUE_APP_OAUTH_AUTH_HEADER=$VUE_APP_OAUTH_AUTH_HEADER" >> .env.production && \
    echo "VUE_APP_OAUTH_URL=$VUE_APP_OAUTH_URL" >> .env.production && \
    echo "VUE_APP_BACKEND_HOST=$VUE_APP_BACKEND_HOST" >> .env.production && \
    echo "VUE_APP_MESSANGER_HOST=$VUE_APP_MESSANGER_HOST" >> .env.production

# Собираем приложение для продакшна с переменными из .env.production
RUN npm run build

# Шаг 2: Настройка nginx для раздачи приложения
FROM nginx:stable-alpine as production-stage

# Копируем собранное приложение из build-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Настройка nginx для работы с SPA (Single Page Application)
RUN echo 'server {\
    listen 80;\
    server_name localhost;\
    root /usr/share/nginx/html;\
    index index.html;\
    location / {\
        try_files $uri $uri/ /index.html;\
    }\
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
