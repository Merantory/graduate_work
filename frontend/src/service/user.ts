import { ref, computed, reactive } from 'vue';
import axios from "axios";

const serverUrl = process.env.VUE_APP_OAUTH_URL;

export interface UserInfo {
    user_id?: number;
    email?: string;
    username?: string;
    role?: string;
    deletedAt?: string;

    [key: string]: any;
}

const tokenInfo = ref<any>(null);
const ACCESS_TOKEN_KEY = "access_token";

function setTokenInfo(info: any) {
    tokenInfo.value = info;
}

const userInfo = computed<UserInfo | null>(() => {
    if (!tokenInfo.value) return null;

    if (tokenInfo.value.user_id && tokenInfo.value.email) {
        return {
            user_id: tokenInfo.value.user_id,
            email: tokenInfo.value.email,
            username: tokenInfo.value.username,
            role: tokenInfo.value.role,
            deletedAt: tokenInfo.value.deletedAt
        };
    }

    if (tokenInfo.value.user_info) {
        return tokenInfo.value.user_info;
    }

    const possibleFields = ['user', 'userInfo', 'principal'];
    for (const field of possibleFields) {
        if (tokenInfo.value[field]) {
            return tokenInfo.value[field];
        }
    }


    return null;
});

const isAuthenticated = computed(() => {
    return !!userInfo.value;
});

const tokenInfoFormatted = computed(() => {
    return JSON.stringify(tokenInfo.value, null, 2);
});

const userRole = computed(() => {
    return userInfo.value?.role || null;
});

const userId = computed(() => {
    return userInfo.value!.user_id || 0;
})

const username = computed(() => {
    return userInfo.value!.username || "";
})

const getAvatarUrl = async (user_id: number): Promise<string | null> => {
    axios.defaults.baseURL = process.env.VUE_APP_BACKEND_HOST;
    try {
        const responseKey = await axios.get(
            `/users/${user_id}/profile/picture`
        );
        const responseAvatar = await axios.get(
            `/users/images?key=${responseKey.data.key}`,
            {
                responseType: 'blob'
            }
        );
        return URL.createObjectURL(responseAvatar.data);
    } catch (error) {
        return null;
    }
};

function logout() {

    tokenInfo.value = null;
    window.sessionStorage.clear()
}

export function useUser() {
    return {
        tokenInfo,
        userInfo,
        userId,
        username,
        isAuthenticated,
        tokenInfoFormatted,
        userRole,
        setTokenInfo,
        getAvatarUrl,
        logout
    };
}
