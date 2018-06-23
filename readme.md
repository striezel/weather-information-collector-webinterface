# Webinterface for weather-information-collector

This will be a webinterface for [weather-information-collector](https://github.com/striezel/weather-information-collector).
It will visualize any data that was collected.

## Build status

### weather-information-collector

[![Build Status](https://travis-ci.org/striezel/weather-information-collector.svg?branch=master)](https://travis-ci.org/striezel/weather-information-collector)

### Webinterface

[![Build Status](https://travis-ci.org/striezel/weather-information-collector-webinterface.svg?branch=master)](https://travis-ci.org/striezel/weather-information-collector-webinterface)

## Setting up the application

### Native setup (Linux)

Besides the checkout of the source code you need some prerequisites to build
the application. It basically boils down to:

* JDK 8 or later (Both OpenJDK 8 or Oracle JDK 8 will do.)
* Maven (the build tool)
* MySQL Connector/J
* Jetty (or Tomcat, or Glassfish) server to deploy the generated *.war file

These can usually be installed by typing

    apt-get install openjdk-8-jdk maven libmysql-java jetty9

or

    yum install java-1.8.0-openjdk-headless maven mysql-connector-java jetty-runner

into a root terminal.

After the installation is through, type the following commands in the root
directory of the checked out source code repository:

    # change to project directory
    cd webinterface/
    # perform "install", i.e. create the whole application + *.war file
    mvn install
    # finally start the application via Jetty
    mvn jetty:run

The last command will fire up a Jetty instance with the webinterface. After that
the application can be accessed in a web browser via <http://localhost:8080>.

### Docker container

#### Preparation: Installation of Docker

If you prefer the non-native approach and like to use Docker instead, you have
to make sure that Docker is installed. For the purpose of this installation,
the readme will assume that you are using Debian 8.0 "jessie". The required
steps should be similar on other Linux distributions.

To install Docker, you need to add the APT repository for jessie-backports to
your APT sources, if that has not been done yet. Execute the following command
as root user to add the AP repository:

    echo "deb http://ftp.de.debian.org/debian/ jessie-backports main" >> /etc/apt/sources.list

After that you can install Docker by typing the following commands:

    # update of package lists
    apt-get update
    # install Docker package and dependencies without further confirmation
    apt-get install -y docker.io

The standard configuration right after the installation of Docker only allows
the root user to use Docker. Since it is more advisable to run Docker as a
different user, you need to add this other user (here: user1) to the group
docker. Type

    gpasswd -a user1 docker

to add the user _user1_ to the docker group. Repeat for other users, if needed.
Group membership will be applied _after the next login_ of the user, so these
users might need to log off and on again before they can continue.

Furthermore you should restart the Docker daemon:

    systemctl start docker

or

    systemctl restart docker

should trigger the restart.

After that the user _user1_ can type

    docker info

into a terminal in order to check whether he/she can execute docker commands in
his/her user context. If the command displays an error, then something is not
quite right yet.

#### Let's party: Build docker image and create container

The following commands have to be issued in the root directory of the checked
out source code:

    # create image - may take serveral minutes during the first run; runs after
    # that are faster, because Docker caches some data
    docker build -t wic-web .
    # start container based on that image and pass port 8080 through
    docker run -d -p 8080:8080 --net=host wic-web

After that the webinterface, a Vaadin application, can be accessed in a web
browser via <http://localhost:8080>. This may take a few seconds, because the
Jetty server needs a few moments to start.

## Known issues

Not really an issue, but rather by design:

You do of course need a MySQL database server that can be accessed by the
webinterface, and the database should have been populated with some data by the
[weather-information-collector](https://github.com/striezel/weather-information-collector).
That is the whole point of the webinterface - to show data that was gathered by
[weather-information-collector](https://github.com/striezel/weather-information-collector).

## Copyright and Licensing

Copyright 2017, 2018  Dirk Stolle

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
