import React from 'react';
import { useColorModeValue, Stack, Img, Text } from '@chakra-ui/react';

const Olt = () => {
  const color_mode = useColorModeValue('dark', 'light');
  const olt_image_source = `images/${color_mode}/process.png`;
  const queue_image_source = `images/${color_mode}/queue.png`;
  return (
    <Stack direction="column" alignItems="center" spacing={0}>
      <Img src={queue_image_source} maxW="40%" mb={2} />
      <Img src={olt_image_source} maxW="20%" />
      <Text fontSize="medium" fontWeight="bold">
        Olt
      </Text>
    </Stack>
  );
};

export default Olt;
