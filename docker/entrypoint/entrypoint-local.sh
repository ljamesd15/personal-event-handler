#!/usr/bin/env bash
set -euo pipefail

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8000 -jar -Dspring.profiles.active=local ./event-handler.jar