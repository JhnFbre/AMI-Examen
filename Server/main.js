const express = require('express');
const cors = require('cors');
const app = express();

const { mongoose } = require('./database');

const bodyParser = require('body-parser');
const bodyParserURLEncoded = bodyParser.urlencoded({extended:true})

// Settings
app.set('port', process.env.PORT || 3000);

// Middlewares
app.use(cors({origin: 'http://localhost:4200'}));
app.use(express.json());

app.use(bodyParser.json());
app.use(bodyParserURLEncoded);

// Routes
app.use('/api/employees', require('./routes/employee.routes'));
app.use('/api/customers', require('./routes/customer.routes'));-

// starting the server
app.listen(app.get('port'), () => {
    console.log(`server on port ${app.get('port')}`);
});