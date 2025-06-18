FROM maven:3.8.5-openjdk-17 AS build

COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM openjdk:17-alpine

# Установка необходимых инструментов
RUN apk add --no-cache wget ca-certificates sudo

# Создание директории для сертификатов
RUN mkdir -p /usr/local/share/ca-certificates/Yandex

# Загрузка сертификата Яндекса
RUN wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" \
    --output-document /usr/local/share/ca-certificates/Yandex/YandexInternalRootCA.crt && \
    chmod 0655 /usr/local/share/ca-certificates/Yandex/YandexInternalRootCA.crt

# Обновление хранилища сертификатов
RUN update-ca-certificates

# Создание директории для хранилища сертификатов Java, если её ещё нет
RUN mkdir -p /etc/security

# Добавление сертификата в Java Key Store
RUN keytool -importcert \
    -alias YandexCA -file /usr/local/share/ca-certificates/Yandex/YandexInternalRootCA.crt \
    -keystore /etc/security/ssl -storepass root1234 \
    --noprompt

COPY --from=build /target/*.jar messanger-app.jar
COPY src/main/resources/root.crt /root/.postgresql/root.crt
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "messanger-app.jar"]
