import {Button, Card, CardActions, CardContent, Container, Grid2, Link, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import RequestUtil from "../util/RequestUtil";
import {ProductDto, ProductListRes} from "../../client/catalog";
import AddToCart from "../components/AddToCart";

const ProductsPage = () => {
    const [products, setProducts] = useState<ProductDto[]>([])

    useEffect(() => {
        RequestUtil.createProductsApi()
            .searchProducts(RequestUtil.createBaseAxiosRequestConfig())
            .then(res => {
                const response: ProductListRes = res.data;
                const products: ProductDto[] = response.products??[];
                setProducts(products)
            })
    }, [])



    return (
        <Container sx={{ py: 8 }} maxWidth="md">
            <Typography variant="h4" component="h1" gutterBottom align="center">
                Our Products
            </Typography>
            <Grid2
                container spacing={4}>
                {products.map((product) => (
                    <Grid2 item key={product.id} xs={12} sm={6} md={4}>
                        <Card
                            sx={{
                                height: '100%',
                                display: 'flex',
                                flexDirection: 'column',
                            }}
                        >
                            {/*<CardMedia*/}
                            {/*    component="img"*/}
                            {/*    sx={{*/}
                            {/*        pt: '56.25%',*/}
                            {/*    }}*/}
                            {/*    image={product.image}*/}
                            {/*    alt={product.name}*/}
                            {/*/>*/}
                            <CardContent sx={{ flexGrow: 1 }}>
                                <Typography gutterBottom variant="h5" component="h2">
                                    {product.name}
                                </Typography>
                                <Typography variant="h6" color="text.secondar" sx={{ mt: 1 }}>
                                    {"SKU:" + (product.sku??"")}
                                </Typography>
                                <Typography variant="h6" color="h6" sx={{ mt: 1 }}>
                                    {product.price + "€"}
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <Link href={"products/" + product.id} >
                                    <Button size="small">Odpri</Button>
                                </Link>
                                <AddToCart productId={product.id??""} quantity={1} />
                            </CardActions>
                        </Card>
                    </Grid2>
                ))}
            </Grid2>
        </Container>
    );
};

export default ProductsPage;