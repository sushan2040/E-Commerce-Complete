import { useEffect, useRef, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import CONSTANTS from "../../utils/Constants";
import api from "../../utils/axiosSetup";

export default function AdminLoginPage() {
    const BaseUrl = CONSTANTS.BASE_URL + "/api-data/auth/login";
    const [formData, setFormData] = useState({ email: '', password: '' });
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };
    const validate = () => {
        const newErrors = {};
        if (!formData.email) {
            newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Email is invalid';
        }
        if (!formData.password) {
            newErrors.password = 'Password is required';
        } else if (formData.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters';
        }
        return newErrors;
    };
    const { login } = useAuth();
    const history = useNavigate();
    function loginUser(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            try {
                const obj = {
                    email: formData.email,
                    password: formData.password
                };
                api
                    .post(BaseUrl, obj, {
                        headers: {
                            "Content-Type": "application/json",  // Set the content type to JSON
                        }
                    })
                    .then((result) => {
                        toast.success("Login Successful", result.data.token);
                        const { token } = result.data;
                        login(result.data.token);
                        localStorage.setItem('authToken', result.data.token);
                        history("/ecommerce/admin/dashboard")
                    })
            } catch (error) {
                if (error.response) {
                    // Server returned an error
                    setServerError(error.response.data.message || 'Server error occurred');
                } else {
                    setServerError('Unable to reach the server');
                }
            }
        }
    }
    useEffect(() => {
        localStorage.removeItem("authToken");
    }, [])
    return (
        <>
            <div className="container mt-5">
                <div className="row justify-content-center">

                    <div className="col-lg-6">
                        <div className="card shadow p-4">
                            <h2 className="text-center mb-4">Sign in</h2>
                            <form onSubmit={loginUser}>
                                <div className="row">
                                    <div className="mb-3 col-sm-6">
                                        <label htmlFor="username" className="form-label">
                                            Enter email
                                        </label>
                                        <input
                                            type="text"
                                            id="username"
                                            name="email"
                                            className="form-control"
                                            placeholder="Enter your username"
                                            value={formData.email}
                                            onChange={handleChange}
                                        />
                                        {errors.email && <span className="error">{errors.email}</span>}
                                    </div>
                                    <div className="mb-3 col-sm-6">
                                        <label htmlFor="password" className="form-label">
                                            Password
                                        </label>
                                        <input
                                            type="password"
                                            id="password"
                                            name="password"
                                            className="form-control"
                                            placeholder="Enter your password"
                                            value={formData.password}
                                            onChange={handleChange}
                                        />
                                        {errors.password && <span className="error">{errors.password}</span>}
                                    </div>
                                    <div className="row" >
                                        <div className="col-sm-12 mt-2" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center' }}>
                                            <NavLink onClick={loginUser} type="button" className="btn btn-primary" style={{ color: 'white', backgroundColor: 'blue' }} >
                                                Login
                                            </NavLink>
                                        </div>
                                    </div>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
                <ToastContainer />
            </div>
        </>
    )
}