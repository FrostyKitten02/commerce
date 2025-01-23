import {Button} from "@mui/material";
import RequestUtil from "../util/RequestUtil";

export default function AddToCart({
                                      cartId,
                                      productId,
                                      quantity
                                  }: { cartId?: string, productId: string, quantity: number }) {
    async function addToCart() {
        let cart = cartId;
        if (cart == undefined) {
            const cartRes = await RequestUtil.createCartApi()
                .getCart(RequestUtil.createBaseAxiosRequestConfig())
            cartId = cartRes?.data?.cart?.id;
        }

        if (cartId == null) {
            console.log("Error getting cartid")
            return;
        }

        const res = await RequestUtil.createCartApi()
            .addToCart(cartId, {
                productId: productId,
                quantity: quantity
            }, RequestUtil.createBaseAxiosRequestConfig())
        if (res.status != 200) {
            console.log("Err adding item to basket")
        }
    }

    return (
        <Button
            size="small"
            onClick={() => {
                addToCart();
            }}
        >
            V košarico
        </Button>
    )
}