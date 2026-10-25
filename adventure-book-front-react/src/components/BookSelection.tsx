import React, { useEffect, useState } from 'react';
import { getBook, getBooks } from '../api/client';
import { BookDetailsDTO, BookDTO } from '../api/types';

interface Props {
    onStart: (bookId: number) => void;
}

const BookSelection: React.FC<Props> = ({ onStart }) => {
    const [books, setBooks] = useState<BookDTO[]>([]);
    const [bookId, setBookId] = useState<number>();
    const [details, setDetails] = useState<BookDetailsDTO>();
    const [error, setError] = useState<string>();

    useEffect(() => {
        getBooks()
            .then(result => {
                setBooks(result);
                setBookId((result.find(book => book.valid) ?? result[0])?.id);
            })
            .catch(e => setError(e.message));
    }, []);

    useEffect(() => {
        setDetails(undefined);
        if (bookId === undefined) {
            return;
        }
        let current = true;
        getBook(bookId)
            .then(result => current && setDetails(result))
            // details only add the unreachable sections, so a failure here must not block the selection
            .catch(() => current && setDetails(undefined));
        return () => {
            current = false;
        };
    }, [bookId]);

    if (error) {
        return <p className="error">Could not load books: {error}</p>;
    }

    const selected = books.find(book => book.id === bookId);

    return (
        <div className="selection">
            <h2>Choose a book</h2>
            <select value={bookId ?? ''} onChange={e => setBookId(Number(e.target.value))}>
                {books.map(book => (
                    <option key={book.id} value={book.id}>
                        {book.title} - {book.author}{book.valid ? '' : ' (invalid)'}
                    </option>
                ))}
            </select>
            <button
                disabled={selected === undefined || !selected.valid}
                onClick={() => selected !== undefined && onStart(selected.id)}
            >
                Start game
            </button>
            {selected && !selected.valid && (
                <div>
                    <p>This book is invalid and cannot be played:</p>
                    <ul>
                        {selected.errors.map((bookError, index) => (
                            <li key={index}>{bookError}</li>
                        ))}
                    </ul>
                </div>
            )}
            {details && details.unreachableSections.length > 0 && (
                <p>Unreachable sections: {details.unreachableSections.join(', ')}</p>
            )}
        </div>
    );
};

export default BookSelection;
