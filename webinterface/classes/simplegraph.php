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

class simplegraph
{
  /**
   * Creates a simple weather data graph with temperature and humidity data.
   *
   * @param data  weather data array
   * @param location location data
   * @param api      API data
   * @return Returns a string containing the HTML code for the graph.
   */
  public static function create($data, $location, $api)
  {
    if (empty($data) || empty($location))
      return null;

    $dates = array();
    $temperature = array();
    $humidity = array();
    foreach ($data as $key => $value)
    {
      $dates[] = $value['dataTime'];
      $temperature[] = $value['temperature_C'];
      $humidity[] = $value['humidity'];
    }

    $ll = formatter::latLon($location['latitude'], $location['longitude']);

    $tpl = new template();
    $tpl->fromFile(templatehelper::baseTemplatePath() . 'graphs.tpl');
    $tpl->loadSection('simplegraph');
    $title = 'Weather of ' . $location['location'] . ' ('
           . $ll['latitude'] . ', ' . $ll['longitude'] . '), data by '
           . $api['name'];
    $tpl->tag('title', $title);
    $tpl->integrate('dates', json_encode($dates));
    $tpl->integrate('temperature', json_encode($temperature));
    $tpl->integrate('humidity', json_encode($humidity));
    return $tpl->generate();
  }
}
?>
