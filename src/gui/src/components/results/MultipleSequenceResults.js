import React from 'react';
import { VStack, Heading, Divider } from '@chakra-ui/react';
import SequenceResults from './SequenceResults';

const MultipleSequenceResults = props => {
  return (
    <VStack mx={4} minW="97%" alignItems="start" spacing={6}>
      <VStack w="100%" alignItems="start">
        <Heading size="lg">Sequence {props.sequence} results</Heading>
        <Divider />
      </VStack>
      {props.combinations.map(comb => (
        <SequenceResults
          key={comb.x + comb.y}
          sequence={props.sequence}
          workers={comb.x}
          olts={comb.y}
        />
      ))}
    </VStack>
  );
};

export default MultipleSequenceResults;
