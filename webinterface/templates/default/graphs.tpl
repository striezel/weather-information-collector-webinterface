<!--section-start::simplegraph--><div id="simplegraph"> </div>
<script>
  var traces = [];
  var dates = {{>dates}};
  var temperature = {{>temperature}};
  var humidity = {{>humidity}};

  traces.push({
      x: dates,
      y: temperature,
      type: 'scatter',
      name: 'Temperature [°C]',
      line: {
          color: 'rgb(255, 0, 0)'
      }
  });
  traces.push({
      x: dates,
      y: humidity,
      type: 'scatter',
      yaxis: 'y2',
      name: 'Humidity [%]',
      line: {
          color: 'rgb(0, 0, 255)'
      }
  });

  var layout = {
      title: '{{title}}' || 'Weather data',
      yaxis: {
          title: 'Temperature [°C]',
          titlefont: {color: 'rgb(255, 0, 0)'},
          tickfont: {color: 'rgb(255, 0, 0)'},
      },
      yaxis2: {
          title: 'Relative humidity [%]',
          titlefont: {color: 'rgb(0, 0, 255)'},
          tickfont: {color: 'rgb(0, 0, 255)'},
          overlaying: 'y',
          side: 'right'
      }
  };

  Plotly.newPlot('simplegraph', traces, layout, {
      displaylogo: false,
      modeBarButtonsToRemove: ['sendDataToCloud']
  });
</script><!--section-end::simplegraph-->
