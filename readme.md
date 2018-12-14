# Webinterface for weather-information-collector

This will be a webinterface for [weather-information-collector](https://github.com/striezel/weather-information-collector).
It will visualize any data that was collected.

This is a work in progress and not finished yet.

## Known issues

Not really an issue, but rather by design:

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
