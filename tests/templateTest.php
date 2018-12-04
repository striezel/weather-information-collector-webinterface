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

// declare(strict_types=1);

use PHPUnit\Framework\TestCase;

final class TemplateTest extends TestCase
{
    public function testLoadTemplateFromFile()
    {
      $tpl = new template();
      $this->assertEquals(true, $tpl->fromFile('test.tpl'));
    }

    public function testLoadTemplateSections()
    {
      $tpl = new template();
      $this->assertEquals(true, $tpl->fromFile('test.tpl'));
      $this->assertEquals(true, $tpl->loadSection('uno'));
      $this->assertEquals(true, $tpl->loadSection('sec2'));
      $this->assertEquals(true, $tpl->loadSection('three_times'));
    }

    public function testGenerate1stSection()
    {
      $tpl = new template();
      $this->assertEquals(true, $tpl->fromFile('test.tpl'));
      $this->assertEquals(true, $tpl->loadSection('uno'));
      $generated = $tpl->generate();

      $this->assertEquals('There is no tag here.', $generated);
    }

    public function testGenerate2ndSection()
    {
      $tpl = new template();
      $this->assertEquals(true, $tpl->fromFile('test.tpl'));
      $this->assertEquals(true, $tpl->loadSection('sec2'));
      $tpl->tag('title', 'foobar');
      $generated = $tpl->generate();
      $expected = "<head>\n  <title>foobar</title>\n</head>";
      $this->assertEquals($expected, $generated);
    }

    public function testGenerate3rdSection()
    {
      $tpl = new template();
      $this->assertEquals(true, $tpl->fromFile('test.tpl'));
      $this->assertEquals(true, $tpl->loadSection('three_times'));
      $tpl->tag('a', 'Abba');
      $tpl->tag('b', 'Bangles');
      $generated = $tpl->generate();
      $expected = "<ul>\n"
        ."  <li>Abba<li>\n"
        ."  <li>Bangles<li>\n"
        ."  <li>Abba<li>\n"
        ."</ul>";
      $this->assertEquals($expected, $generated);
    }
}
?>
