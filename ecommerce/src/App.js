import React from "react";
import { BrowserRouter as Router, Routes, Route, RouterProvider } from 'react-router-dom';
import "./App.css";
import LoginPage from "./Components/ModuleAndSubmodule/Customer/Login";
import HomePage from "./Components/ModuleAndSubmodule/Customer/home";
import SellerLoginPage from "./Components/ModuleAndSubmodule/Seller/SellerLogin";
import CustomerRegistration from "./Components/ModuleAndSubmodule/Customer/CustomerRegistration";
import SellerRegistration from "./Components/ModuleAndSubmodule/Seller/SellerRegistration";
import AdminLoginPage from "./Components/ModuleAndSubmodule/Admin/AdminLoginPage";
import SellerMenus from "./Components/Routes/SellerMenus/SellerMenus";
import AdminMenus from "./Components/Routes/AdminMenus";
import CustomerMenus from "./Components/Routes/CustomerMenus";
import CommonScreen from "./Components/Structure/CommonScreen";
import PersistentDrawerLeft from "./Components/ModuleAndSubmodule/NewSidebar";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/react/index" element={<LoginPage />} />
          <Route path="/" element={<LoginPage />} />
          <Route path="/new-sidebar" element={<PersistentDrawerLeft />} />
          <Route path="/" element={<CommonScreen />}>
            <Route path="/ecommerce/home" element={<HomePage />} />
          </Route>
          <Route path="/ecommerce/logout" element={<LoginPage />} />
          <Route path="/ecommerce/seller/login" element={<SellerLoginPage />} />
          <Route path="/ecommerce/signup" element={<CustomerRegistration />} />
          <Route path="/ecommerce/seller/registration" element={<SellerRegistration />} />
          <Route path="/ecommerce/admin/login" element={<AdminLoginPage />} />
          {AdminMenus()}
          {SellerMenus()}
          {CustomerMenus()}
          {AdminMenus}
        </Routes>
      </Router>
    </>
  );
}

export default App;