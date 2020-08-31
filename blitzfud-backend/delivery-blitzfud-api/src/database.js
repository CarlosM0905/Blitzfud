const mongoose = require('mongoose');

mongoose.connect(process.env.MONGO_URI, {
        useCreateIndex: true,
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useFindAndModify: false
    })
    .then(db => {
        console.log(`Connected to MongoDB: ${db.connection.host}`);
    })
    .catch(err => {
        console.log("Error connecting to DB");
    })