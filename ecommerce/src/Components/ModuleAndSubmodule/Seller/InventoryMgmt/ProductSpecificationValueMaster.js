import { useEffect, useState } from "react";
import Loader from "../../../Structure/Loader";
import Select from 'react-select';
import { WithContext as ReactTags } from 'react-tag-input';
import { Padding } from "@mui/icons-material";
import { Badge, Button } from "react-bootstrap";
import "../../../../css/tagsinput.css";
import DataTable from "react-data-table-component";
import tableCustomStyles from "../../../../css/TableCustomStyles";
import CONSTANTS from "../../../utils/Constants";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import api from "../../../utils/axiosSetup";
import AsyncSelect from "react-select/async"
import Creatable from "react-select/creatable"
import CommonDataTable from "../../../utils/DataTableUtil";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
const ReactSwal = withReactContent(Swal);

export default function ProductSpecificationValueMaster() {

    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        productId: null,
        specificationId: null,
        values: []
    });
    const [products, setProducts] = useState([]);
    const [specifications, setSpecifications] = useState([]);
    const [specificationValue, setSpecificationValue] = useState([]);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [bridgeParameters, setBridgeParameters] = useState([]);
    const [suggestions, setSuggestions] = useState([]);
    const [specificationSuggestions, setSpecificationSuggestions] = useState([]);
    const [productHiddenName, setProductHiddenName] = useState(null);
    const [producthiddenId, setProductHiddenId] = useState(null);
    const [specificationHiddenName, setSpecificationHiddenName] = useState(null);
    const [specificationHiddenId, setSpecificationHiddenId] = useState(null);
    const [productSpecificationValueMasterId, setProductSpecificationValueMasterId] = useState(null);
    const [data, setData] = useState([]);

    function resetForm() {
        setFormData({
            productId: 0,
            specificationId: 0,
            values: []
        })
        setProductHiddenId(null);
        setProductHiddenName(null);
        setSpecificationHiddenId(null);
        setSpecificationHiddenName(null);
        setProductSpecificationValueMasterId(null);
    }

    // Handle selection change
    const handleChange = (selected) => {
        setFormData({ ...formData, values: selected })
    };

    const validate = () => {
        const newErrors = {};
        if (!producthiddenId) {
            newErrors.productId = 'Please select';
        }
        if (!specificationHiddenId) {
            newErrors.specificationId = 'Please select';
        }
        if (formData.values.length == 0) {
            newErrors.values = 'Please enter values';
        }

        return newErrors;
    };

    const fetchProducts = () => {
        axios.get(CONSTANTS.BASE_URL + "/api-data/product/fetch-all-products", {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken')
            }
        })
            .then((result) => {
                console.log(result);
                setProducts(result.data);
            }).catch((e) => {
                console.log(e);
            })
    }
    const fetchCommonData = () => {
        api.get(CONSTANTS.BASE_URL + "/api-data/common-data/get-common-datas", {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken')
            }

        })
            .then((result) => {
                console.log(result);
                setSpecifications(result.data);
                const tempArray = result.data.map((specification) => ({
                    value: specification.commonDataId, // or specification.commonDataName depending on your requirement
                    label: specification.commonDataName, // display name for the select option
                }));

                setSpecificationValue(tempArray);

            }).catch((e) => {
                console.log(e);
            })
    }

    const columns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Tabl
        },
        {
            name: 'Product Name',
            selector: row => row.productName,
            sortable: true,
            center: true,
        },
        {
            name: 'Specification name',
            selector: row => row.specificationName,
            button: true,
            center: true,
        },
        {
            name: 'Value',
            selector: row => row.value,
            button: true,
            center: true,
        },
        {
            name: 'Edit',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" id={`${row.productSpecificationValueMasterId}`} data-toggle="modal" onClick={() => editProductSpecificationMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.brandId}`} data-toggle="modal" onClick={() => deleteProductSpecificationMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    const editProductSpecificationMasterForm = async (row) => {
        const productSpecificationValueMasterId = row.productSpecificationValueMasterId;
        await api.get(`${CONSTANTS.BASE_URL}/api-data/product-specification/get-by-id/${productSpecificationValueMasterId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            var resultData = result.data.data;
            setProductHiddenName(resultData.productName);
            setProductSpecificationValueMasterId(resultData.productSpecificationValueMasterId);
            setProductHiddenId(resultData.productId);
            setSpecificationHiddenId(resultData.specificationId);
            setFormData({ ...formData, productId: resultData.productId });
            setFormData({ ...formData, specificationId: resultData.specificationId })
            var tempValues = [];
            tempValues.push(resultData.value);
            setFormData({
                ...formData, values: tempValues.map((obj) => ({
                    label: obj,
                    value: obj
                }))
            })

            setSpecificationHiddenName(resultData.specificationName);
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteProductSpecificationMasterForm = (row) => {
        const productSpecificationValueMasterId = row.productSpecificationValueMasterId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/product-specification/delete/${productSpecificationValueMasterId}`,
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
                fetchProductSpecificationValueMasterData(0);
            } else if (result.dismiss === Swal.DismissReason.cancel) {
                Swal.fire('Cancelled', 'Your item is safe :)', 'error');
            }
        });
    };
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

    function saveProductSpecificationValueMaster(e) {
        console.log("productId" + producthiddenId);
        console.log("specificationId" + specificationHiddenId);
        setLoading(true);
        e.preventDefault();
        setServerError(null); // Clear previous server errors
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            setLoading(false);
        } else {
            setErrors({});
            // Prepare the product master data
            var obj = {};
            console.log(formData);
            obj.productSpecificationValueMasterId = productSpecificationValueMasterId;
            obj.productId = producthiddenId;
            obj.specificationId = specificationHiddenId;
            var tempValues = [];
            tempValues = formData.values.map((obj) => obj.value);
            obj.values = tempValues;
            console.log(obj);
            api.post(CONSTANTS.BASE_URL + "/api-data/product-specification/save", JSON.stringify(obj), {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem('authToken')
                }
            }).then((result) => {
                setLoading(false);
                if (result.data.status == "success") {
                    toast.success(result.data.message);
                    resetForm();
                    fetchProductSpecificationValueMasterData(0);
                }
            }).then((e) => {
                setLoading(false);
                console.log(e);
            })
        }

    }

    useEffect(() => {
        fetchProducts();
        fetchCommonData();
        fetchBridgeParameters();
        fetchProductSpecificationValueMasterData(0);
    }, [])


    // Function to fetch suggestions from the backend
    const fetchSuggestions = async (value) => {
        if (value.length === 0) {
            setSuggestions([]);
            return;
        }
        setLoading(true);
        try {
            const response = await api.get(CONSTANTS.BASE_URL + '/api-data/product/suggestions', {
                params: { query: value },
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem('authToken')
                }
            });
            console.log(response.data);
            setSuggestions(response.data);
            return response.data.map((obj) => ({
                label: obj.productName,
                value: obj
            }));
        } catch (error) {
            console.error('Error fetching suggestions:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (event, { newValue }) => {
        setFormData({ ...formData, productName: newValue })
    };

    // Handling the selection of a suggestion
    const handleSuggestionSelected = (selectedOption) => {
        console.log(selectedOption);
        setProductHiddenName(selectedOption.value.productName);
        setProductHiddenId(selectedOption.value.productId)
        setFormData({ ...formData, productId: selectedOption.value.productId });
        setFormData({ ...formData, productName: selectedOption.value.productName });

    };

    // Function to fetch suggestions from the backend
    const fetchSpecificationSuggestions = async (value) => {
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
            setSpecificationSuggestions(response.data);
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

    const handleInputChangeSpecification = (event, { newValue }) => {
        setFormData({ ...formData, commonDataName: newValue })
    };

    // Handling the selection of a suggestion
    const handleSpecificationSuggestionSelected = (selectedOption) => {
        setFormData({ ...formData, specificationId: selectedOption.value.commonDataId });
        setSpecificationHiddenName(selectedOption.value.commonDataName);
        setSpecificationHiddenId(selectedOption.value.commonDataId)
    };

    const fetchProductSpecificationValueMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/product-specification/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };
    const handlePerRowsChange = async (newPerPage, page) => {
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/product-specification/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };
    const handlePageChange = page => {
        if (!data.length) return;
        if (data.length == 0) {
            page = 0;
        }
        fetchProductSpecificationValueMasterData(page);
    };


    return (
        <>
            <div className="row">
                <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Seller</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Inventory Management</a></li>
                        <li class="breadcrumb-item mx-2 " aria-current="page">Product Specification Value Master</li>
                    </ol>
                </nav>
                <div className="card card-body">
                    <form onSubmit={saveProductSpecificationValueMaster}>
                        {loading && <Loader />}
                        <div className="row">
                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Product</label>
                                <div className="">
                                    {/* <select className="form-control" value={formData.productId} onChange={(e) => setFormData({ ...formData, productId: e.target.value })}>
                                        <option value="0">--Please select--</option>
                                        {products.map((result, index) => (
                                            <option key={index} value={result.productId}>{result.productName}</option>
                                        ))}
                                    </select> */}
                                    <AsyncSelect

                                        isClearable={false}
                                        loadOptions={fetchSuggestions} // Fetches the suggestions asynchronously
                                        onChange={handleSuggestionSelected}
                                        onInputChange={handleInputChange}
                                        value={{ label: productHiddenName, value: productHiddenName }}
                                        placeholder="Search for a product"
                                        className="form-control"
                                        defaultOptions // Optionally, show some default suggestions
                                    />
                                    {errors.productId && <span className="error">{errors.productId}</span>}
                                </div>
                            </div>
                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Specification</label>
                                <div className="">
                                    {/* <select className="form-control" value={formData.specificationId} onChange={(e) => setFormData({ ...formData, specificationId: e.target.value })}>
                                        <option value="0">--Please select--</option>
                                        {specifications.map((result, index) => (
                                            <option key={index} value={result.commonDataId}>{result.commonDataName}</option>
                                        ))}
                                    </select> */}
                                    <AsyncSelect

                                        isClearable={false}
                                        loadOptions={fetchSpecificationSuggestions} // Fetches the suggestions asynchronously
                                        onChange={handleSpecificationSuggestionSelected}
                                        onInputChange={handleInputChangeSpecification}
                                        value={{ label: specificationHiddenName, value: specificationHiddenName }}
                                        placeholder="Search for a product"
                                        className="form-control"
                                        defaultOptions // Optionally, show some default suggestions
                                    />
                                    {errors.specificationId && <span className="error">{errors.specificationId}</span>}
                                </div>
                            </div>
                            {/* <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Bridge Parameter</label>
                                <div className="">
                                    <select className="form-control" value={formData.bridgeParameterId} onChange={(e) => setFormData({ ...formData, bridgeParameterId: e.target.value })}>
                                        <option value="0">--Please select--</option>
                                        {bridgeParameters.map((result, index) => (
                                            <option key={index} value={result.bridgeParameterId}>{result.bridgeParameterName}</option>
                                        ))}
                                    </select>
                                </div>
                            </div> */}
                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Values</label>
                                <div className="">
                                    {/* react-select component for multi-select */}
                                    <Creatable
                                        isMulti
                                        name="tags"
                                        styles={customStyles}
                                        value={formData.values}
                                        onChange={handleChange}
                                        className="react-select-container"
                                        classNamePrefix="react-select"
                                        placeholder="Select tags"
                                    />
                                    {errors.values && <span className="error">{errors.values}</span>}
                                </div>
                            </div>
                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label"></label>
                                <div className="">
                                    <button type="submit" className="btn btn-primary"><i className="fa fa-save"></i>Save</button>
                                </div>
                            </div>

                        </div>
                    </form>
                    <div className="row mt-3">
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
            </div >
        </>
    )
}
const customStyles = {
    control: (provided) => ({
        ...provided,
        borderColor: '#ccc',
        borderRadius: '8px',
        padding: '5px',
    }),
    multiValue: (provided) => ({
        ...provided,
        backgroundColor: '#007bff',
        color: 'white',
        borderRadius: '16px',
        padding: '5px 10px',
    }),
    multiValueLabel: (provided) => ({
        ...provided,
        color: 'white',
    }),
    multiValueRemove: (provided) => ({
        ...provided,
        color: 'white',
        ':hover': {
            backgroundColor: 'red',
        },
    }),
};