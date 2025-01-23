import React, {useState} from 'react';
import {
    Box,
    Button,
    Container,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography,
} from '@mui/material';
import {Delete} from '@mui/icons-material';
import {CartDto} from "../../client/cart";


export default function CartPage () {
    // Initialize cart state with mock data
    const [cart, setCart] = useState<CartDto>({});

    const handleRemove = (id: string) => {

    };

    const handleQuantityChange = (
        id: string,
        quantity: number,
        price: number
    ) => {

    };

    return (
        <Container maxWidth="md" sx={{ py: 8 }}>
            <Typography variant="h4" gutterBottom>
                Shopping Cart
            </Typography>
            {cart.cartProducts && cart.cartProducts.length > 0 ? (
                <TableContainer component={Paper}>
                    <Table aria-label="cart table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="right">Cena</TableCell>
                                <TableCell align="right">Količina</TableCell>
                                <TableCell align="right">Skupaj</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {cart.cartProducts.map((cartProduct) => {
                                const product = getProduct(cartProduct.productId);
                                return (
                                    <TableRow key={cartProduct.id}>
                                        <TableCell align="right">
                                            ${product.price?.toFixed(2)}
                                        </TableCell>
                                        <TableCell align="right">
                                            <TextField
                                                type="number"
                                                variant="outlined"
                                                size="small"
                                                inputProps={{ min: 1 }}
                                                value={cartProduct.quantity}
                                                onChange={(e) =>
                                                    handleQuantityChange(
                                                        cartProduct.id!,
                                                        parseInt(e.target.value) || 1,
                                                        product.price!
                                                    )
                                                }
                                                sx={{ width: 60 }}
                                            />
                                        </TableCell>
                                        <TableCell align="right">
                                            ${cartProduct.lineTotal?.toFixed(2)}
                                        </TableCell>
                                        <TableCell align="center">
                                            <IconButton
                                                color="error"
                                                onClick={() => handleRemove(cartProduct.id!)}
                                            >
                                                <Delete />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}

                            <TableRow>
                                <TableCell rowSpan={3} />
                                <TableCell colSpan={2} align="right">
                                    <Typography variant="h6">Total</Typography>
                                </TableCell>
                                <TableCell align="right">
                                    <Typography variant="h6">${cart.total?.toFixed(2)}</Typography>
                                </TableCell>
                                <TableCell />
                            </TableRow>
                        </TableBody>
                    </Table>
                </TableContainer>
            ) : (
                <Typography variant="h6" color="text.secondary">
                    Your cart is empty.
                </Typography>
            )}

            {cart.cartProducts && cart.cartProducts.length > 0 && (
                <Box mt={4} display="flex" justifyContent="flex-end">
                    <Button variant="contained" color="primary" size="large">
                        Kupi
                    </Button>
                </Box>
            )}
        </Container>
    );
};
