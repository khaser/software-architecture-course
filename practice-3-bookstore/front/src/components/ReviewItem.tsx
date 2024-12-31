import { Review, UserReview, CriticReview } from "./Model";

export const UserReviewItem = (r : UserReview) => {
    if (!r.isPublished) {
        return <></>
    }
    return (
      <article className="flex items-start space-x-6 p-6">
        <div className="min-w-0 relative flex-auto">
          <h2 className="font-semibold text-slate-900 truncate pr-20">UserID: {r.userID}</h2>
          <dl className="mt-2 flex flex-wrap text-sm leading-6 font-medium">
            <div>
              <dt className="sr-only">Rating</dt>
              <dd className="px-1.5 ring-1 ring-slate-200 rounded">{r.rating}</dd>
            </div>
            <div className="flex-none w-full mt-2 font-normal">
              <dt className="sr-only">Description</dt>
              <dd className="text-slate-400">{r.desc}</dd>
            </div>
          </dl>
        </div>
      </article>
    )
  }

  export const CriticReviewItem = (r : CriticReview) => {
    return (
      <article className="flex items-start space-x-6 p-6">
        <div className="min-w-0 relative flex-auto">
          <h2 className="font-semibold text-slate-900 truncate pr-20">Critic: {r.name}</h2>
          <dl className="mt-2 flex flex-wrap text-sm leading-6 font-medium">
            <div>
              <dt className="sr-only">Rating</dt>
              <dd className="px-1.5 ring-1 ring-slate-200 rounded">{r.rating}</dd>
            </div>
            <div className="flex-none w-full mt-2 font-normal">
              <dt className="sr-only">Cast</dt>
              <dd className="text-slate-400">{r.desc}</dd>
            </div>
          </dl>
        </div>
      </article>
    )
  }