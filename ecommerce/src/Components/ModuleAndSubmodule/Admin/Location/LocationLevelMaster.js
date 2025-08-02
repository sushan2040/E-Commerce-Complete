
import React, { useEffect, useRef, useState } from "react";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import { useAuth } from "../../../../features/AuthProvider ";
import CONSTANTS from "../../../utils/Constants";
import useCommonEffect from "../../../Session/useCommonEffect";
import { toast, ToastContainer } from "react-toastify";
import tableCustomStyles from "../../../../css/TableCustomStyles";
import CommonDataTable from "../../../utils/DataTableUtil";
import api from "../../../utils/axiosSetup";
import Loader from "../../../Structure/Loader";
const ReactSwal = withReactContent(Swal);

export default function LocationLevelMaster() {
    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [countries, setCountries] = useState([]);
    const [currentCountriesLocationLevel, setCurrentCountriesLocationLevels] = useState(0);
    const [locationLevels, setLocationLevels] = useState('');

    const [formData, setFormData] = React.useState({
        locationLevelId: null,
        countryId: null,
        locationLevel1Label: "",
        locationLevel2Label: "",
        locationLevel3Label: "",
        locationLevel4Label: "",
        locationLevel5Label: "",
    });
    function resetForm() {
        setFormData({
            locationLevelId: null,
            countryId: 0,
            locationLevel1Label: "",
            locationLevel2Label: "",
            locationLevel3Label: "",
            locationLevel4Label: "",
            locationLevel5Label: "",
        })
    }
    const handlePageChange = page => {
        if (!data.length) return;
        if (data.length == 0) {
            page = 0;
        }
        fetchlocationLevelMasterData(page);
    };
    function editlocationLevelMasterForm(row) {
        const locationLevelId = row.locationLevelId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/location-level/get-location-level-byid/${locationLevelId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setCurrentCountriesLocationLevels(resultData.locationLevel);
            setFormData({
                locationLevelId: resultData.locationLevelId,
                countryId: resultData.countryId,
                locationLevel1Label: resultData.locationLevel1Label,
                locationLevel2Label: resultData.locationLevel2Label,
                locationLevel3Label: resultData.locationLevel3Label,
                locationLevel4Label: resultData.locationLevel4Label,
                locationLevel5Label: resultData.locationLevel5Label,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deletelocationLevelMasterForm = (row) => {
        const locationLevelId = row.locationLevelId; // Access properties from the row
        ReactSwal.fire({
            title: <strong>Are you sure?</strong>,
            html: <i>Do you really want to delete this item? This action cannot be undone.</i>,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'Cancel',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                ///  console.log("subModuleId" + JSON.stringify(e));
                // Perform the delete action
                api.delete(`${CONSTANTS.BASE_URL}/api-data/location-level/delete/${locationLevelId}`,
                    {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                        },
                    }
                ).then((result) => {
                    handlePerRowsChange(10, 0);
                    resetForm();
                });

                Swal.fire('Deleted!', 'The item has been deleted.', 'success');
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                Swal.fire('Cancelled', 'Your item is safe :)', 'error');
            }
        });
    };



    const handlePerRowsChange = async (newPerPage, page) => {
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/location-level/pagination?page=${page}&per_page=${newPerPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };

    const columns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Table

        },
        {
            name: 'Country Name',
            selector: row => row.countryName,
            sortable: true,
            center: true,
        },
        {
            name: 'Status',
            button: true,
            center: true,
            cell: row => (
                //console.log(row),
                row.status === "Y" ? 'Active' : 'Inactive'
            ),
        },
        {
            name: 'Edit',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-toggle="modal" onClick={() => editlocationLevelMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-toggle="modal" onClick={() => deletelocationLevelMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    const fetchlocationLevelMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/location-level/pagination?page=${page}&per_page=${perPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };

    useEffect(() => {
        fetchCountries();
        fetchlocationLevelMasterData(0);
    }, [])

    const validate = () => {
        const newErrors = {};
        if (!formData.countryId) {
            newErrors.countryId = 'Please select';
        }
        return newErrors;
    };

    function fetchCountries() {
        api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
            .then((result) => {
                var resultData = result.data;
                console.log("countries:" + resultData);
                setCountries(resultData);
            })
    }

    function savelocationLevelMaster(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        // const validationErrors = validate();
        // if (Object.keys(validationErrors).length > 0) {
        //     setErrors(validationErrors);
        //  } else {
        setErrors({});
        console.log("saving");
        var obj = {};
        obj.locationLevelId = formData.locationLevelId;
        obj.countryId = formData.countryId;
        obj.locationLevel1Label = formData.locationLevel1Label;
        obj.locationLevel2Label = formData.locationLevel2Label;
        obj.locationLevel3Label = formData.locationLevel3Label;
        obj.locationLevel4Label = formData.locationLevel4Label;
        obj.locationLevel5Label = formData.locationLevel5Label;
        console.log(obj);
        api.post(CONSTANTS.BASE_URL + "/api-data/location-level/save", obj, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        }).then((result) => {
            toast.success(result.data.message);
            fetchlocationLevelMasterData(0);
            resetForm();
        }).catch((e) => {
            toast.error("Error occurred");
        })

        //}
    }

    function fetchLocationLevelsByCountryId(countryId) {
        api.post(CONSTANTS.BASE_URL + "/api-data/location-level/get-location-levels-bycountryid/" + countryId, {}, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        }).then((result) => {
            const numberOfLevels = result.data; // this is the integer
            setCurrentCountriesLocationLevels(numberOfLevels);

        }).catch((e) => {
            console.log("error:" + e);
        })
    }
    return (
        <>
            <div className="container mt-5">
                {loading && <Loader />} {/* Show loader when loading */}
                <div className="row justify-content-center">
                    <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                            <li class="breadcrumb-item mx-2"><a href="#">Admin</a></li>
                            <li class="breadcrumb-item mx-2 " aria-current="page">Location Levels Master</li>
                        </ol>
                    </nav>
                    <form onSubmit={savelocationLevelMaster}>
                        <div className="col-lg-10 mt-2 mb-4">
                            <div className="card shadow p-4">
                                <div className="row">
                                    <div className="row">
                                        <h3>Country Location Details</h3>
                                        <input type="hidden" value={formData.locationLevelId} onChange={(e) => { setFormData({ ...formData, locationLevelId: e.target.value }) }} />
                                    </div>
                                    <div className="col-sm-4 mt-3">
                                        <label className="form-label">Country : </label>
                                        <select id="country" className="form-control" value={formData.countryId} onChange={(e) => {
                                            setFormData({ ...formData, countryId: e.target.value });
                                            fetchLocationLevelsByCountryId(e.target.value);
                                        }}>
                                            <option value="0">--Please Select--</option>
                                            {countries.map((country, index) => (
                                                <option key={index} value={country.countryId}>
                                                    {country.countryName}
                                                </option>
                                            ))}
                                        </select>
                                        {errors.countryId && <span className="error">{errors.countryId}</span>}
                                    </div>
                                    <div className="row" id="countryLocationDiv">
                                        {currentCountriesLocationLevel == 1 &&
                                            <div className="col-sm-4 mt-3 level1">
                                                <label className="form-label">Location Level 1 : </label>
                                                <input className="form-control" value={formData.locationLevel1Label} placeholder="Enter location level 1" onChange={(e) => { setFormData({ ...formData, locationLevel1Label: e.target.value }) }} />
                                                {errors.locationLevel1Label && <span className="error">{errors.locationLevel1Label}</span>}
                                            </div>
                                        }
                                        {currentCountriesLocationLevel == 2 && <>
                                            <div className="col-sm-4 mt-3 level1">
                                                <label className="form-label">Location Level 1 : </label>
                                                <input className="form-control" value={formData.locationLevel1Label} placeholder="Enter location level 1" onChange={(e) => { setFormData({ ...formData, locationLevel1Label: e.target.value }) }} />
                                                {errors.locationLevel1Label && <span className="error">{errors.locationLevel1Label}</span>}
                                            </div>
                                            <div className="col-sm-4 mt-3 level2">
                                                <label className="form-label">Location Level 2 : </label>
                                                <input className="form-control" value={formData.locationLevel2Label} placeholder="Enter location level 2" onChange={(e) => { setFormData({ ...formData, locationLevel2Label: e.target.value }) }} />
                                                {errors.locationLevel2Label && <span className="error">{errors.locationLevel2Label}</span>}
                                            </div>
                                        </>
                                        }
                                        {currentCountriesLocationLevel == 3 &&
                                            <>
                                                <div className="col-sm-4 mt-3 level1">
                                                    <label className="form-label">Location Level 1 : </label>
                                                    <input className="form-control" value={formData.locationLevel1Label} placeholder="Enter location level 1" onChange={(e) => { setFormData({ ...formData, locationLevel1Label: e.target.value }) }} />
                                                    {errors.locationLevel1Label && <span className="error">{errors.locationLevel1Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level2">
                                                    <label className="form-label">Location Level 2 : </label>
                                                    <input className="form-control" value={formData.locationLevel2Label} placeholder="Enter location level 2" onChange={(e) => { setFormData({ ...formData, locationLevel2Label: e.target.value }) }} />
                                                    {errors.locationLevel2Label && <span className="error">{errors.locationLevel2Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level3">
                                                    <label className="form-label">Location Level 3 : </label>
                                                    <input className="form-control" value={formData.locationLevel3Label} placeholder="Enter location level 3" onChange={(e) => { setFormData({ ...formData, locationLevel3Label: e.target.value }) }} />
                                                    {errors.locationLevel3Label && <span className="error">{errors.locationLevel3Label}</span>}
                                                </div>
                                            </>
                                        }
                                        {currentCountriesLocationLevel == 4 &&
                                            <>
                                                <div className="col-sm-4 mt-3 level1">
                                                    <label className="form-label">Location Level 1 : </label>
                                                    <input className="form-control" value={formData.locationLevel1Label} placeholder="Enter location level 1" onChange={(e) => { setFormData({ ...formData, locationLevel1Label: e.target.value }) }} />
                                                    {errors.locationLevel1Label && <span className="error">{errors.locationLevel1Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level2">
                                                    <label className="form-label">Location Level 2 : </label>
                                                    <input className="form-control" value={formData.locationLevel2Label} placeholder="Enter location level 2" onChange={(e) => { setFormData({ ...formData, locationLevel2Label: e.target.value }) }} />
                                                    {errors.locationLevel2Label && <span className="error">{errors.locationLevel2Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level3">
                                                    <label className="form-label">Location Level 3 : </label>
                                                    <input className="form-control" value={formData.locationLevel3Label} placeholder="Enter location level 3" onChange={(e) => { setFormData({ ...formData, locationLevel3Label: e.target.value }) }} />
                                                    {errors.locationLevel3Label && <span className="error">{errors.locationLevel3Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level4">
                                                    <label className="form-label">Location Level 4 : </label>
                                                    <input className="form-control" value={formData.locationLevel4Label} placeholder="Enter location level 4" onChange={(e) => { setFormData({ ...formData, locationLevel4Label: e.target.value }) }} />
                                                    {errors.locationLevel4Label && <span className="error">{errors.locationLevel4Label}</span>}
                                                </div>
                                            </>

                                        }
                                        {currentCountriesLocationLevel == 5 &&
                                            <>
                                                <div className="col-sm-4 mt-3 level1">
                                                    <label className="form-label">Location Level 1 : </label>
                                                    <input className="form-control" value={formData.locationLevel1Label} placeholder="Enter location level 1" onChange={(e) => { setFormData({ ...formData, locationLevel1Label: e.target.value }) }} />
                                                    {errors.locationLevel1Label && <span className="error">{errors.locationLevel1Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level2">
                                                    <label className="form-label">Location Level 2 : </label>
                                                    <input className="form-control" value={formData.locationLevel2Label} placeholder="Enter location level 2" onChange={(e) => { setFormData({ ...formData, locationLevel2Label: e.target.value }) }} />
                                                    {errors.locationLevel2Label && <span className="error">{errors.locationLevel2Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level3">
                                                    <label className="form-label">Location Level 3 : </label>
                                                    <input className="form-control" value={formData.locationLevel3Label} placeholder="Enter location level 3" onChange={(e) => { setFormData({ ...formData, locationLevel3Label: e.target.value }) }} />
                                                    {errors.locationLevel3Label && <span className="error">{errors.locationLevel3Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level4">
                                                    <label className="form-label">Location Level 4 : </label>
                                                    <input className="form-control" value={formData.locationLevel4Label} placeholder="Enter location level 4" onChange={(e) => { setFormData({ ...formData, locationLevel4Label: e.target.value }) }} />
                                                    {errors.locationLevel4Label && <span className="error">{errors.locationLevel4Label}</span>}
                                                </div>
                                                <div className="col-sm-4 mt-3 level5">
                                                    <label className="form-label">Location Level 5 : </label>
                                                    <input className="form-control" value={formData.locationLevel5Label} placeholder="Enter location level 5" onChange={(e) => { setFormData({ ...formData, locationLevel5Label: e.target.value }) }} />
                                                    {errors.locationLevel5Label && <span className="error">{errors.locationLevel5Label}</span>}
                                                </div>
                                            </>


                                        }
                                    </div>
                                </div>
                                <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                    <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                        <button className="btn btn-primary" type="submit"><i class="fa fa-save mx-1"></i> Save</button>
                                    </div>
                                </div>

                            </div>
                            <div className="card shadow p-4 mt-3">
                                <div className="table-responsive">
                                    <CommonDataTable
                                        columns={columns}
                                        data={data}
                                        progressPending={loading}
                                        pagination
                                        paginationServer
                                        paginationTotalRows={totalRows}
                                        onChangeRowsPerPage={handlePerRowsChange}
                                        onChangePage={handlePageChange}
                                        customStyles={tableCustomStyles}
                                    />
                                </div>
                            </div>
                        </div>
                    </form>

                </div>

                <ToastContainer />
            </div>
        </>
    )
}