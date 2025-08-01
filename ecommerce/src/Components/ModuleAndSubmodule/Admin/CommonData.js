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
import Autosuggest from "react-autosuggest";
import AsyncSelect from 'react-select/async'
const ReactSwal = withReactContent(Swal);


export default function CommonData() {
    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [bridgeParameters, setBridgeParameters] = useState([]);
    const [parentParameters, setParentParameters] = useState([]);
    const [commonDataHiddenId, setCommonDataHiddenId] = useState(null);
    const [commonDataHiddenName, setCommonDataHiddenName] = useState("");

    const [value, setValue] = useState('');
    const [suggestions, setSuggestions] = useState([]);

    // Function to fetch suggestions from the backend
    const fetchSuggestions = async (value) => {
        if (value.length === 0) {
            setSuggestions([]);
            return;
        }
        setLoading(true);
        try {
            const response = await api.get(CONSTANTS.BASE_URL + '/api-data/common-data/suggestions', {
                params: { query: value },
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem('authToken')
                }
            });
            console.log(response.data);
            setSuggestions(response.data);
            return response.data.map((obj) => ({
                label: obj.commonDataName,
                value: obj
            }));
        } catch (error) {
            console.error('Error fetching suggestions:', error);
        } finally {
            setLoading(false);
        }
    };

    const getSuggestions = (value) => {
        fetchSuggestions(value);
    };

    const handleInputChange = (event, { newValue }) => {
        setFormData({ ...formData, commonDataName: newValue })
        setValue(newValue);
    };

    // Handling the selection of a suggestion
    const handleSuggestionSelected = (selectedOption) => {
        console.log(selectedOption);
        setCommonDataHiddenName(selectedOption.value.commonDataName);
        setCommonDataHiddenId(selectedOption.value.commonDataId);
        setFormData({ ...formData, commonDataDesc: selectedOption.value.commonDataDesc })
        setFormData({ ...formData, commonDataName: selectedOption.value.commonDataName });
    };

    const renderSuggestion = (suggestion) => {
        return <div id={suggestion.commonDataId}>{suggestion.commonDataName}</div>;
    };


    const [formData, setFormData] = React.useState({
        commonDataId: null,
        commonDataName: "",
        commonDataDesc: "",
        parentCommonDataId: null,
        bridgeParameterId: null,
        status: ""
    });
    function resetForm() {
        setFormData({
            commonDataId: null,
            commonDataName: "",
            commonDataDesc: "",
            parentCommonDataId: 0,
            bridgeParameterId: 0,
            status: ""
        })
        setCommonDataHiddenId(null);
        setCommonDataHiddenName(null);
    }
    function fetchBridgeParameters() {
        if (bridgeParameters.length != 0) return;
        api.get(CONSTANTS.BASE_URL + "/api-data/common-data/get-bridge-parameters", {}, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        })
            .then((result) => {
                setBridgeParameters(result.data);
            }).catch((e) => {
                console.log(e);
            });
    }
    function fetchParentCommonDatas() {
        if (parentParameters.length != 0) return;
        api.get(CONSTANTS.BASE_URL + "/api-data/common-data/get-common-datas", {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken"),
            }
        })
            .then((result) => {
                console.log(result.data);
                setParentParameters(result.data);
            }).catch((e) => {
                console.log(e);
            });
    }
    const handlePageChange = page => {

        if (data.length == 0) {
            page = 0;
        }
        fetchCommonDataData(page);
    };
    function editCommonDataForm(row) {
        const commonDataId = row.commonDataId;
        api.post(`${CONSTANTS.BASE_URL}/api-data/common-data/get-by-id/${commonDataId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data;
            setFormData({
                commonDataId: resultData.commonDataId,
                commonDataName: resultData.commonDataName,
                commonDataDesc: resultData.commonDataDesc,
                parentCommonDataId: resultData.parentCommonDataId,
                bridgeParametesrId: resultData.bridgeParameterId,
                status: resultData.status,
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteCommonDataForm = (row) => {
        const commonDataId = row.commonDataId; // Access properties from the row
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
                api.post(`${CONSTANTS.BASE_URL}/api-data/common-data/delete-by-id/${commonDataId}`,
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
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/common-data/pagination?page=${page}&per_page=${newPerPage}`, {}, {
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
            name: 'Common Data Name',
            selector: row => row.commonDataName,
            sortable: true,
            center: true,
        },
        {
            name: 'Bridge Parameter Name',
            selector: row => row.bridgeParameterName,
            sortable: true,
            center: true,
        },
        {
            name: 'Parent Common Data Name',
            selector: row => row.parentCommonDataName,
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
                <button type="button" className="btn bg btn-xs" id={`${row.commonDataId}`} data-toggle="modal" onClick={() => editCommonDataForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.commonDataId}`} data-toggle="modal" onClick={() => deleteCommonDataForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    const fetchCommonDataData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/common-data/pagination?page=${page}&per_page=${perPage}`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };

    useEffect(() => {
        fetchBridgeParameters();
        fetchParentCommonDatas()
    }, [])

    const validate = () => {
        const newErrors = {};
        if (!formData.commonDataName) {
            newErrors.commonDataName = 'This field is required';
        }
        if (!formData.commonDataDesc) {
            newErrors.commonDataDesc = 'This field is required';
        }
        return newErrors;
    };

    function saveCommonData(e) {
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
            console.log(formData);
            obj.commonDataId = commonDataHiddenId;
            obj.commonDataName = formData.commonDataName;
            obj.commonDataDesc = formData.commonDataDesc;
            obj.parentCommonDataId = formData.parentCommonDataId;
            obj.bridgeParameterId = formData.bridgeParameterId;
            console.log(obj);
            api.post(CONSTANTS.BASE_URL + "/api-data/common-data/save", obj, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                setLoading(false);
                toast.success(result.data.message);
                fetchCommonDataData(0);
                fetchParentCommonDatas();
                resetForm();
            }).catch((e) => {
                setLoading(false);
                toast.error("Error occurred");
            })
            fetchParentCommonDatas();
        }
    }

    // Function to capitalize the input
    const capitalize = (text) => {
        return text.toUpperCase();
    };

    const handleChange = (e) => {

        const capitalizedValue = capitalize(e.target.value);
        console.log(capitalizedValue);
        setFormData({ ...formData, commonDataName: capitalizedValue });
        setCommonDataHiddenName(capitalizedValue);
    };
    return (
        <>

            {loading && <Loader />} {/* Show loader when loading */}
            <form onSubmit={saveCommonData}>

                <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Admin</a></li>
                        <li class="breadcrumb-item mx-2 " aria-current="page">Common Data</li>
                    </ol>
                </nav>
                <div className="col-lg-12 mt-2 mb-4">
                    <div className="card shadow p-4">
                        <div className="row">
                            <div className="row">
                                <h3>Common Data Master</h3>
                            </div>

                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Common Data Name Suggestion</label>
                                <div className="">
                                    {/* <Autosuggest
                                        suggestions={suggestions}
                                        onChange={(e) => setFormData({ ...formData, commonDataName: e.target.value })}
                                        value={formData.commonDataName}
                                        onSuggestionsFetchRequested={({ value }) => getSuggestions(value)}
                                        onSuggestionsClearRequested={() => setSuggestions([])}
                                        getSuggestionValue={(suggestion) => suggestion}
                                        renderSuggestion={renderSuggestion}
                                        inputProps={{
                                            value,
                                            onChange: handleInputChange,
                                            placeholder: 'Search ',
                                        }}
                                        className="form-control"
                                        onSuggestionSelected={handleSuggestionSelected}
                                    /> */}
                                    {/* <Select
                                        options={suggestions}
                                        value={{
                                            label: commonDataHiddenName,
                                            value: commonDataHiddenName,
                                        }}
                                        onInputChange={handleInputChange}
                                        onChange={handleSuggestionSelected}
                                        placeholder="Search"
                                        className="form-control"
                                    /> */}
                                    <AsyncSelect
                                        cacheOptions
                                        isClearable={false}
                                        loadOptions={fetchSuggestions} // Fetches the suggestions asynchronously
                                        onChange={handleSuggestionSelected}
                                        onInputChange={handleInputChange}
                                        value={{ label: commonDataHiddenName, value: commonDataHiddenName }}
                                        placeholder="Search for a product"
                                        className="form-control"
                                        defaultOptions // Optionally, show some default suggestions
                                    />

                                </div>
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-lable">Common Data name</label>
                                <input type="text" className="form-control" value={formData.commonDataName}
                                    onChange={handleChange} placeholder="Enter common data name" />
                                {errors.commonDataName && <span className="error">{errors.commonDataName}</span>}
                                <input type="hidden" id="commonDataId" value={formData.commonDataId}
                                    onChange={(e) => setFormData({ ...formData, commonDataId: e.target.value })} />
                            </div>

                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-lable">Common Data Desc</label>
                                <input type="text" className="form-control" value={formData.commonDataDesc}
                                    onChange={(e) => setFormData({ ...formData, commonDataDesc: e.target.value })} placeholder="Enter common data desc" />
                                {errors.commonDataDesc && <span className="error">{errors.commonDataDesc}</span>}
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-lable">Bridge Parameter</label>
                                <select className="form-control" value={formData.bridgeParameterId} onChange={(e) => setFormData({ ...formData, bridgeParameterId: e.target.value })}>
                                    <option value="0">--Please select--</option>

                                    {bridgeParameters.map((parameters, index) => (
                                        <option key={index} value={parameters.bridgeParameterId}>
                                            {parameters.bridgeParameterName}
                                        </option>
                                    ))}
                                </select>
                                {errors.bridgeParameterId && <span className="error">{errors.bridgeParameterId}</span>}
                            </div>
                            <div className="col-md-3 col-sm-12 mt-3">
                                <label className="form-lable">Parent Parameter</label>
                                <select className="form-control" value={formData.parentCommonDataId} onChange={(e) => setFormData({ ...formData, parentCommonDataId: e.target.value })}>
                                    <option value="0">--Please select--</option>
                                    {parentParameters.map((result, index) => (
                                        <option key={index} value={result.commonDataId}>{result.commonDataName}</option>
                                    ))}
                                </select>
                                {errors.parentCommonDataId && <span className="error">{errors.parentCommonDataId}</span>}
                            </div>
                            <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                    <button className="btn btn-primary" type="submit"><i class="fa fa-save mx-1"></i> Save</button>
                                </div>
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