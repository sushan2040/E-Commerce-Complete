export default function Carousal() {
    return (
        <div
            id="carouselExampleIndicators"

            className="carousel slide"
            data-bs-ride="carousel"
        >
            {/* Carousel Indicators */}
            <div className="carousel-indicators">
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="0"
                    className="active"
                    aria-current="true"
                    aria-label="Slide 1"
                ></button>
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="1"
                    aria-label="Slide 2"
                ></button>
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="2"
                    aria-label="Slide 3"
                ></button>
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="3"
                    aria-label="Slide 4"
                ></button>
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="4"
                    aria-label="Slide 5"
                ></button>
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="5"
                    aria-label="Slide 6"
                ></button>
                <button
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    data-bs-slide-to="6"
                    aria-label="Slide 7"
                ></button>
            </div>

            {/* Carousel Images */}
            <div className="carousel-inner">
                <div className="carousel-item active">
                    <img
                        src={require("../../../assets/images/c1.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 1"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
                <div className="carousel-item">
                    <img
                        src={require("../../../assets/images/c1.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 2"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
                <div className="carousel-item">
                    <img
                        src={require("../../../assets/images/c2.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 3"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
                <div className="carousel-item">
                    <img
                        src={require("../../../assets/images/c3.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 4"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
                <div className="carousel-item">
                    <img
                        src={require("../../../assets/images/c4.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 5"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
                <div className="carousel-item">
                    <img
                        src={require("../../../assets/images/c7.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 6"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
                <div className="carousel-item">
                    <img
                        src={require("../../../assets/images/c8.jpg")}
                        className="d-block w-100 img-fluid"
                        alt="Slide 7"
                        style={{
                            height: "500px",
                            objectFit: "cover", // Ensures image fills container proportionally
                        }}
                    />
                </div>
            </div>

            {/* Navigation Buttons */}
            <button
                className="carousel-control-prev"
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide="prev"
            >
                <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                <span className="visually-hidden">Previous</span>
            </button>
            <button
                className="carousel-control-next"
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide="next"
            >
                <span className="carousel-control-next-icon" aria-hidden="true"></span>
                <span className="visually-hidden">Next</span>
            </button>
        </div>
    )
}