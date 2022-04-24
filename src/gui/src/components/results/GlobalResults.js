import React, { useState, useEffect } from 'react';
import {
  Box,
  Heading,
  Divider,
  VStack,
  HStack,
  Spinner,
  Text,
} from '@chakra-ui/react';
import axios from 'axios';
import SequenceResults from './SequenceResults';

const GlobalResults = () => {
  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const [sequenceOneData, setSequenceOneData] = useState(null);
  const [loadingSequenceOneData, setLoadingSequenceOneData] = useState(null);
  const [errorLoadingSequenceOneData, setErrorLoadingSequenceOneData] =
    useState(null);

  const url =
    'http://' +
    api_host +
    ':' +
    api_port +
    '/results/verified_time_graphic/sequence/1/workers/3/olts/5';

  useEffect(() => {
    setLoadingSequenceOneData(true);
    axios
      .get(url)
      .then(response => {
        setSequenceOneData(response.data);
      })
      .catch(err => setErrorLoadingSequenceOneData(err))
      .finally(() => setLoadingSequenceOneData(false));
  }, [url]);

  const allDataLoaded = () => {
    return !loadingSequenceOneData && sequenceOneData;
  };

  return sequenceOneData ? (
    <Box minH="86vh" borderWidth={2} rounded="md">
      <VStack mt={4} mb={6} spacing={6}>
        <VStack mx={4} alignItems="start" minW="97%">
          <Heading size="lg">Global Results</Heading>
          <Divider />
        </VStack>
      </VStack>
      <VStack w="100%" mt={4} mb={6} spacing={6}>
        <SequenceResults sequence={1} workers={3} olts={5} />
        <SequenceResults sequence={2} workers={3} olts={5} />
        <SequenceResults sequence={3} workers={3} olts={5} />
      </VStack>
    </Box>
  ) : (
    <HStack spacing={4}>
      <Spinner size="xl" />
      <Heading>Loading</Heading>
    </HStack>
  );
};

export default GlobalResults;
