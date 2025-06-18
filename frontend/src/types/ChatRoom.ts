import {User} from "./User";

export interface ChatRoom {
    id: number
    user1: User
    user2: User
    createdAt: Date
}