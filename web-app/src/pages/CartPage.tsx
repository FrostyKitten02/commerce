import {useEffect, useState} from 'react';
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    Container,
    IconButton,
    Paper,
    Snackbar,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography,
} from '@mui/material';
import {Delete, ShoppingCart} from '@mui/icons-material';
import {CartDto, CartProductDto} from "../../client/cart";
import RequestUtil from "../util/RequestUtil";
import {ImageUtil} from "../util/ImageUtil";
import {useNavigate} from 'react-router-dom';

export default function CartPage() {
    const navigate = useNavigate();
    const [cart, setCart] = useState<CartDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [snackbar, setSnackbar] = useState<{
        open: boolean;
        message: string;
        severity: 'success' | 'error';
    }>({
        open: false,
        message: '',
        severity: 'success'
    });

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        try {
            setLoading(true);
            const response = await RequestUtil.createCartApi()
                .getCart(RequestUtil.createBaseAxiosRequestConfig());
            setCart(response.data.cart || null);
        } catch (error) {
            console.error('Error fetching cart:', error);
            showSnackbar('Error loading cart', 'error');
        } finally {
            setLoading(false);
        }
    };

    const handleRemove = async (cartProductId: string) => {
        try {
            await RequestUtil.createCartApi()
                .removeFromCart(cartProductId, RequestUtil.createBaseAxiosRequestConfig());
            await fetchCart();
            showSnackbar('Item removed from cart', 'success');
        } catch (error) {
            console.error('Error removing item:', error);
            showSnackbar('Error removing item', 'error');
        }
    };

    const handleQuantityChange = async (cartProductId: string, newQuantity: number) => {
        if (newQuantity < 1) return;
        
        try {
            await RequestUtil.createCartApi()
                .updateQuantity(cartProductId, newQuantity, RequestUtil.createBaseAxiosRequestConfig());
            await fetchCart();
            showSnackbar('Quantity updated', 'success');
        } catch (error) {
            console.error('Error updating quantity:', error);
            showSnackbar('Error updating quantity', 'error');
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
            </Container>
        );
    }

    return (
        <Container maxWidth="md" sx={{ py: 8 }}>
            <Typography variant="h4" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <ShoppingCart />
                Shopping Cart
            </Typography>

            {cart?.cartProducts && cart.cartProducts.length > 0 ? (
                <>
                    <TableContainer component={Paper}>
                        <Table aria-label="cart table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Product</TableCell>
                                    <TableCell align="right">Price</TableCell>
                                    <TableCell align="right">Quantity</TableCell>
                                    <TableCell align="right">Total</TableCell>
                                    <TableCell align="center">Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {cart.cartProducts
                                    .sort((a, b) => (a.sku || '').localeCompare(b.sku || ''))
                                    .map((cartProduct: CartProductDto) => (
                                    <TableRow key={cartProduct.id}>
                                        <TableCell>
                                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                                                <Box
                                                    component="img"
                                                    src={ImageUtil.getImageUrl(cartProduct.pictureId)}
                                                    alt={cartProduct.name || 'Product'}
                                                    sx={{ width: 50, height: 50, objectFit: 'cover' }}
                                                />
                                                <Box>
                                                    <Typography variant="body1" fontWeight="medium">
                                                        {cartProduct.name}
                                                    </Typography>
                                                    <Typography variant="body2" color="text.secondary">
                                                        SKU: {cartProduct.sku}
                                                    </Typography>
                                                </Box>
                                            </Box>
                                        </TableCell>
                                        <TableCell align="right">
                                            €{cartProduct.price?.toFixed(2)}
                                        </TableCell>
                                        <TableCell align="right">
                                            <TextField
                                                type="number"
                                                variant="outlined"
                                                size="small"
                                                inputProps={{ min: 1 }}
                                                value={cartProduct.quantity || 1}
                                                onChange={(e) => {
                                                    const newQuantity = parseInt(e.target.value) || 1;
                                                    handleQuantityChange(cartProduct.id!, newQuantity);
                                                }}
                                                sx={{ width: 80 }}
                                            />
                                        </TableCell>
                                        <TableCell align="right">
                                            <Typography variant="body1" fontWeight="bold">
                                                €{cartProduct.lineTotal?.toFixed(2)}
                                            </Typography>
                                        </TableCell>
                                        <TableCell align="center">
                                            <IconButton
                                                color="error"
                                                onClick={() => handleRemove(cartProduct.id!)}
                                                title="Remove item"
                                            >
                                                <Delete />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}

                                <TableRow>
                                    <TableCell rowSpan={3} />
                                    <TableCell colSpan={2} />
                                    <TableCell align="right">
                                        <Typography variant="h6" fontWeight="bold">
                                            Total: €{cart.total?.toFixed(2)}
                                        </Typography>
                                    </TableCell>
                                    <TableCell />
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>

                    <Box mt={4} display="flex" justifyContent="space-between" alignItems="center">
                        <Button 
                            variant="outlined" 
                            onClick={() => window.location.href = '/products'}
                        >
                            Continue Shopping
                        </Button>
                        <Button 
                            variant="contained" 
                            color="primary" 
                            size="large"
                            sx={{ minWidth: 150 }}
                            onClick={() => navigate('/checkout')}
                        >
                            Proceed to Checkout
                        </Button>
                    </Box>
                </>
            ) : (
                <Box textAlign="center" py={8}>
                    <ShoppingCart sx={{ fontSize: 80, color: 'text.secondary', mb: 2 }} />
                    <Typography variant="h6" color="text.secondary" gutterBottom>
                        Your cart is empty
                    </Typography>
                    <Button 
                        variant="contained" 
                        onClick={() => window.location.href = '/products'}
                        sx={{ mt: 2 }}
                    >
                        Browse Products
                    </Button>
                </Box>
            )}

            <Snackbar
                open={snackbar.open}
                autoHideDuration={3000}
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
