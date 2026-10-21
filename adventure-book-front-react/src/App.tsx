import React, { useState } from 'react';
import { createGame } from './api/client';
import { GameDTO } from './api/types';
import BookSelection from './components/BookSelection';
import GamePlay from './components/GamePlay';

const playerId = Math.random().toString(36).substring(2);

const App: React.FC = () => {
    const [game, setGame] = useState<GameDTO>();
    const [error, setError] = useState<string>();

    const start = async (bookId: number) => {
        try {
            setError(undefined);
            setGame(await createGame({ playerId, bookId }));
        } catch (e) {
            setError((e as Error).message);
        }
    };

    return (
        <div>
            <h1>Adventure Book</h1>
            {error && <p>Could not start game: {error}</p>}
            {game
                ? <GamePlay key={game.gameId} initialGame={game} onRestart={() => setGame(undefined)} />
                : <BookSelection onStart={start} />}
        </div>
    );
};

export default App;
