import {ChatRoom} from "./ChatRoom";
import {User} from "./User";

export interface Message {
    id: number
    sender: User
    receiver: User
    content: string
    chatRoom: ChatRoom
    sentAt: Date
}