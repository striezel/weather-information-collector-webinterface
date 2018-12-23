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
      connectgaps: false,
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
      connectgaps: false,
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

<!--section-start::rangegraph--><div id="rangegraph"> </div>
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
      connectgaps: false,
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
      connectgaps: false,
      line: {
          color: 'rgb(0, 0, 255)'
      }
  });

  var selectorOptions = {
    buttons: [
      {
        step: 'month',
        stepmode: 'backward',
        count: 1,
        label: '1m'
      },
      {
        step: 'year',
        stepmode: 'backward',
        count: 1,
        label: '1y'
      },
      {
        step: 'all',
        label: 'full'
      }]
  };

  var layout = {
      title: '{{title}}' || 'Weather data',
      yaxis: {
          title: 'Temperature [°C]',
          titlefont: {color: 'rgb(255, 0, 0)'},
          tickfont: {color: 'rgb(255, 0, 0)'},
      },
      xaxis: {
          rangeselector: selectorOptions,
          rangeslider: {}
      },
      yaxis2: {
          title: 'Relative humidity [%]',
          titlefont: {color: 'rgb(0, 0, 255)'},
          tickfont: {color: 'rgb(0, 0, 255)'},
          overlaying: 'y',
          side: 'right'
      }
  };

  Plotly.newPlot('rangegraph', traces, layout, {
      displaylogo: false,
      modeBarButtonsToRemove: ['sendDataToCloud']
  });
</script><!--section-end::rangegraph-->

<!--section-start::forecastgraph--><div id="forecastgraph"> </div>
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
      connectgaps: false,
      line: {
          color: 'rgb(255, 0, 0)',
          dash: 'dashdot'
      }
  });
  traces.push({
      x: dates,
      y: humidity,
      type: 'scatter',
      yaxis: 'y2',
      name: 'Humidity [%]',
      connectgaps: false,
      line: {
          color: 'rgb(0, 0, 255)',
          dash: 'dashdot'
      }
  });

  var layout = {
      title: '{{title}}' || 'Weather forecast data',
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

  Plotly.newPlot('forecastgraph', traces, layout, {
      displaylogo: false,
      modeBarButtonsToRemove: ['sendDataToCloud']
  });
</script><!--section-end::forecastgraph-->
