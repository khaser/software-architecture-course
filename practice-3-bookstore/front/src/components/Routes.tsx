import { Routes, Route } from "react-router-dom";
import BookList from './BookList'
import BookDetails from "./BookDetails";

const AppRoutes = () => (
    <Routes>
        <Route path="/" element={<BookList />} />
        <Route path="/books/:id" element={<BookDetails />}></Route>

    </Routes>
);

export default AppRoutes;