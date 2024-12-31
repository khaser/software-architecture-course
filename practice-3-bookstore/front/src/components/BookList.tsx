import { MouseEventHandler, useEffect, useState } from "react";
import { apiurl } from "../contsants";
import List from "./List";
import BookItem from "./BookItem";
import { Book } from "./Model";
import { Button, Input, Space } from "antd";


const BookList = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [keyword, setKeyword] = useState<string>("");

    const getAllBooks = async () => {
        const response = await fetch(
          `${apiurl}/books`
        ).then((response) => response.json());
        console.log(response)
        setBooks(response);
      };

    const searchBooks = async () => {
        const response = await fetch(
          `${apiurl}/books/search?keyword=${keyword}`
        ).then((response) => response.json());
        console.log(response)
        setBooks(response);
      };

    const search = (e: any) =>  {
        searchBooks()
    }

    useEffect(() => {
        getAllBooks();
    }, []);

    return (
        <>
        <Space.Compact style={{ width: '100%', margin: 20 }}>
        <Input placeholder="Filter by title" value={keyword} onChange={e => setKeyword(e.target.value)} />
        <Button type="primary" onClick={search}>Search</Button>
        </Space.Compact>
        {/* <div className="flex justify-between">
            <input className="w-full pl-5 pr-5" type="text" placeholder="Filter by title" value={keyword} onChange={e => setKeyword(e.target.value)} />
            <button color="primary" onClick={search}>Search</button>
        </div> */}
        <List>
            {books.map((book : Book) => (
            <BookItem key={book.id} {...book} />
            ))}
        </List>
        </>
    );

}

export default BookList;