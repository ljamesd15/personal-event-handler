#!/bin/bash

source /home/ldavidso/.bashrc && \
	cd /home/ldavidso/workspace/personal-event-handler/ && \
	java -jar -Dspring.profiles.active=local,ide target/event-handler.jar com.personal.eventhandler.PersonalEventHandlerApplication.java
