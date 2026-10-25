import React, { useState } from 'react';
import { createGame } from './api/client';
import { GameDTO } from './api/types';
import BookSelection from './components/BookSelection';
import GamePlay from './components/GamePlay';
import BookExplorer from './components/BookExplorer';

const playerId = Math.random().toString(36).substring(2);

const App: React.FC = () => {
    const [game, setGame] = useState<GameDTO>();
    const [exploringBookId, setExploringBookId] = useState<number>();
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
        <main className="container" style={{ maxWidth: '44rem', minHeight: '100dvh', display: 'grid', alignContent: 'center', paddingBlock: '1.5rem' }}>
            <article style={{ margin: 0 }}>
                <header><h1 style={{ fontSize: '1.5rem' }}>Adventure Book</h1></header>
                {error && <p role="alert"><strong>Could not start game:</strong> {error}</p>}
                {exploringBookId !== undefined
                    ? <BookExplorer bookId={exploringBookId} onExit={() => setExploringBookId(undefined)} />
                    : game
                    ? <GamePlay key={game.gameId} initialGame={game} onRestart={() => setGame(undefined)} />
                    : <BookSelection onStart={start} onExplore={bookId => {
                        setError(undefined);
                        setExploringBookId(bookId);
                    }} />}
            </article>
        </main>
    );
};

export default App;
