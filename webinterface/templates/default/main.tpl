<!--section-start::header-->  <head>
    <title>{{title}}</title>
    <script src="{{jquery_path}}/jquery.min.js"></script>
    <link href="{{twbs_path}}/css/bootstrap.min.css" rel="stylesheet">
    <script src="{{twbs_path}}/js/bootstrap.min.js"></script>
    {{>scripts}}
  </head><!--section-end::header-->

<!--section-start::script--><script src="{{path}}"></script><!--section-end::script-->

<!--section-start::full--><!DOCTYPE html>
<html>
{{>header}}
  <body>
    {{>navbar}}
    <div class="container">
    {{>content}}
    </div>
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
        <li class="active">
          <a href="./locations.php">
            <span class="glyphicon glyphicon-home"> </span> Locations
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

<!--section-start::error--><div class="alert alert-danger" align="justify" role="alert">
    <strong>An error occurred!</strong>
    <br /><br />
    {{message}}
</div><!--section-end::error-->

<!--section-start::back--><div class="btn-group btn-group-justified" role="group">
  <a href="{{url}}" class="btn btn-info">
    <span class="glyphicon glyphicon-backward"> </span> Back
  </a>
</div><!--section-end::back-->
