import React from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS } from 'chart.js/auto';

const BarChart = ({ label, chartData }) => {
  const labels = chartData.map(el => el.x);
  const datasets = [
    {
      label: label,
      data: chartData.map(el => el.y),
      borderColor: 'rgb(65, 234, 212)',
      backgroundColor: 'rgb(65, 234, 212)',
    },
  ];
  return (
    <Bar
      data={{
        labels: labels,
        datasets: datasets,
      }}
    />
  );
};

export default BarChart;
