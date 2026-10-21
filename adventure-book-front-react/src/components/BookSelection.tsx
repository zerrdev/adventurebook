import React, { useEffect, useState } from 'react';
import { getBooks } from '../api/client';
import { BookDTO } from '../api/types';

interface Props {
    onStart: (bookId: number) => void;
}

const BookSelection: React.FC<Props> = ({ onStart }) => {
    const [books, setBooks] = useState<BookDTO[]>([]);
    const [bookId, setBookId] = useState<number>();
    const [error, setError] = useState<string>();

    useEffect(() => {
        getBooks()
            .then(result => {
                setBooks(result);
                setBookId(result[0]?.id);
            })
            .catch(e => setError(e.message));
    }, []);

    if (error) {
        return <p>Could not load books: {error}</p>;
    }

    return (
        <div>
            <h2>Choose a book</h2>
            <select value={bookId ?? ''} onChange={e => setBookId(Number(e.target.value))}>
                {books.map(book => (
                    <option key={book.id} value={book.id}>
                        {book.title} - {book.author}
                    </option>
                ))}
            </select>
            <button disabled={bookId === undefined} onClick={() => bookId !== undefined && onStart(bookId)}>
                Start game
            </button>
        </div>
    );
};

export default BookSelection;
