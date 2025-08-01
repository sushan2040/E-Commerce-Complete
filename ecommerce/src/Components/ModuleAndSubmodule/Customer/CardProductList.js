import { NavLink } from "react-router-dom";

export default function CardProductList() {
    return (
        <NavLink to="/ecommerce/product/view" className="row">
            <div className="col-sm-4">
                <img src={require("../../../assets/images/products/p1.jpg")} style={{ height: '20rem' }} class="card-img-top" alt="..." />
            </div>
            <div className="col-sm-4">
                <h5 className="card-title">Card title</h5>
                <p className="card-text">Some text.</p>
                <a href="#" className="btn btn-success mx-1"><i class="ri-wallet-line"></i>Buy</a>
                <a href="#" className="btn btn-secondary mx-1"><i class="ri-shopping-cart-line"></i>Add to Cart</a>
            </div>
        </NavLink>
    )
}