import { NavLink } from "react-router-dom";
import CONSTANTS from "../../utils/Constants";
import "../../../css/Card.css";


export default function Card({ productId, productName, productImages, productCost }) {
    console.log(productName);
    console.log(productImages);


    return (
        <div className="col-sm-3 col-md-2">
            <NavLink to={`/ecommerce/product/view?productId=${productId}`} className="card mt-3" >

                {productImages && (
                    <div className="carousel-wrapper">
                        <div id="carouselExampleInterval" className="carousel slide" data-bs-ride="carousel">
                            <div className="carousel-inner">
                                <div className="carousel-item active img-container" data-bs-interval="10000">
                                    {productImages[0] && (
                                        <img
                                            src={productImages[0].imagePath}
                                            className="d-block"
                                            alt={`Product image ${productImages[0].isPrimary ? 'primary' : '1'}`}
                                        />
                                    )}
                                </div>
                                <div className="carousel-item img-container" data-bs-interval="2000">
                                    {productImages[1] && (
                                        <img
                                            src={productImages[1].imagePath}
                                            className="d-block"
                                            alt={`Product image ${productImages[1].isPrimary ? 'primary' : '2'}`}
                                        />
                                    )}
                                </div>
                                <div className="carousel-item img-container">
                                    {productImages[2] && (
                                        <img
                                            src={productImages[2].imagePath}
                                            className="d-block"
                                            alt={`Product image ${productImages[2].isPrimary ? 'primary' : '3'}`}
                                        />
                                    )}
                                </div>
                            </div>
                            <button
                                className="carousel-control-prev"
                                type="button"
                                data-bs-target="#carouselExampleInterval"
                                data-bs-slide="prev"
                            >
                                <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span className="visually-hidden">Previous</span>
                            </button>
                            <button
                                className="carousel-control-next"
                                type="button"
                                data-bs-target="#carouselExampleInterval"
                                data-bs-slide="next"
                            >
                                <span className="carousel-control-next-icon" aria-hidden="true"></span>
                                <span className="visually-hidden">Next</span>
                            </button>
                        </div>
                    </div>
                )}
                <label className="form-label" style={{ fontSize: '0.8rem', width: '7rem' }}>{productName}</label>
                <div className="row">
                    <span className="">{productCost}</span>
                </div>
                <div className="row">
                    <div className="col-sm-12 col-md-6">
                        <button style={{ width: '100%' }} className="btn btn-success btn-sm mt-1"><i className="ri-wallet-line"></i>Buy</button>
                    </div>
                    <div className="col-sm-12 col-md-6">

                        <button style={{ width: '100%' }} className="btn btn-secondary btn-sm mt-1"><i className="ri-shopping-cart-line"></i>Add to cart</button>
                    </div>
                </div>
            </NavLink>

        </div>
    )
}