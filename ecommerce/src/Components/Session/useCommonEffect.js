import { useEffect } from "react";
import { useHistory, useNavigate } from "react-router-dom";
import axios from "axios";
import CONSTANTS from '../utils/Constants';
import { useAuth } from "../../features/AuthProvider ";

const useCommonEffect = () => {
    const navigate = useNavigate();
    const { authToken } = useAuth();

    useEffect(() => {

        // Function to check session validity
        const checkSession = () => {
            axios.post(`${CONSTANTS.BASE_URL}/api-data/session-check/expired-or-not`, null, {
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
                    "Content-Type": "application/json"
                },
            })
                .then((result) => {
                    console.log(result.data.status);
                    const resultData = result.data.status;
                    if (resultData === "Not null") {
                        console.log("Session is valid.");
                    } else {
                        console.log("Session expired. Redirecting to login.");
                        navigate("/"); // Redirect to login
                        localStorage.removeItem("authToken");
                    }
                })
                .catch((error) => {
                    console.error("Error checking session:", error);
                    // Handle error (optional)
                    console.log("Session expired. Redirecting to login.");
                    navigate("/"); // Redirect to login
                    localStorage.removeItem("authToken");
                });
        };

        // Set up interval
        const intervalId = setInterval(() => {
            checkSession();
        }, 1000 * 60); // Run every 1 second

        // Cleanup interval on unmount
        return () => clearInterval(intervalId);
    }, [authToken, navigate]); // Dependency array includes authToken and navigate
};

export default useCommonEffect;
