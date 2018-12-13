<!--section-start::locationList--><div class="panel panel-default">
  <div class="panel-heading">Select a location</div>
  <div class="panel-body">If you are not sure, just try one.</div>
  <div class="list-group">
    {{>items}}
  </div>
</div><!--section-end::locationList-->

<!--section-start::locationItem-->
<a href="source.php?location={{locationId}}" class="list-group-item">{{name}} ({{lat}}, {{lon}})</a>
<!--section-end::locationItem-->

<!--section-start::sourceItem-->
<a class="list-group-item" href="#l{{locationId}}a{{apiId}}">{{name}}</a>
<!--section-end::sourceItem-->

<!--section-start::sourceList--><div class="panel panel-default">
  <div class="panel-heading">Select a data source for {{name}} ({{lat}}, {{lon}})</div>
  <div class="panel-body">If you are not sure, just try one.</div>
  <ul class="list-group">
    {{>items}}
  </ul>
</div>

<div class="btn-group btn-group-justified" role="group">
  <a href="locations.php" class="btn btn-info">
    <span class="glyphicon glyphicon-backward"> </span> Back
  </a>
</div><!--section-end::sourceList-->
