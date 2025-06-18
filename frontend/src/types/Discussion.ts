export interface Discussion {
    id: number
    user_id: number
    user_avatar_url: string | null,
    title: string
    content: string
    pinned_at: string | null;  // ISO 8601 string format
    created_at: string
    deleted_at: string | null;
}