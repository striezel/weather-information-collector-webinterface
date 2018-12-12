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
  include 'classes/templatehelper.php';
  include 'classes/database.php';

  $connInfo = configuration::connectionInfo();
  if (empty($connInfo))
  {
    header('HTTP/1.0 500 Internal Server Error', true, 500);
    echo templatehelper::error("Could not get connection information!");
    die();
  }

  $database = new database($connInfo);
  $locations = $database->locationsWithApi();
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
    $tpl->tag('name', $loc['location']);
    $tpl->tag('locationId', $loc['locationId']);
    if ($loc['latitude'] > 0.0) $tpl->tag('lat', $loc['latitude'] . '°N');
    else $tpl->tag('lat', (-1.0*$loc['latitude']) . '°S');
    if ($loc['longitude'] > 0.0) $tpl->tag('lon', $loc['longitude'] . '°E');
    else $tpl->tag('lon', (-1.0*$loc['longitude']) . '°W');
    $tpl->tag('api', $loc['api']);
    $tpl->tag('apiId', $loc['apiId']);
    $items .= $tpl->generate();
  }
  $tpl->loadSection('locationList');
  $tpl->integrate('items', $items);
  $content = $tpl->generate();

  $tpl = templatehelper::prepareMain($content, 'Cities');

  echo $tpl->generate();
?>
