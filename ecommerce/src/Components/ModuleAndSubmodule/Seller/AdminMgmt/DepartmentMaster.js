import React, { useEffect, useRef, useState } from "react";
import CommonScreen from "../../../Structure/CommonScreen";
import CONSTANTS from "../../../utils/Constants";
import { useAuth } from "../../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import tableCustomStyles from "../../../../css/TableCustomStyles";
import useCommonEffect from "../../../Session/useCommonEffect";
import CommonDataTable from "../../../utils/DataTableUtil";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import api from "../../../utils/axiosSetup";
import Loader from "../../../Structure/Loader";
const ReactSwal = withReactContent(Swal);

export default function DepartmentMaster() {
    const departmentId = useRef();
    const departmentName = useRef();
    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);

    const [formData, setFormData] = React.useState({
        departmentId: null,
        departmentName: "",
        status: "",
    });
    function resetForm() {
        setFormData({
            departmentId: null,
            departmentName: "",
            status: "",
        })
    }
    const handlePageChange = page => {

        fetchDepartmentMasterData(page);
    };
    function editDepartmentForm(row) {
        const departmentId = row.departmentId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/department-master/get-department-byid/${departmentId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                departmentId: resultData.departmentId,
                departmentName: resultData.departmentName,
                status: resultData.status,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteDepartmentForm = (row) => {
        const departmentId = row.departmentId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/department-master/delete/${departmentId}`,
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

        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/department-master/pagination?page=${page}&per_page=${newPerPage}`, {}, {
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
            name: 'Department Name',
            selector: row => row.departmentName,
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
                <button type="button" className="btn bg btn-xs" id={`${row.departmentId}`} data-toggle="modal" onClick={() => editDepartmentForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.departmentId}`} data-toggle="modal" onClick={() => deleteDepartmentForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    const fetchDepartmentMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/department-master/pagination?page=${page}&per_page=${perPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };

    useEffect(() => {
        fetchDepartmentMasterData(0);
    }, [])

    const validate = () => {
        const newErrors = {};
        if (!formData.departmentName) {
            newErrors.departmentName = 'This field is required';
        }
        return newErrors;
    };

    function saveDepartment(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            console.log("saving");
            var obj = {};
            obj.departmentId = formData.departmentId;
            obj.departmentName = formData.departmentName;
            console.log(obj);
            api.post(CONSTANTS.BASE_URL + "/api-data/department-master/save", obj, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                toast.success(result.data.message);
                fetchDepartmentMasterData(0);
                resetForm();
            }).catch((e) => {
                toast.error("Error occurred");
            })
        }
    }

    return (
        <>




            <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                <ol class="breadcrumb">
                    <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">Seller</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">Admin Management</a></li>
                    <li class="breadcrumb-item mx-2 " aria-current="page">Department Master</li>
                </ol>
            </nav>
            <div className="col-lg-12 mt-2 mb-4">
                {loading && <Loader />} {/* Show loader when loading */}
                <form onSubmit={saveDepartment}>
                    <div className="card shadow p-4">
                        <div className="row">
                            <div className="row">
                                <h3>Department Master</h3>
                            </div>
                            <div className="col-md-4 col-sm-12 mt-3">
                                <label className="form-lable">Department name</label>
                                <input type="text" className="form-control" value={formData.departmentName}
                                    onChange={(e) => setFormData({ ...formData, departmentName: e.target.value })} placeholder="Enter department name" />
                                {errors.departmentName && <span className="error">{errors.departmentName}</span>}
                                <input type="hidden" id="departmentId" value={formData.departmentId}
                                    onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })} />
                            </div>
                            <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                    <button className="btn btn-primary" type="submit"><i class="fa fa-save mx-1"></i> Save</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div className="col-lg-12 mt-2 mb-2">

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

            </div >


            <ToastContainer />
        </>
    )
}