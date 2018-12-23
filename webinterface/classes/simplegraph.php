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
   * Data gaps larger than 6 hours (+15 seconds) will be shown as visible gaps
   * in the plot.
   *
   * @param data       weather data array
   * @param location   location data
   * @param api        API data
   * @param tplSection section to load from the graphs.tpl file
   * @return Returns a string containing the HTML code for the graph.
   */
  public static function createWithGap($data, $location, $api, $tplSection = 'simplegraph')
  {
    if (empty($data) || empty($location) || empty($tplSection))
      return null;

    $dates = array();
    $temperature = array();
    $humidity = array();
    $prevTimeStamp = intval($data[0]['dt_ts']);
    foreach ($data as $key => $value)
    {
      // Six hours are 21600 seconds, and we add 15 seconds tolerance.
      if ($prevTimeStamp + 21615 < $value['dt_ts'])
      {
        // Time in the middle between those two times ("interTime"):
        // Two divisions instead of adding first and dividing then to avoid
        // overflow on systems with 32 bit signed integers. (Such systems are
        // Windows before PHP 7, as well as any 32 bit OS.)
        // Furthermore, there is no integer division in PHP, so we use intval().
        $interTime = intval($prevTimeStamp / 2 + intval($value['dt_ts']) / 2);
        $dates[] = strftime('%Y-%m-%d %H:%M:%S', $interTime);
        $temperature[] = null;
        $humidity[] = null;
      }
      $prevTimeStamp = intval($value['dt_ts']);
      $dates[] = $value['dataTime'];
      $temperature[] = $value['temperature_C'];
      $humidity[] = $value['humidity'];
    }

    $ll = formatter::latLon($location['latitude'], $location['longitude']);

    $tpl = new template();
    $tpl->fromFile(templatehelper::baseTemplatePath() . 'graphs.tpl');
    $tpl->loadSection($tplSection);
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
