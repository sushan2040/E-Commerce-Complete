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
export default function CountryMaster() {

    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [formData, setFormData] = React.useState({
        countryId: null,
        countryName: "",
        countryLocationLevels: "",
        countryFlag: "",
        status: "",
    });
    function resetForm() {
        setFormData({
            countryId: null,
            countryName: "",
            countryLocationLevels: "",
            countryFlag: "",
            status: "",
        })
    }
    const handlePageChange = async page => {
        if (!data.length) return; // Prevent unnecessary API call on mount
        if (data.length == 0) {
            page = 0;
        }
        fetchCountryMasterData(page);
    };



    const handlePerRowsChange = async (newPerPage, page) => {
        setLoading(true);
        if (data.length == 0) {
            page = 0;
        }
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/country/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
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
            name: 'Country Location Levels',
            selector: row => row.countryLocationLevels,
            sortable: true,
            center: true,
        },
        {
            name: 'Country Flag',
            selector: row => row.countryFlag,
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
                <button type="button" className="btn bg btn-xs" id={`${row.countryId}`} data-toggle="modal" onClick={() => editCountryMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.countryId}`} data-toggle="modal" onClick={() => deleteCountryMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    const fetchCountryMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/country/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };

    useEffect(() => {

    }, [])

    function editCountryMasterForm(row) {
        const countryId = row.countryId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/country/get-country-byid/${countryId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                countryId: resultData.countryId,
                countryName: resultData.countryName,
                countryLocationLevels: resultData.countryLocationLevels,
                countryFlag: resultData.countryFlag,
                status: resultData.status,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteCountryMasterForm = (row) => {
        const countryId = row.countryId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/country/delete/${countryId}`,
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
        if (!formData.countryName) {
            newErrors.countryName = 'This field is required';
        }
        if (!formData.countryFlag) {
            newErrors.countryFlag = 'This field is required';
        }
        if (!formData.countryLocationLevels) {
            newErrors.countryLocationLevels = 'This field is required';
        }
        return newErrors;
    };


    function saveCountry(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            var obj = {};
            obj.countryId = formData.countryId
            obj.countryName = formData.countryName
            obj.countryLocationLevels = formData.countryLocationLevels
            obj.countryFlag = formData.countryFlag
            api.post(CONSTANTS.BASE_URL + "/api-data/country/save", obj, {
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
            }).catch((e) => {
                console.log("error" + e);
                toast.error(CONSTANTS.FAIL_MESSAGE);
            })
            fetchCountryMasterData(0);
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
                    <li class="breadcrumb-item mx-2 " aria-current="page">Country Master</li>
                </ol>
            </nav>
            <div className="col-lg-12 mt-2 mb-4">
                <div className="card shadow p-4">
                    <form onSubmit={saveCountry}>
                        <div className="row" >
                            <div className="row">
                                <h3>Country Details</h3>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Country Name</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.countryName}
                                        onChange={(e) => setFormData({ ...formData, countryName: e.target.value })} className="form-control" placeholder="Enter country name" />
                                    <input value={formData.countryId} type="hidden" onChange={(e) => setFormData({ ...formData, countryId: e.target.value })} />
                                    {errors.countryName && <span className="error">{errors.countryName}</span>}
                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Country Flag</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.countryFlag}
                                        onChange={(e) => setFormData({ ...formData, countryFlag: e.target.value })} className="form-control" placeholder="Enter country flag" />
                                    <input value={formData.countryFlag} type="hidden" onChange={(e) => setFormData({ ...formData, countryFlag: e.target.value })} />
                                    {errors.countryFlag && <span className="error">{errors.countryFlag}</span>}
                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Country Location Levels</label>
                                <div className="">
                                    <input type="text" name="countryLevels" className="form-control" placeholder="Enter number"
                                        value={formData.countryLocationLevels}
                                        onChange={(e) => setFormData({ ...formData, countryLocationLevels: e.target.value })} />
                                    {errors.countryLocationLevels && <span className="error">{errors.countryLocationLevels}</span>}
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
                <ToastContainer />
            </div>


        </>
    )
}