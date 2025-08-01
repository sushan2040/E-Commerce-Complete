import { Route } from "react-router-dom";
import DepartmentMaster from "../../ModuleAndSubmodule/Seller/AdminMgmt/DepartmentMaster";
import CommonScreen from "../../Structure/CommonScreen";

export default function BusinessAdminMgmt() {
    return (
        <>
            <Route path='/' element={<CommonScreen />}>
                <Route path='ecommerce/admin/department-master/add' element={<DepartmentMaster />} />
            </Route>
        </>
    )
}