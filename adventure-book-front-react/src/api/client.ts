import { BookDTO, ChooseOptionDTO, CreateGameDTO, GameDTO, GameOptionsDTO, GameSectionDTO } from './types';

const BASE_URL = '/api';

const request = async <T>(path: string, init?: RequestInit): Promise<T> => {
    const response = await fetch(`${BASE_URL}${path}`, {
        ...init,
        headers: { 'Content-Type': 'application/json' },
    });
    if (!response.ok) {
        throw new Error(`${response.status} ${response.statusText}`);
    }
    return response.json();
};

export const getBooks = () => request<BookDTO[]>('/books');

export const createGame = (body: CreateGameDTO) =>
    request<GameDTO>('/game/', { method: 'POST', body: JSON.stringify(body) });

export const getGameSection = (gameId: string) => request<GameSectionDTO>(`/game/section/${gameId}`);

export const getGameOptions = (gameId: string) => request<GameOptionsDTO[]>(`/game/options/${gameId}`);

export const chooseOption = (gameId: string, body: ChooseOptionDTO) =>
    request<GameDTO>(`/game/choose/${gameId}`, { method: 'POST', body: JSON.stringify(body) });
