<!--section-start::header-->  <head>
    <title>{{title}}</title>
    <script src="{{jquery_path}}/jquery.min.js"></script>
    <link href="{{twbs_path}}/css/bootstrap.min.css" rel="stylesheet">
    <script src="{{twbs_path}}/js/bootstrap.min.js"></script>
  </head><!--section-end::header-->

<!--section-start::full--><!DOCTYPE html>
<html>
{{>header}}
  <body>
    {{>navbar}}
    {{>content}}
  </body>
</html><!--section-end::full-->

<!--section-start::navbar-->
<nav class="navbar navbar-default navbar-static-top">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-navbar-collapse-intro" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <span class="navbar-brand">
        <span class="glyphicon glyphicon-cloud"> </span>
        <strong>Weather information collector</strong>
      </span>
    </div>

    <div class="collapse navbar-collapse" id="bs-navbar-collapse-intro">
      <ul class="nav navbar-nav">
        <li>
          <a href="#" id="intro">
            <span class="glyphicon glyphicon-question-sign"> </span> Introduction
          </a>
        </li>
        <li class="active">
          <a href="./locations.php">
            <span class="glyphicon glyphicon-home"> </span> Cities
          </a>
        </li>
        <li>
          <a href="#">
            <span class="glyphicon glyphicon-transfer"> </span> Test
          </a>
        </li>
      </ul>
    </div>
  </div>
</nav>
<!--section-end::navbar-->

<!--section-start::welcome--><div class="col-xs-12" align="justify">
    Welcome!
    <br /><br />
    This application is not implemented yet. Come back later.
</div><!--section-end::welcome-->
