<?php
/*
 -------------------------------------------------------------------------------
    This file is part of the weather information collector webinterface.
    Copyright (C) 2018  Dirk Stolle

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
 -------------------------------------------------------------------------------
*/

require_once 'configuration.php';

class database
{
  private $pdo;

  /**
   * Constructs a new database object with the given connection information.
   *
   * @param connectionInformation array that contains the connection
   * information, e.g.
   *    array(
   *      'db.host' => 'localhost',
   *      'db.name' => 'weather_information_collector',
   *      'db.user' => 'db_user',
   *      'db.password' => 'secret',
   *      'db.port' => 3306
   *    );
   */
  public function __construct($connectionInformation)
  {
    $ci = array();
    if (!is_array($connectionInformation))
      $ci = configuration::connectionInfo();
    else
      $ci = $connectionInformation;
    // create PDO
    $dsn = 'mysql:host=' . $ci['db.host'] . ';port=' . intval($ci['db.port'])
          .';dbname=' . $ci['db.name'];
    try {
      $this->pdo = new PDO($dsn, $ci['db.user'], $ci['db.password']);
    } catch (PDOException $e) {
      $this->pdo = null;
      // Logging to LOG_USER, because that is the only one available on Windows
      // systems and we want to be cross-plattform.
      openlog('wic_web', LOG_CONS | LOG_PERROR | LOG_PID, LOG_USER);
      syslog(LOG_WARNING, 'Connection to database failed: ' . $e->getMessage());
      closelog();
    }
  }

  /**
   * Lists all named locations that have current weather data in the database.
   *
   * @return Returns an array of arrays containing location data.
   */
  public function locations()
  {
    if (null === $this->pdo)
      return null;
    $sql = 'SELECT DISTINCT location.name AS locName, location.locationID AS locId, latitude, longitude'
         . ' FROM weatherdata'
         . ' LEFT JOIN api ON weatherdata.apiID = api.apiID'
         . ' LEFT JOIN location ON location.locationID = weatherdata.locationID'
         . ' WHERE NOT ISNULL(location.name)'
         . '   AND NOT ISNULL(api.name)'
         . ' ORDER BY locName ASC;';
    $stmt = $this->pdo->query($sql);
    $data = array();
    while (false !== ($row = $stmt->fetch(PDO::FETCH_ASSOC)))
    {
      $data[] = array(
        'location' => $row['locName'],
        'locationId' => $row['locId'],
        'latitude' => $row['latitude'],
        'longitude' => $row['longitude']
      );
    }
    $stmt->closeCursor();
    unset($stmt);
    return $data;
  }

  /**
   * Gets information about a location by its ID.
   *
   * @param id  id of the location
   * @return array containing the location information
   */
  public function locationById($id)
  {
    if (null === $this->pdo)
      return null;
    $sql = 'SELECT location.name AS locName, location.locationID AS locId, latitude, longitude'
         . ' FROM location'
         . ' WHERE NOT ISNULL(location.name) AND locationID = ' . intval($id)
         . ' LIMIT 1;';
    $stmt = $this->pdo->query($sql);
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    unset($stmt);
    if ($row !== false)
    {
      $row['locationId'] = $row['locId'];
      $row['location'] = $row['locName'];
    }
    else
    {
      $row = null;
    }
    return $row;
  }

  /**
   * Gets information about an API by its ID.
   *
   * @param id  id of the API
   * @return array containing the API information
   */
  public function apiById($id)
  {
    if (null === $this->pdo)
      return null;
    $sql = 'SELECT * FROM api'
         . ' WHERE NOT ISNULL(api.name) AND apiID = ' . intval($id)
         . ' LIMIT 1;';
    $stmt = $this->pdo->query($sql);
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    unset($stmt);
    if ($row !== false)
    {
      $row['apiId'] = intval($id);
      $row['api'] = $row['name'];
    }
    else
    {
      $row = null;
    }
    return $row;
  }

  /**
   * Lists all APIs that have current weather data in the database for the given
   * location.
   *
   * @param locationId  id of the location
   * @return Returns an array of arrays containing location data.
   */
  public function apisOfLocation($locationId)
  {
    if (null === $this->pdo)
      return null;
    $locationId = intval($locationId);
    $sql = 'SELECT DISTINCT api.apiID AS theApiId, api.name AS apiName'
         . ' FROM api'
         . ' LEFT JOIN weatherdata ON weatherdata.apiID = api.apiID'
         . ' LEFT JOIN location ON location.locationID = weatherdata.locationID'
         . ' WHERE NOT ISNULL(location.locationID)'
         . '     AND location.locationID=' . $locationId
         . ' ORDER BY api.name ASC;';
    $stmt = $this->pdo->query($sql);
    $data = array();
    while (false !== ($row = $stmt->fetch(PDO::FETCH_ASSOC)))
    {
      $data[] = array(
        'api' => $row['apiName'],
        'apiId' => $row['theApiId']
      );
    }
    $stmt->closeCursor();
    unset($stmt);
    return $data;
  }

  /**
   * Lists all weather data in the database for the given location and API.
   *
   * @param locationId  id of the location
   * @param apiId       id of the API
   * @param hours       time span in hours to get the data for
   * @return Returns an array of arrays containing location data.
   */
  public function weatherData($locationId, $apiId, $hours = 1)
  {
    if (null === $this->pdo)
      return null;
    $locationId = intval($locationId);
    $apiId = intval($apiId);
    $sql = 'SELECT MAX(dataTime) AS mdt FROM weatherdata'
         . " WHERE locationID = '" . $locationId . "' AND apiID = '". $apiId ."';";
    $stmt = $this->pdo->query($sql);
    $maxDataTime = $stmt->fetch(PDO::FETCH_ASSOC);
    $maxDataTime = $maxDataTime['mdt'];
    if ($maxDataTime == null)
      return null;
    $hours = intval($hours);
    if ($hours < 1)
      $hours = 1;
    $data = array();
    $sql = 'SELECT DISTINCT dataTime, temperature_C, temperature_F, temperature_K, humidity, rain, pressure FROM weatherdata'
         . " WHERE locationID = '" . $locationId . "' AND apiID = '". $apiId ."'"
         . " AND dataTime > DATE_SUB('".$maxDataTime."', INTERVAL ".$hours.' HOUR)'
         . ' ORDER BY dataTime ASC;';
    $stmt = $this->pdo->query($sql);
    while (false !== ($row = $stmt->fetch(PDO::FETCH_ASSOC)))
    {
      $data[] = $row;
    }
    $stmt->closeCursor();
    unset($stmt);
    return $data;
  }
}
?>
