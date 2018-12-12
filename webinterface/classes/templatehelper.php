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

require_once 'template.php';

class templatehelper
{
  /**
   * Gets the base path for templates. Includes a slash at the end.
   *
   * @return path of template directory
   */
  public static function baseTemplatePath()
  {
    return WIC_WEB_ROOT . '/templates/default/';
  }
  /**
   * Prepares the main template with everything but the main content.
   *
   * @param content  content to include
   * @param title    the title of the page
   * @return true, if template was loaded successfully; false otherwise
   */
  public static function prepareMain($content = '', $title = 'No title')
  {
    $tpl = new template();
    $tpl->fromFile(templatehelper::baseTemplatePath() . 'main.tpl');
    $tpl->loadSection('header');
    $tpl->tag('twbs_path', './libs/twbs/3.3.7');
    $tpl->tag('jquery_path', './libs/jquery');
    $tpl->tag('title', $title);
    $header = $tpl->generate();

    $tpl->loadSection('navbar');
    $navbar = $tpl->generate();

    $tpl->loadSection('full');
    $tpl->integrate('header', $header);
    $tpl->integrate('navbar', $navbar);
    $tpl->integrate('content', $content);

    return $tpl;
  }

  /**
   * Generates the output for an error page.
   *
   * @param message the error message
   * @return Returns a fully generated error page with the given message.
   */
  public static function error($message = '')
  {
    $tpl = new template();
    $tpl->fromFile(templatehelper::baseTemplatePath() . 'main.tpl');
    $tpl->loadSection('error');
    $tpl->tag('message', $message);
    $content = $tpl->generate();

    $tpl = templatehelper::prepareMain($content, "Oops, something went wrong!");
    return $tpl->generate();
  }
}
?>
