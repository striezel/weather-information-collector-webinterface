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
  include 'classes/template.php';

  $base_tpl_path = __DIR__ . '/templates/default/';
  $tpl = new template();
  $tpl->fromFile($base_tpl_path . 'main.tpl');
  $tpl->loadSection('header');
  $tpl->tag('twbs_path', './libs/twbs/3.3.7');
  $tpl->tag('title', 'Work in progress');
  $header = $tpl->generate();

  $tpl->loadSection('navbar');
  $content = $tpl->generate();

  $tpl->loadSection('welcome');
  $content = $content . $tpl->generate();

  $tpl->loadSection('full');
  $tpl->include('header', $header);
  $tpl->include('content', $content);

  echo $tpl->generate();
?>
