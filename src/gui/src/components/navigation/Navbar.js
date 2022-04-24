import React from 'react';
import { Box, Stack, Button, Text } from '@chakra-ui/react';
import { Link } from 'react-router-dom';
import { ColorModeSwitcher } from 'components/utils/ColorModeSwitcher';

const Navbar = () => {
  return (
    <Box borderWidth="2px" borderRadius="lg" p={2} mb={4}>
      <Stack
        w="100%"
        direction="row"
        justifyContent="space-between"
        alignItems="center"
      >
        <Stack direction="row" h="60%" spacing="6px">
          <Link to="/">
            <Button w="200px" variant="outline">
              <Text>Live Overview</Text>
            </Button>
          </Link>
          <Link to="/results">
            <Button w="200px" variant="outline">
              <Text>Results</Text>
            </Button>
          </Link>
          <Link to="/new_simulation">
            <Button w="200px" variant="outline">
              <Text>New simulation</Text>
            </Button>
          </Link>
        </Stack>
        <ColorModeSwitcher justifySelf="end" />
      </Stack>
    </Box>
  );
};

export default Navbar;
