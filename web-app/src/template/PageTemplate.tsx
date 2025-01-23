import {AppBar, Box, Container, Link, Toolbar, Typography} from "@mui/material";
import Button from '@mui/material/Button';
import AdbIcon from '@mui/icons-material/Adb';
import {Outlet} from "react-router-dom";


const pages = [
    {
        name: "Izdelki",
        path: "/products"
    },
    {
        name: "košarica",
        path: "/cart"
    },
    {
        name: "Prijava",
        path: "/log-in"
    }
]

export default function PageTemplate() {
    return (
        <>
            <AppBar position="static">
                <Container maxWidth="xl">
                    <Toolbar disableGutters>
                        <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                            {pages.map((page) => (
                                <Link href={page.path}>
                                    <Button
                                        key={page.path}
                                        sx={{ my: 2, color: 'white', display: 'block' }}
                                    >
                                        {page.name}
                                    </Button>
                                </Link>
                            ))}
                        </Box>
                    </Toolbar>
                </Container>
            </AppBar>
            <Outlet />
        </>
    );
}