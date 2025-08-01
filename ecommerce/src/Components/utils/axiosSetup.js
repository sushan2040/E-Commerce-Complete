import axios from "axios";
import CONSTANTS from "./Constants";

const api = axios.create({
    baseURL: CONSTANTS.BASE_URL,
});

// Attach interceptors to Axios
export const setupAxiosInterceptors = (setLoading, setItemCount) => {
    api.interceptors.request.use(
        (config) => {
            setItemCount(5); // Simulate adding 5 items into the cart
            setLoading(true);
            return config;
        },
        (error) => {
            setLoading(false);
            return Promise.reject(error);
        }
    );

    api.interceptors.response.use(
        (response) => {
            setLoading(false);
            return response;
        },
        (error) => {
            setLoading(false);
            return Promise.reject(error);
        }
    );
};

export default api;
