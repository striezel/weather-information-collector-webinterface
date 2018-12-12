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

  if (!isset($_GET['location']) || empty($_GET['location']))
  {
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
    $items .= $tpl->generate();
  }

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
  if ($location['latitude'] > 0.0) $tpl->tag('lat', $location['latitude'] . '°N');
  else $tpl->tag('lat', (-1.0*$location['latitude']) . '°S');
  if ($location['longitude'] > 0.0) $tpl->tag('lon', $location['longitude'] . '°E');
  else $tpl->tag('lon', (-1.0*$location['longitude']) . '°W');
  $content = $tpl->generate();

  $tpl = templatehelper::prepareMain($content, 'Data sources');

  echo $tpl->generate();
?>
