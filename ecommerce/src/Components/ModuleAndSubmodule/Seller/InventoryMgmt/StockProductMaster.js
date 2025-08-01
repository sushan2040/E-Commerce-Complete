import CommonScreen from "../../../Structure/CommonScreen";

export default function StockProductMaster() {
    return (
        <>
                <div className="container mt-5">
                    <div className="row justify-content-center">

                        <div className="col-lg-8 mt-2 mb-4">
                            <div className="card shadow p-4">
                                <div className="row" >
                                    <div className="row">
                                        <h3>Stock Product Details</h3>
                                    </div>
                                    <div className="col-sm-3 mt-3">
                                        <label className="form-label">
                                            Select Brand
                                        </label>
                                        <select className="form-control">
                                            <option value="0">--Please Select--</option>
                                        </select>
                                    </div>
                                    <div className="col-sm-3 mt-3">
                                        <label className="form-label">
                                            Select Warehouse
                                        </label>
                                        <select className="form-control">
                                            <option value="0">--Please Select--</option>
                                        </select>
                                    </div>
                                    <div className="col-sm-3 mt-3">
                                        <label className="form-label">
                                            Select Product
                                        </label>
                                        <select className="form-control">
                                            <option value="0">--Please Select--</option>
                                        </select>
                                    </div>
                                    <div className="col-sm-3 mt-3">
                                        <label className="form-label">
                                            Enter product quantity
                                        </label>
                                        <input type="text" className="form-control" placeholder="Enter product qauntity" />
                                    </div>
                                    <div className="col-sm-3 mt-3">
                                        <label className="form-label">
                                            Current product quantity in this warehouse
                                        </label>
                                        <input type="text" className="form-control" value="0" />
                                    </div>
                                    <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                        <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                            <button className="btn btn-primary"><i class="fa fa-save mx-1"></i> Save</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
        </>
    )
}