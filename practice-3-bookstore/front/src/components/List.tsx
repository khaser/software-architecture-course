export default function List({ children } : any) {
    return (
      <ul className="divide-y divide-slate-100">
        {children}
      </ul>
    )
  }