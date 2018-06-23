window.io_github_striezel_weather_information_collector_webinterface_plotly_MultiTimeSeriesChartComponent = function () {
    var newDiv = document.createElement("div");
    var attr = document.createAttribute('id');
    attr.value = 'mtscc_plotly';
    newDiv.setAttributeNode(attr);
    var elem = this.getElement();
    elem.appendChild(newDiv);

    this.onStateChange = function() {
        var traces = [];
        var dates = this.getState().dates;
        var labels = this.getState().names;
        var values = this.getState().values;

        for (var i = 0; i < values.length; ++i)
        {
            traces.push({
                x: dates,
                y: values[i],
                type: 'scatter',
                name: labels[i] || 'Series ' + i.toString()
            });
        }

        var layout = {
            title: this.getState().title || 'Time series'
        };

        Plotly.newPlot('mtscc_plotly', traces, layout, {
            displaylogo: false,
            modeBarButtonsToRemove: ['sendDataToCloud']
        });
    }
}
