import CommonScreen from "../../../Structure/CommonScreen";

export default function WarehouseMaster() {
    return (
        <>
            <div className="container mt-5">
                <div className="row justify-content-center">
                    <div className="col-lg-8 mt-2 mb-4">
                        <div className="card shadow p-4">
                            <div className="row" >
                                <div className="row">
                                    <h3>Warehouse Details</h3>
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">Warehouse name</label>
                                    <input type="text" className="form-control" placeholder="Enter warehouse name" />
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">Brand select</label>
                                    <select className="form-control">
                                        <option value="0">--Please Select--</option>
                                    </select>
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">Country :</label>
                                    <select className="form-control">
                                        <option value="0">--Please Select--</option>
                                    </select>
                                </div>
                                <div className="row">
                                    <div className="col-sm-4 mt-3">
                                        <label className="form-label">Location level 1</label>
                                        <input type="text" className="form-control" placeholder="Enter location level name" />
                                    </div>
                                    <div className="col-sm-4 mt-3">
                                        <label className="form-label">Location level 2</label>
                                        <input type="text" className="form-control" placeholder="Enter location level name" />
                                    </div>
                                    <div className="col-sm-4 mt-3">
                                        <label className="form-label">Location level 3</label>
                                        <input type="text" className="form-control" placeholder="Enter location level name" />
                                    </div>
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">Pincode</label>
                                    <input type="text" className="form-control" placeholder="Enter pincode" />
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">Contact person name</label>
                                    <input type="text" className="form-control" placeholder="Enter contact person name" />
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">Contact person number</label>
                                    <input type="text" className="form-control" placeholder="Enter contact person number" />
                                </div>
                                <div className="col-sm-4 mt-3">
                                    <label className="form-label">email</label>
                                    <input type="text" className="form-control" placeholder="Enter email" />
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