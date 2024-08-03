#!/bin/bash
# Auth variables
export WEATHER_RABBITMQ_ADMIN="WeatherAdmin"
export WEATHER_RABBITMQ_SERVICE_USER="WeatherService"
export WEATHER_RABBITMQ_PUBLISHER="WeatherPublisher"
export WEATHER_RABBITMQ_ADMIN_PASSWORD="XXX"
export WEATHER_RABBITMQ_SERVICE_PASSWORD="XXX"
export WEATHER_RABBITMQ_PUBLISHER_PASSWORD="XXX"

# Infra variables
export WEATHER_RABBITMQ_VHOST="weather"
export WEATHER_RABBITMQ_QUEUE="weather_queue"
export WEATHER_RABBITMQ_EXCHANGE="weather_exchange"
export WEATHER_RABBITMQ_ROUTE_KEY="weather"

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
sudo rabbitmqctl add_user "$WEATHER_RABBITMQ_ADMIN" "$WEATHER_RABBITMQ_ADMIN_PASSWORD"
sudo rabbitmqctl add_user "$WEATHER_RABBITMQ_SERVICE_USER" "$WEATHER_RABBITMQ_SERVICE_PASSWORD"
sudo rabbitmqctl add_user "$WEATHER_RABBITMQ_PUBLISHER" "$WEATHER_RABBITMQ_PUBLISHER_PASSWORD"
sudo rabbitmqctl add_vhost "$WEATHER_RABBITMQ_VHOST"

# Assign permissions
sudo rabbitmqctl set_permissions -p / "$WEATHER_RABBITMQ_ADMIN" ".*" ".*" ".*"
sudo rabbitmqctl set_permissions -p "$WEATHER_RABBITMQ_VHOST" "$WEATHER_RABBITMQ_ADMIN" ".*" ".*" ".*"
sudo rabbitmqctl set_permissions -p "$WEATHER_RABBITMQ_VHOST" "$WEATHER_RABBITMQ_SERVICE_USER" ".*" ".*" ".*"
sudo rabbitmqctl set_permissions -p "$WEATHER_RABBITMQ_VHOST" "$WEATHER_RABBITMQ_PUBLISHER" ".*" ".*" ".*"
sudo rabbitmqctl set_user_tags "$WEATHER_RABBITMQ_ADMIN" administrator management

# Setup AMQP exchanges, queues, and bindings
rabbitmqadmin declare exchange --vhost="$WEATHER_RABBITMQ_VHOST" name="$WEATHER_RABBITMQ_EXCHANGE" type=topic -u "$WEATHER_RABBITMQ_ADMIN" -p "$WEATHER_RABBITMQ_ADMIN_PASSWORD"

# Test MQTT publish and bindings
mosquitto_pub -h localhost -t weather -m '{"time":"2024-02-21T23:37:46.410Z","temperature":0,"pressure":0,"humidity":0,"windSpeed":0,"windDirection":"N","luminosity":0,"uvIndex":0,"sensorMetadata":{"longitude":0,"latitude":0,"sensorId":"string","tags":["string"],"location":"string"}}' -q 1 -p 1883 -u "$WEATHER_RABBITMQ_PUBLISHER" -P "$WEATHER_RABBITMQ_PUBLISHER_PASSWORD"
mosquitto_pub -h localhost -t weather -m '{"time":"2024-02-21T23:37:46.410Z","temperature":0,"pressure":0,"humidity":0,"windSpeed":0,"windDirection":"N","luminosity":0,"uvIndex":0,"sensorMetadata":{"longitude":0,"latitude":0,"sensorId":"string","tags":["string"],"location":"string"}}' -q 1 -p 1883 -u "$WEATHER_RABBITMQ_PUBLISHER" -P "$WEATHER_RABBITMQ_PUBLISHER_PASSWORD"
