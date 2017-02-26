var anomalous = {
  x: [1, 2, 3, 4],
  y: [10, 15, 13, 17],
  mode: 'markers',
  type: 'scatter',
  name: 'Anomalous'
};

var normal = {
  x: [2, 3, 4, 5],
  y: [16, 5, 11, 9],
  mode: 'lines',
  type: 'scatter',
  name: 'Normal'
};
/*
var trace3 = {
x: [1, 2, 3, 4],
y: [12, 9, 15, 12],
mode: 'lines+markers',
type: 'scatter'
};
*/
var data = [normal, anomalous];

var layout = {
  margin: {
    l: 25,
    r: 10,
    b: 25,
    t: 10,
    pad: 4
  },
  paper_bgcolor: 'transparent',
  plot_bgcolor: '#494949',
  font: {
    family: 'Ubuntu, sans-serif',
    color: '#fff'
  },
  xaxis: {
    gridcolor: '#5E5E5E'
  },
  yaxis: {
    gridcolor: '#5E5E5E'
  }
};

Plotly.newPlot('tradeDataGraphPlotly', data, layout);

document.getElementsByClassName('modebar')[0].style.display = "none";