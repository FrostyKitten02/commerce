import {Button, Snackbar, Alert} from "@mui/material";
import {useState} from "react";
import RequestUtil from "../util/RequestUtil";

export default function AddToCart({
                                      cartId,
                                      productId,
                                      quantity
                                  }: { cartId?: string, productId: string, quantity: number }) {
    const [loading, setLoading] = useState(false);
    const [snackbar, setSnackbar] = useState<{
        open: boolean;
        message: string;
        severity: 'success' | 'error';
    }>({
        open: false,
        message: '',
        severity: 'success'
    });

    async function addToCart() {
        setLoading(true);
        try {
            let cart = cartId;
            if (cart == undefined) {
                const cartRes = await RequestUtil.createCartApi()
                    .getCart(RequestUtil.createBaseAxiosRequestConfig());
                cart = cartRes?.data?.cart?.id;
            }

            if (cart == null) {
                setSnackbar({
                    open: true,
                    message: "Error getting cart",
                    severity: 'error'
                });
                return;
            }

            const res = await RequestUtil.createCartApi()
                .addToCart(cart, {
                    productId: productId,
                    quantity: quantity
                }, RequestUtil.createBaseAxiosRequestConfig());

            if (res.status === 200) {
                setSnackbar({
                    open: true,
                    message: "Product added to cart successfully!",
                    severity: 'success'
                });
            } else {
                setSnackbar({
                    open: true,
                    message: "Error adding item to cart",
                    severity: 'error'
                });
            }
        } catch (error) {
            console.error("Error adding to cart:", error);
            setSnackbar({
                open: true,
                message: "Error adding item to cart",
                severity: 'error'
            });
        } finally {
            setLoading(false);
        }
    }

    const handleCloseSnackbar = () => {
        setSnackbar(prev => ({ ...prev, open: false }));
    };

    return (
        <>
            <Button
                size="small"
                onClick={addToCart}
                disabled={loading}
            >
                {loading ? "Adding..." : "V košarico"}
            </Button>
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
        </>
    )
}