# E-Commerce Web Application

A robust e-commerce platform built with React and JWT authentication, enabling businesses to manage inventory, sell products, and delegate tasks to employees with role-based access control, while customers can browse, search, and purchase products.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Folder Structure](#folder-structure)
- [Usage](#usage)
- [Authentication and Authorization](#authentication-and-authorization)
- [Role-Based Access Control](#role-based-access-control)
- [Administrative Features](#administrative-features)
- [License](#license)

## Features

- **Business Management**:
  - Sellers/businesses can register and manage their profiles.
  - Add, update, and manage product inventory, including rates and specifications.
  - Create employee logins with role-based access for tasks like inventory management, logistics, and quality control.
  - Handle customer refund requests and product approvals.

- **Customer Experience**:
  - Customers can register, log in, and browse products by category.
  - Add or remove products from a cart with dynamic quantity updates.
  - Search functionality to find products easily.

- **Administrative Control**:
  - Admins can manage dynamic web pages for businesses.
  - Create and manage admin menus and sub-menus.
  - Oversee platform-wide operations, including product and bill approvals.

- **Employee Roles**:
  - Businesses can assign roles (e.g., Clerk, QA Engineer) with specific access to relevant pages (e.g., inventory management, quality control).

## Technologies Used

- **Frontend**: React, Css,JavaScript.
- **Backend**: Java
- **Framework**: Spring boot
- **Authentication**: JWT (JSON Web Tokens) for secure authentication and authorization
- **Database**: PostgreSQL.
- **Other Tools**: Axios

## Folder Structure

The project follows a clean and modular folder structure for maintainability:

```
e-commerce-webapp/
├── public/                   # Static assets
├── src/                      # Source code
│   ├── assets/               # Images and other assets
│   ├── Components/           # Reusable React components
│   ├── css/                # Page components (e.g., Home, Product, Cart)
│   ├── features/              # React context for state management
│   ├── locales/                # Custom React hooks
│   └── App.js               # Main App component
├── README.md                 # Project documentation
└── package.json              # Project dependencies and scripts
```

## Usage

- **For Customers**:
  - Register or log in to browse products by category.
  - Use the search bar to find specific products.
  - Add products to the cart, adjust quantities, or remove items.
  
- **For Businesses**:
  - Register your business and set up product inventory.
  - Add static data like product specifications.
  - Create employee accounts and assign roles (e.g., Clerk for inventory, QA Engineer for quality control).
  - Manage refund requests and product approvals.

- **For Admins**:
  - Log in to the admin panel to manage dynamic pages, menus, and sub-menus.
  - Oversee platform operations, including product and bill approvals.

## Authentication and Authorization

The application uses **JWT (JSON Web Tokens)** for secure authentication and authorization:
- Users (customers, businesses, employees, admins) authenticate via email and password.
- Tokens are issued upon successful login and validated for protected routes.
- Best security practices are followed, including:
  - Secure storage of sensitive data.
  - Input validation and sanitization.
  - Role-based access control to restrict unauthorized access.

## Role-Based Access Control

- **Business Owners**: Full access to manage their business, inventory, and employee roles.
- **Employees**:
  - **Clerk**: Access to add/edit products in the inventory.
  - **QA Engineer**: Access to quality control pages and approval workflows.
  - **Logistics**: Manage warehouse counts and logistics.
- **Admins**: Full platform access, including menu creation and page management.
- **Customers**: Access to browse, search, and purchase products.

## Administrative Features

Admins have access to a comprehensive dashboard to:
- Create and manage dynamic web pages for businesses.
- Define admin menus and sub-menus for navigation.
- Approve or reject products and bills.
- Monitor platform activity and manage user roles.

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
