import {useEffect, useState} from 'react';
import {
    Box,
    Button,
    Card,
    CardContent,
    Container,
    Grid2,
    Typography,
    Alert,
    AppBar,
    Toolbar,
    Chip,
    CircularProgress,
    List,
    ListItem,
    ListItemIcon,
    ListItemText
} from '@mui/material';
import {
    Logout,
    CheckCircle,
    Error,
    Warning,
    Refresh,
    Schedule,
    Speed,
    Computer
} from '@mui/icons-material';
import StorageUtil from "../util/StorageUtil";
import {useNavigate} from "react-router-dom";
import { HealthMonitorService, HealthReport, ServiceStatus } from "../services/HealthMonitorService";

export default function AdminHealthPage() {
    const [healthReport, setHealthReport] = useState<HealthReport | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [lastRefresh, setLastRefresh] = useState<Date | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (!StorageUtil.isAdminSession()) {
            navigate('/admin');
            return;
        }
        loadHealthStatus();
        
        // Auto-refresh every 30 seconds
        const interval = setInterval(loadHealthStatus, 30000);
        return () => clearInterval(interval);
    }, [navigate]);

    const loadHealthStatus = async () => {
        setLoading(true);
        setError('');
        
        try {
            const config = await ConfigUtil.getConfig();
            const healthMonitorUrl = config.healthMonitorUrl || 'http://localhost:8086';
            
            const response = await axios.get(`${healthMonitorUrl}/api/health/status`);
            setHealthReport(response.data);
            setLastRefresh(new Date());
        } catch (error: any) {
            console.error('Error loading health status:', error);
            setError('Unable to connect to health monitoring service');
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        StorageUtil.clearSession();
        navigate('/admin');
    };

    const getStatusIcon = (status: string) => {
        switch (status) {
            case 'UP':
                return <CheckCircle sx={{ color: 'success.main' }} />;
            case 'DOWN':
                return <Error sx={{ color: 'error.main' }} />;
            default:
                return <Warning sx={{ color: 'warning.main' }} />;
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'UP':
                return 'success';
            case 'DOWN':
                return 'error';
            default:
                return 'warning';
        }
    };

    const getOverallStatusMessage = (status: string) => {
        switch (status) {
            case 'ALL_UP':
                return 'All Systems Operational';
            case 'ALL_DOWN':
                return 'All Systems Down';
            case 'PARTIAL':
                return 'Partial Service Outage';
            default:
                return 'Unknown Status';
        }
    };

    const formatResponseTime = (time: number) => {
        if (time < 1000) {
            return `${time}ms`;
        }
        return `${(time / 1000).toFixed(2)}s`;
    };

    const formatLastChecked = (timestamp: string) => {
        return new Date(timestamp).toLocaleTimeString();
    };

    return (
        <Box>
            <AppBar position="static" color="error">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Admin - Health Monitoring
                    </Typography>
                    <Button color="inherit" onClick={() => navigate('/admin/products')} sx={{ mr: 2 }}>
                        Products
                    </Button>
                    <Button color="inherit" onClick={handleLogout} startIcon={<Logout />}>
                        Logout
                    </Button>
                </Toolbar>
            </AppBar>

            <Container maxWidth="lg" sx={{ mt: 4 }}>
                {error && (
                    <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
                        {error}
                    </Alert>
                )}

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Typography variant="h4" component="h1">
                        System Health Dashboard
                    </Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        {lastRefresh && (
                            <Typography variant="body2" color="text.secondary">
                                Last updated: {lastRefresh.toLocaleTimeString()}
                            </Typography>
                        )}
                        <Button
                            variant="contained"
                            color="primary"
                            startIcon={loading ? <CircularProgress size={20} /> : <Refresh />}
                            onClick={loadHealthStatus}
                            disabled={loading}
                        >
                            {loading ? 'Refreshing...' : 'Refresh'}
                        </Button>
                    </Box>
                </Box>

                {healthReport ? (
                    <>
                        {/* Overall Status Card */}
                        <Card sx={{ mb: 4 }}>
                            <CardContent>
                                <Grid2 container spacing={3} alignItems="center">
                                    <Grid2 xs={12} md={6}>
                                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                                            {getStatusIcon(healthReport.overallStatus === 'ALL_UP' ? 'UP' : 'DOWN')}
                                            <Box>
                                                <Typography variant="h5" component="h2">
                                                    {getOverallStatusMessage(healthReport.overallStatus)}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    Last checked: {formatLastChecked(healthReport.reportTime)}
                                                </Typography>
                                            </Box>
                                        </Box>
                                    </Grid2>
                                    <Grid2 xs={12} md={6}>
                                        <Grid2 container spacing={3}>
                                            <Grid2 xs={4}>
                                                <Box sx={{ textAlign: 'center' }}>
                                                    <Typography variant="h4" color="success.main">
                                                        {healthReport.servicesUp}
                                                    </Typography>
                                                    <Typography variant="body2">Services Up</Typography>
                                                </Box>
                                            </Grid2>
                                            <Grid2 xs={4}>
                                                <Box sx={{ textAlign: 'center' }}>
                                                    <Typography variant="h4" color="error.main">
                                                        {healthReport.servicesDown}
                                                    </Typography>
                                                    <Typography variant="body2">Services Down</Typography>
                                                </Box>
                                            </Grid2>
                                            <Grid2 xs={4}>
                                                <Box sx={{ textAlign: 'center' }}>
                                                    <Typography variant="h4">
                                                        {healthReport.totalServices}
                                                    </Typography>
                                                    <Typography variant="body2">Total Services</Typography>
                                                </Box>
                                            </Grid2>
                                        </Grid2>
                                    </Grid2>
                                </Grid2>
                            </CardContent>
                        </Card>

                        {/* Services Grid */}
                        <Grid2 container spacing={3}>
                            {healthReport.services.map((service, index) => (
                                <Grid2 key={index} xs={12} md={6} lg={4}>
                                    <Card sx={{ height: '100%' }}>
                                        <CardContent>
                                            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                                                <Typography variant="h6" component="h3">
                                                    {service.serviceName}
                                                </Typography>
                                                <Chip
                                                    icon={getStatusIcon(service.status)}
                                                    label={service.status}
                                                    color={getStatusColor(service.status) as any}
                                                    variant="outlined"
                                                />
                                            </Box>

                                            <List dense>
                                                <ListItem disablePadding>
                                                    <ListItemIcon>
                                                        <Speed fontSize="small" />
                                                    </ListItemIcon>
                                                    <ListItemText
                                                        primary="Response Time"
                                                        secondary={formatResponseTime(service.responseTime)}
                                                    />
                                                </ListItem>
                                                <ListItem disablePadding>
                                                    <ListItemIcon>
                                                        <Schedule fontSize="small" />
                                                    </ListItemIcon>
                                                    <ListItemText
                                                        primary="Last Checked"
                                                        secondary={formatLastChecked(service.lastChecked)}
                                                    />
                                                </ListItem>
                                                <ListItem disablePadding>
                                                    <ListItemIcon>
                                                        <Computer fontSize="small" />
                                                    </ListItemIcon>
                                                    <ListItemText
                                                        primary="Endpoint"
                                                        secondary={service.url}
                                                    />
                                                </ListItem>
                                            </List>

                                            {service.status === 'DOWN' && service.errorMessage && (
                                                <Alert severity="error" sx={{ mt: 2 }}>
                                                    <Typography variant="body2">
                                                        {service.errorMessage}
                                                    </Typography>
                                                </Alert>
                                            )}
                                        </CardContent>
                                    </Card>
                                </Grid2>
                            ))}
                        </Grid2>
                    </>
                ) : loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 400 }}>
                        <CircularProgress />
                    </Box>
                ) : (
                    <Card>
                        <CardContent>
                            <Typography variant="h6" textAlign="center" color="text.secondary">
                                No health data available. Click Refresh to load service status.
                            </Typography>
                        </CardContent>
                    </Card>
                )}
            </Container>
        </Box>
    );
}