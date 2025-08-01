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
export default function LocationLevel4Master() {

    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [countries, setCountries] = useState([]);
    const [formData, setFormData] = React.useState({
        level4Id: null,
        level3Id: null,
        level4Name: "",
        status: "",
    });
    function resetForm() {
        setFormData({
            level4Id: null,
            level3Id: null,
            level4Name: "",
            status: "",
        })
    }
    const handlePageChange = async page => {
        if (!data.length) return;
        if (data.length == 0) {
            page = 0;
        }
        fetchLevel4MasterData(page);
    };



    const handlePerRowsChange = async (newPerPage, page) => {
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/location-level4/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
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
            name: 'Level3 Name',
            selector: row => row.level3Name,
            sortable: true,
            center: true,
        },
        {
            name: 'Level4 Name',
            selector: row => row.level4Name,
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
                <button type="button" className="btn bg btn-xs" id={`${row.level3Id}`} data-toggle="modal" onClick={() => editLevel4MasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.level3Id}`} data-toggle="modal" onClick={() => deleteLevel4MasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    const fetchLevel4MasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/location-level4/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
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
    }, [])

    function editLevel4MasterForm(row) {
        const level4Id = row.level4Id;
        api.get(`${CONSTANTS.BASE_URL}/api-data/location-level4/get-level4-byid/${level4Id}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                level4Id: resultData.level4Id,
                level3Id: resultData.level3Id,
                level4Name: resultData.level4Name,
                status: resultData.status,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteLevel4MasterForm = (row) => {
        const level4Id = row.level4Id; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/location-level4/delete/${level4Id}`,
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
        if (!formData.level3Id) {
            newErrors.level3Id = 'Please select';
        }
        if (!formData.level4Name) {
            newErrors.level4Name = 'This field is required';
        }
        return newErrors;
    };
    const fetchCountries = async () => {
        const response = await api.get(CONSTANTS.BASE_URL + "/api-data/location-level3/fetch-all-level3s")
        var resultData = response.data;
        console.log("countries:" + resultData);
        setCountries(resultData);
    }

    function saveLevel4(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            var obj = {};
            obj.level4Id = formData.level4Id;
            obj.level3Id = formData.level3Id;
            obj.level4Name = formData.level4Name
            api.post(CONSTANTS.BASE_URL + "/api-data/location-level4/save", obj, {
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
                fetchLevel4MasterData(0);
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
                    <li class="breadcrumb-item mx-2 " aria-current="page">Location Level 4 Master</li>
                </ol>
            </nav>
            <div className="col-lg-12 mt-2 mb-4">
                <div className="card shadow p-4">
                    <form onSubmit={saveLevel4}>
                        <div className="row" >
                            <div className="row">
                                <h3>Location Level4 Details</h3>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Level4 Name</label>
                                <div className="">
                                    <select className="form-control" value={formData.level3Id} onChange={(e) => setFormData({ ...formData, level3Id: e.target.value })}>
                                        <option value="0">--Please select--</option>
                                        {countries.map((country, index) => (
                                            <option key={index} value={country.level3Id}>
                                                {country.level3Name}
                                            </option>
                                        ))}
                                    </select>
                                    {errors.level3Id && <span className="error">{errors.level3Id}</span>}
                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Level4 Name</label>
                                <div className="">
                                    <input type="text" name="countryName" value={formData.level4Name}
                                        onChange={(e) => setFormData({ ...formData, level4Name: e.target.value })} className="form-control" placeholder="Enter country name" />
                                    <input value={formData.level4Id} type="hidden" onChange={(e) => setFormData({ ...formData, level4Id: e.target.value })} />
                                    {errors.level4Name && <span className="error">{errors.level4Name}</span>}
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