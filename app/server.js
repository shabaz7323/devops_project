const express = require('express');
const app = express();
app.get('/', (req,res) => res.send('Advanced DevOps Project Working'));
app.listen(3000, () => console.log('Server running on 3000'));
