import React, { useEffect, useRef, useState } from "react";
import CommonScreen from "../../Structure/CommonScreen";
import CONSTANTS from "../../utils/Constants";
import { useAuth } from "../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import useCommonEffect from "../../Session/useCommonEffect";
import CommonDataTable from "../../utils/DataTableUtil";
import tableCustomStyles from "../../../css/TableCustomStyles"
import withReactContent from 'sweetalert2-react-content';
import Swal from "sweetalert2";
import api from "../../utils/axiosSetup";
import Loader from "../../Structure/Loader";
import axios from "axios";

const ReactSwal = withReactContent(Swal);
export default function CountryCurrencyMaster() {

    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [countries, setCountries] = useState([]);
    const [formData, setFormData] = React.useState({
        countryCurrencyMasterId: null,
        countryId: 0,
        currencyName: "",
        currencySymbol: "",
        currencyCode: "",
        status: "",
    });
    function resetForm() {
        setFormData({
            countryCurrencyMasterId: null,
            countryId: 0,
            currencyName: "",
            currencySymbol: "",
            currencyCode: "",
            status: "",
        })
    }
    const handlePageChange = async page => {
        if (data.length == 0) {
            page = 0;
        }
        fetchCountryCurrencyMasterData(page);
    };



    const handlePerRowsChange = async (newPerPage, page) => {
        if (!data.length) return; // Prevent unnecessary API call on mount
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/country-currency/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
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
            name: 'Currency Name',
            selector: row => row.currencyName,
            sortable: true,
            center: true,
        },
        {
            name: 'Currency Symbol',
            selector: row => row.currencySymbol,
            sortable: true,
            center: true,
        },
        {
            name: 'Currency Code',
            selector: row => row.currencyCode,
            sortable: true,
            center: true,
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
                <button type="button" className="btn bg btn-xs" id={`${row.countryCurrencyMasterId}`} data-toggle="modal" onClick={() => editCountryCurrencyMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.countryCurrencyMasterId}`} data-toggle="modal" onClick={() => deleteCountryCurrencyMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    const fetchCountryCurrencyMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/country-currency/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
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
        fetchCountryCurrencyMasterData(0);
    }, [])

    function editCountryCurrencyMasterForm(row) {
        const countryCurrencyMasterId = row.countryCurrencyMasterId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/country-currency/get-country-currency-byid/${countryCurrencyMasterId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                countryCurrencyMasterId: resultData.countryCurrencyMasterId,
                countryId: resultData.countryId,
                currencyName: resultData.currencyName,
                currencySymbol: resultData.currencySymbol,
                currencyCode: resultData.currencyCode,
                status: resultData.status,

            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteCountryCurrencyMasterForm = (row) => {
        const countryCurrencyMasterId = row.countryCurrencyMasterId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/country-currency/delete/${countryCurrencyMasterId}`,
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
    const validate = () => {
        const newErrors = {};
        if (!formData.countryId) {
            newErrors.countryId = 'Please select';
        }
        if (!formData.currencyName) {
            newErrors.currencyName = 'This field is required';
        }
        if (!formData.currencySymbol) {
            newErrors.currencySymbol = 'This field is required';
        }
        if (!formData.currencyCode) {
            newErrors.currencyCode = 'This field is required';
        }
        return newErrors;
    };
    const fetchCountries = async () => {
        const response = await api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
        var resultData = response.data;
        console.log("countries:" + resultData);
        setCountries(resultData);
    }
    function checkIfComboExists(e) {
        console.log(e.target.value);
        axios.post(CONSTANTS.BASE_URL + "/api-data/country-currency/check-combination?countryId=" + e.target.value,
            {
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem('authToken'),
                }
            }
        ).then((result) => {
            if (result.data == 0) {
                setFormData({ ...formData, countryId: e.target.value })
            } else {
                toast.error('This entry has been added please edit the existing one');

            }
        })
    }

    function saveCountryCurrency(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            var obj = {};
            obj.countryCurrencyMasterId = formData.countryCurrencyMasterId;
            obj.countryId = formData.countryId
            obj.currencyName = formData.currencyName
            obj.currencySymbol = formData.currencySymbol
            obj.currencyCode = formData.currencyCode
            api.post(CONSTANTS.BASE_URL + "/api-data/country-currency/save", obj, {
                headers: {
                    'Content-Type': "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                var resultData = result.data.status;
                if (resultData == "success") {
                    toast.success(CONSTANTS.SAVE_MESSAGE);
                } else {
                    toast.error(CONSTANTS.FAIL_MESSAGE);
                }
                fetchCountryCurrencyMasterData(0);
            }).catch((e) => {
                console.log("error" + e);
                toast.error(CONSTANTS.FAIL_MESSAGE);
            })

            resetForm();
        }
    }

    return (
        <>

            {loading && <Loader />} {/* Show loader when loading */}

            <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                <ol class="breadcrumb">
                    <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">Admin</a></li>
                    <li class="breadcrumb-item mx-2 " aria-current="page">Country Currency Master</li>
                </ol>
            </nav>

            <div className="col-lg-12 mt-2 mb-4">
                <div className="card shadow p-4">
                    <form onSubmit={saveCountryCurrency}>
                        <div className="row" >
                            <div className="row">
                                <h3>Country Currency Details</h3>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Country Name</label>
                                <div className="">
                                    <select className="form-control" value={formData.countryId} onChange={(e) => checkIfComboExists(e)}>
                                        <option value="0">--Please select--</option>
                                        {countries.map((country, index) => (
                                            <option key={index} value={country.countryId}>
                                                {country.countryName}
                                            </option>
                                        ))}
                                    </select>
                                    {errors.countryId && <span className="error">{errors.countryId}</span>}
                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Currency Name</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.currencyName}
                                        onChange={(e) => setFormData({ ...formData, currencyName: e.target.value })} className="form-control" placeholder="Enter country name" />
                                    <input value={formData.countryCurrencyMasterId} type="hidden" onChange={(e) => setFormData({ ...formData, countryCurrencyMasterId: e.target.value })} />
                                    {errors.currencyName && <span className="error">{errors.currencyName}</span>}
                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Currency Symbol</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.currencySymbol}
                                        onChange={(e) => setFormData({ ...formData, currencySymbol: e.target.value })} className="form-control" placeholder="Enter country name" />
                                    {errors.currencySymbol && <span className="error">{errors.currencySymbol}</span>}
                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Currency Code</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.currencyCode}
                                        onChange={(e) => setFormData({ ...formData, currencyCode: e.target.value })} className="form-control" placeholder="Enter country name" />
                                    {errors.currencyCode && <span className="error">{errors.currencyCode}</span>}
                                </div>
                            </div>
                            <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                    <button type="submit" className="btn btn-primary mx-1"><i class="fa fa-save mx-1"></i> Save</button>
                                    <button onClick={resetForm} className="btn btn-primary mx-1">< i class="ri-refresh-line"></i> Refresh</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
            <div className="col-lg-12 mt-2 mb-2">
                <div className="card shadow p-4 mt-4">
                    <div className="row" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center' }}>
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

            </div>

            <ToastContainer />
        </>
    )
}