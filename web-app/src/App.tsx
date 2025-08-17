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
import LoginPage from "./pages/LoginPage";
import AdminLoginPage from "./pages/AdminLoginPage";
import AdminProductsPage from "./pages/AdminProductsPage";
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
                        path: "log-in",
                        element: <LoginPage />
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
