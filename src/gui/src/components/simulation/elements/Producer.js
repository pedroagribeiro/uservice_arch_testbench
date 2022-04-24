import React from 'react';
import { useColorModeValue, Box, Stack, Img, Text } from '@chakra-ui/react';

const Producer = () => {
  const color_mode = useColorModeValue('dark', 'light');
  const producer_image_source = `images/${color_mode}/process.png`;
  const queue_image_source = `images/${color_mode}/queue.png`;
  return (
    <Box>
      <Stack direction="column" alignItems="center" spacing={0}>
        <Img src={queue_image_source} maxW="40%" mb={2} />
        <Img src={producer_image_source} maxW="20%" />
        <Text fontSize="medium" fontWeight="bold">
          Producer
        </Text>
      </Stack>
    </Box>
  );
};

export default Producer;
