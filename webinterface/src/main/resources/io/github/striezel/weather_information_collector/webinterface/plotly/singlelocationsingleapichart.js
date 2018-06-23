window.io_github_striezel_weather_information_collector_webinterface_plotly_SingleLocationSingleApiChart = function () {
    var newDiv = document.createElement("div");
    var attr = document.createAttribute('id');
    attr.value = 'slsac_plotly';
    newDiv.setAttributeNode(attr);
    var elem = this.getElement();
    elem.appendChild(newDiv);

    this.onStateChange = function() {
        var traces = [];
        var dates = this.getState().dates;
        var temperature = this.getState().temperature;
        var humidity = this.getState().humidity;

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
            title: this.getState().title || 'Weather data',
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

        Plotly.newPlot('slsac_plotly', traces, layout, {
            displaylogo: false,
            modeBarButtonsToRemove: ['sendDataToCloud']
        });
    }
}
