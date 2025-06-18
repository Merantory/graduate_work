import { ref, computed } from 'vue';
import SockJS from 'sockjs-client';
import { Client, IMessage, IFrame } from '@stomp/stompjs';
import { Message } from '@/types/Message';
import { User } from '@/types/User';
import { useUser } from "@/service/user";
import {ChatRoom} from "@/types/ChatRoom";

const MESSANGER_API_URL = process.env.VUE_APP_MESSANGER_HOST;
const MESSANGER_SOCKET_URL = process.env.VUE_APP_MESSANGER_HOST + '/ws';
const SSO_USER_API_URL = process.env.VUE_APP_OAUTH_URL
const BACKEND_API_URL = process.env.VUE_APP_BACKEND_HOST;

const currentUser = ref<User | null>(null);
const messages = ref<Message[]>([]);
const selectedChatRoom = ref<number | null>(null);
const contacts = ref<User[]>([]);
const connected = ref(false);
let client: Client | null = null;

export const connect = (username: string) => {
    if (useUser().userId.value != null) {
        client = new Client({
            webSocketFactory: () => new SockJS(MESSANGER_SOCKET_URL),
            connectHeaders: {
                username: useUser().username.value
            },
            debug: (msg: string) => console.log(msg),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: (frame: IFrame) => {
                onConnected(frame);
                console.log("WebSocket connected");
            },
            onDisconnect: () => {
                connected.value = false;
                console.log('Disconnected from WebSocket');
            },
            onStompError: (frame: IFrame) => {
                console.error('STOMP error', frame);
            }
        });
        currentUser.value = { id: useUser().userId.value, name: useUser().username.value, user_avatar_url: null };
        client.activate();
    }
}

const onConnected = (frame: IFrame) => {
    connected.value = true;
    if (!client || !currentUser.value) return;
    client.subscribe(`/user/queue/messages`, onMessageReceived);
    client.publish({
        destination: '/app/chat.addUser',
        body: JSON.stringify({ username: currentUser.value.name }),
        headers: {}
    });
    loadContacts();
}

const onMessageReceived = (payload: IMessage) => {
    const message = JSON.parse(payload.body) as Message;
    message.sentAt = new Date(message.sentAt);
    messages.value.push(message);
}

export const sendMessage = (content: string, receiverUsername: string) => {
    if (!client || !connected.value || !currentUser.value) {
        console.error('Cannot send message: not connected');
        return;
    }
    const payload = {
        sender: useUser().username.value,
        content: content,
        receiver: receiverUsername
    };
    client.publish({
        destination: '/app/chat.sendMessage',
        body: JSON.stringify(payload),
        headers: {}
    });
}

export const loadChatHistory = async (chatRoomId: number) => {
    try {
        selectedChatRoom.value = chatRoomId;
        const response = await fetch(`${MESSANGER_API_URL}/api/messages/${chatRoomId}`);
        if (!response.ok) {
            throw new Error('Failed to load chat history');
        }
        const data = await response.json();
        messages.value = data.map((msg: any) => ({
            ...msg,
            sentAt: new Date(msg.sentAt)
        }));
    } catch (error) {
        console.error('Error loading chat history:', error);
    }
}

export const loadContacts = async () => {
    try {
        const response = await fetch(`${SSO_USER_API_URL}/users`);
        if (!response.ok) {
            throw new Error('Failed to load contacts');
        }

        const data = await response.json();
        contacts.value = data.filter((user: User) =>
            user.id !== currentUser.value?.id
        );
        for (let i = 0; i < contacts.value.length; ++i) {
            contacts.value[i].user_avatar_url = await useUser().getAvatarUrl(contacts.value[i].id);
        }
    } catch (error) {
        console.error('Error loading contacts:', error);
    }
}
export const disconnect = () => {
    if (client && connected.value) {
        client.deactivate();
    }
}

export const useChat = () => {
    return {
        currentUser,
        messages,
        selectedChatRoom,
        contacts,
        connected,
        connect,
        sendMessage,
        loadChatHistory,
        loadContacts,
        disconnect
    };
}