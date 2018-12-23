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

  define('WIC_WEB_ROOT', __DIR__);

  include 'classes/templatehelper.php';
  include 'classes/database.php';
  include 'classes/formatter.php';

  if (!isset($_GET['type'])
    || (($_GET['type'] !== 'forecast') && ($_GET['type'] !== 'current')))
  {
    header('HTTP/1.0 400 Bad Request', true, 400);
    echo templatehelper::error("Missing or invalid weather data type!");
    die();
  }

  if (!isset($_GET['location']) || empty($_GET['location']))
  {
    header('HTTP/1.0 400 Bad Request', true, 400);
    echo templatehelper::error("No location has been selected.");
    die();
  }
  $_GET['location'] = intval($_GET['location']);

  $connInfo = configuration::connectionInfo();
  if (empty($connInfo))
  {
    header('HTTP/1.0 500 Internal Server Error', true, 500);
    echo templatehelper::error("Could not get connection information!");
    die();
  }

  $database = new database($connInfo);
  $apis = array();
  if ($_GET['type'] === 'forecast')
    $apis = $database->apisOfLocationForecast($_GET['location']);
  else
    $apis = $database->apisOfLocation($_GET['location']);
  if (empty($apis))
  {
    header('HTTP/1.0 500 Internal Server Error', true, 500);
    echo templatehelper::error("Could not get a list of data sources!");
    die();
  }

  $tpl = new template();
  $tpl->fromFile(templatehelper::baseTemplatePath() . 'locations.tpl');
  $tpl->loadSection('sourceItem');
  $items = '';
  foreach ($apis as $api) {
    $tpl->tag('name', $api['api']);
    $tpl->tag('apiId', $api['apiId']);
    $tpl->tag('locationId', $_GET['location']);
    $tpl->tag('type', $_GET['type']);
    $items .= $tpl->generate();
  }

  $location = array();
  if ($_GET['type'] === 'forecast')
    $location = $database->locationsForecast();
  else
    $location = $database->locations();
  function locationFilter($item)
  {
    return $item['locationId'] == $_GET['location'];
  }
  $location = array_filter($location, 'locationFilter');
  $location = array_shift($location);

  $tpl->loadSection('sourceList');
  $tpl->integrate('items', $items);
  $tpl->tag('name', $location['location']);
  $ll = formatter::latLon($location['latitude'], $location['longitude']);
  $tpl->tag('lat', $ll['latitude']);
  $tpl->tag('lon', $ll['longitude']);
  $tpl->tag('type', $_GET['type']);
  $content = $tpl->generate();

  $navItems = array(
    array('url' => './types.php', 'icon' => 'th-list', 'caption' => 'Weather type'),
    array('url' => './locations.php?type=' . $_GET['type'], 'icon' => 'home', 'caption' => 'Locations'),
    array(
      'url' => './source.php?location=' . $_GET['location'] . '&type=' . $_GET['type'],
      'icon' => 'duplicate', 'caption' => 'Data sources', 'active' => true
    )
  );
  $tpl = templatehelper::prepareMain($content, 'Data sources', array(), $navItems);

  echo $tpl->generate();
?>
