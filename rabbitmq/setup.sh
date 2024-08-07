#!/bin/bash
# Auth variables
export RABBITMQ_WEATHER_ADMIN="WeatherAdmin"
export RABBITMQ_WEATHER_SERVICE_USER="WeatherService"
export RABBITMQ_WEATHER_PUBLISHER="WeatherPublisher"
export RABBITMQ_WEATHER_ADMIN_PASSWORD="GreenfieldAutomation24"
export RABBITMQ_WEATHER_SERVICE_PASSWORD="GreenfieldAutomation24"
export RABBITMQ_WEATHER_PUBLISHER_PASSWORD="GreenfieldAutomation24"

# Infra variables
export RABBITMQ_WEATHER_VHOST="weather"
export RABBITMQ_WEATHER_QUEUE="weather_queue"
export RABBITMQ_WEATHER_EXCHANGE="weather_exchange"
export RABBITMQ_WEATHER_ROUTE_KEY="weather"

# Add plugins
sudo rabbitmq-plugins enable rabbitmq_management
sudo rabbitmq-plugins enable rabbitmq_mqtt
wget http://127.0.0.1:15672/cli/rabbitmqadmin
chmod +x rabbitmqadmin
sudo mv rabbitmqadmin /etc/rabbitmq
export PATH="$PATH:/etc/rabbitmq/"
sudo mv ~/Downloads/rabbitmq_delayed_message_exchange-3.13.0.ez /usr/lib/rabbitmq/lib/rabbitmq_server-3.13.6/plugins/
sudo rabbitmq-plugins enable rabbitmq_delayed_message_exchange

# Create users and virtual hosts
sudo rabbitmqctl add_user "$RABBITMQ_WEATHER_ADMIN" "$RABBITMQ_WEATHER_ADMIN_PASSWORD"
sudo rabbitmqctl add_user "$RABBITMQ_WEATHER_SERVICE_USER" "$RABBITMQ_WEATHER_SERVICE_PASSWORD"
sudo rabbitmqctl add_user "$RABBITMQ_WEATHER_PUBLISHER" "$RABBITMQ_WEATHER_PUBLISHER_PASSWORD"
sudo rabbitmqctl add_vhost "$RABBITMQ_WEATHER_VHOST"

# Assign permissions
sudo rabbitmqctl set_permissions -p / "$RABBITMQ_WEATHER_ADMIN" ".*" ".*" ".*"
sudo rabbitmqctl set_permissions -p "$RABBITMQ_WEATHER_VHOST" "$RABBITMQ_WEATHER_ADMIN" ".*" ".*" ".*"
sudo rabbitmqctl set_permissions -p "$RABBITMQ_WEATHER_VHOST" "$RABBITMQ_WEATHER_SERVICE_USER" ".*" ".*" ".*"
sudo rabbitmqctl set_permissions -p "$RABBITMQ_WEATHER_VHOST" "$RABBITMQ_WEATHER_PUBLISHER" ".*" ".*" ".*"
sudo rabbitmqctl set_user_tags "$RABBITMQ_WEATHER_ADMIN" administrator management

# Setup AMQP exchanges, queues, and bindings
rabbitmqadmin declare exchange --vhost="$RABBITMQ_WEATHER_VHOST" name="$RABBITMQ_WEATHER_EXCHANGE" type=topic -u "$RABBITMQ_WEATHER_ADMIN" -p "$RABBITMQ_WEATHER_ADMIN_PASSWORD"

# Test MQTT publish and bindings
mosquitto_pub -h localhost -t weather -m '{"time":"2024-02-21T23:37:46.410Z","temperature":0,"pressure":0,"humidity":0,"windSpeed":0,"windDirection":"N","luminosity":0,"uvIndex":0,"sensorMetadata":{"longitude":0,"latitude":0,"sensorId":"string","tags":["string"],"location":"string"}}' -q 1 -p 1883 -u "$RABBITMQ_WEATHER_PUBLISHER" -P "$RABBITMQ_WEATHER_PUBLISHER_PASSWORD"
mosquitto_pub -h localhost -t weather -m '{"time":"2024-02-21T23:37:46.410Z","temperature":0,"pressure":0,"humidity":0,"windSpeed":0,"windDirection":"N","luminosity":0,"uvIndex":0,"sensorMetadata":{"longitude":0,"latitude":0,"sensorId":"string","tags":["string"],"location":"string"}}' -q 1 -p 1883 -u "$RABBITMQ_WEATHER_PUBLISHER" -P "$RABBITMQ_WEATHER_PUBLISHER_PASSWORD"
