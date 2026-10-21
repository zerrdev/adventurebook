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

    const load = useCallback(async (current: GameDTO) => {
        try {
            setError(undefined);
            const currentSection = await getGameSection(current.gameId);
            setSection(currentSection);
            setOptions(isFinished(current, currentSection) ? [] : await getGameOptions(current.gameId));
        } catch (e) {
            setError((e as Error).message);
        }
    }, []);

    useEffect(() => {
        load(initialGame);
    }, [initialGame, load]);

    const choose = async (gotoId: number) => {
        try {
            const updated = await chooseOption(game.gameId, { gotoId });
            setGame(updated);
            await load(updated);
        } catch (e) {
            setError((e as Error).message);
        }
    };

    const finished = isFinished(game, section) || (section !== undefined && options.length === 0);

    return (
        <div>
            <p>Health points: {game.healthPoints}</p>
            {error && <p>Error: {error}</p>}
            {section && <p>{section.text}</p>}
            {!finished && (
                <ul>
                    {options.map(option => (
                        <li key={option.gotoId}>
                            <button onClick={() => choose(option.gotoId)}>{option.description}</button>
                        </li>
                    ))}
                </ul>
            )}
            {finished && (
                <div>
                    <h3>{game.healthPoints <= 0 ? 'You died. Game over.' : 'The end.'}</h3>
                    <button onClick={onRestart}>New game</button>
                </div>
            )}
        </div>
    );
};

export default GamePlay;
