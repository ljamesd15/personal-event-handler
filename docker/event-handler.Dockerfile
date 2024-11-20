FROM amazoncorretto:21-alpine

RUN apk update \
    && apk upgrade \
    && apk add --no-cache bash \
    && apk add iputils-ping

# Debug port
EXPOSE 8011
# Server port
EXPOSE 8081

COPY --chown=appuser:appuser docker/entrypoint/entrypoint-local.sh /home/appuser/bin/
COPY ../target /app
WORKDIR /app
ENTRYPOINT ["bash", "/home/appuser/bin/entrypoint-local.sh"]