import {useEffect, useState} from 'react';
import {
    Box,
    Button,
    Card,
    CardContent,
    Container,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Grid2,
    IconButton,
    TextField,
    Typography,
    Alert,
    AppBar,
    Toolbar,
    Chip
} from '@mui/material';
import {Add, Logout, Edit, Delete} from '@mui/icons-material';
import RequestUtil from "../util/RequestUtil";
import StorageUtil from "../util/StorageUtil";
import {useNavigate} from "react-router-dom";
import {ProductDto} from "../../client/catalog";

interface ProductFormData {
    name: string;
    description: string;
    price: string;
    quantity: string;
}

export default function AdminProductsPage() {
    const [products, setProducts] = useState<ProductDto[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [open, setOpen] = useState(false);
    const [editingProduct, setEditingProduct] = useState<ProductDto | null>(null);
    const navigate = useNavigate();

    const [formData, setFormData] = useState<ProductFormData>({
        name: '',
        description: '',
        price: '',
        quantity: ''
    });

    useEffect(() => {
        if (!StorageUtil.isAdminSession()) {
            navigate('/admin');
            return;
        }
        loadProducts();
    }, [navigate]);

    const loadProducts = async () => {
        setLoading(true);
        try {
            const response = await RequestUtil.createProductsApi().getAllProducts();
            setProducts(response.data.products || []);
        } catch (error) {
            console.error('Error loading products:', error);
            setError('Napaka pri nalaganju izdelkov');
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        StorageUtil.clearSession();
        navigate('/admin');
    };

    const handleOpenDialog = (product?: ProductDto) => {
        if (product) {
            setEditingProduct(product);
            setFormData({
                name: product.name || '',
                description: product.description || '',
                price: product.price?.toString() || '',
                quantity: product.quantity?.toString() || ''
            });
        } else {
            setEditingProduct(null);
            setFormData({
                name: '',
                description: '',
                price: '',
                quantity: ''
            });
        }
        setOpen(true);
        setError('');
        setSuccess('');
    };

    const handleCloseDialog = () => {
        setOpen(false);
        setEditingProduct(null);
        setFormData({
            name: '',
            description: '',
            price: '',
            quantity: ''
        });
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const validateForm = () => {
        if (!formData.name.trim()) {
            setError('Ime izdelka je obvezno');
            return false;
        }
        if (!formData.description.trim()) {
            setError('Opis izdelka je obvezen');
            return false;
        }
        if (!formData.price || isNaN(Number(formData.price)) || Number(formData.price) <= 0) {
            setError('Cena mora biti veljavno pozitivno število');
            return false;
        }
        if (!formData.quantity || isNaN(Number(formData.quantity)) || Number(formData.quantity) < 0) {
            setError('Količina mora biti veljavno pozitivno število ali 0');
            return false;
        }
        return true;
    };

    const handleSubmit = async () => {
        if (!validateForm()) return;

        setLoading(true);
        setError('');

        try {
            const productData = {
                name: formData.name.trim(),
                description: formData.description.trim(),
                price: Number(formData.price),
                quantity: Number(formData.quantity)
            };

            if (editingProduct) {
                await RequestUtil.createProductsApi().updateProduct(
                    editingProduct.id!,
                    productData,
                    RequestUtil.createBaseAxiosRequestConfig()
                );
                setSuccess('Izdelek uspešno posodobljen');
            } else {
                await RequestUtil.createProductsApi().createProduct(
                    productData,
                    RequestUtil.createBaseAxiosRequestConfig()
                );
                setSuccess('Izdelek uspešno dodan');
            }

            handleCloseDialog();
            await loadProducts();
        } catch (error: any) {
            console.error('Error saving product:', error);
            setError('Napaka pri shranjevanju izdelka');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (productId: number) => {
        if (!window.confirm('Ali ste prepričani, da želite izbrisati ta izdelek?')) {
            return;
        }

        setLoading(true);
        try {
            await RequestUtil.createProductsApi().deleteProduct(
                productId,
                RequestUtil.createBaseAxiosRequestConfig()
            );
            setSuccess('Izdelek uspešno izbrisan');
            await loadProducts();
        } catch (error: any) {
            console.error('Error deleting product:', error);
            setError('Napaka pri brisanju izdelka');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box>
            <AppBar position="static" color="error">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Admin - Upravljanje Izdelkov
                    </Typography>
                    <Button color="inherit" onClick={handleLogout} startIcon={<Logout />}>
                        Odjava
                    </Button>
                </Toolbar>
            </AppBar>

            <Container maxWidth="lg" sx={{ mt: 4 }}>
                {error && (
                    <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
                        {error}
                    </Alert>
                )}
                {success && (
                    <Alert severity="success" sx={{ mb: 2 }} onClose={() => setSuccess('')}>
                        {success}
                    </Alert>
                )}

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Typography variant="h4" component="h1">
                        Izdelki
                    </Typography>
                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<Add />}
                        onClick={() => handleOpenDialog()}
                        disabled={loading}
                    >
                        Dodaj Izdelek
                    </Button>
                </Box>

                <Grid2 container spacing={3}>
                    {products.map((product) => (
                        <Grid2 key={product.id} xs={12} sm={6} md={4}>
                            <Card>
                                <CardContent>
                                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                                        <Typography variant="h6" component="h2" sx={{ flexGrow: 1 }}>
                                            {product.name}
                                        </Typography>
                                        <Box>
                                            <IconButton
                                                size="small"
                                                onClick={() => handleOpenDialog(product)}
                                                disabled={loading}
                                            >
                                                <Edit />
                                            </IconButton>
                                            <IconButton
                                                size="small"
                                                color="error"
                                                onClick={() => handleDelete(product.id!)}
                                                disabled={loading}
                                            >
                                                <Delete />
                                            </IconButton>
                                        </Box>
                                    </Box>
                                    
                                    <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                        {product.description}
                                    </Typography>
                                    
                                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                        <Typography variant="h6" color="primary">
                                            €{product.price?.toFixed(2)}
                                        </Typography>
                                        <Chip 
                                            label={`Zaloga: ${product.quantity}`}
                                            color={product.quantity! > 0 ? "success" : "error"}
                                            size="small"
                                        />
                                    </Box>
                                </CardContent>
                            </Card>
                        </Grid2>
                    ))}
                </Grid2>

                {products.length === 0 && !loading && (
                    <Box sx={{ textAlign: 'center', mt: 4 }}>
                        <Typography variant="h6" color="text.secondary">
                            Ni izdelkov. Dodajte prvi izdelek.
                        </Typography>
                    </Box>
                )}
            </Container>

            {/* Add/Edit Product Dialog */}
            <Dialog open={open} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
                <DialogTitle>
                    {editingProduct ? 'Uredi Izdelek' : 'Dodaj Nov Izdelek'}
                </DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        name="name"
                        label="Ime Izdelka"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={formData.name}
                        onChange={handleChange}
                        required
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        margin="dense"
                        name="description"
                        label="Opis"
                        type="text"
                        fullWidth
                        variant="outlined"
                        multiline
                        rows={3}
                        value={formData.description}
                        onChange={handleChange}
                        required
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        margin="dense"
                        name="price"
                        label="Cena (€)"
                        type="number"
                        fullWidth
                        variant="outlined"
                        value={formData.price}
                        onChange={handleChange}
                        required
                        inputProps={{ min: 0, step: 0.01 }}
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        margin="dense"
                        name="quantity"
                        label="Količina"
                        type="number"
                        fullWidth
                        variant="outlined"
                        value={formData.quantity}
                        onChange={handleChange}
                        required
                        inputProps={{ min: 0 }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog} disabled={loading}>
                        Prekliči
                    </Button>
                    <Button onClick={handleSubmit} variant="contained" disabled={loading}>
                        {loading ? 'Shranjujem...' : (editingProduct ? 'Posodobi' : 'Dodaj')}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};