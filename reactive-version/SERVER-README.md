## API Documentation

To interact with the API, please refer to the [Postman Collection](https://documenter.getpostman.com/view/26519722/2s9Ykhh4Qv) provided.

The collection includes detailed information about each endpoint, including request parameters, headers, and expected
responses.

## Technologies Used

- **Spring Framework:** Used for building the backend of the application.
- **MySQL:** Database management system used to store application data.

## How to Run

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/SvetlinDimitrov/TrackerWebAppProject.git
    ```

2. **Navigate to the Project Directory:**
    ```bash
    cd reactive-version
    ```

3. **Fill in the Application Properties:**

   After cloning the repository, navigate to the `reactive-version` project directory. You'll need to fill in all the
   fields in the `application.properties` file. Refer to the [README](README.md) provided in the project for guidance on
   where to obtain the information.

4. **Remove Development Profile Line:**

   In the `application.properties` file, remove the line `spring.profiles.active=dev`.

5. **Running Tests:**

   If you want to run all the tests, you'll need to create an `application-secret.properties` file. Copy all the
   contents from `application.properties` into it.

6. **JDK Version:**

   Ensure you have JDK 17 installed for this project.

7. **Start the Application:**

   This application is configured to run with Docker Compose for easy deployment. Ensure you have Docker Desktop
   installed and running on your system. When the application is started, it will automatically look for
   a `compose.yaml` file and initiate it. If Docker Desktop is not installed or running, you won`t be able to start the
   application.
