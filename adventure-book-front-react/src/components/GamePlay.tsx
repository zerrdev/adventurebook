import React, { useCallback, useEffect, useState } from 'react';
import { chooseOption, getGameOptions, getGameSection } from '../api/client';
import { GameDTO, GameOptionsDTO, GameSectionDTO } from '../api/types';

interface Props {
    initialGame: GameDTO;
    onRestart: () => void;
}

const isFinished = (game: GameDTO, section?: GameSectionDTO) =>
    game.gameState === 'FINISHED' || game.healthPoints <= 0 || section?.type === 'END';

const GamePlay: React.FC<Props> = ({ initialGame, onRestart }) => {
    const [game, setGame] = useState<GameDTO>(initialGame);
    const [section, setSection] = useState<GameSectionDTO>();
    const [options, setOptions] = useState<GameOptionsDTO[]>([]);
    const [error, setError] = useState<string>();
    const [loading, setLoading] = useState(true);

    const load = useCallback(async (current: GameDTO) => {
        setLoading(true);
        setOptions([]);
        try {
            setError(undefined);
            const currentSection = await getGameSection(current.gameId);
            setSection(currentSection);
            setOptions(isFinished(current, currentSection) ? [] : await getGameOptions(current.gameId));
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        load(initialGame);
    }, [initialGame, load]);

    const choose = async (gotoId: number) => {
        setLoading(true);
        setError(undefined);
        try {
            const updated = await chooseOption(game.gameId, { gotoId });
            setGame(updated);
            await load(updated);
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setLoading(false);
        }
    };

    const finished = isFinished(game, section);

    return (
        <section aria-label="Gameplay">
            <nav aria-label="Game status and exit">
                <ul><li><strong>Health points: {game.healthPoints}</strong></li></ul>
                {!finished && <ul><li><button className="secondary outline" onClick={onRestart}>Give up</button></li></ul>}
            </nav>
            {error && <p role="alert"><strong>Error:</strong> {error}</p>}
            {game.consequenceText && <p><em>{game.consequenceText}</em></p>}
            {section && <p>{section.text}</p>}
            {loading && <p role="status" aria-busy="true">Loading...</p>}
            {!loading && !finished && (
                <fieldset aria-label="Choices">
                    {options.map(option => (
                        <div role="group" key={option.gotoId}>
                            <button onClick={() => choose(option.gotoId)}>{option.description}</button>
                        </div>
                    ))}
                </fieldset>
            )}
            {!loading && finished && (
                <div>
                    <h3>{game.healthPoints <= 0 ? 'You died. Game over.' : 'The end.'}</h3>
                    <button onClick={onRestart}>New game</button>
                </div>
            )}
        </section>
    );
};

export default GamePlay;
