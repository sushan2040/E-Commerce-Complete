import React from "react";
import StarRating from "./StarRating.js";
import "../../../css/starrating.css";
import { NavLink } from "react-router-dom";

export default function CardGroup(props) {
    const cards = Array.from({ length: props.number }, (_, index) => (
        <NavLink to="/ecommerce/product/view" className="col-md-3 col-sm-12" key={index} >
            <img
                src={require("../../../assets/images/products/p2.jpg")}
                className="card-img-top"
                alt="..."
            />
            <StarRating />
            <div className="card-body">
                <a href="#" className="btn btn-success mx-1">
                    <i className="ri-wallet-line"></i> Buy
                </a>
                <a href="#" className="btn btn-secondary mx-1">
                    <i className="ri-shopping-cart-line"></i> Add to Cart
                </a>
            </div>
        </NavLink>
    ));

    return <div className="card-group" style={{ display: 'flex', flexDirection: 'row' }}>{cards}</div>;
}
