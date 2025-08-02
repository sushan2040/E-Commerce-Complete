import React, { useEffect, useRef, useState } from "react";
import CommonScreen from "../../../Structure/CommonScreen";
import CONSTANTS from "../../../utils/Constants";
import { useAuth } from "../../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import useCommonEffect from "../../../Session/useCommonEffect";
import CommonDataTable from "../../../utils/DataTableUtil";
import tableCustomStyles from "../../../../css/TableCustomStyles"
import withReactContent from 'sweetalert2-react-content';
import Swal from "sweetalert2";
import api from "../../../utils/axiosSetup";
import Loader from "../../../Structure/Loader";

const ReactSwal = withReactContent(Swal);
export default function LocationLevel2Master() {

    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [countries, setCountries] = useState([]);
    const [formData, setFormData] = React.useState({
        level2Id: null,
        countryId: null,
        level2Name: "",
        status: "",
    });
    function resetForm() {
        setFormData({
            level2Id: null,
            countryId: null,
            level2Name: "",
            status: "",
        })
    }
    const handlePageChange = async page => {
        if (data.length == 0) {
            page = 0;
        }
        fetchLevel2MasterData(page);
    };



    const handlePerRowsChange = async (newPerPage, page) => {
        if (!data.length) return; // Prevent unnecessary API call on mount
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/location-level2/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
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
            name: 'Country Name',
            selector: row => row.level2Name,
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
                <button type="button" className="btn bg btn-xs" id={`${row.level2Id}`} data-toggle="modal" onClick={() => editLevel2MasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.level2Id}`} data-toggle="modal" onClick={() => deleteLevel2MasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    const fetchLevel2MasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/location-level2/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
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
        fetchLevel2MasterData(0);
    }, [])

    function editLevel2MasterForm(row) {
        const level2Id = row.level2Id;
        api.get(`${CONSTANTS.BASE_URL}/api-data/location-level2/get-level2-byid/${level2Id}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                level2Id: resultData.level2Id,
                countryId: resultData.countryId,
                level2Name: resultData.level2Name,
                status: resultData.status,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteLevel2MasterForm = (row) => {
        const level2Id = row.level2Id; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/location-level2/delete/${level2Id}`,
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
        if (!formData.level2Name) {
            newErrors.level2Name = 'This field is required';
        }
        return newErrors;
    };
    const fetchCountries = async () => {
        const response = await api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
        var resultData = response.data;
        console.log("countries:" + resultData);
        setCountries(resultData);
    }

    function saveLevel2(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            var obj = {};
            obj.level2Id = formData.level2Id;
            obj.level1Id = formData.countryId
            obj.level2Name = formData.level2Name
            api.post(CONSTANTS.BASE_URL + "/api-data/location-level2/save", obj, {
                headers: {
                    'Content-Type': "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                var resultData = result.data.message;
                if (resultData == "success") {
                    toast.success(CONSTANTS.SAVE_MESSAGE);
                } else {
                    toast.error(CONSTANTS.FAIL_MESSAGE);
                }
                fetchLevel2MasterData(0);
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
                    <li class="breadcrumb-item mx-2 " aria-current="page">Location Level 2 Master</li>
                </ol>
            </nav>

            <div className="col-lg-12 mt-2 mb-4">
                <div className="card shadow p-4">
                    <form onSubmit={saveLevel2}>
                        <div className="row" >
                            <div className="row">
                                <h3>Location Level2 Details</h3>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Country Name</label>
                                <div className="">
                                    <select className="form-control" value={formData.countryId} onChange={(e) => setFormData({ ...formData, countryId: e.target.value })}>
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
                                <label className="form-label">Level2 Name</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.level2Name}
                                        onChange={(e) => setFormData({ ...formData, level2Name: e.target.value })} className="form-control" placeholder="Enter country name" />
                                    <input value={formData.level2Id} type="hidden" onChange={(e) => setFormData({ ...formData, level2Id: e.target.value })} />
                                    {errors.level2Name && <span className="error">{errors.level2Name}</span>}
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