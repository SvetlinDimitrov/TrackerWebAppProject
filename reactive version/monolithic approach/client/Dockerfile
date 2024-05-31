# Use an official Node.js runtime as the base image
FROM node:16

# Set the working directory in the Docker container to /app
WORKDIR /app

# Accept the VITE_CRYPTO_SECRET and VITE_API_URL as build arguments
ARG VITE_CRYPTO_SECRET
ARG VITE_API_URL

# Set the VITE_CRYPTO_SECRET and VITE_API_URL as environment variables
ENV VITE_CRYPTO_SECRET=$VITE_CRYPTO_SECRET
ENV VITE_API_URL=$VITE_API_URL

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install the project dependencies
RUN npm install

# Copy the rest of the project files to the working directory
COPY . .

# Build the Vite project
RUN npm run build

# Install the `serve` package to serve the built app
RUN npm install -g serve

# Expose port 5000 for the `serve` package
EXPOSE 3000

# Start the `serve` package to serve the built app
CMD ["serve", "-s", "dist", "-l", "3000"]