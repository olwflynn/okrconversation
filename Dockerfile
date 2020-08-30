# Use the official image as a parent image.
FROM maven

# Set the working directory.
WORKDIR /

# Copy the file from your host to your current location.
COPY . .

# Run the command inside your image filesystem. i.e.
RUN mvn clean install

# Add metadata to the image to describe which port the container is listening on at runtime.
# EXPOSE 9200

# Run the specified command within the container.
CMD tail -f /dev/null