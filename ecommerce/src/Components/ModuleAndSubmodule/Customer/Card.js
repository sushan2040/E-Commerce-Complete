import { NavLink } from "react-router-dom";
import CONSTANTS from "../../utils/Constants";
import "../../../css/Card.css";


export default function Card({ productId, productName, productImages, productCost }) {
    console.log(productName);
    console.log(productImages);


    return (
        <div className="col-sm-3 col-md-2">
            <NavLink to={`/ecommerce/product/view?productId=${productId}`} className="card mt-3" >

                {productImages &&
                    <div className="">
                        <div id="carouselExampleInterval" class="carousel slide" data-bs-ride="carousel">
                            <div class="carousel-inner">
                                <div class="carousel-item active img-container" data-bs-interval="10000">
                                    {productImages[0] && <img src={productImages[0].imagePath} class="d-block w-100" alt="..." />}
                                </div>
                                <div class="carousel-item img-container" data-bs-interval="2000">
                                    {productImages[1] && <img src={productImages[1].imagePath} class="d-block w-100" alt="..." />}
                                </div>
                                <div class="carousel-item img-container">
                                    {productImages[2] && <img src={productImages[2].imagePath} class="d-block w-100" alt="..." />}
                                </div>
                            </div>
                            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleInterval" data-bs-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span class="visually-hidden">Previous</span>
                            </button>
                            <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleInterval" data-bs-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                <span class="visually-hidden">Next</span>
                            </button>
                        </div>

                    </div>
                }
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