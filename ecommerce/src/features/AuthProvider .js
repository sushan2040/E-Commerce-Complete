import axios from 'axios';
import React, { createContext, useState, useContext } from 'react';
import CONSTANTS from '../Components/utils/Constants';

// Create a context for authentication
const AuthContext = createContext();

// AuthProvider component to manage the context state
export const AuthProvider = ({ children }) => {
    // Initial state: Retrieve token from localStorage if available
    const [authToken, setAuthToken] = useState(localStorage.getItem('authToken') || null);

    // Login function: store token in state and localStorage
    const login = (token) => {
        console.log("saving token:" + token);
        setAuthToken(token);
        localStorage.setItem('authToken', token); // Store token in localStorage
    };

    // Logout function: clear token from state and localStorage
    const logout = () => {
        console.log("Logging out");
        setAuthToken(null);
        resetAccess();
        localStorage.removeItem('authToken'); // Remove token from localStorage
    };

    const resetAccess = () => {
        axios.post(CONSTANTS.BASE_URL + "/api-data/auth/logout", {}, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
            }
        })
    }

    // Provide the context value (authToken and the functions)
    return (
        <AuthContext.Provider value={{ authToken, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// Custom hook to use the AuthContext
export const useAuth = () => {
    return useContext(AuthContext);
};