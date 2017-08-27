# This Dockerfile will set up a Debian 9-based container that is able to
# run the webinterface for weather-information-collector.
#
# Version 0.1
#
# History
# -------
#
# version 0.1 - initial version

# Use Debian 9 as base image.
FROM debian:9
MAINTAINER Dirk Stolle <striezel-dev@web.de>

# Packages should be up to date.
RUN apt-get update && apt-get upgrade -y

# install Git: required for SCM checkout of project
RUN apt-get install --no-install-recommends --no-install-suggests -y git
# install JDK: required during build
RUN apt-get install --no-install-recommends --no-install-suggests -y default-jdk
# install Maven (Java build system) + MySQL Connector/J (dependency)
RUN apt-get install --no-install-recommends --no-install-suggests -y maven libmysql-java
# install Jetty server, version 9
RUN apt-get install --no-install-recommends --no-install-suggests -y jetty9

# Create directory where application will reside.
RUN mkdir -p /opt/wic-web
# Clone Git repository into that directory.
RUN git clone https://github.com/striezel/weather-information-collector-webinterface.git /opt/wic-web

# Relevant files are in the webinterface/ subdirectory of the Git repository.
WORKDIR /opt/wic-web/webinterface
# Build Maven project.
RUN mvn install

# Expose port 8080 - that is the default port for Jetty.
EXPOSE 8080

# Start Jetty server.
CMD ["mvn", "jetty:run"]
