import React, { useEffect, useState } from "react";
import CommonScreen from "../../../Structure/CommonScreen";
import CONSTANTS from "../../../utils/Constants";
import { toast, ToastContainer } from "react-toastify";
import { useAuth } from "../../../../features/AuthProvider ";
import CommonDataTable from "../../../utils/DataTableUtil";
import tableCustomStyles from "../../../../css/TableCustomStyles"
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import api from "../../../utils/axiosSetup";
import Loader from "../../../Structure/Loader";
import axios from "axios";
import { Form } from "react-router-dom";
const ReactSwal = withReactContent(Swal);
export default function ProductMaster() {

    const [countries, setCountries] = useState([]);
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(null);
    const { authToken } = useAuth();
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [totalRows, setTotalRows] = useState(0);
    const [perPage, setPerPage] = useState(10);
    const [brands, setBrands] = useState([]);
    const [productCategories, setProductCategories] = useState([]);
    // State to store the selected files
    const [files, setFiles] = useState([]);
    const [primary, setPrimary] = useState(null);

    // Handle file selection for multiple files
    const handleFilesChange = (e) => {
        if (e.target.files.length > 3) {
            toast.error('You cannot upload more than three images!');
            return;
        } else {
            setFiles([...e.target.files]); // Store the selected files in the files array
        }

    };

    // Handle primary image selection
    const handlePrimaryChange = (e) => {
        if (e.target.files.length > 1) {
            toast.error('You can upload only one primary image!');
            return;
        } else {
            setPrimary(e.target.files[0]); // Store the selected primary image file
        }

    };


    useEffect(() => {
        fetchCountries();
        fetchBrands();
        fetchProductCategories();
        fetchProductMasterData(0);
    }, [])

    const columns = [
        {
            name: 'Sr No',
            cell: (row, index) => index + 1,
            sortable: true,
            center: true, // For direct alignment in some libraries like React Data Table

        },
        {
            name: 'Product Name',
            selector: row => row.productName,
            sortable: true,
            center: true,
        },
        {
            name: 'Product cost',
            selector: row => row.cost,
            sortable: true,
            center: true,
        },
        {
            name: 'Minimum price',
            selector: row => row.minPrice,
            sortable: true,
            center: true,
        },
        {
            name: 'Maximum price',
            selector: row => row.maxPrice,
            sortable: true,
            center: true,
        },
        {
            name: 'Current discount',
            selector: row => row.currentDiscount,
            sortable: true,
            center: true,
        },
        {
            name: 'Country of origin',
            selector: row => row.countryName,
            sortable: true,
            center: true,
        },
        {
            name: 'Status',
            button: true,
            center: true,
            cell: row => (
                console.log(row),
                row.status === "Y" ? 'Active' : 'Inactive'
            ),
        },
        {
            name: 'Edit',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" id={`${row.productId}`} data-toggle="modal" onClick={() => editProductMasterForm(row)}><i className="ri-pencil-line"></i></button>

            ),
        },
        {
            name: 'Action',
            button: true,
            center: true,
            cell: row => (
                <button type="button" className="btn bg btn-xs" data-id={`${row.productId}`} data-toggle="modal" onClick={() => deleteProductMasterForm(row)}> <i class="ri-delete-bin-line"></i></button >

            ),
        },
    ];
    const fetchProductMasterData = async page => {
        setLoading(true);
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/product/pagination?page=${page}&per_page=${perPage}&delay=0`, {}, {
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
        const response = await api.post(`${CONSTANTS.BASE_URL}/api-data/product/pagination?page=${page}&per_page=${newPerPage}&delay=0`, {}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
            }
        });
        setData(response.data.data);
        setTotalRows(response.data.totalPages);
        setPerPage(newPerPage);
        setLoading(false);
    };
    function editProductMasterForm(row) {
        const productId = row.productId;
        api.get(`${CONSTANTS.BASE_URL}/api-data/product/get-product-byid/${productId}`,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("authToken")}`,
                },
            }
        ).then((result) => {
            console.log(result);
            result = result.data;
            setFormData({
                productId: result.data.productId,
                productName: result.data.productName,
                productDesc: result.data.productDesc,
                cost: result.data.cost,
                minPrice: result.data.minPrice,
                maxPrice: result.data.maxPrice,
                currentDiscount: result.data.currentDiscount,
                impInfo: result.data.impInfo,
                manufacturerName: result.data.manufacturerName,
                countryOfOrigin: result.data.countryOfOrigin,
                itemPartNumber: result.data.itemPartNumber,
                productDiamensions: result.data.productDiamensions,
                netQty: result.data.netQty,
                status: result.data.status,
                brandId: result.data.brandId,
                productCategoryId: result.data.productCategoryId
            });
        }).catch((e) => {
            console.log(e);
        });
    }
    const deleteProductMasterForm = (row) => {
        const productId = row.producId; // Access properties from the row
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
                api.delete(`${CONSTANTS.BASE_URL}/api-data/product/delete/${productId}`,
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
        if (!formData.productName) {
            newErrors.productName = 'Please enter product name';
        }
        if (!formData.productDesc) {
            newErrors.productDesc = 'Please enter product description';
        }
        if (!formData.cost) {
            newErrors.cost = 'Please enter product cost';
        }
        if (!formData.minPrice) {
            newErrors.minPrice = 'Please enter product minimum price';
        }
        if (!formData.maxPrice) {
            newErrors.maxPrice = 'Please enter product maximum price';
        }
        if (!formData.currentDiscount) {
            newErrors.currentDiscount = 'Please enter product current discount';
        }
        if (!formData.impInfo) {
            newErrors.impInfo = 'This field is required';
        }
        if (!formData.manufacturerName) {
            newErrors.manufacturerName = 'This field is required';
        }
        if (!formData.countryOfOrigin) {
            newErrors.countryOfOrigin = 'Please select';
        }
        // if (!formData.productDiamensions) {
        //     newErrors.productDiamensions = 'This field is required';
        // }
        if (!formData.itemPartNumber) {
            newErrors.itemPartNumber = 'This field is required';
        }
        if (!formData.netQty) {
            newErrors.netQty = 'This field is required';
        }
        if (!formData.brandId) {
            newErrors.brandId = 'Please select';
        }
        if (!formData.productCategoryId) {
            newErrors.productCategoryId = 'Please select';
        }
        return newErrors;
    };
    const [formData, setFormData] = React.useState({
        productId: null,
        productName: "",
        productDesc: "",
        cost: 0,
        minPrice: 0.0,
        maxPrice: 0.0,
        currentDiscount: 0.0,
        impInfo: "",
        manufacturerName: "",
        countryOfOrigin: 0,
        itemPartNumber: "",
        productDiamensions: "",
        netQty: 0,
        status: "Y",
        brandId: null,
        productCategoryId: null
    });

    function saveProductMaster(e) {
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
            var obj = new FormData();
            obj.append("productMasterBean", JSON.stringify({
                productId: formData.productId,
                productName: formData.productName,
                productDesc: formData.productDesc,
                cost: formData.cost,
                minPrice: formData.minPrice,
                maxPrice: formData.maxPrice,
                currentDiscount: formData.currentDiscount,
                impInfo: formData.impInfo,
                manufacturerName: formData.manufacturerName,
                countryOfOrigin: formData.countryOfOrigin,
                itemPartNumber: formData.itemPartNumber,
                productDiamentions: formData.productDiamensions,
                netQty: formData.netQty,
                status: formData.status,
                brandId: formData.brandId,
                productCategoryId: formData.productCategoryId

            }));

            // Append files (multiple images and primary image)
            files.forEach(file => {
                obj.append("files", file); // Adding the multiple images
            });

            obj.append("primary", primary); // Adding the primary image
            api.post(CONSTANTS.BASE_URL + "/api-data/product/save", obj, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    "Authorization": "Bearer " + localStorage.getItem("authToken"),
                }
            }).then((result) => {
                setLoading(false);
                console.log(result);
                var resultData = result.data.status;
                if (resultData == "success") {
                    toast.success(CONSTANTS.SAVE_MESSAGE);
                } else {
                    toast.error(CONSTANTS.FAIL_MESSAGE);
                }
                fetchProductMasterData(0);
            }).catch((e) => {
                setLoading(false);
                console.log("error" + e);
                toast.error(CONSTANTS.FAIL_MESSAGE);
            })
            resetForm();
        }
    }
    function resetForm() {
        setFormData({
            productId: null,
            productName: "",
            productDesc: "",
            cost: 0,
            minPrice: 0.0,
            maxPrice: 0.0,
            currentDiscount: 0.0,
            impInfo: "",
            manufacturerName: "",
            countryOfOrigin: 0,
            itemPartNumber: "",
            productDiamensions: "",
            netQty: 0,
            status: "Y",
            brandId: 0,
            productCategoryId: null
        })
    }
    const handlePageChange = async page => {
        if (!data.length) return;
        if (data.length == 0) {
            page = 0;
        }
        fetchProductMasterData(page);
    };
    const fetchCountries = async () => {
        const countries = await api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries");
        var resultData = countries.data;
        console.log("countries:" + resultData);
        setCountries(resultData);
    }
    const fetchBrands = async () => {
        const response = await api.get(CONSTANTS.BASE_URL + "/api-data/brand/fetch-all-Brands")
        var resultData = response.data;
        console.log("brands:" + resultData);
        setBrands(resultData);
    }

    const fetchProductCategories = async () => {
        await axios.get(CONSTANTS.BASE_URL + "/api-data/common-data/fetch-product-categories", {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('authToken'),
                "Content-Type": "application/json"
            }
        }).then((result) => {
            setProductCategories(result.data);
        })
    }

    return (
        <>
            <nav className="" aria-label="breadcrumb " style={{ marginLeft: '2rem' }}>
                <ol class="breadcrumb">
                    <li class="breadcrumb-item mx-2"><a href="#">Home</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">Seller</a></li>
                    <li class="breadcrumb-item mx-2"><a href="#">Inventory Management</a></li>
                    <li class="breadcrumb-item mx-2 " aria-current="page">Product Master</li>
                </ol>
            </nav>
            <div className="card card-body">
                {loading && <Loader />}
                <form onSubmit={saveProductMaster}>
                    <div className="row">
                        <div className="row">
                            <h4>Product Information</h4>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Product Name</label>
                            <div className="">
                                <input value={formData.productId} onChange={(e) => setFormData({ ...formData, productId: e.target.value })} type="hidden" />
                                <input type="text" value={formData.productName} onChange={(e) => setFormData({ ...formData, productName: e.target.value })} className="form-control" placeholder="Enter product name" />
                                {errors.productName && <span className="error">{errors.productName}</span>}
                            </div>

                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Brand Name</label>
                            <div className="">
                                <select className="form-control" value={formData.brandId} onChange={(e) => setFormData({ ...formData, brandId: e.target.value })}>
                                    <option value="0">--Please select--</option>
                                    {brands.map((result, index) => (
                                        <option key={index} value={result.brandId}>{result.brandName}</option>
                                    ))}
                                </select>
                                {errors.brandId && <span className="error">{errors.brandId}</span>}
                            </div>

                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Product Category</label>
                            <div className="">
                                <select className="form-control" value={formData.productCategoryId} onChange={(e) => setFormData({ ...formData, productCategoryId: e.target.value })}>
                                    <option value="0">--Please select--</option>
                                    {productCategories.map((result, index) => (
                                        <option key={index} value={result.commonDataId}>{result.commonDataDesc}</option>
                                    ))}
                                </select>
                                {errors.productCategoryId && <span className="error">{errors.productCategoryId}</span>}
                            </div>

                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Product description</label>
                            <div className="">
                                <input type="text" value={formData.productDesc} className="form-control" onChange={(e) => setFormData({ ...formData, productDesc: e.target.value })} placeholder="Enter product description" />
                                {errors.productDesc && <span className="error">{errors.productDesc}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Product cost</label>
                            <div className="">
                                <input type="text" value={formData.cost} className="form-control" onChange={(e) => setFormData({ ...formData, cost: e.target.value })} placeholder="Enter product cost" />
                                {errors.cost && <span className="error">{errors.cost}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Min price</label>
                            <div className="">
                                <input type="text" value={formData.minPrice} className="form-control" onChange={(e) => setFormData({ ...formData, minPrice: e.target.value })} placeholder="Enter product's min cost" />
                                {errors.minPrice && <span className="error">{errors.minPrice}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Max price</label>
                            <div className="">
                                <input type="text" value={formData.maxPrice} className="form-control" onChange={(e) => setFormData({ ...formData, maxPrice: e.target.value })} placeholder="Enter product's max cost" />
                                {errors.maxPrice && <span className="error">{errors.maxPrice}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Current discount</label>
                            <div className="">
                                <input type="text" value={formData.currentDiscount} className="form-control" onChange={(e) => setFormData({ ...formData, currentDiscount: e.target.value })} placeholder="Enter product's current discount" />
                                {errors.currentDiscount && <span className="error">{errors.currentDiscount}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Important Information</label>
                            <div className="">
                                <textarea value={formData.impInfo} className="form-control" onChange={(e) => setFormData({ ...formData, impInfo: e.target.value })} placeholder="Type something.."></textarea>
                                {errors.impInfo && <span className="error">{errors.impInfo}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">Product status</label>
                            <div className="">
                                <input type="checkbox" value={formData.status} onChange={(e) => setFormData({ ...formData, status: e.target.value })} className="form-check-input" />
                                {errors.status && <span className="error">{errors.status}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label>Upload Product Images (Multiple):</label>
                            <input
                                type="file"
                                multiple
                                accept="image/*"
                                max={3}
                                onChange={handleFilesChange}
                            />
                        </div>

                        <div className="col-md-3 col-sm-12">
                            <label>Upload Primary Image:</label>
                            <input
                                type="file"
                                onChange={handlePrimaryChange}
                            />
                        </div>

                    </div>
                    <div className="row">
                        <h4>Technical Information</h4>
                    </div>
                    <div className="row">
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">
                                Manufacturer Name
                            </label>
                            <div className="">
                                <input type="text" value={formData.manufacturerName} onChange={(e) => setFormData({ ...formData, manufacturerName: e.target.value })} className="form-control" placeholder="Enter manufacturer name" />
                                {errors.manufacturerName && <span className="error">{errors.manufacturerName}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">
                                Country of origin
                            </label>
                            <div className="">
                                <select id="countryOfOrigin" value={formData.countryOfOrigin} onChange={(e) => setFormData({ ...formData, countryOfOrigin: e.target.value })} className="form-control">
                                    <option value="0">--Please Select--</option>
                                    {countries.map((country, index) => (
                                        <option key={index} value={country.countryId}>
                                            {country.countryName}
                                        </option>
                                    ))}
                                </select>
                                {errors.countryOfOrigin && <span className="error">{errors.countryOfOrigin}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">
                                Item part number
                            </label>
                            <div className="">
                                <input type="text" value={formData.itemPartNumber} onChange={(e) => setFormData({ ...formData, itemPartNumber: e.target.value })} className="form-control" placeholder="Enter Item part number" />
                                {errors.itemPartNumber && <span className="error">{errors.itemPartNumber}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">
                                Product diamensions
                            </label>
                            <div className="">
                                <input type="text" value={formData.productDiamensions} className="form-control" onChange={(e) => setFormData({ ...formData, productDiamensions: e.target.value })} placeholder="Enter product diamensions" />
                                {errors.productDiamensions && <span className="error">{errors.productDiamensions}</span>}
                            </div>
                        </div>
                        <div className="col-md-3 col-sm-12">
                            <label className="form-label">
                                Net Quantity
                            </label>
                            <div className="">
                                <input type="text" value={formData.netQty} onChange={(e) => setFormData({ ...formData, netQty: e.target.value })} className="form-control" placeholder="Enter net quantity" />
                                {errors.netQty && <span className="error">{errors.netQty}</span>}
                            </div>
                        </div>
                        <div className="row mt-4" style={{ display: 'flex', flexDirection: 'row', justifyContent: 'right' }}>
                            <div className="col-md-3 col-sm-12" style={{ display: 'flex', justifyContent: 'right' }}>
                                <button className="btn btn-primary"><i class="fa fa-save mx-1"></i> Save</button>
                            </div>
                        </div>
                    </div>
                    <ToastContainer />
                </form>
            </div>
            <div className="card card-body mt-3">
                <div className="row">
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
        </>
    )
}