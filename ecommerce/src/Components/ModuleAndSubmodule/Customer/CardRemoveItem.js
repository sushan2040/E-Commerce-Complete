import { NavLink, useNavigate } from "react-router-dom";


export default function RemoveCartItem() {
    const navigate = useNavigate();
    function goToProductViewPage() {
        navigate("/ecommerce/product/view")
    }
    return (
        <div className="col-sm-2 col-md-3 card mt-3" style={{ width: '18rem' }}>
            <div className="row justify-content-center mt-3 mb-3">
                <input className="form-check-input" type="checkbox" />
            </div>

            <img onClick={goToProductViewPage} src={require("../../../assets/images/products/p1.jpg")} class="card-img-top" alt="..." />
            <div className="card-body">
                <h5 className="card-title">Card title</h5>
                <p className="card-text">Some text.</p>
            </div>
            <div className="card-body">
                <a href="#" className="btn btn-danger mx-1"><i class="ri-delete-bin-line"></i>Remove Item</a>
            </div>
        </div >
    )
}