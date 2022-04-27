import React, { useState, useEffect } from 'react';
import {
  Box,
  Heading,
  Spinner,
  HStack,
  VStack,
  Divider,
  TableContainer,
  Table,
  Thead,
  Tbody,
  Tr,
  Td,
  Th,
  Flex,
  Grid,
} from '@chakra-ui/react';
import axios from 'axios';
import { useParams } from 'react-router';
import OltDistributionByHandler from './OltDistributionByHandler';

const SimulationResult = props => {
  const { id } = useParams();

  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const [oltDistribution, setOltDistribution] = useState(null);

  const url =
    'http://' + api_host + ':' + api_port + '/results/single?id=' + id;

  const url_distribution =
    'http://' +
    api_host +
    ':' +
    api_port +
    '/results/handler_distribution_by_olt/' +
    id;

  useEffect(() => {
    setLoading(true);
    axios
      .get(url)
      .then(response => {
        setResult(response.data);
      })
      .catch(err => setError(err));
    axios
      .get(url_distribution)
      .then(response => setOltDistribution(response.data))
      .catch(err => setError(err))
      .finally(() => setLoading(false));
  }, [url, url_distribution]);

  const generateArrayFromOltNumber = olts => {
    const array_number = Array.from({ length: olts }, (v, i) => i);
    return array_number.map(elem => JSON.stringify(elem));
  };

  if (loading) {
    return (
      <HStack spacing={4}>
        <Spinner size="xl" />
        <Heading>Loading</Heading>
      </HStack>
    );
  }

  if (error) {
    return <Heading>{JSON.stringify(error)}</Heading>;
  }

  return result && oltDistribution ? (
    <Box minH="86vh" borderWidth={2} rounded="md">
      <VStack mt={4} spacing={6} mb={6}>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg">Run identificator: {result.id}</Heading>
          <Divider />
        </VStack>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg" mt={8}>
            Defining parameters:
          </Heading>
          <Divider />
          <Flex w="100%" justifyContent="center">
            <TableContainer mt={4} maxW="30%">
              <Table variant="striped" colorScheme="gray" size="sm">
                <Thead>
                  <Tr>
                    <Th>Parameter</Th>
                    <Th isNumeric>Value</Th>
                  </Tr>
                </Thead>
                <Tbody>
                  <Tr>
                    <Td>Workers</Td>
                    <Td>{result.workers}</Td>
                  </Tr>
                  <Tr>
                    <Td>Olts</Td>
                    <Td>{result.olts}</Td>
                  </Tr>
                  <Tr>
                    <Td>Algorithm</Td>
                    <Td>{result.algorithm}</Td>
                  </Tr>
                  <Tr>
                    <Td>Sequence</Td>
                    <Td>{result.sequence}</Td>
                  </Tr>
                </Tbody>
              </Table>
            </TableContainer>
          </Flex>
        </VStack>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg" mt={8}>
            Run metrics:
          </Heading>
          <Divider />
          <Flex w="100%" justifyContent="center">
            <TableContainer mt={4} maxW="30%">
              <Table variant="striped" colorScheme="gray" size="sm">
                <Thead>
                  <Tr>
                    <Th>Parameter</Th>
                    <Th isNumeric>Value</Th>
                  </Tr>
                </Thead>
                <Tbody>
                  <Tr>
                    <Td>Theoretical Total Time</Td>
                    <Td>{result.theoretical_total_time_limit}</Td>
                  </Tr>
                  <Tr>
                    <Td>Verified Total Time</Td>
                    <Td>{result.verified_total_time}</Td>
                  </Tr>
                  <Tr>
                    <Td>Theoretical Timedout Requests Limit</Td>
                    <Td>{result.theoretical_timedout_requests_limit}</Td>
                  </Tr>
                  <Tr>
                    <Td>Verified Timedout Requests</Td>
                    <Td>{result.verified_timedout_requests}</Td>
                  </Tr>
                  <Tr>
                    <Td>Start Instant</Td>
                    <Td>{result.start_instant}</Td>
                  </Tr>
                  <Tr>
                    <Td>End Instant</Td>
                    <Td>{result.end_instant}</Td>
                  </Tr>
                </Tbody>
              </Table>
            </TableContainer>
          </Flex>
        </VStack>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg" mt={8}>
            OLT specific metrics:
          </Heading>
          <Divider />
          <Flex w="100%" justifyContent="center">
            <TableContainer mt={4} maxW="60%">
              <Table variant="striped" colorScheme="gray" size="sm">
                <Thead>
                  <Tr>
                    <Th>OLT</Th>
                    <Th isNumeric>Minimum Processing Time</Th>
                    <Th isNumeric>Maximum Processing Time</Th>
                    <Th isNumeric>Average Processing Time</Th>
                  </Tr>
                </Thead>
                <Tbody>
                  {result.per_olt_metrics.map(metric => (
                    <Tr>
                      <Td>{metric.olt}</Td>
                      <Td>{metric.minimumProcessingTime}</Td>
                      <Td>{metric.maximumProcessingTime}</Td>
                      <Td>{metric.averageProcessingTime}</Td>
                    </Tr>
                  ))}
                </Tbody>
              </Table>
            </TableContainer>
          </Flex>
        </VStack>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg" mt={8}>
            OLT distribution by worker:
          </Heading>
          <Divider />
          <VStack w="100%" spacing={4} alignItems="center">
            <Grid mt={6} templateColumns="repeat(3, 1fr)" gap={10}>
              {generateArrayFromOltNumber(result.olts).map(olt => (
                <OltDistributionByHandler
                  key={olt}
                  olt={olt}
                  chartData={oltDistribution[olt]}
                />
              ))}
            </Grid>
          </VStack>
        </VStack>
      </VStack>
    </Box>
  ) : (
    <HStack spacing={4}>
      <Spinner size="xl" />
      <Heading>Loading</Heading>
    </HStack>
  );
};

export default SimulationResult;
