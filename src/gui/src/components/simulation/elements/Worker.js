import React from 'react';
import { useColorModeValue, Stack, Img, Text } from '@chakra-ui/react';

const Worker = () => {
  const color_mode = useColorModeValue('dark', 'light');
  const worker_image_source = `images/${color_mode}/process.png`;
  const queue_image_source = `images/${color_mode}/queue.png`;
  return (
    <Stack direction="column" alignItems="center" spacing={0}>
      <Img src={queue_image_source} maxW="40%" mb={2} />
      <Img src={worker_image_source} maxW="20%" />
      <Text fontSize="medium" fontWeight="bold">
        Worker
      </Text>
    </Stack>
  );
};

export default Worker;
