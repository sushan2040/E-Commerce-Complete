import CommonScreen from "../../Structure/CommonScreen";
import { useEffect, useRef, useState } from "react";
import { NavLink } from "react-router-dom";

export default function ProfilePage() {
    const phoneInputRef = useRef(null); // Reference for the input field
    const [phoneNumber, setPhoneNumber] = useState(""); // State to store the phone number

    const [showModal, setShowModal] = useState(false);

    const handleShowModal = () => setShowModal(true);
    const handleCloseModal = () => setShowModal(false);

    useEffect(() => {
        // Initialize intl-tel-input on component mount


    }, []);
    return (
        <>
            <div>
                <div className="container mt-5">
                    <div className="row justify-content-center">

                        <div className="col-md-6 col-sm-12 mb-2 mt-2">
                            <div className="card shadow p-4">
                                <div className="row" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
                                    <div className="col-sm-12 mt-2" style={{ display: "flex", flexDirection: 'row', justifyContent: "center" }}>
                                        <h4 className="form-label">Profile</h4>

                                    </div>
                                    <div className="col-sm-8 form-floating mt-2">

                                        <input type="email" id="floatingEmail" className="form-control" placeholder="Enter email" />
                                        <label for="floatingEmail" className="form-label form-floating">Enter email</label>
                                    </div>

                                    <div className="col-sm-8 form-floating mt-2">
                                        {/* Button to trigger modal */}
                                        <button className="btn btn-primary" onClick={handleShowModal}>
                                            Change Password
                                        </button>

                                        {/* Modal */}
                                        {showModal && (
                                            <div className="modal show d-block" tabIndex="-1" role="dialog">
                                                <div className="modal-dialog" role="document">
                                                    <div className="modal-content">
                                                        <div className="modal-header">
                                                            <h5 className="modal-title">Change Password</h5>
                                                            <button type="button" className="btn-close" onClick={handleCloseModal}></button>
                                                        </div>
                                                        <div className="modal-body">
                                                            <div className="col-sm-8 form-floating mt-2">
                                                                <input
                                                                    type="password"
                                                                    id="currentPassword"
                                                                    className="form-control"
                                                                    placeholder="Enter current password"
                                                                />
                                                                <label htmlFor="currentPassword" className="form-label form-floating">
                                                                    Enter Current Password
                                                                </label>
                                                            </div>
                                                            <div className="col-sm-8 form-floating mt-2">
                                                                <input
                                                                    type="password"
                                                                    id="newPassword"
                                                                    className="form-control"
                                                                    placeholder="Enter new password"
                                                                />
                                                                <label htmlFor="newPassword" className="form-label form-floating">
                                                                    Enter New Password
                                                                </label>
                                                            </div>
                                                            <div className="col-sm-8 form-floating mt-2">
                                                                <input
                                                                    type="password"
                                                                    id="confirmPassword"
                                                                    className="form-control"
                                                                    placeholder="Confirm new password"
                                                                />
                                                                <label htmlFor="confirmPassword" className="form-label form-floating">
                                                                    Enter New Password (Confirm)
                                                                </label>
                                                            </div>
                                                        </div>
                                                        <div className="modal-footer">
                                                            <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>
                                                                Close
                                                            </button>
                                                            <button type="button" className="btn btn-primary">
                                                                Save Changes
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        )}
                                    </div>

                                    <div className="col-sm-8 form-floating mt-2">
                                        <input type="password" id="floatingPassword" className="form-control" placeholder="Enter first name" />
                                        <label for="floatingPassword" className="form-label form-floating">First name</label>
                                    </div>
                                    <div className="col-sm-8 form-floating mt-2">
                                        <input type="password" id="floatingPassword" className="form-control" placeholder="Enter first name" />
                                        <label for="floatingPassword" className="form-label form-floating">Last name</label>
                                    </div>
                                    <div className="col-sm-8 form-floating mt-2">
                                        <input type="password" id="floatingPassword" className="form-control" placeholder="Enter first name" />
                                        <label for="floatingPassword" className="form-label form-floating">Business name</label>
                                    </div>
                                    <div className="col-sm-8 mt-2">
                                        <label className="form-label form-floating">Phone number:</label>
                                        <input ref={phoneInputRef} // Reference the input field
                                            type="tel" // Make sure it's a telephone input
                                            placeholder="Enter phone number" className="form-control" />
                                    </div>


                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div></>
    );
}