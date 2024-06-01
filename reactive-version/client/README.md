# Steps to Start the Front End

1. **Create `application-prod.properties`**
    - Go into the server app.
    - Create a file named `application-prod.properties` and fill it with the properties from `application.properties`.
    - For more information, check the server's README file.

2. **Build the Server Application**
    - Run the following commands to clean and build the application:
      ```sh
      mvn clean
      mvn build
      ```

3. **Ensure Database Connection**
    - Make sure the database is connected with the correct name in the Java server.
   
4. **Run Docker Compose**
    - Navigate to the directory containing your `docker-compose.yaml` file.
    - Run the following command to start the Docker containers:
      ```sh
      docker-compose up
      ```
      
5. **Install Front End Dependencies**
    - Run the following command to install the necessary npm packages:
      ```sh
      npm install
      ```

6. **Start the Development Server**
    - Run the following command to start the development server:
      ```sh
      npm run dev
      ```

Now your front end should be up and running!
