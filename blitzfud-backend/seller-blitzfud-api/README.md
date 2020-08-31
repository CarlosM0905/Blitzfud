# seller-blitzfud-api

## Requirements

- [NodeJS](https://nodejs.org/)
- [MongoDB](https://www.mongodb.com/)
  
## Configuration

To run the server, please take the following steps:

1. Install the dependencies
2. Create a .env file on the main folder
3. Set up the following environment variables in the file (Replace the uppercase keywords with the ones of your preference):
    ```
    PORT = XXXX
    HOST = localhost:XXXX
    MONGO_URI = mongodb://localhost:YYYY/ZZZZ
    JWT_KEY = AAAA
    ```
4. Start mongod service if it's inactive:
    - For Linux-based OS: <code>systemctl start mongod</code>
5. Launch a terminal in root directory
6. Execute server via:  <code>npm run dev</code>