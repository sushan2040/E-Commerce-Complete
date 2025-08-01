import CommonScreen from "../../../Structure/CommonScreen";

export default function ReceivedOrderRequests() {
    return (
        <>
            <div className="container mt-5">
                <div className="row justify-content-center">

                    <div className="col-lg-8 mt-2 mb-4">
                        <div className="card shadow p-4">
                            <div className="row" >
                                <div className="row">
                                    <h3>Product Order Placement Details</h3>
                                </div>

                                <div className="table-responsive">
                                    <table className="table table-stripped table-condensed table-borderd table-hover">
                                        <thead>
                                            <tr>
                                                <th className="text-align-center">Description</th>
                                                <th className="text-align-center">Order Details</th>
                                                <th className="text-align-center">Payment</th>
                                                <th className="text-align-center">Staus</th>
                                                <th className="text-align-center">Refund</th>
                                            </tr>
                                        </thead>
                                        <tbody>

                                            <tr>
                                                <td>Order has been placed by user </td>
                                                <td><button className="btn btn-primary">View Order</button> </td>
                                                <td><span>Payment pending</span></td>
                                                <td> <button className="btn btn-success">Confirm placement</button></td>
                                                <td>-</td>
                                            </tr>
                                            <tr>
                                                <td>Order has been placed by user </td>
                                                <td><button className="btn btn-primary">View Order</button> </td>
                                                <td><span>Payment completed</span></td>
                                                <td> <button className="btn btn-success">Confirm placement</button></td>
                                                <td>-</td>
                                            </tr>
                                            <tr>
                                                <td>Order has been placed by user </td>
                                                <td><button className="btn btn-primary">View Order</button> </td>
                                                <td><span>Payment pending</span></td>
                                                <td> <button className="btn btn-success">Confirm placement</button></td>
                                                <td>-</td>
                                            </tr>
                                            <tr>
                                                <td>Order has been canceled by user </td>
                                                <td><button className="btn btn-primary">View Order</button> </td>
                                                <td><span>Payment completed</span></td>
                                                <td> <button className="btn btn-danger">withdraw placement</button></td>
                                                <td>Available</td>
                                            </tr>
                                            <tr>
                                                <td>Order has been canceled by user </td>
                                                <td><button className="btn btn-primary">View Order</button> </td>
                                                <td><span>Payment pending</span></td>
                                                <td> <button className="btn btn-danger">withdraw placement</button></td>
                                                <td>Not Available</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}