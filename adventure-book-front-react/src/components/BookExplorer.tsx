import React, { useEffect, useState } from 'react';
import { getExplorationNode, getExplorationNodes } from '../api/client';
import { ExplorationNode, ExplorationNodeSummary } from '../api/types';

interface Props {
    bookId: number;
    onExit: () => void;
}

const BookExplorer: React.FC<Props> = ({ bookId, onExit }) => {
    const [nodes, setNodes] = useState<ExplorationNodeSummary[]>([]);
    const [node, setNode] = useState<ExplorationNode>();
    const [history, setHistory] = useState<number[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string>();

    useEffect(() => {
        let current = true;
        const load = async () => {
            try {
                const index = await getExplorationNodes(bookId);
                if (!current) return;
                setNodes(index);
                const first = index.find(item => item.type === 'BEGIN') ?? index[0];
                if (first) {
                    const initial = await getExplorationNode(bookId, first.id);
                    if (current) setNode(initial);
                }
            } catch (e) {
                if (current) setError((e as Error).message);
            } finally {
                if (current) setLoading(false);
            }
        };
        load();
        return () => { current = false; };
    }, [bookId]);

    const navigate = async (id: number, back = false) => {
        setLoading(true);
        setError(undefined);
        try {
            const next = await getExplorationNode(bookId, id);
            setHistory(previous => back ? previous.slice(0, -1) : node ? [...previous, node.id] : previous);
            setNode(next);
        } catch (e) {
            setError('This choice is unavailable. Try another choice or go back.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="game explorer">
            {error && <p className="error" role="alert">{error}</p>}
            {!loading && !error && nodes.length === 0 && <p>This book has no story to explore.</p>}
            {node && <>
                <p>{node.text}</p>
            </>}
            {loading && <p role="status">Loading...</p>}
            {!loading && node && <>
                <ul className="options">
                    {node.options.map((option, index) => <li key={index}>
                        <button disabled={loading} onClick={() => navigate(option.gotoId)}>
                            {option.description}
                        </button>
                    </li>)}
                </ul>
                {node.options.length === 0 && (node.type === 'END'
                    ? <h3>The end.</h3>
                    : <p>No choices available here.</p>)}
            </>}
            <div className="selection-actions">
                <button disabled={loading || history.length === 0}
                    onClick={() => navigate(history[history.length - 1], true)}>Back</button>
                <button onClick={onExit}>Back to books</button>
            </div>
        </div>
    );
};

export default BookExplorer;
