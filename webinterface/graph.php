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
include 'classes/simplegraph.php';

if (!isset($_GET['location']) || empty($_GET['location']))
{
  echo templatehelper::error("No location has been selected.");
  die();
}
$_GET['location'] = intval($_GET['location']);
if (!isset($_GET['api']) || empty($_GET['api']))
{
  echo templatehelper::error("No data source has been selected.");
  die();
}
$_GET['api'] = intval($_GET['api']);

$connInfo = configuration::connectionInfo();
if (empty($connInfo))
{
  header('HTTP/1.0 500 Internal Server Error', true, 500);
  echo templatehelper::error("Could not get connection information!");
  die();
}

$database = new database($connInfo);
$data = array();
$full = isset($_GET['full']);
if (!$full)
{
  $data = $database->weatherData($_GET['location'], $_GET['api'], 48);
}
else
{
  $data = $database->weatherData($_GET['location'], $_GET['api'], 0);
}
if (empty($data))
{
  header('HTTP/1.0 500 Internal Server Error', true, 500);
  echo templatehelper::error("Could not get weather data from the database!");
  die();
}
$location = $database->locationById($_GET['location']);
if (null == $location)
{
  header('HTTP/1.0 500 Internal Server Error', true, 500);
  echo templatehelper::error("Could not get location data from the database!");
  die();
}
$api = $database->apiById($_GET['api']);
if (null == $api)
{
  header('HTTP/1.0 500 Internal Server Error', true, 500);
  echo templatehelper::error("Could not get data source information from the database!");
  die();
}

$graph = null;
if (!$full)
{
  $graph = simplegraph::createWithGap($data, $location, $api);
}
else
{
  $graph = simplegraph::createWithGap($data, $location, $api, 'rangegraph');
}

$tpl = new template();
$tpl->fromFile(templatehelper::baseTemplatePath() . 'main.tpl');
$tpl->loadSection('back');
$tpl->tag('url', 'source.php?location=' . $_GET['location']);
$backButton = $tpl->generate();

$scripts = array('./libs/plotly/plotly.min.js');
$ll = formatter::latLon($location['latitude'], $location['longitude']);
$title = 'Weather of ' . $location['location'] . ' (' . $ll['latitude']
       . ', ' . $ll['longitude'] . ')';
if (!$full)
{
  $title .= ' for the last 48 hours';
}
$title .= ', data provided by ' . $api['name'];
$navItems = array(
  array('url' => './locations.php', 'icon' => 'home', 'caption' => 'Locations'),
  array(
    'url' => './source.php?location=' . $_GET['location'],
    'icon' => 'duplicate', 'caption' => 'Data sources'
  ),
  array(
    'url' => './graph.php?location=' . $_GET['location'] . '&api=' . $_GET['api'],
    'icon' => 'stats', 'caption' => 'Recent data', 'active' => !$full
  ),
  array(
    'url' => './graph.php?location=' . $_GET['location'] . '&api=' . $_GET['api'] . '&full=1',
    'icon' => 'stats', 'caption' => 'All data', 'active' => $full
  )
);
$tpl = templatehelper::prepareMain($graph . $backButton, $title, $scripts, $navItems);
echo $tpl->generate();
?>
