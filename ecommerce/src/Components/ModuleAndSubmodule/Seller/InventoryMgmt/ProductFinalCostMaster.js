import { useEffect, useState } from "react";
import Loader from "../../../Structure/Loader";
import AsyncSelect from "react-select/async"
import api from "../../../utils/axiosSetup";
import CONSTANTS from "../../../utils/Constants";
import CommonDataTable from "../../../utils/DataTableUtil";
import tableCustomStyles from "../../../../css/TableCustomStyles";
import { data } from "react-router-dom";
import Select from 'react-select'
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";

export default function ProductFinalCostMaster() {

    const [loading, setLoading] = useState(false);
    const [productHiddenName, setProductHiddenName] = useState(null);
    const [suggestions, setSuggestions] = useState([]);
    const [producthiddenId, setProductHiddenId] = useState(null);
    const [productFinalCostHiddenId, setProductFinalCostHiddenId] = useState(null);
    const [errors, setErrors] = useState([]);
    const [dynamicDropdowns, setDynamicDropdowns] = useState([]);
    const [selectedValues, setSelectedValues] = useState({});
    const [cost, setCost] = useState(0.0);
    const [finalResult, setFinalResult] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [specificationDropdowns, setSpecificationDropdowns] = useState([]);
    const [specificationNames, setSpecificationNames] = useState('');
    const [perPage, setPerPage] = useState(10);
    const [paginationData, setPaginationData] = useState([]);

    const [data, setData] = useState([]);
    const [formData, setFormData] = useState({
        countryId: [],
        productName: '',
        cost: 0.0
    })
    const [countries, setCountries] = useState([]);
    // Handle select change
    const handleChange = (event, key) => {
        const { value } = event.target;
        console.log(value);
        // Update the selected value for the corresponding dropdown (key)
        setSelectedValues((prevValues) => ({
            ...prevValues,
            [key]: value, // key is the name of the dropdown (label)
        }));
        console.log(selectedValues);
        console.log(selectedValues[key]);
    };
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
        setSelectedValues({});
        fetchProductsSpecifications(selectedOption.value.productId);
    };

    const fetchProductsSpecifications = async (producthiddenId) => {
        await api.get(CONSTANTS.BASE_URL + "/api-data/product-specification/get-products-specification-and-values", {
            params: {
                productId: producthiddenId
            }
        }).then((result) => {
            console.log(result.data);
            setSpecificationDropdowns(result.data);
            const dynamicDrop = Object.entries(result.data).map(([key, value]) => (
                <div className="col-sm-3 mb-1 mt-1" key={key}>
                    <label className="form-label">{key}</label>
                    <select
                        className="form-control"
                        value={selectedValues[key]} // Binding the selected value
                        onChange={(event) => handleChange(event, key)} // Calling handleChange with the key
                    >
                        <option value="0" >Please Select</option> {/* Default option */}
                        {value.map((option, index) => (
                            <option key={index} value={option.productSpecificationValueMasterId}>
                                {option.value}
                            </option>
                        ))}
                    </select>
                </div>
            ));
            setDynamicDropdowns(dynamicDrop);
        })

    }
    const fetchCountries = async () => {
        const response = await api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
        var resultData = response.data;
        console.log("countries:" + resultData);
        setCountries(resultData.map((country, index) => ({
            label: country.countryName,
            value: country.countryName,
            id: country.countryId
        }
        )));
    }
    let finalIndex = 0;
    function addProductFinalCostMaster(e) {
        e.preventDefault();
        console.log(selectedValues);
        console.log(producthiddenId);
        console.log(formData.countryId);
        console.log(formData.cost);
        console.log(productHiddenName)
        var specifications = "";
        var specificationMap = new Map();
        specifications = Object.entries(selectedValues).map(([key, value]) => (
            specifications = value
        ))
        Object.entries(selectedValues).map(([key, value]) => (
            specificationMap.set(key, value)
        ))
        var obj = {};
        obj.specifications = specifications;
        obj.productSpecList = specifications;
        obj.productId = parseInt(producthiddenId);
        obj.countryId = parseInt(formData.countryId);
        obj.cost = parseFloat(formData.cost);
        obj.productFinalCostId = parseInt(productFinalCostHiddenId);
        obj.productName = productHiddenName;
        axios.post(CONSTANTS.BASE_URL + "/product-final-cost/check-combination", obj, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
                "Content-Type": "application/json"
            }
        }).then((result) => {
            if (result.data == 1) {
                toast.error('This combination already exists');
                //setProductHiddenId(null);
                setFormData({
                    // countryId: null,
                    //cost: 0
                })
                //setProductFinalCostHiddenId(null);
                //setProductHiddenName(null);

            } else {
                console.log(selectedValues);
                console.log(producthiddenId);
                console.log(formData.countryId);
                console.log(formData.cost);
                console.log(productHiddenName)
                var specifications = "";
                var specificationMap = new Map();
                specifications = Object.entries(selectedValues).map(([key, value]) => (
                    specifications = value
                ))
                Object.entries(selectedValues).map(([key, value]) => (
                    specificationMap.set(key, value)
                ))

                var obj = {};
                obj.specifications = specifications;
                obj.productSpecList = specifications;
                obj.productId = parseInt(producthiddenId);
                obj.countryId = parseInt(formData.countryId);
                obj.cost = parseFloat(formData.cost);
                obj.productFinalCostId = parseInt(productFinalCostHiddenId);
                obj.productName = productHiddenName;


                console.log(obj);
                var tempArray = finalResult;
                tempArray.push(obj);
                setFinalResult(tempArray);
                console.log(finalResult)
                var specificationName = "";
                Object.entries(specificationDropdowns).map(([key, value]) => {
                    // Iterate over the value array (which is an array of elements)
                    value.forEach(element => {
                        if (specifications.indexOf("" + element.productSpecificationValueMasterId) !== -1) {
                            specificationName += element.value + ",";
                        }
                    });
                });

                formData.countryId.forEach(selected => {
                    if (element.id === selected) {
                        obj.countryName = element.value;
                        console.log(specificationName);
                        obj.specificationName = specificationName;;
                        setData((prevData) => [...prevData, obj])
                    }
                })
            }
        })

    }

    const fetchProductFinalCostMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/product-final-cost/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setPaginationData(response.data.data);
        setTotalRows(response.data.totalPages);
        setLoading(false);
    };

    function saveProductFinalCostMaster(e) {
        e.preventDefault();

        // Ensure `data` is available and properly structured before sending
        axios.post(CONSTANTS.BASE_URL + "/product-final-cost/save", data, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
                "Content-Type": "application/json"
            }
        })
            .then((response) => {
                toast.success(response.data.message);
                fetchProductFinalCostMasterData(0);
            })
            .catch(error => {
                toast.error("Error occured while saving");
            });
    }



    useEffect(() => {
        fetchCountries();
        fetchProductFinalCostMasterData(0);
    }, [])


    const columns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Table

        },

        {
            name: 'Specifications',
            selector: row => row.specificationName,
            sortable: true,
            center: true,
        },
        {
            name: 'Country Name',
            selector: row => row.countryName,
            sortable: true,
            center: true,
        },
        {
            name: 'Cost',
            button: true,
            center: true,
            selector: row => row.cost,
        },
        {
            name: 'Edit',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" id={`${row.commonDataId}`} data-toggle="modal" onClick={() => editProductFinalCostForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.commonDataId}`} data-toggle="modal" onClick={() => deleteProductFinalCostForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    const productFinalColumns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Table

        },

        {
            name: 'Specifications',
            selector: row => row.productSpecList,
            sortable: true,
            center: true,
        },
        {
            name: 'Country Name',
            selector: row => row.countryName,
            sortable: true,
            center: true,
        },
        {
            name: 'Cost',
            button: true,
            center: true,
            selector: row => row.cost,
        },
        {
            name: 'Edit',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" id={`${row.commonDataId}`} data-toggle="modal" onClick={() => editProductFinalCostForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.commonDataId}`} data-toggle="modal" onClick={() => deleteProductFinalCostForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    function editProductFinalCostForm(row) {
        console.log(row);

    }
    function deleteProductFinalCostForm(row) {

    }

    // Handle the change event for react-select (multiple selection)
    const handleCountryChange = (selectedOptions) => {
        const selectedCountryIds = selectedOptions.map(option => option.id);
        setFormData({
            ...formData,
            countryId: selectedCountryIds
        });
    };
    const handlePerRowsChange = async (newPerPage, page) => {
        if (data.length == 0) {
            page = 0;
        }
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/product-final-cost/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setPaginationData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };
    const handlePageChange = async page => {
        if (!data.length) return;
        if (data.length == 0) {
            page = 0;
        }
        fetchProductFinalCostMasterData(page);
    };

    return (
        <>
            <div className="row">
                <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Seller</a></li>
                        <li class="breadcrumb-item mx-2"><a href="#">Inventory Management</a></li>
                        <li class="breadcrumb-item mx-2 " aria-current="page">Product Final Cost Master</li>
                    </ol>
                </nav>
                <div className="card card-body">
                    <form onSubmit={addProductFinalCostMaster}>
                        {loading && <Loader />}
                        <div className="row">
                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Product</label>
                                <div className="">
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
                            {dynamicDropdowns}
                            <div className="col-sm-3 mb-1 mt-1">
                                <label className="form-label">Country</label>
                                <div className="">
                                    {/* <select className="form-control" multiple value={formData.countryId} onChange={(e) => setFormData({ ...formData, countryId: e.target.value })}>
                                        <option value="0">--Please select--</option>
                                        {countries.map((country, index) => (
                                            <option key={index} value={country.countryId}>
                                                {country.countryName}
                                            </option>
                                        ))}
                                    </select> */}
                                    <Select
                                        isMulti  // This makes the select allow multiple selections
                                        options={countries}

                                        onChange={handleCountryChange}

                                        className="react-select-container"
                                        placeholder="Select Countries"
                                    />
                                </div>
                            </div>
                            <div className="col-sm-3 mt-1 mb-1">
                                <label className="form-label">Cost</label>
                                <div className="">
                                    <input className="form-control" placeholder="0.0" value={formData.cost} onChange={(e) => setFormData({ ...formData, cost: e.target.value })} />
                                </div>
                            </div>
                            <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                                <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                    <button className="btn btn-primary" type="submit"><i class="fa fa-plus mx-1"></i> Add</button>
                                </div>
                            </div>

                        </div>
                    </form>
                    <form onSubmit={saveProductFinalCostMaster}>
                        <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                            <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                <button className="btn btn-primary" type="submit"><i class="fa fa-save mx-1"></i> Save</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div className="card shadow p-4 mt-3">
                    <div className="table-responsive">

                        <CommonDataTable
                            columns={columns}
                            data={data}
                            progressPending={loading}


                            customStyles={tableCustomStyles}
                        />
                    </div>
                </div>
                <div className="card shadow p-4 mt-3">
                    <div className="table-responsive">

                        <CommonDataTable
                            columns={productFinalColumns}
                            data={paginationData}
                            progressPending={loading}
                            totalRows={totalRows}
                            pagination
                            paginationServer
                            paginationTotalRows={totalRows}
                            onChangeRowsPerPage={handlePerRowsChange}
                            onChangePage={handlePageChange}
                            customStyles={tableCustomStyles}
                        />
                    </div>
                </div>
                <ToastContainer />
            </div>
        </>
    )
}