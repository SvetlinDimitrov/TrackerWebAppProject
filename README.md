# TrackerWebApp

Sure, here's a revised description for your web application:

TrackerWebApp is a comprehensive tool designed for health enthusiasts like myself who appreciate the importance of tracking daily nutrient intake. This application provides detailed information about a wide range of nutrients, including daily intake recommendations and which foods are rich in specific nutrients. 

The application also includes a feature to track your personal achievements and progress over time. 

## Features

- Nutrition Information Provider:
This feature offers comprehensive information about a wide array of nutrients. It includes detailed descriptions, daily intake recommendations, and statistics on which foods are particularly rich in specific nutrients. This allows users to gain a thorough understanding of their nutritional intake and make informed dietary choices.

- Nutrition Tracker:
This feature allows you to track your daily nutrient intake, similar to MyFitnessPal. It also provides additional statistics for a more detailed view of your nutrition.

- Achievement Tracker:
This versatile feature enables you to track any goal with a measurable target. For instance, if your goal is to swim 5 km, you can easily log and track this. The Achievement Tracker provides detailed daily, weekly, monthly, and yearly progress statistics, making it simple to monitor your progress towards any goal.

For visual details about the application, please refer to the images folder in this repository where you'll find screenshots showcasing various features of the application.

## Technologies Used

- Front-End: React
  - Visualization Library: D3.js
- Back-End: Java
  - Architecture: Microservices
  - Build Tool: Maven
  - Framework: Spring Boot
  - Cloud Framework: Spring Cloud
- Documentation: Swagger

## API Reference

The API is fully documented with Postman and Swagger:

- [Postman Collection](https://documenter.getpostman.com/view/26519722/2s9Ykhh4Qv): This collection includes all the API endpoints with example requests and responses. You can import this collection into Postman to explore and test the API.

- Swagger Documentation: The API also includes Swagger documentation, which provides an interactive interface for exploring the API. You can access the Swagger UI at `/swagger-ui.html` endpoint when you run the application.

## Installation

The application is containerized using Docker and can be easily set up with Docker Compose. Follow these steps to get it running:

1. Navigate to the `microservice/docker` directory in the project repository.
2. Run the `docker-compose` file. This will automatically download the necessary Docker images from the project's Docker repository and start the application.

Please ensure that you have Docker and Docker Compose installed on your machine before proceeding.

**Note:** When you first start the application, it may take a while for the Eureka server and gateway to fetch all the servers. During this time, you may see an error message in the console, and the server may return a 503 error. This is normal and should resolve itself within about a minute. If you see a loading screen, just refresh it a few times until the application loads.

## Usage

### REST API

Detailed instructions for using the REST API are provided in the Postman documentation. You can find examples of requests, responses, and other useful information there.

### React Client

To use the client application:

1. Register for an account.
2. Log in with your new account.

Once you're logged in, you can start using the application. The interface is designed to be intuitive and user-friendly, so you should be able to navigate through the features easily.

## Running Tests

The application includes both unit and integration tests. These can be run using Maven, a build automation tool used primarily for Java projects.

To run the tests, navigate to the project directory in your terminal and run the following command: mvn test

## Deployment

This application is not currently deployed, but there are plans for deployment in the future. Updates regarding deployment will be posted in this section.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Contact

If you want to contact me you can reach me at dimitrovmail99@gmail.com.

## Future Plans

The current plan for the future development of this application involves transitioning the microservices architecture to Kubernetes. This will enhance the scalability and reliability of the application.
