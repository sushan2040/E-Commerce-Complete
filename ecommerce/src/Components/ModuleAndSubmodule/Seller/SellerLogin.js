import { useEffect, useRef, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import CONSTANTS from "../../utils/Constants";
import api from "../../utils/axiosSetup";
import { useSSR } from "react-i18next";
import Loader from "../../Structure/Loader";

export default function SellerLoginPage() {
  const BaseUrl = CONSTANTS.BASE_URL + "/api-data/auth/login";
  const empBaseUrl = CONSTANTS.BASE_URL + "/api-data/auth/employee/login";

  // Set loading state before the request
  //setLoading(true);
  const [loading, setLoading] = useState(false);
  const username = useRef("");
  const password = useRef("");
  const empUsername = useState("");
  const empPassword = useState("");
  const { login } = useAuth();
  const history = useNavigate();

  function loginEmployeeUser(event) {
    setLoading(true);
    event.preventDefault();
    // Get values from the refs
    const usernameValue = empUsername.current.value;
    const passwordValue = empPassword.current.value;
    setLoading(true);
    const obj = {
      email: usernameValue,
      password: passwordValue
    };
    api
      .post(empBaseUrl, obj, {
        headers: {
          "Content-Type": "application/json",  // Set the content type to JSON
        }
      })
      .then((result) => {
        setLoading(false);
        toast.success("Login Successful", result.data.token);
        const { token } = result.data.token;  // Modify this according to your API response structure
        login(result.data.token);
        localStorage.setItem('authToken', result.data.token);
        history("/ecommerce/seller/employee/dashboard")
      })
      .catch((error) => {
        setLoading(false);
        console.error("Login failed:", error);
        toast.error("Login failed. Please check your credentials.");
      })
      .finally(() => {
      });

  }

  function loginUser(event) {
    setLoading(true);
    event.preventDefault();
    // Get values from the refs
    const usernameValue = username.current.value;
    const passwordValue = password.current.value;
    setLoading(true);
    console.log("Logging in with username:", usernameValue, "password:", passwordValue);

    // Create the payload (body)
    const obj = {
      email: usernameValue,
      password: passwordValue
    };
    api
      .post(BaseUrl, obj, {
        headers: {
          "Content-Type": "application/json",  // Set the content type to JSON
        }
      })
      .then((result) => {
        // Successful login
        setLoading(false);
        toast.success("Login Successful", result.data.token);

        // Assuming the JWT token is sent in the response body as 'token'
        const { token } = result.data.token;  // Modify this according to your API response structure

        login(result.data.token);

        // Store token in localStorage (or sessionStorage)
        localStorage.setItem('authToken', result.data.token);

        history("/ecommerce/seller/dashboard")

        // You could update the context or state to reflect the logged-in user
        // For example, using context or useState to set auth token globally:
        // updateAuthToken(token);

        // Optionally, redirect to another page (e.g., home page or dashboard)
        // navigate('/dashboard');
      })
      .catch((error) => {
        setLoading(false);
        // Error handling (e.g., incorrect credentials, server error)
        console.error("Login failed:", error);
        toast.error("Login failed. Please check your credentials.");

        // Reset loading state if necessary
        //setLoading(false);
      })
      .finally(() => {
        setLoading(false);
        // Reset loading state when request is finished
        // setLoading(false);
      });



  }

  useEffect(() => {
    localStorage.removeItem("authToken");
  }, [])

  return (
    <div className="container mt-5">
      {loading && <Loader />}
      <div className="row justify-content-center">
        <div className="col-lg-6 mt-2 mb-4">
          <div className="card shadow p-4">
            <h2 className="text-center mb-4">Business Admins Login</h2>
            <form>
              <div className="row">
                <div className="mb-3 col-sm-6">
                  <label htmlFor="username" className="form-label">
                    Username
                  </label>
                  <input
                    type="text"
                    id="username"
                    className="form-control"
                    ref={username}
                    placeholder="Enter your username"
                  />
                </div>
                <div className="mb-3 col-sm-6">
                  <label htmlFor="password" className="form-label">
                    Password
                  </label>
                  <input
                    type="password"
                    id="password"
                    className="form-control"
                    ref={password}
                    placeholder="Enter your password"
                  />
                </div>
                <div className="row" >
                  <div className="col-sm-12 mt-2" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center' }}>
                    <NavLink onClick={loginUser} type="button" className="btn btn-primary" style={{ color: 'white', backgroundColor: 'blue' }} >
                      Login
                    </NavLink>
                  </div>
                  <div className="row" >
                    <div className="col-sm-12 mt-2" style={{
                      fontWeight: '300',
                      display: 'flex', flexDirection: 'row', justifyContent: 'center'
                    }}>
                      <span >Don't have an account? Sign up here</span>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-12 mt-2" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center' }} >
                      <NavLink className="btn btn-light" to="/ecommerce/seller/registration" style={{ borderRadius: '2rem', border: '0.5px solid gray' }}>Create Account</NavLink>
                    </div>
                  </div>
                  <div className="col-sm-12 mt-2" style={{
                    fontWeight: '300',
                    display: 'flex', flexDirection: 'row', justifyContent: 'center'
                  }}>
                    <span >Are you a customer? <NavLink to="/" style={{ color: 'blue' }}>login in here</NavLink></span>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div className="col-lg-6 mt-2 mb-4">
          <div className="card shadow p-4">
            <h2 className="text-center mb-4">Employee Login</h2>
            <form>
              <div className="row">
                <div className="mb-3 col-sm-6">
                  <label htmlFor="username" className="form-label">
                    Username
                  </label>
                  <input
                    type="text"
                    id="username"
                    className="form-control"
                    ref={empUsername}
                    placeholder="Enter your username"
                  />
                </div>
                <div className="mb-3 col-sm-6">
                  <label htmlFor="password" className="form-label">
                    Password
                  </label>
                  <input
                    type="password"
                    id="password"
                    className="form-control"
                    ref={empPassword}
                    placeholder="Enter your password"
                  />
                </div>
                <div className="row" >
                  <div className="col-sm-12 mt-2" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center' }}>
                    <NavLink onClick={loginEmployeeUser} type="button" className="btn btn-primary" style={{ color: 'white', backgroundColor: 'blue' }} >
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
  )
}