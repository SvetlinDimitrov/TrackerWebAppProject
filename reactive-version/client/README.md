# Client App

This document provides details about the client application.

## Technologies Used

- **Vue.js:** A progressive JavaScript framework for building user interfaces.
- **Tailwind CSS:** A utility-first CSS framework for rapidly building custom designs.
- **PrimeVue:** A UI component library for Vue.js based on PrimeUI.

## How to Run

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/SvetlinDimitrov/TrackerWebAppProject.git
    ```

2. **Ensure Docker Desktop is Running:**

    Make sure Docker Desktop is running on your machine.

3. **Navigate to the Docker Local Directory:**
   
    ```bash
    cd reactive-version/client/docker_local
    ```

4. **Configure Environment Variables:**

    Open the `.env` file and provide the necessary information. Refer to the [README](../README.md) for guidance on where to obtain this information.

5. **Start Docker Services:**

    Run the following command to start the database and server:
    
    ```bash
    docker-compose up
    ```

6. **Navigate Back to the Project Directory:**
   
    ```bash
    cd ..
    ```

7. **Install Dependencies:**
   
    ```bash
    npm install
    ```

8. **Start the Development Server:**

    ```bash
    npm run dev
    ```

9. **Access the Application:**

    Open your browser and go to [http://localhost:3000](http://localhost:3000) to view the application.

