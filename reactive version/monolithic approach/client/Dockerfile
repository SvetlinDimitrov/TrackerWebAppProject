# Use an official Node.js runtime as the base image
FROM node:16

# Set the working directory in the Docker container to /app
WORKDIR /app

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