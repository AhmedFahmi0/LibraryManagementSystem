CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_year timestamp,
    ISBN VARCHAR(13),
    UNIQUE (ISBN),
    available BOOLEAN DEFAULT true 
);


CREATE TABLE IF NOT EXISTS patrons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS borrowing_records (
    id SERIAL PRIMARY KEY,
    book_id BIGINT,
    patron_id BIGINT,
    borrowing_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP,
    CONSTRAINT FK_BorrowingRecords_BookId
        FOREIGN KEY (book_id)
        REFERENCES books(id),
    CONSTRAINT FK_BorrowingRecords_PatronId
        FOREIGN KEY (patron_id)
        REFERENCES patrons(id)
);


CREATE TABLE IF NOT EXISTS user_credentials (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);