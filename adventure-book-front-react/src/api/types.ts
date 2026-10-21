export type BookSectionType = 'BEGIN' | 'NODE' | 'END';

export type GameState = 'STARTED' | 'FINISHED';

export interface BookDTO {
    id: number;
    title: string;
    author: string;
    valid: boolean;
    errors: string[];
}

export interface BookDetailsDTO extends BookDTO {
    unreachableSections: number[];
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
    consequenceText?: string | null;
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
