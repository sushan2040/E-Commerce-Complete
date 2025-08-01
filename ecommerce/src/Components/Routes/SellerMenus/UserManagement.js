import { Route } from 'react-router-dom';
import RoleMaster from '../../ModuleAndSubmodule/Admin/RoleMaster';
import AddUser from '../../ModuleAndSubmodule/Seller/UserMgmt/AddUser';

export default function UserMgmt() {
    return (
        <>
            <Route path="ecommerce/user-mgmt/role-master/add" element={<RoleMaster />} />
            <Route path="ecommerce/user-mgmt/add-user/add" element={<AddUser />} />

        </>
    )
}