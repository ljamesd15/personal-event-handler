# Personal event handler

## Authentication/Authorization
The event handler uses JWT tokens for AuthN/Z when communicating with services fronted by API Gateways (e.g. Kong for Weather Service).

## Local development
To start RabbitMQ service
1. ```$ systemctl start rabbitmq-server```
2. If necessary enable 1883 port access
   1. ```$ sudo ufw allow 1883 && sudo ufw allow 8883 && sudo ufw enable```
3. If running on WSL enable port forwarding for mQTT traffic
   1. ```PS C:\WINDOWS\system32> netsh interface portproxy add v4tov4 listenport=1883 listenaddress=0.0.0.0 connectport=1883 connectaddress=$(wsl -d "Ubuntu-24.04" hostname -I)```
   2. ```PS C:\WINDOWS\system32> netsh interface portproxy add v4tov4 listenport=8883 listenaddress=0.0.0.0 connectport=8883 connectaddress=$(wsl -d "Ubuntu-24.04" hostname -I)```
   3. ```PS C:\WINDOWS\system32> netsh interface portproxy show v4tov4```

To start the service
1. ```mvn clean package```
2. ```$ java -jar -Dspring.profiles.active=local,ide target/event-handler.jar com.personal.eventhandler.PersonalEventHandlerApplication.java```

## Making a change
1. Make changes necessary and add unit tests
2. Run the new unit tests to ensure they pass
   1. Ex. ```mvn test -Dtest=WeatherEventHandlerTest#receiveMessageTest```
3. Run all unit tests to ensure nothing broke ```mvn clean test```
4. Start service (see above) and manually smoke test changes
5. Add changes with a meaningful commit message
6. Push changes to a feature branch and submit a pull request to main branch