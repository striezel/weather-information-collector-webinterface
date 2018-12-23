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

  $connInfo = configuration::connectionInfo();
  if (empty($connInfo))
  {
    header('HTTP/1.0 500 Internal Server Error', true, 500);
    echo templatehelper::error("Could not get connection information!");
    die();
  }

  $database = new database($connInfo);
  $locations = array();
  if ($_GET['type'] === 'forecast')
    $locations = $database->locationsForecast();
  else
    $locations = $database->locations();
  if (empty($locations))
  {
    header('HTTP/1.0 500 Internal Server Error', true, 500);
    echo templatehelper::error("Could not get a list of locations!");
    die();
  }

  $tpl = new template();
  $tpl->fromFile(templatehelper::baseTemplatePath() . 'locations.tpl');
  $tpl->loadSection('locationItem');
  $items = '';
  foreach ($locations as $loc) {
    $tpl->tag('type', $_GET['type']);
    $tpl->tag('name', $loc['location']);
    $tpl->tag('locationId', $loc['locationId']);
    $ll = formatter::latLon($loc['latitude'], $loc['longitude']);
    $tpl->tag('lat', $ll['latitude']);
    $tpl->tag('lon', $ll['longitude']);
    $items .= $tpl->generate();
  }
  $tpl->loadSection('locationList');
  $tpl->integrate('items', $items);
  $content = $tpl->generate();

  $navItems = array(
    array('url' => './types.php', 'icon' => 'th-list', 'caption' => 'Weather type'),
    array('url' => './locations.php?type=' . $_GET['type'], 'active' => true, 'icon' => 'home', 'caption' => 'Locations')
  );
  $tpl = templatehelper::prepareMain($content, 'Cities', array(), $navItems);

  echo $tpl->generate();
?>
