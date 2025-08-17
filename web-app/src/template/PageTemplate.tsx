import {AppBar, Box, Container, Link, Toolbar, Typography} from "@mui/material";
import Button from '@mui/material/Button';
import AdbIcon from '@mui/icons-material/Adb';
import {Logout} from '@mui/icons-material';
import {Outlet} from "react-router-dom";
import {useEffect, useState} from "react";
import StorageUtil from "../util/StorageUtil";


const pages = [
    {
        name: "Products",
        path: "/products"
    },
    {
        name: "Cart",
        path: "/cart"
    }
]

export default function PageTemplate() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // Check authentication status on component mount
        setIsLoggedIn(StorageUtil.isLoggedIn());

        // Listen for storage changes (in case user logs in/out in another tab)
        const handleStorageChange = () => {
            setIsLoggedIn(StorageUtil.isLoggedIn());
        };

        window.addEventListener('storage', handleStorageChange);
        
        // Also listen for custom events when login/logout happens in same tab
        window.addEventListener('auth-changed', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
            window.removeEventListener('auth-changed', handleStorageChange);
        };
    }, []);

    const handleLogout = () => {
        StorageUtil.logout();
        setIsLoggedIn(false);
        // Trigger custom event for other components
        window.dispatchEvent(new Event('auth-changed'));
    };

    return (
        <>
            <AppBar position="static">
                <Container maxWidth="xl">
                    <Toolbar disableGutters>
                        <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                            {pages.map((page) => (
                                <Link href={page.path} key={page.path}>
                                    <Button
                                        sx={{ my: 2, color: 'white', display: 'block' }}
                                    >
                                        {page.name}
                                    </Button>
                                </Link>
                            ))}
                        </Box>
                        
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                            {isLoggedIn ? (
                                <Button
                                    color="inherit"
                                    onClick={handleLogout}
                                    startIcon={<Logout />}
                                    sx={{ color: 'white' }}
                                >
                                    Logout
                                </Button>
                            ) : (
                                <Link href="/log-in">
                                    <Button
                                        sx={{ my: 2, color: 'white', display: 'block' }}
                                    >
                                        Login
                                    </Button>
                                </Link>
                            )}
                        </Box>
                    </Toolbar>
                </Container>
            </AppBar>
            <Outlet />
        </>
    );
}