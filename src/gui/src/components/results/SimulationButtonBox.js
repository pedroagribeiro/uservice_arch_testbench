import React from 'react';
import {
  Button,
  HStack,
  Text,
  Box,
  Tag,
  TagLeftIcon,
  TagLabel,
  useColorMode,
  useColorModeValue,
} from '@chakra-ui/react';
import { Link } from 'react-router-dom';
import { MdFileDownload } from 'react-icons/md';
import { AiOutlineLoading3Quarters } from 'react-icons/ai';
import { GiCheckeredFlag } from 'react-icons/gi';

const SimulationButtonBox = props => {
  const { toggleColorMode } = useColorMode();
  const next_mode = useColorModeValue('light', 'dark');

  const tagColor = status => {
    if (status === 'queued') {
      return 'purple';
    }
    if (status === 'on_going') {
      return 'orange';
    }
    if (status === 'finished') {
      return 'green';
    }
  };

  const statusToReadableText = status => {
    if (status === 'queued') {
      return 'Queued';
    }
    if (status === 'on_going') {
      return 'On going';
    }
    if (status === 'finished') {
      return 'Finished';
    }
  };

  const statusToIcon = status => {
    if (status === 'queued') {
      return MdFileDownload;
    }
    if (status === 'on_going') {
      return AiOutlineLoading3Quarters;
    }
    if (status === 'finished') {
      return GiCheckeredFlag;
    }
  };

  return (
    <Box
      bg={next_mode === 'dark' ? 'gray.600' : 'gray.100'}
      minW="100%"
      borderWidth={1}
      rounded="md"
    >
      <HStack m={4} justifyContent="space-between" alignItems="center">
        <HStack justifyContent="space-between">
          <Text>
            <strong>Run identificator</strong>: {props.id}
          </Text>
          <Text>
            <strong>Workers</strong>: {props.workers}
          </Text>
          <Text>
            <strong>Olts</strong>: {props.olts}
          </Text>
          <Text>
            <strong>Algorithm</strong>: {props.algorithm}
          </Text>
          <Text>
            <strong>Sequence</strong>: {props.sequence}
          </Text>
          <Text>
            <strong>Provisions</strong>: {props.requests}
          </Text>
        </HStack>
        <HStack spacing={4}>
          <Tag size="lg" varian="subtle" colorScheme={tagColor(props.status)}>
            <TagLeftIcon boxSize="12px" as={statusToIcon(props.status)} />
            <TagLabel>{statusToReadableText(props.status)}</TagLabel>
          </Tag>
          <Link to={`/charts/${props.id}`}>
            <Button disabled={props.status !== 'finished'} colorScheme="teal">
              View full detailed run information
            </Button>
          </Link>
        </HStack>
      </HStack>
    </Box>
  );
};

export default SimulationButtonBox;
