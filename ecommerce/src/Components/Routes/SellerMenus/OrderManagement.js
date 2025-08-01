import { Route } from 'react-router-dom';
import InvoiceMgmt from "../../ModuleAndSubmodule/Seller/InventoryMgmt/InvoiceMgmt";
import ProductOrders from "../../ModuleAndSubmodule/Seller/InventoryMgmt/ProductOrders";
import OrderMgMyDashboard from '../../ModuleAndSubmodule/Seller/OrderMgmt/OrderMgmtDashboard';
import ReceivedOrderRequests from '../../ModuleAndSubmodule/Seller/OrderMgmt/ReceivedOrderRequests';
import OrderDetailsPage from '../../ModuleAndSubmodule/Seller/OrderMgmt/OrderDetailsPage';
import CheckAvailibilityOrder from '../../ModuleAndSubmodule/Seller/OrderMgmt/CheckAvailabilityOrder';
import SendNotificationsOrder from '../../ModuleAndSubmodule/Seller/OrderMgmt/SendNotificationsOrder';
import GenerateInvoice from '../../ModuleAndSubmodule/Seller/OrderMgmt/GenerateInvoice';

export default function OrderManagement() {
    return (
        <>
            <Route path='ecommerce/order-mgmt/dashboard' element={<OrderMgMyDashboard />} />
            <Route path='ecommerce/order-mgmt/received-order-requests' element={<ReceivedOrderRequests />} />
            <Route path='ecommerce/order-mgmt/order-details-page' element={<OrderDetailsPage />} />
            <Route path='ecommerce/order-mgmt/check-availabiliy-products' element={<CheckAvailibilityOrder />} />
            <Route path='ecommerce/order-mgmt/send-notification' element={<SendNotificationsOrder />} />
            <Route path='ecommerce/order-mgmt/generate-invoice' element={<GenerateInvoice />} />
        </>
    )
}