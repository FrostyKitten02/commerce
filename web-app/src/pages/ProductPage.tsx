import {
    Container,
    Grid2,
    Typography,
    Button,
    Box,
    Paper,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import {ProductDto} from "../../client/catalog";
import {useEffect, useState} from "react";
import RequestUtil from "../util/RequestUtil";
import {useParams} from "react-router-dom";
import AddToCart from "../components/AddToCart";

function ProductPage() {
    const { id } = useParams<{ id: string }>();
    const [product, setProduct] = useState<ProductDto>({})

    useEffect(() => {
        if (id == undefined) {
            return;
        }

        RequestUtil.createProductsApi()
            .getProduct(id, RequestUtil.createBaseAxiosRequestConfig())
            .then(res => {
                const product: ProductDto = res?.data?.product??{};
                setProduct(product);
            })
    }, [])

    return (
        <Container maxWidth="md" sx={{ py: 8 }}>
            <Paper elevation={3} sx={{ p: 4 }}>
                <Grid2 container spacing={4}>
                    <Grid2 item xs={12} md={6}>
                        <Typography variant="h4" component="h1" gutterBottom>
                            {product.name}
                        </Typography>
                        <Typography variant="subtitle1" color="text.secondary" gutterBottom>
                            SKU: {product.sku}
                        </Typography>
                        <Typography variant="body1" paragraph>
                            {product.description}
                        </Typography>
                        <Box mt={2} mb={4}>
                            <Typography variant="h5" color="primary">
                                ${product.price?.toFixed(2)}
                            </Typography>
                        </Box>
                        <AddToCart productId={product.id??""} quantity={1} />
                    </Grid2>
                </Grid2>
            </Paper>
        </Container>
    );
};

export default ProductPage;
