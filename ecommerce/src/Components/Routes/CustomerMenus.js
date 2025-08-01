import { Route } from 'react-router-dom';
import ProductView from "../ModuleAndSubmodule/Customer/ProductView";
import ProfilePage from "../ModuleAndSubmodule/Customer/ProfilePage";
import ProductList from "../ModuleAndSubmodule/Customer/ProductsList";
import Cart from "../ModuleAndSubmodule/Customer/Cart";
import CommonScreen from '../Structure/CommonScreen';

export default function CustomerMenus() {
    return (
        <>
            <Route path='/' element={<CommonScreen />}>
                <Route path="ecommerce/product/view" element={<ProductView />} />
                <Route path="ecommerce/profile" element={<ProfilePage />} />
                <Route path="ecommerce/allproducts" element={<ProductList />} />
                <Route path="ecommerce/cart/view" element={<Cart />} />
            </Route>
        </>
    )
}