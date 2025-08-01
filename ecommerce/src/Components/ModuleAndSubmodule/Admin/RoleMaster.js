import React, { useState } from "react";
import CONSTANTS from "../../utils/Constants";
import { ExpandLess, ExpandMore } from "@mui/icons-material";
import { Collapse, List, ListItem, ListItemButton, ListItemIcon, ListItemText } from "@mui/material";
import { toast, ToastContainer } from "react-toastify";
import CommonDataTable from "../../utils/DataTableUtil";
import tableCustomStyles from "../../../css/TableCustomStyles";
import useCommonEffect from "../../Session/useCommonEffect";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import api from "../../utils/axiosSetup";
import Loader from "../../Structure/Loader";
const ReactSwal = withReactContent(Swal);


export default function RoleMaster() {
    const [formData, setFormData] = useState({
        roleId: null,
        roleName: "",
        viewAccess: [],
        editAccess: [],
        deleteAccess: [],
    });
    const [tableData, setTableData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [data, setData] = React.useState([]);
    const [openItems, setOpenItems] = React.useState({});
    const [activeButtons, setActiveButtons] = useState({}); // State for active buttons
    const handlePageChange = page => {
        fetchRoleMasterData(page);
    };
    const handlePerRowsChange = async (newPerPage, page) => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/role-master/pagination?page=${page}&per_page=${newPerPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setTableData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };
    const [serverError, setServerError] = useState(null);
    function resetForm() {
        setFormData({
            roleId: null,
            roleName: "",
            viewAccess: [],
            editAccess: [],
            deleteAccess: [],
        })
        setActiveButtons({});
    }
    const validate = () => {
        const newErrors = {};
        if (!formData.roleName) {
            newErrors.roleName = 'This field is required';
        }
        return newErrors;
    };
    function saveRoleMaster(e) {
        setLoading(true);
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setLoading(false);
            setErrors(validationErrors);
        } else {
            setErrors({});
            console.log("viewAccess:" + formData.viewAccess);
            console.log("editAccess:" + formData.editAccess);
            console.log("deleteAccess:" + formData.deleteAccess);
            if (formData.viewAccess.length == 0 && formData.editAccess.length == 0 && formData.deleteAccess.length == 0) {
                toast.error('Please add atleast one access!');
                return;
            }
            var obj = {};
            obj.roleId = formData.roleId;
            obj.roleName = formData.roleName;
            obj.viewAccess = formData.viewAccess;
            obj.editAccess = formData.editAccess;
            obj.deleteAccess = formData.deleteAccess;
            api.post(CONSTANTS.BASE_URL + "/api-data/role-master/save", obj, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                setLoading(false);
                toast.success(result.data.message);
                fetchRoleMasterData(0);
                resetForm();
            }).catch((e) => {
                setLoading(false);
                toast.error("Error occurred");
            })
        }
    }

    const fetchRoleMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/role-master/pagination?page=${page}&per_page=${perPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setTableData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };



    React.useEffect(() => {
        const fetchLoggedInUsersAccess = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                console.error('No token found in localStorage');
                return;
            }
            try {
                const response = await api.post(
                    CONSTANTS.BASE_URL + '/api-data/auth/get-user-access',
                    {},
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );
                console.log(response.data);
                setData(response.data);
            } catch (error) {
                console.error('Error fetching user access:', error);
            }
        };
        if (data.length == 0) {
            fetchLoggedInUsersAccess();
        }
        fetchRoleMasterData(0);
    }, []);

    const handleToggle = (id) => {
        setOpenItems((prev) => ({
            ...prev,
            [id]: !prev[id],
        }));
    };

    const handleButtonClick = (subModuleId, actionType, isParent = false, childSubModules) => {
        setActiveButtons((prevState) => ({
            ...prevState,
            [`${subModuleId}-${actionType}`]: !prevState[`${subModuleId}-${actionType}`], // Toggle active state
        }));

        if (actionType === "view") {
            if (isParent) {
                // When the parent button is clicked, update all child submodules too
                updateSubModulesState(subModuleId, actionType, childSubModules);
            } else {
                // Handle single submodule click
                setFormData((prevFormData) => {
                    var newViewAccess = [];
                    if (!prevFormData.viewAccess.includes(subModuleId)) {
                        newViewAccess = [...prevFormData.viewAccess, subModuleId]; // Add to viewAccess
                    }
                    return {
                        ...prevFormData,
                        viewAccess: newViewAccess,
                    };
                });
            }

        } else if (actionType === "edit") {
            if (isParent) {
                // When the parent button is clicked, update all child submodules too
                updateSubModulesState(subModuleId, actionType, childSubModules);
            } else {
                setFormData((prevFormData) => {
                    var newEditAccess = [];
                    if (prevFormData.editAccess.includes(subModuleId)) {
                        newEditAccess = [...prevFormData.editAccess, subModuleId]; // Add to editAccess
                    }
                    return {
                        ...prevFormData,
                        editAccess: newEditAccess,
                    };
                });
            }

        } else if (actionType === "delete") {
            if (isParent) {
                // When the parent button is clicked, update all child submodules too
                updateSubModulesState(subModuleId, actionType, childSubModules);
            } else {
                setFormData((prevFormData) => {
                    var newDeleteAccess = [];
                    if (prevFormData.deleteAccess.includes(subModuleId)) {
                        newDeleteAccess = [...prevFormData.deleteAccess, subModuleId]; // Add to deleteAccess
                    }


                    return {
                        ...prevFormData,
                        deleteAccess: newDeleteAccess,
                    };
                });
            }
        }
        console.log("viewAccess:" + formData.viewAccess + " editAccess:" + formData.editAccess + " deleteAccess:" + formData.deleteAccess);
        // Handle child submodules if any
        if (childSubModules) {
            childSubModules.forEach((childSubModule) => {
                setActiveButtons((prevState) => ({
                    ...prevState,
                    [`${childSubModule.subModuleId}-${actionType}`]: !prevState[`${childSubModule.subModuleId}-${actionType}`], // Toggle active state
                }));
                var childIsParent = childSubModule.subModule && childSubModule.subModule.length > 0;
                handleButtonClick(childSubModule.subModuleId, actionType, childIsParent, childSubModule.subModule);
            });
        }
    };


    // Recursively update the state of all submodules under a parent
    const updateSubModulesState = (parentId, actionType, childSubModules) => {
        // Toggle logic for each action (view, edit, delete)
        const toggleState = (actionType === "view" || actionType === "edit" || actionType === "delete");

        // Function to update state for each module (parent and children)
        const updateStateForModule = (module) => {
            const { subModuleId, subModule } = module;

            // Update state for the parent module
            setFormData((prevFormData) => {
                let updatedState;

                // Logic for view, edit, or delete actions
                if (actionType === "view") {
                    updatedState = toggleState
                        ? [...prevFormData.viewAccess, subModuleId]
                        : prevFormData.viewAccess.filter((id) => id !== subModuleId);
                } else if (actionType === "edit") {
                    updatedState = toggleState
                        ? [...prevFormData.editAccess, subModuleId]
                        : prevFormData.editAccess.filter((id) => id !== subModuleId);
                } else if (actionType === "delete") {
                    updatedState = toggleState
                        ? [...prevFormData.deleteAccess, subModuleId]
                        : prevFormData.deleteAccess.filter((id) => id !== subModuleId);
                }

                // Return updated state with appropriate action type access
                return {
                    ...prevFormData,
                    [`${actionType}Access`]: updatedState,
                };
            });

            // If this module has children, propagate the state to them
            if (subModule && subModule.length > 0) {
                subModule.forEach(updateStateForModule);
            }
        };

        // Find the parent submodule and start propagating the state to its children
        const parentModule = data.find((item) => item.subModuleId === parentId);
        if (parentModule) {
            updateStateForModule(parentModule);
        }

        // If there are child submodules, update them as well
        if (childSubModules) {
            childSubModules.forEach((child) => {
                updateStateForModule(child);
            });
        }
    };


    function editRoleMasterForm(row) {
        const roleId = row.roleId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/role-master/get-role-master-byid/${roleId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                roleId: resultData.roleId,
                roleName: resultData.roleName,
                viewAccess: resultData.viewAccess,
                editAccess: resultData.editAccess,
                deleteAccess: resultData.deleteAccess,
            });
            for (let i = 0; i < resultData.viewAccess.length; i++) {
                let subModuleId = resultData.viewAccess[i];
                console.log("setting viewlist:" + subModuleId);
                setActiveButtons((prevState) => ({
                    ...prevState,
                    [`${subModuleId}-${"view"}`]: !prevState[`${subModuleId}-${"view"}`], // Toggle active state
                }));
            }
            for (let i = 0; i < resultData.editAccess.length; i++) {
                let subModuleId = resultData.editAccess[i];
                console.log("setting editList:" + subModuleId);
                setActiveButtons((prevState) => ({
                    ...prevState,
                    [`${subModuleId}-${"edit"}`]: !prevState[`${subModuleId}-${"edit"}`], // Toggle active state
                }));
            }
            for (let i = 0; i < resultData.deleteAccess.length; i++) {
                let subModuleId = resultData.deleteAccess[i];
                console.log("setting deleteList:" + subModuleId);
                setActiveButtons((prevState) => ({
                    ...prevState,
                    [`${subModuleId}-${"delete"}`]: !prevState[`${subModuleId}-${"delete"}`], // Toggle active state
                }));
            }
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteRoleMasterForm = (row) => {
        const roleId = row.roleId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/role-master/delete/${roleId}`,
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


    // Render the modules and buttons with dynamic colors
    const renderModules = (modules) => {
        return modules.map((item) => {
            const hasChildren = item.subModule?.length > 0;

            return (
                <div key={item.subModuleId}>
                    <ListItem disablePadding>
                        <ListItemButton onClick={() => handleToggle(item.subModuleId)}>
                            <ListItemIcon>
                                <i className={item.icon}></i>
                            </ListItemIcon>
                            <ListItemText primary={item.subModuleName} />
                            <a
                                className={`btn mx-1 ${activeButtons[`${item.subModuleId}-view`] ? 'btn-success' : 'btn-secondary'}`}
                                data-added="false"
                                id={`${item.subModuleId}-view`}
                                onClick={(e) => {
                                    e.stopPropagation(); // Prevent ListItemButton click from triggering
                                    handleButtonClick(item.subModuleId, "view", !hasChildren, item.subModule); // If it has submodules, mark as parent
                                }}
                            >
                                <i className="fa fa-eye mx-1"></i>
                            </a>
                            <a
                                className={`btn mx-1 ${activeButtons[`${item.subModuleId}-edit`] ? 'btn-success' : 'btn-secondary'}`}
                                data-added="false"
                                id={`${item.subModuleId}-edit`}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleButtonClick(item.subModuleId, "edit", !hasChildren, item.subModule);
                                }}
                            >
                                <i className="fa fa-pencil mx-1"></i>
                            </a>
                            <a
                                className={`btn mx-1 ${activeButtons[`${item.subModuleId}-delete`] ? 'btn-success' : 'btn-secondary'}`}
                                data-added="false"
                                id={`${item.subModuleId}-delete`}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleButtonClick(item.subModuleId, "delete", !hasChildren, item.subModule);
                                }}
                            >
                                <i className="fa fa-trash-o mx-1"></i>
                            </a>
                            {hasChildren ? (openItems[item.subModuleId] ? <ExpandLess /> : <ExpandMore />) : null}
                        </ListItemButton>
                    </ListItem >

                    {/* Display nested submodules only when expanded */}
                    {
                        hasChildren && (
                            <Collapse in={openItems[item.subModuleId]}>
                                <List sx={{ pl: 4 }}>
                                    {renderModules(item.subModule)} {/* Recursively render submodules */}
                                </List>
                            </Collapse>
                        )
                    }
                </div >
            );
        });
    };

    const MyComponent = ({ data }) => {
        return <List>{renderModules(data)}</List>;
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
            selector: row => row.roleName,
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
                <button type="button" className="btn bg btn-xs" id={`${row.roleId}`} data-toggle="modal" onClick={() => editRoleMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.roleId}`} data-toggle="modal" onClick={() => deleteRoleMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    return (
        <>

            {loading && <Loader />} {/* Show loader when loading */}

            <div className="row">
                <div className="col-lg-12 mt-2 mb-4">
                    <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                            <li class="breadcrumb-item mx-2"><a href="#">Admin</a></li>
                            <li class="breadcrumb-item mx-2 " aria-current="page">Role Master</li>
                        </ol>
                    </nav>
                    <form onSubmit={saveRoleMaster}>
                        <div className="card shadow p-4">
                            <div className="row">
                                <h3>Role Master</h3>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Role Name</label>
                                    <input
                                        className="form-control"
                                        id="roleName"
                                        placeholder="Enter role name"
                                        value={formData.roleName}
                                        onChange={(e) => setFormData({ ...formData, roleName: e.target.value })}
                                    />
                                    <input type="hidden" value={formData.roleId} onChange={(e) => setFormData({ ...formData, roleId: e.target.value })} />
                                    {errors.roleName && <span className="error">{errors.roleName}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label"></label>
                                    <div className="">
                                        <button className="btn btn-primary" type="submit"><i className="fa fa-save"></i>Save</button>
                                    </div>
                                </div>
                            </div>
                            <div className="row mt-3 mb-3">
                                <div className="col-md-12">
                                    <MyComponent data={data} />
                                </div>
                            </div>

                        </div>
                    </form>
                    <ToastContainer />
                </div>
                <div className="col-lg-12 mt-2 mb-4">
                    <div className="card shadow p-4">
                        <div className="row">
                            <CommonDataTable
                                columns={columns}
                                data={tableData}
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




        </>
    );
}
