const net = require('net');

// Connect to the TCP server
const client = net.createConnection({port: 1111, host: '127.0.0.1'}, () => {
    console.log('Connected to the server');

    // Send a message to the server
    client.write('Hello, server!');
});

// Listen for data from the server
client.on('data', (data) => {
    console.log('Received from server:', data.toString());
});

// Handle connection close
client.on('end', () => {
    console.log('Disconnected from the server');
});

// Handle errors
client.on('error', (err) => {
    console.error('Client error:', err);
});
