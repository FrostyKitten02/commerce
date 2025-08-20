import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {ThemeProvider} from '@mui/material/styles';
import {CssBaseline} from '@mui/material';
import PageTemplate from "./template/PageTemplate";
import ProductsPage from "./pages/ProductsPage";
import CartPage from "./pages/CartPage";
import CheckoutPage from "./pages/CheckoutPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import AdminLoginPage from "./pages/AdminLoginPage";
import AdminProductsPage from "./pages/AdminProductsPage";
import AdminHealthPage from "./pages/AdminHealthPage";
import {ConfigUtil} from "./util/ConfigUtil";
import ProductPage from "./pages/ProductPage";
import {theme} from "./theme/theme";

function App() {
    ConfigUtil.readConfig();
    const router = createBrowserRouter([
            {
                path: "/",
                element: <PageTemplate />,
                children: [
                    {
                        path: "",
                        element: <ProductsPage />
                    },
                    {
                        path: "products",
                        children: [
                            {
                                path: "",
                                element: <ProductsPage />,
                            },
                            {
                                path: ":id",
                                element: <ProductPage />
                            }
                        ]
                    },
                    {
                        path: "cart",
                        element: <CartPage />
                    },
                    {
                        path: "checkout",
                        element: <CheckoutPage />
                    },
                    {
                        path: "log-in",
                        element: <LoginPage />
                    },
                    {
                        path: "register",
                        element: <RegisterPage />
                    }
                ]
            },
            {
                path: "/admin",
                element: <AdminLoginPage />
            },
            {
                path: "/admin/products",
                element: <AdminProductsPage />
            },
            {
                path: "/admin/health",
                element: <AdminHealthPage />
            }
        ])

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <RouterProvider router={router} />
        </ThemeProvider>
    )
}

export default App
