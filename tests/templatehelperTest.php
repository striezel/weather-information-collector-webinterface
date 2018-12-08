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

define ('WIC_WEB_ROOT', '../webinterface');

use PHPUnit\Framework\TestCase;

final class TemplateHelperTest extends TestCase
{
    public function testBaseTemplatePath()
    {
      $path = templatehelper::baseTemplatePath();
      // Template path must not be empty.
      $this->assertFalse(empty($path));
      // Template path should end with 'templates/default/'.
      $this->assertStringEndsWith('templates/default/', $path);
      // Template directory should exist.
      $this->assertDirectoryExists($path);
      // Directory should contain a readable main.tpl.
      $this->assertFileExists($path . 'main.tpl');
      $this->assertFileIsReadable($path . 'main.tpl');
    }

    public function testPrepareMain()
    {
      $tpl = templatehelper::prepareMain('unit test', "some title");
      // Template must not be null.
      $this->assertFalse($tpl == null);
      // Template should be an instance of template class.
      $this->assertTrue($tpl instanceof template);

      // Now check the generated template.
      $code = $tpl->generate();
      $this->assertTrue(strpos($code, '<title>some title</title>') !== false);
      $this->assertTrue(strpos($code, '<title>some title</title>') > 0);
      $this->assertTrue(strpos($code, 'unit test') !== false);
      $this->assertTrue(strpos($code, 'unit test') > 0);
    }
}
?>
