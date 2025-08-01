
import CommonScreen from "../../Structure/CommonScreen";
import CONSTANTS from "../../utils/Constants";
import React, { useEffect, useRef, useState } from "react";
import { useAuth } from "../../../features/AuthProvider ";
import { toast, ToastContainer } from "react-toastify";
import tableCustomStyles from "../../../css/TableCustomStyles";
import DataTable from 'react-data-table-component';
import Swal from 'sweetalert2';
import withReactContent from 'sweetalert2-react-content';
import CommonDataTable from "../../utils/DataTableUtil";
import Loader from "../../Structure/Loader";
import api from "../../utils/axiosSetup";

const ReactSwal = withReactContent(Swal);

export default function SubModuleMasterAdd() {

    /**DataTable Code Start */
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [formData, setFormData] = React.useState({
        subModuleId: "",
        subModuleName: "",
        menuTypeId: "",
        parentId: "",
        status: "",
        requestMapping: "",
        icon: "",
    });
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const columns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Table

        },
        {
            name: 'Submodule name',
            selector: row => row.subModuleName,
            sortable: true,
            center: true,
        },
        {
            name: 'Menu type',
            selector: row => row.menuTypeName,
            sortable: true,
            center: true,
        },
        {
            name: 'Parent Submodule',
            selector: row => row.parentName,
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
                <button type="button" className="btn bg btn-xs" id={`${row.subModuleId}`} data-toggle="modal" onClick={() => editSubmoduleMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.subModuleId}`} data-toggle="modal" onClick={() => deleteSubmoduleMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    function resetForm() {
        setFormData({
            subModuleId: "",
            subModuleName: "",
            menuTypeId: "",
            parentId: "",
            status: "",
            requestMapping: "",
            icon: "",
        })
    }

    function editSubmoduleMasterForm(row) {
        const subModuleId = row.subModuleId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/submodule/get-submodule-byid/${subModuleId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                subModuleId: resultData.subModuleId,
                subModuleName: resultData.subModuleName,
                menuTypeId: resultData.menuTypeId,
                parentId: resultData.parentId,
                status: resultData.status,
                requestMapping: resultData.requestMapping,
                icon: resultData.icon
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteSubmoduleMasterForm = (row) => {
        const subModuleId = row.subModuleId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/submodule/delete/${subModuleId}`,
                    {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                        },
                    }
                ).then((result) => {
                    handlePerRowsChange(10, 0);
                    resetForm();
                    fetchParentModules();
                });

                Swal.fire('Deleted!', 'The item has been deleted.', 'success');
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                Swal.fire('Cancelled', 'Your item is safe :)', 'error');
            }
        });
    };

    const fetchSubModuleMasterData = async page => {
        setLoading(true);
        console.log("pagination:" + authToken);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/submodule/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };

    const handlePageChange = page => {
        if (data.length == 0) {
            page = 0;
        }
        fetchSubModuleMasterData(page);
    };

    const handlePerRowsChange = async (newPerPage, page) => {
        setLoading(true);
        if (data.length == 0) {
            page = 0;
        }
        console.log("pagination:" + authToken);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/submodule/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });

        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };

    useEffect(() => {

    }, []);

    const [menuTypeList, setMenuTypeList] = useState([]);
    const [parentModuleList, setParentModuleList] = useState([]);
    const { authToken } = useAuth();

    function fetchMenuType() {
        if (menuTypeList.length != 0) return;
        console.log("authotoken in getmenutypelist:" + authToken);
        api.get(CONSTANTS.BASE_URL + "/api-data/menu-type/get-menu-type-list", {}, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        })
            .then((result) => {
                setMenuTypeList(result.data);
            }).catch((e) => {
                console.log(e);
            });
    }
    function fetchParentModules() {
        if (parentModuleList.length != 0) return;
        api.get(CONSTANTS.BASE_URL + "/api-data/submodule/get-submodule-list", {}, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        })
            .then((result) => {
                setParentModuleList(result.data);
            }).catch((e) => {
                console.log(e);
            });
    }
    const validate = () => {
        const newErrors = {};
        if (!formData.subModuleName) {
            newErrors.subModuleName = 'This field is required';
        }
        if (!formData.menuTypeId) {
            newErrors.menuTypeId = 'Please Select';
        }
        // if (!formData.requestMapping) {
        //     newErrors.requestMapping = 'This field is required';
        // }
        if (!formData.icon) {
            newErrors.icon = 'This field is required';
        }
        return newErrors;
    };
    function saveSubModule(e) {
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            setErrors({});
            var obj = {};
            obj.subModuleId = formData.subModuleId
            obj.subModuleName = formData.subModuleName
            obj.menuTypeId = formData.menuTypeId
            obj.parentId = formData.parentId
            obj.requestMapping = formData.requestMapping
            obj.icon = formData.icon
            if (formData.status)
                obj.status = "Y";
            else
                obj.status = "N";
            api.post(CONSTANTS.BASE_URL + "/api-data/submodule/save", obj, {
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                    "Content-Type": "application/json"
                }
            }).then((result) => {
                if (result.data.message == "success") {
                    toast.success("Data saved successfully");
                } else {
                    toast.error("Error occurred while saving");
                }
                handlePerRowsChange(10, 0);
                resetForm();
                fetchParentModules();
            }).catch((e) => {
                if (e.response) {
                    // Server returned an error
                    setServerError(e.response.data.message || 'Server error occurred');
                } else {
                    setServerError('Unable to reach the server');
                }
            })

        }
    }

    useEffect(() => {
        fetchMenuType();
        fetchParentModules();
        //refreshTable();
    }, [])

    return (
        <>
            <div >
                {loading && <Loader />} {/* Show loader when loading */}
                <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Admin</a></li>
                        <li class="breadcrumb-item mx-2 " aria-current="page">Submodule Master</li>
                    </ol>
                </nav>
                <div className="card card-body">
                    <form onSubmit={saveSubModule}>
                        <div className="row">
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Module/SubModule Name</label>
                                <input className="form-control" id="subModuleName" placeholder="Enter submodule name"
                                    value={formData.subModuleName}
                                    onChange={(e) => setFormData({ ...formData, subModuleName: e.target.value })} />
                                {errors.subModuleName && <span className="error">{errors.subModuleName}</span>}
                                <input id="subModuleId" name="subModuleId" value={formData.subModuleId} type="hidden" onChange={(e) => setFormData({ ...formData, subModuleId: e.target.value })} />
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Menu Type</label>
                                <select className="form-control" name="menuTypeId" value={formData.menuTypeId} onChange={(e) => setFormData({ ...formData, menuTypeId: e.target.value })}>
                                    <option value="0">--Please Select--</option>
                                    {menuTypeList.map((menuType, index) => (
                                        <option key={index} value={menuType.menuTypeId}>
                                            {menuType.menuTypeName}
                                        </option>
                                    ))}
                                </select>
                                {errors.menuTypeId && <span className="error">{errors.menuTypeId}</span>}
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Parent Module</label>
                                <select className="form-control" name="parentId" value={formData.parentId} onChange={(e) => setFormData({ ...formData, parentId: e.target.value })}>
                                    <option value="0">--Please Select--</option>
                                    {parentModuleList.map((submodule, index) => (
                                        <option key={index} value={submodule.subModuleId}>
                                            {submodule.subModuleName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Request Mapping</label>
                                <input className="form-control" name="requestMapping" value={formData.requestMapping} onChange={(e) => setFormData({ ...formData, requestMapping: e.target.value })} id="requestMapping" placeholder="Enter request mapping"
                                />
                                {errors.requestMapping && <span className="error">{errors.requestMapping}</span>}
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label">Icon</label>
                                <input className="form-control" name="icon" value={formData.icon} id="icon" placeholder="Enter remixicon icon"
                                    onChange={(e) => setFormData({ ...formData, icon: e.target.value })}
                                />
                                {errors.icon && <span className="error">{errors.icon}</span>}
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-label mx-1">Status</label>
                                <input type="checkbox" name="status" className="form-check-input" id="status" />
                            </div>
                            <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                <div className="col-md-2 col-sm-2" style={{ display: 'flex', justifyContent: 'right' }}>
                                    <button className="btn btn-primary mx-1" type="submit"><i class="fa fa-save mx-1"></i> Save</button>
                                    <button className="btn btn-primary mx-1" onClick={resetForm}>
                                        < i class="ri-refresh-line"></i> Refresh</button>
                                </div>

                            </div>
                        </div>
                    </form>
                </div>
                <div className="card card-body mt-3">
                    <div className="row">
                        <div className="table-responsive">

                            {/*  */}
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