import React, { useState } from "react";

const StarRating = () => {
    const [rating, setRating] = useState(0); // State to manage the current rating

    const handleRating = (n) => {
        setRating(n);
    };

    return (
        <div>
            <div className="stars" >
                {[1, 2, 3, 4, 5].map((star) => (
                    <span
                        key={star}
                        className={`star ${rating >= star ? getStarClass(star) : ""}`}
                        onClick={() => handleRating(star)}
                    >
                        ★
                    </span>
                ))}
            </div>
            <div id="output">Rating is: {rating}/5</div>
        </div>
    );
};

const getStarClass = (n) => {
    switch (n) {
        case 1:
            return "one";
        case 2:
            return "two";
        case 3:
            return "three";
        case 4:
            return "four";
        case 5:
            return "five";
        default:
            return "";
    }
};

export default StarRating;
