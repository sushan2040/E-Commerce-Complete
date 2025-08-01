import React, { useEffect, useRef, useState } from "react";
import CommonScreen from "../../Structure/CommonScreen";
import CONSTANTS from "../../utils/Constants";
import { useAuth } from "../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import tableCustomStyles from "../../../css/TableCustomStyles";
import useCommonEffect from "../../Session/useCommonEffect";
import CommonDataTable from "../../utils/DataTableUtil";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import api from "../../utils/axiosSetup";
import Loader from "../../Structure/Loader";
const ReactSwal = withReactContent(Swal);

export default function BrandMaster() {
    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);

    const [formData, setFormData] = React.useState({
        brandId: null,
        brandName: "",
        status: "",
    });
    function resetForm() {
        setFormData({
            brandId: null,
            brandName: "",
            status: "",
        })
    }
    const handlePageChange = page => {
        if (!data.length) return;
        if (data.length == 0) {
            page = 0;
        }
        fetchbrandMasterData(page);
    };
    function editbrandMasterForm(row) {
        const brandId = row.brandId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/brand/get-Brand-byid/${brandId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                brandId: resultData.brandId,
                brandName: resultData.brandName,
                status: resultData.status,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deletebrandMasterForm = (row) => {
        const brandId = row.brandId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/brand/delete/${brandId}`,
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
        if (data.length == 0) {
            page = 0;
        }
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/brand/pagination?page=${page}&per_page=${newPerPage}`, {}, {
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
            name: 'Brand Name',
            selector: row => row.brandName,
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
                <button type="button" className="btn bg btn-xs" id={`${row.brandId}`} data-toggle="modal" onClick={() => editbrandMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.brandId}`} data-toggle="modal" onClick={() => deletebrandMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    const fetchbrandMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/brand/pagination?page=${page}&per_page=${perPage}`, {}, {
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

    const validate = () => {
        const newErrors = {};
        if (!formData.brandName) {
            newErrors.brandName = 'This field is required';
        }
        return newErrors;
    };

    function savebrandMaster(e) {
        setLoading(true);
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            console.log("saving");
            var obj = {};
            obj.brandId = formData.brandId;
            obj.brandName = formData.brandName;
            console.log(obj);
            api.post(CONSTANTS.BASE_URL + "/api-data/brand/save", obj, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                setLoading(false);
                toast.success(result.data.message);
                fetchbrandMasterData(0);
            }).catch((e) => {
                setLoading(false);
                toast.error("Error occurred");
            })

        }
    }

    return (
        <>

            {loading && <Loader />} {/* Show loader when loading */}
            <form onSubmit={savebrandMaster}>

                <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Admin</a></li>
                        <li class="breadcrumb-item mx-2 " aria-current="page">Brands Master</li>
                    </ol>
                </nav>
                <div className="col-lg-12 mt-2 mb-4">
                    <div className="card shadow p-4">

                        <div className="row">
                            <h3>Brand Master</h3>
                        </div>
                        <div className="col-md-3 col-sm-12 mt-3">
                            <label className="form-lable">Brand name</label>
                            <input type="text" className="form-control" value={formData.brandName}
                                onChange={(e) => setFormData({ ...formData, brandName: e.target.value })} placeholder="Enter brand name" />
                            {errors.brandName && <span className="error">{errors.brandName}</span>}
                            <input type="hidden" id="brandId" value={formData.brandId}
                                onChange={(e) => setFormData({ ...formData, brandId: e.target.value })} />
                        </div>
                        <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                            <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                <button className="btn btn-primary" type="submit"><i class="fa fa-save mx-1"></i> Save</button>
                            </div>
                        </div>

                    </div>

                </div>
                <div className="col-lg-12 mb-4">
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

            <ToastContainer />
        </>
    )
}