import { Route } from 'react-router-dom';
import LogisticsDashboard from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/LogisticsDashboard';
import CheckAvailibilityLogistics from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/CheckAvailibilityLogistics';
import OngoingShipments from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/OnGoingShipments';
import ProcessedShipments from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/ProcessedShipments';
import QAInspection from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/QAInspection';
import FeedbackComplaint from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/FeedbackComplaint';
import ChangeOrderStatus from '../../ModuleAndSubmodule/Seller/LogisticsMgmt/ChangeOrderStatus';
import StockProductMaster from '../../ModuleAndSubmodule/Seller/InventoryMgmt/StockProductMaster';
export default function LogisticsMgmt() {
    return (
        <>
            <Route path='ecommerce/logistics/dashboard' element={<LogisticsDashboard />} />
            <Route path='ecommerce/logistics/check-availibility-logistics' element={<CheckAvailibilityLogistics />} />
            <Route path='ecommerce/logistics/ongoing-shipments' element={<OngoingShipments />} />
            <Route path='ecommerce/logistics/processed-shipments' element={<ProcessedShipments />} />
            <Route path='ecommerce/logistics/qa-inspection' element={<QAInspection />} />
            <Route path='ecommerce/logistics/feedback-complaints' element={<FeedbackComplaint />} />
            <Route path='ecommerce/logistics/change-order-status' element={<ChangeOrderStatus />} />
            <Route path='ecommerce/logistics/stock-product-master' element={<StockProductMaster />} />
        </>
    )
}