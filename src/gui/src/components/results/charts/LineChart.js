import React from 'react';
import { Box } from '@chakra-ui/react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS } from 'chart.js/auto';

const LineChart = props => {
  const labels_cenas = props.data[0].xx;
  const datasets_cenas = props.data.map(element => ({
    label: 'Algorithm ' + element.algorithm,
    data: element.yy,
    borderColor: 'rgb(' + 50 * element.algorithm + ', 192, 192)',
    backgroundColor: 'rgb(' + 50 * element.algorithm + ', 192, 192)',
    fill: true,
  }));
  return (
    <Box w="700px">
      <Line
        data={{
          labels: labels_cenas,
          datasets: datasets_cenas,
        }}
      />
    </Box>
  );
};

export default LineChart;
