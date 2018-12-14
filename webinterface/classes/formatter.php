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

class formatter
{
  /**
   * Formats latitude and longitude values more nicely.
   *
   * @param latitude  latitude value as floating point number
   * @param longitude longitude value as floating point number
   * @return array with keys 'latitude' and 'longitude', where the value is the
   * nicer format of the given values
   */
  public static function latLon($latitude, $longitude)
  {
    $result = array();
    if ($latitude > 0.0) $result['latitude'] = round($latitude, 2) . '°N';
    else $result['latitude'] = round(-1.0 * $latitude, 2) . '°S';
    if ($longitude > 0.0) $result['longitude'] = round($longitude, 2) . '°E';
    else $result['longitude'] = round(-1.0 * $longitude, 2) . '°W';
    return $result;
  }
}
?>
