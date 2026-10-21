export type BookSectionType = 'BEGIN' | 'NODE' | 'END';

export type GameState = 'STARTED' | 'FINISHED';

export interface BookDTO {
    id: number;
    title: string;
    author: string;
}

export interface CreateGameDTO {
    playerId: string;
    bookId: number;
}

export interface GameDTO {
    gameId: string;
    playerId: string;
    sectionId: number;
    healthPoints: number;
    gameState?: GameState;
}

export interface GameOptionsDTO {
    description: string;
    gotoId: number;
}

export interface GameSectionDTO {
    text: string;
    type?: BookSectionType;
}

export interface ChooseOptionDTO {
    gotoId: number;
}
