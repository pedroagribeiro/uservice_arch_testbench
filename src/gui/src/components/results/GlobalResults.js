import React, { useState, useEffect } from 'react';
import {
  Box,
  Heading,
  Divider,
  VStack,
  HStack,
  Spinner,
  Center,
} from '@chakra-ui/react';
import axios from 'axios';
import MultipleSequenceResults from './MultipleSequenceResults';
import LineChart from './charts/LineChart';

const GlobalResults = () => {
  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const [combinations, setCombinations] = useState([]);
  const [sequences, setSequences] = useState([]);
  const [verifiedTimesWorkersComparison, setVerifiedTimesWorkersComparison] =
    useState({});
  const [
    verifiedTimeoutsWorkersComparison,
    setVerifiedTimeoutsWorkersComparison,
  ] = useState({});

  const [loadingCombinations, setLoadingCombinations] = useState(false);
  const [loadingSequences, setLoadingSequences] = useState(false);
  const [loadingTotalTimeComparison, setLoadingTotalTimeComparison] =
    useState(false);
  const [loadingTotalTimeoutsComparison, setLoadingTotalTimeoutsComparsion] =
    useState(false);

  const url_combinations =
    'http://' + api_host + ':' + api_port + '/results/combinations';

  const url_sequences =
    'http://' + api_host + ':' + api_port + '/results/sequences';

  const url_total_time_workers_comparison =
    'http://' +
    api_host +
    ':' +
    api_port +
    '/results/total_verified_time_workers_comparison';

  const url_total_timeouts_workers_comparison =
    'http://' +
    api_host +
    ':' +
    api_port +
    '/results/total_verified_timeouts_workers_comparison';

  useEffect(() => {
    setLoadingSequences(true);
    axios
      .get(url_sequences)
      .then(response => {
        setSequences(response.data);
      })
      .catch(err => console.log(err))
      .finally(() => setLoadingSequences(false));
    setLoadingCombinations(true);
    axios
      .get(url_combinations)
      .then(response => {
        console.log(response.data);
        setCombinations(response.data);
      })
      .catch(err => console.log(err))
      .finally(() => setLoadingCombinations(false));
    setLoadingTotalTimeComparison(true);
    axios
      .get(url_total_time_workers_comparison)
      .then(response => {
        console.log(response.data);
        setVerifiedTimesWorkersComparison(response.data);
      })
      .catch(err => console.log(err))
      .finally(() => setLoadingTotalTimeComparison(false));
    setLoadingTotalTimeoutsComparsion(true);
    axios.get(url_total_timeouts_workers_comparison).then(response => {
      console.log(response.data);
      setVerifiedTimeoutsWorkersComparison(response.data);
    });
  }, [
    url_sequences,
    url_combinations,
    url_total_time_workers_comparison,
    url_total_timeouts_workers_comparison,
  ]);

  const allDataLoaded = () => {
    return (
      !loadingSequences &&
      !loadingCombinations &&
      !loadingTotalTimeComparison &&
      !loadingTotalTimeoutsComparison &&
      sequences.length !== 0 &&
      combinations.length !== 0
    );
  };

  const transform_total_data = data => {
    const r = [
      { algorithm: 1, xx: [], yy: [] },
      { algorithm: 2, xx: [], yy: [] },
      { algorithm: 3, xx: [], yy: [] },
      { algorithm: 4, xx: [], yy: [] },
    ];
    for (const [key, value] of Object.entries(data)) {
      value.forEach((v, i) => {
        r[i].xx.push(parseInt(key));
        r[i].yy.push(value[i].y);
      });
    }
    return r;
  };

  return allDataLoaded ? (
    <Box minH="86vh" borderWidth={2} rounded="md">
      <VStack mt={4} mb={6} spacing={6}>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg">Global Results</Heading>
          <Divider />
        </VStack>
      </VStack>
      <VStack mt={4} mb={6} spacing={6}>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg">
            Total time & timeouts comparison by workers
          </Heading>
          <Divider />
          <HStack w="100%" justifyContent="space-around">
            <VStack justifyContent="center" spacing={4}>
              <Heading size="sm">Verified total time</Heading>
              <LineChart
                data={transform_total_data(verifiedTimesWorkersComparison)}
              />
            </VStack>
            <VStack>
              <Heading size="sm">Verified timedout provisions</Heading>
              <LineChart
                data={transform_total_data(verifiedTimeoutsWorkersComparison)}
              />
            </VStack>
          </HStack>
        </VStack>
      </VStack>
      <VStack mt={4} mb={6} spacing={6}>
        {sequences.map(seq => (
          <MultipleSequenceResults
            key={seq}
            sequence={seq}
            combinations={combinations}
          />
        ))}
      </VStack>
    </Box>
  ) : (
    <Center>
      <HStack spacing={4}>
        <Spinner size="xl" />
        <Heading>Loading</Heading>
      </HStack>
    </Center>
  );
};

export default GlobalResults;
