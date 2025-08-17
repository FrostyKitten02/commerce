import {useState, useEffect} from 'react';
import {
    Alert,
    Box,
    Button,
    Container,
    Paper,
    Snackbar,
    TextField,
    Typography,
    CircularProgress,
    Grid,
} from '@mui/material';
import {ShoppingCart, CheckCircle} from '@mui/icons-material';
import {CartDto} from "../../client/cart";
import {SubmitCartReq} from "../../client/checkout";
import RequestUtil from "../util/RequestUtil";
import {useNavigate} from 'react-router-dom';

export default function CheckoutPage() {
    const navigate = useNavigate();
    const [cart, setCart] = useState<CartDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [orderSuccess, setOrderSuccess] = useState(false);
    const [snackbar, setSnackbar] = useState<{
        open: boolean;
        message: string;
        severity: 'success' | 'error';
    }>({
        open: false,
        message: '',
        severity: 'success'
    });

    const [formData, setFormData] = useState<SubmitCartReq>({
        cartId: '',
        shippingAddress: '',
        shippingCity: '',
        shippingPostalCode: '',
        shippingCountry: ''
    });

    const [errors, setErrors] = useState<{[key: string]: string}>({});

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        try {
            setLoading(true);
            const response = await RequestUtil.createCartApi()
                .getCart(RequestUtil.createBaseAxiosRequestConfig());
            const cartData = response.data.cart;
            
            if (!cartData || !cartData.cartProducts || cartData.cartProducts.length === 0) {
                showSnackbar('Your cart is empty', 'error');
                navigate('/cart');
                return;
            }
            
            setCart(cartData);
            setFormData(prev => ({ ...prev, cartId: cartData.id || '' }));
        } catch (error) {
            console.error('Error fetching cart:', error);
            showSnackbar('Error loading cart', 'error');
            navigate('/cart');
        } finally {
            setLoading(false);
        }
    };

    const validateForm = (): boolean => {
        const newErrors: {[key: string]: string} = {};

        if (!formData.shippingAddress.trim()) {
            newErrors.shippingAddress = 'Shipping address is required';
        }
        if (!formData.shippingCity.trim()) {
            newErrors.shippingCity = 'City is required';
        }
        if (!formData.shippingPostalCode.trim()) {
            newErrors.shippingPostalCode = 'Postal code is required';
        }
        if (!formData.shippingCountry.trim()) {
            newErrors.shippingCountry = 'Country is required';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        try {
            setSubmitting(true);
            await RequestUtil.createCheckoutApi()
                .submitCart(formData, RequestUtil.createBaseAxiosRequestConfig());
            
            setOrderSuccess(true);
            showSnackbar('Order placed successfully!', 'success');
            
            // Redirect to products page after a delay
            setTimeout(() => {
                navigate('/products');
            }, 3000);
        } catch (error) {
            console.error('Error submitting order:', error);
            showSnackbar('Error placing order. Please try again.', 'error');
        } finally {
            setSubmitting(false);
        }
    };

    const handleInputChange = (field: keyof SubmitCartReq) => (
        event: React.ChangeEvent<HTMLInputElement>
    ) => {
        setFormData(prev => ({
            ...prev,
            [field]: event.target.value
        }));
        
        // Clear error when user starts typing
        if (errors[field]) {
            setErrors(prev => ({
                ...prev,
                [field]: ''
            }));
        }
    };

    const showSnackbar = (message: string, severity: 'success' | 'error') => {
        setSnackbar({ open: true, message, severity });
    };

    const handleCloseSnackbar = () => {
        setSnackbar(prev => ({ ...prev, open: false }));
    };

    if (loading) {
        return (
            <Container maxWidth="md" sx={{ py: 8, textAlign: 'center' }}>
                <CircularProgress />
                <Typography variant="h6" sx={{ mt: 2 }}>
                    Loading checkout...
                </Typography>
            </Container>
        );
    }

    if (orderSuccess) {
        return (
            <Container maxWidth="md" sx={{ py: 8, textAlign: 'center' }}>
                <CheckCircle sx={{ fontSize: 80, color: 'success.main', mb: 2 }} />
                <Typography variant="h4" gutterBottom>
                    Order Placed Successfully!
                </Typography>
                <Typography variant="body1" color="text.secondary" gutterBottom>
                    Thank you for your order. You will be redirected to the products page.
                </Typography>
                <CircularProgress sx={{ mt: 3 }} />
            </Container>
        );
    }

    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            <Typography variant="h4" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <ShoppingCart />
                Checkout
            </Typography>

            <Grid container spacing={4}>
                {/* Order Summary */}
                <Grid item xs={12} md={6}>
                    <Paper sx={{ p: 3 }}>
                        <Typography variant="h6" gutterBottom>
                            Order Summary
                        </Typography>
                        
                        {cart?.cartProducts?.map((item) => (
                            <Box key={item.id} sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                                <Typography variant="body2">
                                    {item.name} (x{item.quantity})
                                </Typography>
                                <Typography variant="body2">
                                    €{item.lineTotal?.toFixed(2)}
                                </Typography>
                            </Box>
                        ))}
                        
                        <Box sx={{ borderTop: 1, borderColor: 'divider', pt: 2, mt: 2 }}>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                                <Typography variant="h6">
                                    Total
                                </Typography>
                                <Typography variant="h6">
                                    €{cart?.total?.toFixed(2)}
                                </Typography>
                            </Box>
                        </Box>
                    </Paper>
                </Grid>

                {/* Shipping Form */}
                <Grid item xs={12} md={6}>
                    <Paper sx={{ p: 3 }}>
                        <Typography variant="h6" gutterBottom>
                            Shipping Information
                        </Typography>
                        
                        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                            <TextField
                                fullWidth
                                label="Address"
                                value={formData.shippingAddress}
                                onChange={handleInputChange('shippingAddress')}
                                error={!!errors.shippingAddress}
                                helperText={errors.shippingAddress}
                                margin="normal"
                                required
                            />
                            
                            <TextField
                                fullWidth
                                label="City"
                                value={formData.shippingCity}
                                onChange={handleInputChange('shippingCity')}
                                error={!!errors.shippingCity}
                                helperText={errors.shippingCity}
                                margin="normal"
                                required
                            />
                            
                            <TextField
                                fullWidth
                                label="Postal Code"
                                value={formData.shippingPostalCode}
                                onChange={handleInputChange('shippingPostalCode')}
                                error={!!errors.shippingPostalCode}
                                helperText={errors.shippingPostalCode}
                                margin="normal"
                                required
                            />
                            
                            <TextField
                                fullWidth
                                label="Country"
                                value={formData.shippingCountry}
                                onChange={handleInputChange('shippingCountry')}
                                error={!!errors.shippingCountry}
                                helperText={errors.shippingCountry}
                                margin="normal"
                                required
                            />
                            
                            <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
                                <Button
                                    variant="outlined"
                                    onClick={() => navigate('/cart')}
                                    disabled={submitting}
                                    sx={{ flex: 1 }}
                                >
                                    Back to Cart
                                </Button>
                                
                                <Button
                                    type="submit"
                                    variant="contained"
                                    disabled={submitting}
                                    sx={{ flex: 1 }}
                                >
                                    {submitting ? (
                                        <>
                                            <CircularProgress size={20} sx={{ mr: 1 }} />
                                            Placing Order...
                                        </>
                                    ) : (
                                        'Place Order'
                                    )}
                                </Button>
                            </Box>
                        </Box>
                    </Paper>
                </Grid>
            </Grid>

            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert
                    onClose={handleCloseSnackbar}
                    severity={snackbar.severity}
                    sx={{ width: '100%' }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Container>
    );
}