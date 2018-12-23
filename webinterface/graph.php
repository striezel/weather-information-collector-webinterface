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
if (!isset($_GET['api']) || empty($_GET['api']))
{
  header('HTTP/1.0 400 Bad Request', true, 400);
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
if ($_GET['type'] === 'current')
{
  // Get current & past weather data.
  if (!$full)
  {
    // ... for the latest 48 hours only.
    $data = $database->weatherData($_GET['location'], $_GET['api'], 48);
  }
  else
  {
    // ... for all the time.
    $data = $database->weatherData($_GET['location'], $_GET['api'], 0);
  }
}
else
{
  // Get latest forecast data.
  $data = $database->forecastData($_GET['location'], $_GET['api']);
}
if ($data === null)
{
  header('HTTP/1.0 500 Internal Server Error', true, 500);
  echo templatehelper::error("Could not get weather data from the database!");
  die();
}
if (empty($data))
{
  header('HTTP/1.0 204 No Content', true, 204);
  echo templatehelper::error("There is no for the selected type, location and source!");
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

$ll = formatter::latLon($location['latitude'], $location['longitude']);
$pageTitle = 'No title';
$graphTitle = '';
if ($_GET['type'] === 'current')
{
  $pageTitle = 'Weather of ' . $location['location'] . ' (' . $ll['latitude']
         . ', ' . $ll['longitude'] . ')';
  $graphTitle = $pageTitle;
  if (!$full)
  {
    $pageTitle .= ' for the last 48 hours';
    $graphTitle .= ', last 48h only';
  }
  $pageTitle .= ', data provided by ' . $api['name'];
  $graphTitle .= ', data by ' . $api['name'];
}
else
{
  $pageTitle = 'Weather forecast for ' . $location['location'] . ' (' . $ll['latitude']
         . ', ' . $ll['longitude'] . '), data provided by ' . $api['name'];
  $graphTitle = 'Forecast for ' . $location['location'] . ' (' . $ll['latitude']
         . ', ' . $ll['longitude'] . '), data by ' . $api['name'];
}

$graph = null;
if ($_GET['type'] === 'current')
{
  if (!$full)
  {
    $graph = simplegraph::createWithGap($data, $location, $api, 'simplegraph', $graphTitle);
  }
  else
  {
    $graph = simplegraph::createWithGap($data, $location, $api, 'rangegraph', $graphTitle);
  }
}
else
{
  $graph = simplegraph::createWithGap($data, $location, $api, 'forecastgraph', $graphTitle);
}

$tpl = new template();
$tpl->fromFile(templatehelper::baseTemplatePath() . 'main.tpl');
$tpl->loadSection('back');
$tpl->tag('url', 'source.php?location=' . $_GET['location'] . '&type=' . $_GET['type']);
$backButton = $tpl->generate();

$scripts = array('./libs/plotly/plotly.min.js');
$navItems = array(
  array('url' => './types.php', 'icon' => 'th-list', 'caption' => 'Weather type'),
  array('url' => './locations.php?type=' . $_GET['type'], 'icon' => 'home', 'caption' => 'Locations'),
  array(
    'url' => './source.php?location=' . $_GET['location'] . '&type=' . $_GET['type'],
    'icon' => 'duplicate', 'caption' => 'Data sources'
  ),
  array(
    'url' => './graph.php?location=' . $_GET['location'] . '&api=' . $_GET['api']. '&type=' . $_GET['type'],
    'icon' => 'stats', 'caption' => 'Recent data', 'active' => !$full
  )
);
if ($_GET['type'] === 'current')
{
  $navItems[] = array(
    'url' => './graph.php?location=' . $_GET['location'] . '&api=' . $_GET['api'] . '&type=' . $_GET['type'] . '&full=1',
    'icon' => 'stats', 'caption' => 'All data', 'active' => $full
  );
}

$tpl = templatehelper::prepareMain($graph . $backButton, $pageTitle, $scripts, $navItems);
echo $tpl->generate();
?>
