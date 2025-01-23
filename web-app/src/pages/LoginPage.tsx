import {useState} from 'react';
import {Avatar, Box, Button, Container, Grid2, Link, TextField, Typography,} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import RequestUtil from "../util/RequestUtil";
import StorageUtil from "../util/StorageUtil";
import {redirect} from "react-router-dom";

export default function LoginPage() {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });

    // Handle input changes
    const handleChange = (e: any) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === 'checkbox' ? checked : value,
        });
    };

    const validate = () => {
        let err: boolean = false;

        // Simple email regex for validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!formData.email) {
            err = true;
        } else if (!emailRegex.test(formData.email)) {
            err = true;
        }

        if (!formData.password) {
            err = true;
        }


        return !err;
    };

    // Handle form submission
    const handleSubmit = (e: any) => {
        e.preventDefault();
        if (validate()) {
            console.log('Form Data:', formData);
            RequestUtil.createAuthApi().login({
                username: formData.email,
                password: formData.password
            }).then((res) => {
                const auth: string | undefined = res.data.token;
                if (auth == undefined) {
                    console.log("Err login")
                    return;
                }

                StorageUtil.setSessionToken(auth);
                setFormData({
                    email: "",
                    password: ""
                })
            });
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
                <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Prijava
                </Typography>
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="email"
                        label="Email"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        value={formData.email}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Geslo"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={formData.password}
                        onChange={handleChange}
                    />
                    {/*<FormControlLabel*/}
                    {/*    control={*/}
                    {/*        <Checkbox*/}
                    {/*            value="remember"*/}
                    {/*            color="primary"*/}
                    {/*            name="remember"*/}
                    {/*            checked={formData.remember}*/}
                    {/*            onChange={handleChange}*/}
                    {/*        />*/}
                    {/*    }*/}
                    {/*    label="Remember me"*/}
                    {/*/>*/}
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2 }}
                    >
                        Prijava
                    </Button>
                    <Grid2 container>
                        {/*<Grid2 item xs>*/}
                        {/*    <Link href="#" variant="body2">*/}
                        {/*        Forgot password?*/}
                        {/*    </Link>*/}
                        {/*</Grid2>*/}
                        <Grid2 item>
                            <Link href="register" variant="body2">
                                {"Registracija"}
                            </Link>
                        </Grid2>
                    </Grid2>
                </Box>
            </Box>
        </Container>
    );
};