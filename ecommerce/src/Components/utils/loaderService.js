import { useState } from "react";

// Store a global state for the loader
let setLoaderVisible;

export const LoaderProvider = ({ children }) => {
    const [isLoading, setIsLoading] = useState(false);
    setLoaderVisible = setIsLoading; // Expose the setter function globally

    return (
        <>
            {isLoading && <div className="loader-container"><div className="spinner"></div></div>}
            {children}
        </>
    );
};

// Functions to show/hide loader
export const showLoader = () => setLoaderVisible && setLoaderVisible(true);
export const hideLoader = () => setLoaderVisible && setLoaderVisible(false);
