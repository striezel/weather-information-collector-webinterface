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
   * Lists all named locations together with the APIs from which data for that
   * locations are present in the database.
   *
   * @return Returns an array of arrays containing location data.
   */
  public function locationsWithApi()
  {
    if (null === $this->pdo)
      return null;
    $sql = 'SELECT DISTINCT location.name AS locName, location.locationID AS locId, latitude, longitude, api.name AS apiName, api.apiID AS theApiId'
         . ' FROM weatherdata'
         . ' LEFT JOIN api ON weatherdata.apiID = api.apiID'
         . ' LEFT JOIN location ON location.locationID = weatherdata.locationID'
         . ' WHERE NOT ISNULL(location.name)'
         . '   AND NOT ISNULL(api.name)'
         . ' ORDER BY locName ASC, apiName ASC;';
    $stmt = $this->pdo->query($sql);
    $data = array();
    while (false !== ($row = $stmt->fetch(PDO::FETCH_ASSOC)))
    {
      $data[] = array(
        'location' => $row['locName'],
        'locationId' => $row['locId'],
        'latitude' => $row['latitude'],
        'longitude' => $row['longitude'],
        'api' => $row['apiName'],
        'apiId' => $row['theApiId']
      );
    }
    $stmt->closeCursor();
    unset($stmt);
    return $data;
  }
}
?>
