import { useState, useEffect } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container, Typography, AppBar, Toolbar, Button, Card, CardContent, Grid } from '@mui/material';
import axios from 'axios';

function App() {
    const [doctors, setDoctors] = useState([]);

    useEffect(() => {
        // Fetch doctors from backend
        axios.get('http://localhost:8083/api/doctors/all')
            .then(response => {
                setDoctors(response.data);
            })
            .catch(error => {
                console.error("Error fetching doctors:", error);
            });
    }, []);

    return (
        <Router>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Hospital Management System
                    </Typography>
                    <Button color="inherit">Login</Button>
                </Toolbar>
            </AppBar>
            <Container maxWidth="lg" sx={{ mt: 4 }}>
                <Typography variant="h4" gutterBottom>
                    Doctors List
                </Typography>
                <Grid container spacing={3}>
                    {doctors.map(doctor => (
                        <Grid item xs={12} sm={6} md={4} key={doctor.id}>
                            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                                {/* Display Doctor Image */}
                                <div style={{ height: 200, overflow: 'hidden' }}>
                                    <img
                                        src={doctor.imageUrl || `https://ui-avatars.com/api/?name=${doctor.name}&background=random`}
                                        alt={doctor.name}
                                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                    />
                                </div>
                                <CardContent sx={{ flexGrow: 1 }}>
                                    <Typography variant="h5" component="div" gutterBottom>
                                        {doctor.name}
                                    </Typography>
                                    <Typography sx={{ mb: 1.5 }} color="text.secondary">
                                        {doctor.specialization}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Availability: {doctor.availability}
                                    </Typography>
                                    <Typography variant="h6" color="primary" sx={{ mt: 2 }}>
                                        Fee: ${doctor.consultationFee}
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
                {doctors.length === 0 && (
                    <Typography variant="body1" sx={{ mt: 2 }}>
                        No doctors found or backend not connected.
                    </Typography>
                )}
            </Container>
        </Router>
    )
}

export default App
