import {useState} from 'react';
import {Avatar, Box, Button, Container, TextField, Typography, Alert} from '@mui/material';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import RequestUtil from "../util/RequestUtil";
import StorageUtil from "../util/StorageUtil";
import {useNavigate} from "react-router-dom";

export default function AdminLoginPage() {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e: any) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
        setError('');
    };

    const validate = () => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!formData.email) {
            setError('Email je obvezen');
            return false;
        } else if (!emailRegex.test(formData.email)) {
            setError('Neveljaven email');
            return false;
        }

        if (!formData.password) {
            setError('Geslo je obvezno');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        setError('');

        try {
            const response = await RequestUtil.createAuthApi().login({
                username: formData.email,
                password: formData.password
            });

            const token = response.data.token;
            if (!token) {
                setError('Neuspešna prijava');
                return;
            }

            StorageUtil.setSessionToken(token);
            StorageUtil.setAdminSession(true);
            
            setFormData({
                email: "",
                password: ""
            });

            navigate('/admin/products');
        } catch (error: any) {
            console.error('Login error:', error);
            setError('Napačen email ali geslo');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container component="main" maxWidth="xs">
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <Avatar sx={{ m: 1, bgcolor: 'error.main' }}>
                    <AdminPanelSettingsIcon />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Admin Prijava
                </Typography>
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                    {error && (
                        <Alert severity="error" sx={{ mt: 2, mb: 2 }}>
                            {error}
                        </Alert>
                    )}
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="email"
                        label="Admin Email"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        value={formData.email}
                        onChange={handleChange}
                        disabled={loading}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Admin Geslo"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={formData.password}
                        onChange={handleChange}
                        disabled={loading}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="error"
                        sx={{ mt: 3, mb: 2 }}
                        disabled={loading}
                    >
                        {loading ? 'Prijavljam...' : 'Admin Prijava'}
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};