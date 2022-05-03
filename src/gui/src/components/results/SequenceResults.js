import React, { useState, useEffect } from 'react';
import {
  VStack,
  HStack,
  Heading,
  Divider,
  Spinner,
  Flex,
} from '@chakra-ui/react';
import axios from 'axios';
import LineChart from './charts/LineChart';

const SequenceResults = props => {
  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const [verifiedTime, setVerifiedTime] = useState(null);
  const [loadingVerifiedTime, setLoadingVerifiedTime] = useState(false);

  const [verifiedTimeouts, setVerifiedTimeouts] = useState(null);
  const [loadingVerifiedTimeouts, setLoadingVerifiedTimeouts] = useState(false);

  const url_verified_time =
    'http://' +
    api_host +
    ':' +
    api_port +
    `/results/verified_time_graphic/sequence/${props.sequence}/workers/${props.workers}/olts/${props.olts}`;

  const url_verified_timedout =
    'http://' +
    api_host +
    ':' +
    api_port +
    `/results/verified_timeouts_graphic/sequence/${props.sequence}/workers/${props.workers}/olts/${props.olts}`;

  useEffect(() => {
    setLoadingVerifiedTime(true);
    axios
      .get(url_verified_time)
      .then(response => {
        console.log('URL: ' + url_verified_time);
        console.log('RESPOSTA: ' + response.data);
        setVerifiedTime(response.data);
      })
      .catch(err => console.log(err))
      .finally(() => setLoadingVerifiedTime(false));
    axios
      .get(url_verified_timedout)
      .then(response => {
        setVerifiedTimeouts(response.data);
      })
      .catch(err => console.log(err))
      .finally(() => setLoadingVerifiedTimeouts(false));
  }, [url_verified_time, url_verified_timedout]);

  return verifiedTime && verifiedTimeouts ? (
    <Flex direction="column" minW="97%">
      <VStack mb={6} alignItems="start">
        <Heading size="md" fontWeight="bold">
          Results for configuration: {props.workers} workers | {props.olts} olts
        </Heading>
        <Divider />
      </VStack>
      <HStack justifyContent="space-around">
        <VStack justifyContent="center" spacing={4}>
          <Heading size="sm">Verified total time</Heading>
          <LineChart data={verifiedTime} />
        </VStack>
        <VStack>
          <Heading size="sm">Verified timedout provisions</Heading>
          <LineChart data={verifiedTimeouts} />
        </VStack>
      </HStack>
    </Flex>
  ) : (
    <HStack spacing={4}>
      <Spinner size="xl" />
      <Heading>Loading</Heading>
    </HStack>
  );
};

export default SequenceResults;
