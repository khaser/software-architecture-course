export type Rating = number;

export type Name = string;

export interface Author {
    name: Name
}

export interface Book {
    id: number,
    name: string,
    desc: string,
    rating: Rating,
    categories: string[],
    authors: Author[],
}

export interface Review {
    id: number,
    desc: String,
    rating: number,
}

export interface UserReview extends Review {
    userID: number,
    desc: string,
    rating: Rating,
    isPublished: Boolean,
    type: 'User'
}

export interface CriticReview extends Review {
    name: Name,
    desc: String,
    rating: Rating,
    type: 'Critic'
}