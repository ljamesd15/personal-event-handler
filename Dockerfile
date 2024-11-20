FROM amazoncorretto:21-alpine

# Debug port
EXPOSE 8011
# Server port
EXPOSE 8081

COPY --chown=appuser:appuser docker/entrypoint/entrypoint.sh /home/appuser/bin/
COPY ./target /app
WORKDIR /app
ENTRYPOINT ["bash", "/home/appuser/bin/entrypoint.sh"]