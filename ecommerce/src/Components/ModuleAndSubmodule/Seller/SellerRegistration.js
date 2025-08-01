import { useEffect, useRef, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import CONSTANTS from "../../utils/Constants";
import { toast, ToastContainer } from "react-toastify";
import { useAuth } from "../../../features/AuthProvider ";
import api from "../../utils/axiosSetup";


export default function SellerRegistration() {

    const history = useNavigate();
    const phoneInputRef = useRef(null); // Reference for the input field
    const [phoneNumber, setPhoneNumber] = useState(""); // State to store the phone number
    const [countries, setCountries] = useState([]);
    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const username = useRef("");
    const password = useRef("");
    const firstName = useRef("");
    const lastName = useRef("");
    const businessName = useRef("");
    const country = useRef(0);
    //  const countryCode = useRef("");
    function fetchCountries() {
        api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
            .then((result) => {
                var resultData = result.data;
                console.log("countries:" + resultData);
                setCountries(resultData);
            })
    }

    function register() {
        setLoading(true);
        var obj = {};
        obj.email = username.current.value;
        obj.password = password.current.value;
        obj.firstName = firstName.current.value;
        obj.lastName = lastName.current.value;
        // obj.countryCode = countryCode.current.value;
        obj.phoneNumber = phoneInputRef.current.value;
        obj.businessName = businessName.current.value;
        obj.countryId = country.current.value;
        api.post(CONSTANTS.BASE_URL + "/api-data/auth/seller-register", obj, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            setLoading(false);

            console.log(response.data);
            var resultData = response.data.status;
            if (resultData == "success") {
                console.log("User registered..");
                toast.success("User successfully registered!");
            } else {
                console.log("Error occured while registering");
                toast.error("Error occured while registering");
            }
            setTimeout(function () {
                history('/ecommerce/seller/login');
            }, 3000);

        }).catch((e) => {
            console.log("Error occured while registering" + e);
            toast.error("Error occured while registering");
            setLoading(false);
            setTimeout(function () {
                history('/ecommerce/seller/login');
            }, 3000);
        })
    }


    useEffect(() => {
        fetchCountries();

    }, []);
    return (
        <>
            <div className="container mt-5">
                <div className="row justify-content-center">
                    <div className="col-md-6 col-sm-12 mb-2 mt-2" >
                        <div className="card shadow p-4">
                            <img src={require("../../../assets/images/registration-page-bg1.jpg")} style={{ height: '20%', width: '100%' }} />
                        </div>
                    </div>
                    <div className="col-md-6 col-sm-12 mb-2 mt-2">
                        <div className="card shadow p-4">
                            <div className="row" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
                                <div className="col-sm-12 mt-2" style={{ display: "flex", flexDirection: 'row', justifyContent: "center" }}>
                                    <h4 className="form-label">Business Registration</h4>

                                </div>
                                <div className="col-sm-8 form-floating mt-2">

                                    <input type="email" ref={username} id="floatingEmail" className="form-control" placeholder="Enter email" />
                                    <label for="floatingEmail" className="form-label form-floating">Enter email</label>
                                </div>
                                <div className="col-sm-8 form-floating mt-2">
                                    <input type="password" ref={password} id="floatingPassword" className="form-control" placeholder="Enter password" />
                                    <label for="floatingPassword" className="form-label form-floating">Enter password</label>
                                </div>
                                <div className="col-sm-8 form-floating mt-2">
                                    <input type="text" id="floatingPassword" ref={businessName} className="form-control" placeholder="Enter business name" />
                                    <label for="floatingPassword" className="form-label form-floating">Enter business name</label>
                                </div>
                                <div className="col-sm-8  mt-2">
                                    <label for="country" className="form-label">Country</label>
                                    <select id="country" ref={country} className="form-control">
                                        <option value="0">--Please Select--</option>
                                        {countries.map((country, index) => (
                                            <option key={index} value={country.countryId}>
                                                {country.countryName}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className="col-sm-8 mt-2">
                                    <label className="form-label form-floating">Enter phone number(optional)</label>
                                    <input ref={phoneInputRef} // Reference the input field
                                        type="tel" // Make sure it's a telephone input
                                        placeholder="Enter phone number" className="form-control" />
                                </div>
                                <div class="mt-2" style={{ width: '100%', height: '40px', display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
                                    <div class="checkbox">
                                        <input type="checkbox" id="remember-me" className="form-check-input" />
                                        <label for="remember-me">Remember me</label>
                                    </div>
                                    <div class="pass-link">
                                        <a href="#">Forgot password?</a>
                                    </div>
                                </div>
                                <div className="col-sm-3 mt-2">
                                    <button className="btn btn-primary mt-2" onClick={register}>Sign up</button>
                                </div>
                                <div className="col-sm-8 mt-2" style={{ backgroundColor: '#EEEEEE', borderRadius: '7rem', color: 'black', textDecoration: "underline" }}>
                                    <span>click to go on <NavLink to="/ecommerce/seller/login" style={{ textDecoration: "underline" }}>login page</NavLink></span>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                <ToastContainer />
            </div>
        </>
    )
}