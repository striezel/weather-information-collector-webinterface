<!--section-start::simplegraph--><div id="simplegraph"> </div>
<script>
  var traces = [];
  var dates = {{>dates}};
  var temperature = {{>temperature}};
  var humidity = {{>humidity}};
  var rain = {{>rain}};
  var snow = {{>snow}};

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
  traces.push({
      x: dates,
      y: rain,
      type: 'bar',
      yaxis: 'y3',
      name: 'Rain [mm/m²]',
      connectgaps: false,
      marker: {color: 'rgb(128, 128, 255)'}
  });
  traces.push({
      x: dates,
      y: snow,
      type: 'bar',
      yaxis: 'y3',
      name: 'Snow [mm/m²]',
      connectgaps: false,
      marker: {color: 'rgb(128, 128, 128)'}
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
          anchor: 'x',
          side: 'right'
      },
      yaxis3: {
          title: 'Precipitation [mm/m²]',
          titlefont: {color: 'rgb(128, 128, 255)'},
          tickfont: {color: 'rgb(128, 128, 255)'},
          overlaying: 'y',
          side: 'right',
          anchor: 'free',
          position: 0.9
      },
      xaxis: {domain: [0.1, 0.8]},
      barmode: 'stack'
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
  var rain = {{>rain}};
  var snow = {{>snow}};

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
  traces.push({
      x: dates,
      y: rain,
      type: 'bar',
      yaxis: 'y3',
      name: 'Rain [mm/m²]',
      connectgaps: false,
      marker: {color: 'rgb(128, 128, 255)'}
  });
  traces.push({
      x: dates,
      y: snow,
      type: 'bar',
      yaxis: 'y3',
      name: 'Snow [mm/m²]',
      connectgaps: false,
      marker: {color: 'rgb(128, 128, 128)'}
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
          anchor: 'x',
          side: 'right'
      },
      yaxis3: {
          title: 'Precipitation [mm/m²]',
          titlefont: {color: 'rgb(128, 128, 255)'},
          tickfont: {color: 'rgb(128, 128, 255)'},
          overlaying: 'y',
          side: 'right',
          anchor: 'free',
          position: 0.9
      },
      xaxis: {domain: [0.1, 0.8]},
      barmode: 'stack'
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
  var rain = {{>rain}};
  var snow = {{>snow}};

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
  traces.push({
      x: dates,
      y: rain,
      type: 'bar',
      yaxis: 'y3',
      name: 'Rain [mm/m²]',
      connectgaps: false,
      marker: {color: 'rgb(128, 128, 255)'}
  });
  traces.push({
      x: dates,
      y: snow,
      type: 'bar',
      yaxis: 'y3',
      name: 'Snow [mm/m²]',
      connectgaps: false,
      marker: {color: 'rgb(128, 128, 128)'}
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
          anchor: 'x',
          side: 'right'
      },
      yaxis3: {
          title: 'Precipitation [mm/m²]',
          titlefont: {color: 'rgb(128, 128, 255)'},
          tickfont: {color: 'rgb(128, 128, 255)'},
          overlaying: 'y',
          side: 'right',
          anchor: 'free',
          position: 0.9
      },
      xaxis: {domain: [0.1, 0.8]},
      barmode: 'stack'
  };

  Plotly.newPlot('forecastgraph', traces, layout, {
      displaylogo: false,
      modeBarButtonsToRemove: ['sendDataToCloud']
  });
</script><!--section-end::forecastgraph-->
