const mongoose = require('mongoose');

mongoose.connect(process.env.MONGO_URI, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useCreateIndex: true
    })
    .then(db => {
        console.log(`Connected to MongoDB: ${db.connection.host}`);
    })
    .catch(err => {
        console.log("Error connecting to DB");
    })