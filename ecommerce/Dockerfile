# Step 1: Build the React app
FROM node:20-alpine as build

WORKDIR /app

# Install dependencies and build the app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build

# Step 2: Serve the React app using a static file server (serve)
FROM node:20-alpine as production

WORKDIR /app

# Install serve (to serve the static files)
RUN npm install -g serve

# Copy the build output from the build stage
COPY --from=build /app/build /app/build

# Expose port 80
EXPOSE 80

# Command to start serving the app
CMD ["serve", "-s", "build", "-l", "80"]
