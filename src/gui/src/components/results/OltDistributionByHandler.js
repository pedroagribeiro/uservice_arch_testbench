import React from 'react';
import { VStack, Heading } from '@chakra-ui/react';
import BarChart from './charts/BarChart';

const OltDistributionByHandler = props => {
  return (
    <VStack spacing={4}>
      <Heading size="md">OLT {props.olt}</Heading>
      <BarChart label={'Número de pedidos'} chartData={props.chartData} />
    </VStack>
  );
};

export default OltDistributionByHandler;
