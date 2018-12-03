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

class template
{
  const TAG_OPENER = '{{';
  const TAG_CLOSER = '}}';
  const INC_OPENER = '{{>';
  const INC_CLOSER = '}}';

  private $sections;
  private $tags;
  private $template;

  public function __construct()
  {
    $this->sections = array();
    $this->tags = array();
    $this->template = null;
  }

  /**
   * Loads template from a file.
   *
   * @param fileName  name of the file to load
   * @return true, if template was loaded successfully; false otherwise
   */
  public function fromFile(string $fileName)
  {
    if (!is_readable($fileName))
      return false;
    $content = file_get_contents($fileName);
    if (($content === false) || ($content === ''))
      return false;
    $pattern = '/<!--section-start::(.*)-->(.*)<!--section-end::(\1)-->/Uis';
    $matches = array();
    $count = preg_match_all($pattern, $content, $matches);
    if (($count === false) || ($count === 0))
      return false;
    $this->sections = array();
    for($i = 0; $i < $count; ++$i)
    {
      $this->sections[$matches[1][$i]] = $matches[2][$i];
    } // for

    // Clear previously set tags and template, because they are not really valid
    // anymore.
    $this->tags = array();
    $this->template = null;
    return true;
  }

  /**
   * Loads a section of the template.
   *
   * @param sectionName  name of the section to load
   * @return true if section was loaded, false otherwise
   */
  public function loadSection($sectionName)
  {
    if (!array_key_exists($sectionName, $this->sections))
      return false;
    $this->template = $this->sections[$sectionName];
    return true;
  }

  /**
   * Sets the replacements text for a tag.
   *
   * @param name  name of the tag to replace
   * @param replacement the replacement
   */
  public function tag($name, $replacement)
  {
    $this->tags[$name] = $replacement;
  }

  /**
   * Generates the final template, ready for display.
   *
   * @return the template with all tag values filled
   */
  public function generate()
  {
    $out = $this->template;
    foreach ($this->tags as $tagName => $tagValue)
    {
      $out = str_replace(self::TAG_OPENER . $tagName . self::TAG_CLOSER,
               htmlentities($tagValue), $out);
    }
    return $out;
  }

  public function __toString()
  {
    return $this->generate();
  }
}

?>
