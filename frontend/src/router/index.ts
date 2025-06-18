import {createRouter, createWebHistory} from 'vue-router'
import DiscussionView from "@/views/DiscussionView.vue";
import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/LoginView.vue";
import CreateDiscussionView from "@/views/CreateDiscussionView.vue";
import DiscussionDetailsView from "@/views/DiscussionDetailsView.vue";
import UserProfileView from "@/views/UserProfileView.vue";
import login from "@/service/login";
import ChatView from "@/views/ChatView.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'HomeView',
            redirect: '/home'
        },
        {
            path: '/home',
            component: HomeView
        },
        {
            path: "/discussion",
            component: DiscussionView
        },
        {
            path: '/discussion/create',
            name: 'CreateDiscussionView',
            component: CreateDiscussionView
        },
        {
            path: '/discussion/:id',
            name: 'DiscussionDetailsView',
            component: DiscussionDetailsView,
            props: true
        },
        {
            path: "/login",
            name: 'LoginView',
            component: LoginView
        },
        {
            path: '/messages',
            component: ChatView
        },
        {
            path: '/profile/:id',
            name: 'UserProfileView',
            component: UserProfileView,
            props: true
        },
    ]
})

router.beforeEach((to, from, next) => {

    // если путь равен /code, то пытаемся достать параметр code из запроса, запросить токены, и после их получения
    // сделать переход на домашнюю страницу
    if (to.path === '/code' && to.query.code != null) {
        login.getTokens(to.query.code.toString() || " ").then(() => {
            next({name: 'HomeView'});
        });
    } else {
        next()
    }
});

export default router