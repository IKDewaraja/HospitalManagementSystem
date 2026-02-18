const express = require('express');
const http = require('http');
const { Server } = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = new Server(server, {
    cors: { origin: "*" } // Allows connections from any frontend
});

// This runs when someone (like an Admin) opens the dashboard
io.on('connection', (socket) => {
    console.log('An Admin connected to the Notification Dashboard');

    socket.on('disconnect', () => {
        console.log('Admin disconnected');
    });
});

// A simple test route to see if it's working
app.get('/', (req, res) => {
    res.send('Notification Service is Running...');
});

const PORT = 3000;
server.listen(PORT, () => {
    console.log(`Notification Service listening on port ${PORT}`);
});