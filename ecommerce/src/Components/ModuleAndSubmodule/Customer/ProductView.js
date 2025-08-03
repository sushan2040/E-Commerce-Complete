import CommonScreen from "../../Structure/CommonScreen"
import StarRating from "./StarRating"
import p1 from "../../../assets/images/No_Image_Available.jpg";
import p2 from "../../../assets/images/No_Image_Available2.jpg";
import p3 from "../../../assets/images/No_Image_Available3.jpg";
import p4 from "../../../assets/images/No_Image_Available4.jpg";
import { useEffect, useState } from "react";
import axios from "axios";
import CONSTANTS from "../../utils/Constants";
import { toast } from "react-toastify";

export default function ProductView() {

    const [selectedImage, setSelectedImage] = useState(''); // Initial image for the second div
    const [productName, setProductName] = useState('');
    const [productDesc, setProductDesc] = useState('');
    const [productCost, setProductCost] = useState(0);
    const [currencySymbol, setCurrencySymbol] = useState('');
    const [specificationList, setSpecificationList] = useState([]);
    const [notConfigured, setNotConfigured] = useState(false);
    let [count, setCount] = useState(0);

    function incrementCount() {
        count = count + 1;
        setCount(count);
    }
    function decrementCount() {
        count = count - 1;
        setCount(count);
    }
    function addToCart() {
        var urlSearchParams = new URLSearchParams(window.location.search);
        axios.post(CONSTANTS.BASE_URL + "/customer/add-product-to-cart?productFinalCostMasterId=" + parseInt(urlSearchParams.get('productId')) + "&quantity=" + count, {}, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
                "Content-Type": "application/json"
            }
        }).then((result) => {
            if (result.status == "sucess") {
                toast.success(result.message);
            } else {
                toast.error(result.message);
            }

        })
    }
    function fetchProductDetails() {
        var urlSearchParams = new URLSearchParams(window.location.search);
        axios.get(CONSTANTS.BASE_URL + "/product-final-cost/fetch-final-product-by-id-contrywise", {
            params: {
                productId: urlSearchParams.get('productId'),
                currencyCode: localStorage.getItem('currencyCode')
            },
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
            }
        }).then((result) => {
            console.log(result);
            if (result.data.length !== 0) {
                const resultData = result.data;
                setProductName(resultData.productName);
                setProductDesc(resultData.productDesc);
                setProductCost(resultData.cost);
                setCurrencySymbol(resultData.currencySymbol);
                setSpecificationList(resultData.specificationList);
                if (resultData.productImages.length !== 0) {
                    setProducts(resultData.productImages);
                    setSelectedImage(resultData.productImages[0].imagePath);
                } else {
                    setProducts([{
                        productImagesId: 1, imagePath: p1
                    },
                    {
                        productImagesId: 2, imagePath: p2
                    },
                    {
                        productImagesId: 3, imagePath: p3
                    },
                    {
                        productImagesId: 4, imagePath: p4
                    }
                    ])
                    setSelectedImage(p1);
                }

                setNotConfigured(false);
            } else {
                setNotConfigured(true);
                setProducts([{
                    productImagesId: 1, imagePath: p1
                },
                {
                    productImagesId: 2, imagePath: p2
                },
                {
                    productImagesId: 3, imagePath: p3
                },
                {
                    productImagesId: 4, imagePath: p4
                }
                ])
                setSelectedImage(p1);
            }
        })
    }
    useEffect(() => {
        fetchProductDetails();
    }, [])

    const [products, setProducts] = useState([{
        productImagesId: 1, imagePath: p1
    },
    {
        productImagesId: 2, imagePath: p2
    },
    {
        productImagesId: 3, imagePath: p3
    },
    {
        productImagesId: 4, imagePath: p4
    }
    ]);

    const handleImageClick = (imagePath) => {
        setSelectedImage(imagePath); // Update the selected image
    };


    return (
        <>
            <div style={{ backgroundColor: "#EEEEEE" }}>
                {/* Carousel Section */}

                <div className="row">
                    <div className="col-lg-12">
                        <div className="card card-body">
                            <div className="row">
                                <div id="list-example" className="list-group col-md-2 col-sm-12">
                                    {products.map((product, index) => (
                                        <a
                                            key={product.productImagesId}
                                            className="list-group-item list-group-item-action mt-2 mb-2 col-md-12 col-sm-3"

                                            style={{
                                                display: "flex",
                                                flexDirection: "row",
                                                justifyContent: "center",
                                            }}
                                        >
                                            <img
                                                className="img-fluid"
                                                src={product.imagePath}

                                                onClick={() => handleImageClick(product.imagePath)} // Add click event
                                                style={{
                                                    width: "5rem",
                                                    height: "5rem",
                                                    cursor: "pointer", // Makes it clear the image is clickable
                                                }}
                                            />
                                        </a>
                                    ))}
                                </div>
                                <div className="col-md-4 col-sm-12">
                                    <div
                                        data-bs-spy="scroll"
                                        data-bs-target="#list-example"
                                        data-bs-offset="0"
                                        className="scrollspy-example"
                                        tabIndex="0"
                                    >
                                        <img
                                            className="img-fluid"
                                            src={selectedImage} // Show the selected image here
                                            alt="Selected"
                                            style={{
                                                width: "600px",
                                                height: "500px",
                                                objectFit: "cover",
                                            }}
                                        />
                                    </div>
                                </div>
                                {!notConfigured && <div className="col-md-6 col-sm-12">
                                    <div className="row" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
                                        <div className="col-md-12 col-sm-12">
                                            <h4>{productName}</h4>
                                        </div>
                                        <div className="col-md-12 col-sm-12">
                                            <StarRating />
                                        </div>
                                        <div className="col-md-12 col-sm-12">
                                            {productDesc}
                                        </div>
                                        <div className="col-md-12 col-sm-12" style={{ fontSize: '0.9rem' }}>
                                            10k bought this month
                                        </div>
                                        <div className="col-md-12 col-sm-12" style={{ fontSize: '1.4rem' }}>
                                            <div className="price-container p-2" >
                                                <span className="rupee">{currencySymbol} <span className="value">{productCost}</span></span>



                                            </div>

                                        </div>
                                        <div className="col-sm-12 " style={{ justifyContent: 'flex-start' }}>
                                            <div className="col-sm-3">
                                                <input readOnly className="form-control mb-1 mt-1" value={count} />
                                            </div>
                                            <button className="btn btn-secondary mx-2" onClick={incrementCount}>+</button>
                                            <button className="btn btn-secondary mx-2" onClick={decrementCount}>-</button>

                                        </div>
                                        <div className="col-sm-12 col-sm-12 card-body" style={{
                                            display: 'flex', flexDirection: 'row',
                                            justifyContent: 'flex-start'
                                        }}>
                                            <a href="#" className="btn btn-success mx-1">
                                                <i className="ri-wallet-line"></i> Buy
                                            </a>
                                            <a href="#" className="btn btn-secondary mx-1" onClick={addToCart}>
                                                <i className="ri-shopping-cart-line" ></i> Add to Cart
                                            </a>
                                        </div>
                                        <div className="col-md-12 col-sm-12">
                                            <span style={{ fontSize: '1rem', fontWeight: 'bold' }}>Specifications</span>
                                            {specificationList && specificationList.map((specification) => (
                                                <div className="row">
                                                    <div className="col-sm-3 mb-1 mt-1" style={{ borderRadius: '2rem', borderColor: 'gray' }}>
                                                        <span>{specification.specificationName}</span>
                                                    </div>
                                                    <div className="col-sm-3 mb-1 mt-1">
                                                        :<span className="mx-1">{specification.value}</span>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>

                                    </div>
                                </div>
                                }
                                <div className="col-md-6 col-sm-12" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                    {notConfigured && <span>This product is not available in your region</span>}
                                </div>
                            </div>


                        </div>


                    </div>
                </div>
            </div>
        </>
    )
}