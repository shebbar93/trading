# Use the official MySQL image as the base
FROM mysql:5.7

# Set the root password for MySQL
ENV MYSQL_ROOT_PASSWORD=password

# Create the order-service database
ENV MYSQL_DATABASE=trading_signals_service

# Expose the default MySQL port
EXPOSE 3306
