# NutriGuideBuddy

https://github.com/SvetlinDimitrov/TrackerWebAppProject/assets/105767427/e8170f3e-9bb7-45d5-abce-4030761d0725

### Description

NutriGuideBuddy is a comprehensive nutrition tracking web application designed to help users monitor their dietary habits and achieve their nutritional goals. This project is currently in its early release stage and includes the bare minimum stylings. It is not yet fully responsive.

### Features

- **User Authentication:** Secure login and registration functionality.
- **Food Logging:** Track daily food intake with nutritional information.
- **Progress Tracking:** Monitor your progress over time with visual graphs and statistics.
- **Database Integration:** Store user data and food logs securely.
- **Basic Styling:** Minimalistic design focusing on functionality.
- **Non-responsive Design:** Currently not optimized for mobile devices.

### Installation

If you want to run the app locally, I have created a Docker folder where you can find the `docker-compose.yaml` file. You will need to have Docker Desktop installed on your machine.

Before running `docker-compose up`, you need to fill in the `.env` file. Follow these steps:

1. **Nutritionix API:**
    - Create at least a free tier account on [Nutritionix API](https://www.nutritionix.com/business/api).
    - Obtain the API key and secret.
    - Fill in the `.env` file with:
      ```plaintext
      FOOD_DATA_API_KEY=your_api_key
      FOOD_DATA_API_ID=your_api_id
      ```

2. **Brevo (formerly Sendinblue):**
    - Create an account on [Brevo](https://app.brevo.com/billing/account/plans/marketing), which is the email sender app used.
    - Obtain the API key upon registration.
    - Create a sender email on their website.
    - Fill in the `.env` file with:
      ```plaintext
      SENDINBLUE_API_KEY=your_sendinblue_api_key
      EMAIL_SENDER=your_sender_email
      ```

3. **JWT Secret:**
    - Create a JWT secret. It should be at least 256 bytes long (example: `your_256_byte_secret`).
    - Fill in the `.env` file with:
      ```plaintext
      JWT_SECRET=your_256_byte_secret
      ```

Once you have filled in all the fields, run the following command:
``bash
docker-compose up


#### More Information

- For more detailed information about the client side, please refer to the [Client README](client/README.md).
- For more detailed information about the server side, please refer to the [Server README](SERVER-README.md).

### Early Release Notice

Please note that NutriGuideBuddy is in its early release stage. The application includes the bare minimum styling and is not responsive. Future updates will focus on improving the user interface and making the application mobile-friendly.

### Contributing

We welcome contributions! Feel free to open an issue or submit a pull request for any improvements or bug fixes. If you're interested in collaborating on this project, don't hesitate to contact me at [dimitrovmail99@gmail.com](mailto:dimitrovmail99@gmail.com), and I can invite you to join.

### License

This project is licensed under the MIT License. See the [LICENSE](../LICENSE) file for details.

