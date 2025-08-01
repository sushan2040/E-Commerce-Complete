import React, { useEffect, useState } from "react";
import CommonDataTable from "../../../utils/DataTableUtil";
import { toast, ToastContainer } from "react-toastify";
import useCommonEffect from "../../../Session/useCommonEffect";
import tableCustomStyles from "../../../../css/TableCustomStyles";
import CONSTANTS from "../../../utils/Constants";
import api from "../../../utils/axiosSetup";
import Loader from "../../../Structure/Loader";


export default function AddUser() {
    const [formData, setFormData] = useState({
        userId: null,
        roleId: null,
        email: "",
        mobile: "",
        firstName: "",
        middleName: "",
        lastName: "",
        userName: "",
        password: "",
        level1Id: null,
        level2Id: null,
        level3Id: null,
        level4Id: null,
        level5Id: null,
        areacode: "",
        address: "",
        status: "",
        departmentId: null
    });
    const [tableData, setTableData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [countries, setCountries] = useState([]);
    const [level2List, setLevel2List] = useState([]);
    const [level3List, setLevel3List] = useState([]);
    const [level4List, setLevel4List] = useState([]);
    const [level5List, setLevel5List] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [data, setData] = React.useState([]);
    const [roles, setRoles] = useState([]);
    const [deptList, setDeptList] = useState([]);

    const columns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Table

        },
        {
            name: 'Fullname',
            selector: row => row.firstName + " " + row.middleName + " " + row.lastName,
            sortable: true,
            center: true,
        },
        {
            name: 'username',
            selector: row => row.username,
            sortable: true,
            center: true,
        },
        {
            name: 'Role Name',
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
                <button type="button" className="btn bg btn-xs" id={`${row.empId}`} data-toggle="modal" onClick={() => editEmployeeMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.empId}`} data-toggle="modal" onClick={() => deleteEmployeeMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];

    const fetchRoles = async => {
        api.get(CONSTANTS.BASE_URL + "/api-data/role-master/get-roles", {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        }).then((result) => {
            console.log(result);
            setRoles(result.data);
        })

    }

    function saveUserMaster(e) {
        setLoading(true);
        e.preventDefault();
        var obj = {};
        obj.roleId = formData.roleId;
        obj.username = formData.username;
        obj.password = formData.password;
        obj.firstName = formData.firstName;
        obj.middleName = formData.middleName;
        obj.lastName = formData.lastName;
        obj.email = formData.email;
        obj.password = formData.password;
        obj.mobile = formData.mobile;
        obj.level1Id = formData.level1Id;
        obj.level2Id = formData.level2Id;
        obj.level3Id = formData.level3Id;
        obj.level4Id = formData.level4Id;
        obj.level5Id = formData.level5Id;
        obj.areacode = formData.areacode;
        obj.address = formData.address;
        obj.status = formData.status;
        obj.departmentId = formData.departmentId;
        api.post(CONSTANTS.BASE_URL + "/api-data/employee-master/save", obj, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        }).then((result) => {
            setLoading(false);
            toast.success(result.data.message);
            fetchEmployeeMasterData(0);
            resetForm();
        }).catch((e) => {
            setLoading(false);
            toast.error("Error occurred");
        })

    }
    function editEmployeeMasterForm() {

    }
    function deleteEmployeeMasterForm() {

    }
    function resetForm() {
        setFormData({
            userId: null,
            roleId: null,
            email: "",
            mobile: "",
            firstName: "",
            middleName: "",
            lastName: "",
            username: "",
            password: "",
            level1Id: null,
            level2Id: null,
            level3Id: null,
            level4Id: null,
            level5Id: null,
            areacode: "",
            address: "",
            status: "",
            departmentId: null
        })
    }
    const handlePageChange = page => {
        if (data.length == 0) {
            page = 0;
        }
        fetchEmployeeMasterData(page);
    };
    const handlePerRowsChange = async (newPerPage, page) => {
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/employee-master/pagination?page=${page}&per_page=${newPerPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setTableData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };

    function setLevel2Elements(e) {
        api.get(CONSTANTS.BASE_URL + "/api-data/location-level2/get-location-levelchild-bylevel-parentid/" + e.target.value)
            .then((result) => {
                var resultData = result.data;
                console.log("level2List:" + resultData);
                setLevel2List(resultData);
            })
    }
    function setLevel3Elements(e) {
        api.get(CONSTANTS.BASE_URL + "/api-data/location-level3/get-location-levelchild-bylevel-parentid/" + e.target.value)
            .then((result) => {
                var resultData = result.data;
                console.log("level2List:" + resultData);
                setLevel3List(resultData);
            })
    }
    function setLevel4Elements(e) {
        api.get(CONSTANTS.BASE_URL + "/api-data/location-level4/get-location-levelchild-bylevel-parentid/" + e.target.value)
            .then((result) => {
                var resultData = result.data;
                console.log("level2List:" + resultData);
                setLevel4List(resultData);
            })
    }
    function setLevel5Elements(e) {
        api.get(CONSTANTS.BASE_URL + "/api-data/location-level5/get-location-levelchild-bylevel-parentid/" + e.target.value)
            .then((result) => {
                var resultData = result.data;
                console.log("level5List:" + resultData);
                setLevel5List(resultData);
            })
    }


    const fetchCountries = async => {
        api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
            .then((result) => {
                var resultData = result.data;
                console.log("countries:" + resultData);
                setCountries(resultData);
            })
    }
    const fetchDepartments = async => {
        api.get(CONSTANTS.BASE_URL + "/api-data/department-master/get-department-master-list")
            .then((result) => {
                var resultData = result.data;
                console.log("dept:" + resultData);
                setDeptList(resultData);
            })
    }

    const fetchEmployeeMasterData = async page => {
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/employee-master/pagination?page=${page}&per_page=${perPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };
    useEffect(() => {
        fetchRoles();
        fetchCountries();
        fetchDepartments();
    }, [])

    return (


        <>
            {loading && <Loader />}
            <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                <ol class="breadcrumb">
                    <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">Seller</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">User Management</a></li>
                    <li class="breadcrumb-item mx-2 " aria-current="page">Employee Master</li>
                </ol>
            </nav>
            <div className="row">
                <div className="col-lg-6 mt-2 mb-4">
                    <form onSubmit={saveUserMaster}>
                        <div className="card shadow p-4">
                            <div className="row">
                                <h3>Employee Master</h3>
                                <div className="row">
                                    <div className="col-sm-10">
                                        <h5 className="card text-bg-primary p-1" > <span className="card-text"><span class="badge text-bg-secondary mx-1">1</span>Personal Details </span></h5>
                                    </div>
                                </div>


                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Role Name</label>
                                    <div className="">
                                        <select className="form-control" value={formData.roleId} onChange={(e) => setFormData({ ...formData, roleId: e.target.value })}>
                                            <option value="0">--Please Select--</option>
                                            {roles.map((role, index) => (
                                                <option key={index} value={role.roleId}>
                                                    {role.roleName}
                                                </option>
                                            ))}
                                        </select>
                                        <input type="hidden" value={formData.roleId} onChange={(e) => setFormData({ ...formData, roleId: e.target.value })} />
                                        {errors.roleId && <span className="error">{errors.roleId}</span>}
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Email</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            placeholder="Enter email"
                                            value={formData.email}
                                            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                        />
                                    </div>
                                    {errors.email && <span className="error">{errors.email}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Mobile Number</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            placeholder="Enter mobile number"
                                            value={formData.mobile}
                                            onChange={(e) => setFormData({ ...formData, mobile: e.target.value })}
                                        />
                                    </div>
                                    {errors.mobile && <span className="error">{errors.mobile}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">First Name</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            placeholder="Enter first name"
                                            value={formData.firstName}
                                            onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                                        />
                                    </div>
                                    <input type="hidden" value={formData.firstName} onChange={(e) => setFormData({ ...formData, firstName: e.target.value })} />
                                    {errors.roleName && <span className="error">{errors.roleName}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Middle Name</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            placeholder="Enter middle name"
                                            value={formData.middleName}
                                            onChange={(e) => setFormData({ ...formData, middleName: e.target.value })}
                                        />
                                    </div>
                                    {errors.middleName && <span className="error">{errors.middleName}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Last Name</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            placeholder="Enter last name"
                                            value={formData.lastName}
                                            onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                                        />
                                    </div>
                                    {errors.lastName && <span className="error">{errors.lastName}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">User Name</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            id="roleName"
                                            placeholder="Enter username"
                                            value={formData.username}
                                            onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                                        />
                                    </div>
                                    {errors.username && <span className="error">{errors.username}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Password</label>
                                    <div className="">
                                        <input
                                            className="form-control"
                                            id="roleName"
                                            placeholder="Enter password"
                                            value={formData.password}
                                            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                                        />
                                    </div>
                                    {errors.password && <span className="error">{errors.password}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Department</label>
                                    <div className="">
                                        <select className="form-control" value={formData.departmentId} onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })}>
                                            <option value="0">---Please select-</option>
                                            {deptList.map((result, index) => (
                                                <option key={index} value={result.departmentId}>{result.departmentName}</option>
                                            ))}
                                        </select>
                                    </div>
                                    {errors.departmentId && <span className="error">{errors.departmentId}</span>}
                                </div>


                                <div className="row mt-4 mb-2">
                                    <div className="col-sm-10">
                                        <h5 className="card text-bg-primary p-1" > <span className="card-text"><span class="badge text-bg-secondary mx-1">2</span>Address Details </span></h5>
                                    </div>
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Country</label>
                                    <div className="">
                                        <select className="form-control" value={formData.level1Id} onChange={(e) => { setFormData({ ...formData, level1Id: e.target.value }); setLevel2Elements(e); }}>
                                            <option>--Please Select--</option>
                                            {countries.map((country, index) => (
                                                <option key={index} value={country.countryId}>
                                                    {country.countryName}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    {errors.level1Id && <span className="error">{errors.level1Id}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">State</label>
                                    <div className="">
                                        <select className="form-control" value={formData.level2Id} onChange={(e) => { setFormData({ ...formData, level2Id: e.target.value }); setLevel3Elements(e); }}>
                                            <option>--Please Select--</option>
                                            {level2List.map((role, index) => (
                                                <option key={index} value={role.level2Id}>
                                                    {role.level2Name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    {errors.level2Id && <span className="error">{errors.level2Id}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">District</label>
                                    <div className="">
                                        <select className="form-control" value={formData.level3Id} onChange={(e) => { setFormData({ ...formData, level3Id: e.target.value }); setLevel4Elements(e); }}>
                                            <option>--Please Select--</option>
                                            {level3List.map((role, index) => (
                                                <option key={index} value={role.level3Id}>
                                                    {role.level3Name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    {errors.level3Id && <span className="error">{errors.level3Id}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3" >
                                    <label className="form-label">Taluka</label>
                                    <div className="">
                                        <select className="form-control" value={formData.level4Id} onChange={(e) => { setFormData({ ...formData, level4Id: e.target.value }); setLevel5Elements(e); }}>
                                            <option>--Please Select--</option>
                                            {level4List.map((role, index) => (
                                                <option key={index} value={role.level4Id}>
                                                    {role.level4Name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    {errors.level4Id && <span className="error">{errors.level4Id}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">City</label>
                                    <div className="">
                                        <select className="form-control" value={formData.level5Id} onChange={(e) => setFormData({ ...formData, level5Id: e.target.value })}>
                                            <option>--Please Select--</option>
                                            {level5List.map((role, index) => (
                                                <option key={index} value={role.level5Id}>
                                                    {role.level5Name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    {errors.level5Id && <span className="error">{errors.level5Id}</span>}
                                </div>
                                <div className="col-md-4 col-sm-12 mt-3">
                                    <label className="form-label">Areacode</label>
                                    <div className="">
                                        <input className="form-control" value={formData.areacode} placeholder="Enter pincode" onChange={(e) => setFormData({ ...formData, areacode: e.target.value })} />
                                    </div>
                                    {errors.areacode && <span className="error">{errors.areacode}</span>}
                                </div>
                                <div className="col-md-12 col-sm-12 mt-3">
                                    <label className="form-label">Address</label>
                                    <div className="">
                                        <textarea className="form-control" placeholder="Enter address" value={formData.address} onChange={(e) => setFormData({ ...formData, address: e.target.value })} ></textarea>
                                    </div>
                                    {errors.address && <span className="error">{errors.address}</span>}
                                </div>

                                <div className="row" style={{ justifyContent: 'right' }}>
                                    <div className="col-md-4 col-sm-12 mt-3" >
                                        <label className="form-label"></label>
                                        <div className="">
                                            <button className="btn btn-primary" type="submit"><i className="fa fa-save"></i>Save</button>
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </div>
                    </form>
                    <ToastContainer />
                </div>
                <div className="col-lg-6 mt-2 mb-4">
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