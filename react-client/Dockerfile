# Build stage
FROM node:14 as build

WORKDIR /app

COPY package.json ./
RUN npm install
COPY . .
ARG REACT_APP_API_SERVER
ENV REACT_APP_API_SERVER=${REACT_APP_API_SERVER}
RUN npm run build

# Production stage
FROM nginx:stable-alpine as production

COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]