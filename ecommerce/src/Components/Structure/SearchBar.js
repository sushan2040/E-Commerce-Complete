import "../../App.css"
export default function SearchBar() {
    return (
        <div className="search-container " style={{ width: '40rem' }}>
            <input type="text" className="form-control search-input" placeholder="Search..." />
            <i className="fas fa-search search-icon"></i>
        </div>
    )
}