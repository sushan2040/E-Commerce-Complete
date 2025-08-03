import { NavLink, useNavigate } from "react-router-dom";
import CONSTANTS from "../../utils/Constants";
import axios from "axios";
import { toast } from "react-toastify";


export default function RemoveCarItem({ productId, productName, productImages, productCost }) {
    console.log(productName);
    console.log(productImages);

    function substractFromCart() {
        axios.post(CONSTANTS.BASE_URL + "/customer/substract-product-from-cart?productFinalCostMasterId=" + parseInt(productId) + "&quantity=" + 1, {}, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
            }
        }).then((result) => {
            if (result.status == "sucess") {
                toast.success(result.message);
            } else {
                toast.error(result.message);
            }
            //  fetchUsersCartCount();
        })
    }
    return (
        <div className="col-sm-3 col-md-2">
            <div className="card mt-3" >
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
                    <button style={{ width: '100%' }} onClick={substractFromCart} className="btn btn-danger btn-sm mt-1"><i class="ri-delete-bin-line"></i>Remove</button>
                </div>
            </div>

        </div>
    )
}