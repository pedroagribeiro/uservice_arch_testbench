import React from 'react';
import { Box } from '@chakra-ui/react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS } from 'chart.js/auto';

const LineChart = props => {
  const algorithmColor = algorithm => {
    if (algorithm === 1) {
      return 'rgb(247, 23, 53)';
    }
    if (algorithm === 2) {
      return 'rgb(255, 159, 28)';
    }
    if (algorithm === 3) {
      return 'rgb(65, 234, 212)';
    }
    if (algorithm === 4) {
      return 'rgb(1, 22, 39)';
    }
  };

  const labels_cenas = props.data[0].xx;
  const datasets_cenas = props.data.map(element => ({
    label: 'Algorithm ' + element.algorithm,
    data: element.yy,
    borderColor: algorithmColor(element.algorithm),
    backgroundColor: algorithmColor(element.algorithm),
  }));
  return (
    <Box w="700px">
      <Line
        data={{
          labels: labels_cenas,
          datasets: datasets_cenas,
        }}
        options={{
          scales: {
            y: {
              beginAtZero: true,
            },
          },
        }}
      />
    </Box>
  );
};

export default LineChart;
