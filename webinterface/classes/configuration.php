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

  class configuration
  {
    /**
     * Gets a list of potential configuration file names.
     *
     * @return Returns a list of potential configuration file names.
     */
    public static function potentialFileNames()
    {
      $result = array(
        "/etc/weather-information-collector/weather-information-collector.conf",
        "/etc/weather-information-collector/wic.conf",
        "/etc/wic/wic.conf",
        "./weather-information-collector.conf",
        "./wic.conf"
      );
      $info = posix_getpwuid(posix_getuid());
      if (isset($info['dir']))
      {
        $result = array_merge(array(
          $info['dir'] . '/.wic/wic.conf',
          $info['dir'] . '/.weather-information-collector/wic.conf',
          $info['dir'] . '/.weather-information-collector/weather-information-collector.conf'
        ), $result);
      }
      return $result;
    }

    /**
     * Gets database connection information from a configuration file.
     *
     * @param filename  name of the configuration file
     * @return Returns an array that contains the relevant database information.
     *         Returns an empty array, if the file was not found and also none
     *         of the usual locations contained a configuration file.
     */
    public static function connectionInfo($filename = '')
    {
      $files = array_merge(array($filename), configuration::potentialFileNames());
      $data = array();
      foreach ($files as $file) {
        if (file_exists($file))
        {
          $data = parse_ini_file($file);
          break;
        }
      } // foreach

      // Remove all keys that are not related to the database.
      $keys = array_keys($data);
      foreach ($keys as $keyName)
      {
        if (($keyName !== 'db.host') && ($keyName !== 'db.port')
          && ($keyName !== 'db.user') && ($keyName !== 'db.password')
          && ($keyName !== 'db.name'))
        {
          unset($data[$keyName]);
        }
      } // foreach

      return $data;
    }
  }
?>
