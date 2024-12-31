import { Link } from "react-router-dom";
import { Book } from "./Model";

export default function BookItem(book : Book ) {
    return (
      <article className="flex items-start space-x-6 p-6">
        <div className="min-w-0 relative flex-auto">
          <h2 className="font-semibold text-slate-900 truncate pr-20"><Link to={`/books/${book.id}`}>{book.name}</Link></h2>
          <dl className="mt-2 flex flex-wrap text-sm leading-6 font-medium">
            <div>
              <dt className="sr-only">Rating</dt>
              <dd className="px-1.5 ring-1 ring-slate-200 rounded">{book.rating}</dd>
            </div>
            <div>
              <dt className="sr-only">Category</dt>
              <dd className="flex items-center">
                <svg width="2" height="2" fill="currentColor" className="mx-2 text-slate-300" aria-hidden="true">
                  <circle cx="1" cy="1" r="1" />
                </svg>
                {book.categories.join(", ")}
              </dd>
            </div>
            <div className="flex-none w-full mt-2 font-normal">
              <dt className="sr-only">Cast</dt>
              <dd className="text-slate-400">{book.authors.map((it) => it.name).join(", ")}</dd>
            </div>
          </dl>
        </div>
      </article>
    )
  }