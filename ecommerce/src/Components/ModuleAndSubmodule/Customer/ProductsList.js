import Card from "./Card";
import CardProductList from "./CardProductList";
import CommonScreen from "../../Structure/CommonScreen";
import StarRating from "./StarRating";

export default function ProductList() {
    var urlSearchParams = new URLSearchParams(window.location.search);
    var category = urlSearchParams.get("category");
    return (
        <>
            <div className="row">

                <h3 style={{ display: 'flex', flexDirection: 'row', justifyContent: 'left' }}>
                    {category}
                </h3>
                <div className="row">
                    <div className="col-sm-2 mb-3 mt-2" >
                        <button class="btn btn-primary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">Apply filters</button>
                    </div>

                </div>
                <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasRight" aria-labelledby="offcanvasRightLabel">
                    <div class="offcanvas-header">
                        <h5 id="offcanvasRightLabel">Offcanvas right</h5>
                        <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                    </div>
                    <div class="offcanvas-body">
                        <div className="row">
                            <h4>Department</h4>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Clothes</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Electronics</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Food</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Gifts</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Bikes</label>
                            </div>
                            <div className="rcol-sm-3ow">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Gadgets</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Books</label>
                            </div>
                        </div>
                        <div className="row">
                            <h4>Brands</h4>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">Puma</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">Nike</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">LG</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">Sony</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">Coolpad</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">Apple</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="checkbox" id="radio1" className="form-check-input" />
                                <label className="form-label">Blackberry</label>
                            </div>
                        </div>
                        <div className="row">
                            <h4>Customer Reviews</h4>
                            <div className="row">
                                <StarRating /> Five stars
                            </div>
                            <div className="row">
                                <StarRating /> Four stars
                            </div>
                            <div className="row">
                                <StarRating /> Three stars
                            </div>
                            <div className="row">
                                <StarRating /> Two stars
                            </div>
                            <div className="row">
                                <StarRating /> One stars
                            </div>
                        </div>
                        <div className="row">
                            <h4>Price</h4>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Below 7000</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Below 4000</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Below 3000</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Below 2000</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">Below 1000</label>
                            </div>

                        </div>
                        <div className="row">
                            <h4>Discount</h4>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">50%</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">40%</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">30%</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">20%</label>
                            </div>
                            <div className="col-sm-3">
                                <input type="radio" id="radio1" className="form-check-input" />
                                <label className="form-label">10%</label>
                            </div>
                        </div>

                    </div>
                </div>
                <div className="row">
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                    <CardProductList />
                </div>


            </div>
        </>
    )
}