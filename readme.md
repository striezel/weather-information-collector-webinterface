# Webinterface for weather-information-collector

This is a webinterface for [weather-information-collector](https://gitlab.com/striezel/weather-information-collector).
It will visualize the data that was collected.

This is a work in progress and not completely finished yet.

## Installation

### Prerequisites

* PHP 5.6 or newer (PHP 5.4 might still work, but has not been tested.)
* MySQL extension for PHP
* a webserver (e.g. Apache, lighttpd, nginx,...)

Those can usually be installed by typing

    apt-get install php php-mysql lighttpd

into a terminal as root user.

### Configuration

A configuration file containing the database connection information for the
MySQL/MariaDB database has to be created in one of the following locations:

* `user's home directory`/.wic/wic.conf
* `user's home directory`/.weather-information-collector/wic.conf
* `user's home directory`/.weather-information-collector/weather-information-collector.conf
* /etc/weather-information-collector/weather-information-collector.conf
* /etc/weather-information-collector/wic.conf
* /etc/wic/wic.conf

In those lines above, `user's home directory` is the home directory of the user
that runs the PHP scripts on the webserver. On Debian-based systems that user is
usually `www-data` and has the home directory `/var/www`.

The format is the same as the [core configuration file](https://gitlab.com/striezel/weather-information-collector/blob/master/doc/configuration-core.md)
of the [weather-information-collector](https://gitlab.com/striezel/weather-information-collector/),
just that it does not need more than the database-related settings. Such a file
could have the following contents:

    db.host=localhost
    db.name=weather_information_collector
    db.user=db_user_name
    db.password=super-secret-password!
    db.port=3306

### Deployment

Place the content of the directory `webinterface/` anywhere inside the document
root of your webserver. It is usually located in `/var/www/html/` or a similar
directory. Just make sure you keep the directory structure.

The webinterface can then be accessed via
<http://your.server.domain/path/to/index.php>.

## Note

You do of course need a MySQL database server that can be accessed by the
webinterface, and the database should have been populated with some data by the
[weather-information-collector](https://gitlab.com/striezel/weather-information-collector).
That is the whole point of the webinterface - to show data that was gathered by
[weather-information-collector](https://gitlab.com/striezel/weather-information-collector).

## Copyright and Licensing

### Main code

Copyright 2018  Dirk Stolle

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

### Library code

The webinterface uses some libraries which can be found in the folder
`webinterface/libs/`. These libraries may have different licenses, but all of
them are compatible with the main code's license. Currently the following
libraries are in use:

* jQuery: MIT License
* Bootstrap: MIT License
* Plotly.js: MIT License
