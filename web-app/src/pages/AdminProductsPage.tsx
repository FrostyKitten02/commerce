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
    Chip,
    Input
} from '@mui/material';
import {Add, Logout, Edit, Delete} from '@mui/icons-material';
import axios from 'axios';
import RequestUtil from "../util/RequestUtil";
import StorageUtil from "../util/StorageUtil";
import {ImageUtil} from "../util/ImageUtil";
import {useNavigate} from "react-router-dom";
import {CreateProductDto, ProductDto, UpdateProductDto} from "../../client/catalog";

interface ProductFormData {
    name: string;
    description: string;
    price: string;
    file?: File;
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
        file: undefined
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
            const response = await RequestUtil.createProductsApi().searchProducts();
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
                file: undefined
            });
        } else {
            setEditingProduct(null);
            setFormData({
                name: '',
                description: '',
                price: '',
                file: undefined
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
            file: undefined
        });
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        setFormData(prev => ({
            ...prev,
            file: file
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
        return true;
    };

    const handleSubmit = async () => {
        if (!validateForm()) return;

        setLoading(true);
        setError('');

        try {
            if (editingProduct) {
                // For updates, create FormData with dot notation for @ModelAttribute
                const formDataToSend = new FormData();
                
                // Use dot notation for nested object properties
                formDataToSend.append('product.name', formData.name.trim());
                formDataToSend.append('product.description', formData.description.trim());
                formDataToSend.append('product.price', formData.price);
                
                if (formData.file) {
                    formDataToSend.append('file', formData.file);
                }

                const config = RequestUtil.createBaseAxiosRequestConfig();
                config.headers = {
                    ...config.headers,
                    'Content-Type': 'multipart/form-data',
                };

                // Use direct axios call instead of generated client for multipart
                const catalogApi = RequestUtil.createProductsApi();
                const baseURL = (catalogApi as any).configuration?.basePath || 'http://localhost:8001/api';
                
                await axios.patch(
                    `${baseURL}/products/${editingProduct.id}`,
                    formDataToSend,
                    config
                );
                setSuccess('Izdelek uspešno posodobljen');
            } else {
                // For new products, create FormData with dot notation for @ModelAttribute
                const formDataToSend = new FormData();
                
                // Use dot notation for nested object properties
                formDataToSend.append('product.name', formData.name.trim());
                formDataToSend.append('product.description', formData.description.trim());
                formDataToSend.append('product.price', formData.price);
                
                if (formData.file) {
                    formDataToSend.append('file', formData.file);
                }

                const config = RequestUtil.createBaseAxiosRequestConfig();
                config.headers = {
                    ...config.headers,
                    'Content-Type': 'multipart/form-data',
                };

                // Use direct axios call instead of generated client for multipart
                const catalogApi = RequestUtil.createProductsApi();
                const baseURL = (catalogApi as any).configuration?.basePath || 'http://localhost:8001/api';
                
                const response = await axios.post(
                    `${baseURL}/products`,
                    formDataToSend,
                    config
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

    const handleDelete = async (productId: string) => {
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
                                {product.pictureId && (
                                    <Box
                                        component="img"
                                        src={ImageUtil.getImageUrl(product.pictureId.toString()) || ''}
                                        alt={product.name}
                                        sx={{
                                            width: '100%',
                                            height: 200,
                                            objectFit: 'cover',
                                            backgroundColor: 'grey.100'
                                        }}
                                        onError={(e) => {
                                            // Hide image if it fails to load
                                            (e.target as HTMLElement).style.display = 'none';
                                        }}
                                    />
                                )}
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
                                    
                                    <Typography variant="h6" color="primary">
                                        €{product.price?.toFixed(2)}
                                    </Typography>
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
                    
                    <Box sx={{ mb: 2 }}>
                        <Typography variant="body2" sx={{ mb: 1 }}>
                            Slika izdelka (opcijsko)
                        </Typography>
                        <Input
                            type="file"
                            onChange={handleFileChange}
                            inputProps={{ accept: 'image/*' }}
                            fullWidth
                        />
                        {formData.file && (
                            <Typography variant="caption" color="text.secondary" sx={{ mt: 1, display: 'block' }}>
                                Izbrana datoteka: {formData.file.name}
                            </Typography>
                        )}
                        {editingProduct && editingProduct.pictureId && (
                            <Typography variant="caption" color="text.secondary" sx={{ mt: 1, display: 'block' }}>
                                Trenutna slika: {editingProduct.pictureId}
                            </Typography>
                        )}
                    </Box>
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