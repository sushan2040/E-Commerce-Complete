import CommonScreen from "../../../Structure/CommonScreen";

export default function GenerateInvoice() {
    return (
        <>
            <div className="container mt-5">
                <div className="row justify-content-center">

                    <div className="col-lg-8 mt-2 mb-4">
                        <div className="card shadow p-4">
                            <div className="row">
                                <h3>Invoice Management Details</h3>
                            </div>

                            <div className="table-responsive">
                                <table className="table table-stripped table-condensed table-borderd table-hover">
                                    <thead>
                                        <tr>
                                            <th className="text-align-center">Customer Name</th>
                                            <th className="text-align-center">Order Details</th>
                                            <th className="text-align-center">Payment</th>
                                            <th className="text-align-center">Invoice action</th>
                                            <th className="text-align-center">Invoice Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                        <tr>
                                            <td>John doe</td>
                                            <td><button className="btn btn-primary">View Order</button> </td>
                                            <td><span>Payment pending</span></td>
                                            <td> <button className="btn btn-success">View Invoice</button></td>
                                            <td>Invoice Generated</td>
                                        </tr>
                                        <tr>
                                            <td>Michael smith</td>
                                            <td><button className="btn btn-primary">View Order</button> </td>
                                            <td><span>Payment pending</span></td>
                                            <td> <button className="btn btn-success" disabled>View Invoice</button></td>
                                            <td>Invoice pending</td>
                                        </tr>
                                        <tr>
                                            <td>Smith williams</td>
                                            <td><button className="btn btn-primary">View Order</button> </td>
                                            <td><span>Payment completed</span></td>
                                            <td> <button className="btn btn-success">View Invoice</button></td>
                                            <td>Invoice Generated</td>
                                        </tr>
                                        <tr>
                                            <td>Hillary Clinton</td>
                                            <td><button className="btn btn-primary">View Order</button> </td>
                                            <td><span>Payment completed</span></td>
                                            <td> <button className="btn btn-success">Generate Invoice</button></td>
                                            <td>Invoice not Generated</td>
                                        </tr>
                                        <tr>
                                            <td>Donald trump</td>
                                            <td><button className="btn btn-primary">View Order</button> </td>
                                            <td><span>Payment completed</span></td>
                                            <td> <button className="btn btn-danger">Delete Invoice</button></td>
                                            <td>Invoice Generated</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}