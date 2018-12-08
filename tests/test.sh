#!/bin/bash
#
# Bash script to run the PHPUnit tests
# (C) 2018  Dirk Stolle
#
# License: GPL v3+

phpunit --version
if [[ $? -ne 0 ]]
then
  echo "PHPUnit is either not installed or not in the current path."
  echo "Please fix that by installing PHPUnit or adjustung the PATH environment"
  echo "variable."
  exit 1
fi

# Run PHPUnit on all *Test.php files in the current directory.
phpunit --bootstrap autoload.php ./
