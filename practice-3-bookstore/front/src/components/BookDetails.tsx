import { useParams } from "react-router-dom";
import { Book, CriticReview, UserReview } from "./Model";
import List from "./List";
import { apiurl } from "../contsants";
import { useEffect, useState } from "react";
import { CriticReviewItem, UserReviewItem } from "./ReviewItem";

const BookDetails = () => {
	const { id } = useParams()
    const [book, setBook] = useState<Book>();
    const [reviews, setReviews] = useState<(UserReview | CriticReview)[]>();

    const getBookInfo = async () => {
        const response = await fetch(
          `${apiurl}/book/${id}`
        ).then((response) => response.json());
        console.log(response);
        setBook(response);
      };

    const getReviews = async () => {
        const response = await fetch(
            `${apiurl}/book/${id}/reviews`
        ).then((response) => response.json());
        console.log(response);
        setReviews(response);
    }

    useEffect(() => {
        getBookInfo();
    }, []);


    useEffect(() => {
        getReviews();
    }, []);

    if (book == undefined || reviews == undefined) {
        return <></>;
    }

    return (
				<div className='bookdetail'>
						<div key={book.id} className='bookdetail-container'>
							<div className='ml-8 leading-loose'>
								<h2 className='font-semibold text-slate-900 truncate pr-20'>
									{book.name}
								</h2>
								<p className='font-bold'>
									Written By: {book.authors.map((it) => it.name).join(", ")}
								</p>
								<p className='mb-3 font-bold'>
									Book rating:
									{book.rating}
								</p>
								
								<p className='pt-2 leading-relaxed'>
									<span>
										{book.desc}
									</span>
								</p>

                                <ul>
                                    {reviews?.map((review) => 
                                        review.type == 'Critic' ? 
                                            <CriticReviewItem key={review.id} {...(review)}/>
                                        :
                                            <UserReviewItem key={review.id} {...(review)}/>
                                    )}
                                </ul>

							</div>
						</div>
				</div>
		);
}

export default BookDetails