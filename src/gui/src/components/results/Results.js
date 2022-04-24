import React, { useState, useEffect } from 'react';
import {
  Box,
  Spinner,
  Heading,
  HStack,
  Button,
  Divider,
  VStack,
} from '@chakra-ui/react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import SimulationButtonBox from './SimulationButtonBox';

const Results = () => {
  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [result, setResults] = useState(null);

  const url = 'http://' + api_host + ':' + api_port + '/results';

  useEffect(() => {
    setLoading(true);
    axios
      .get(url)
      .then(response => {
        setResults(response.data.sort((a, b) => a.id - b.id));
      })
      .catch(err => setError(err))
      .finally(() => setLoading(false));
  }, [url]);

  if (loading) {
    return (
      <HStack spacing={4}>
        <Spinner size="xl" />
        <Heading>Loading</Heading>
      </HStack>
    );
  }

  return (
    <Box minH="86vh" borderWidth="1px" borderRadius="lg" py={6}>
      <VStack spacing={6} mx={6}>
        <HStack ml={8} spacing={8} justifyContent="start" h="50px">
          <Link to="/global_results">
            <Button colorScheme="teal">Global Graphics</Button>
          </Link>
          <Divider orientation="vertical" />
          <Heading size="md">
            These results concern the totality of the performed runs
          </Heading>
        </HStack>
        <Divider mx={8} />
        {result.map((r, i) => (
          <SimulationButtonBox key={i} {...r} />
        ))}
      </VStack>
    </Box>
  );
};

export default Results;
