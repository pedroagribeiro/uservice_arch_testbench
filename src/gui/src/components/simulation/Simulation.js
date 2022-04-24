import React from 'react';
import { Box, Grid, GridItem, Stack } from '@chakra-ui/react';
import Producer from 'components/simulation/elements/Producer';
import Broker from 'components/simulation/elements/Broker';
import Worker from 'components/simulation/elements/Worker';
import Olt from 'components/simulation/elements/Olt';

const Simulation = () => {
  return (
    <Box minH="86vh" borderWidth="1px" borderRadius="lg" py={6}>
      <Stack alignItems="center" minH="86vh" direction="row" spacing={4}>
        <Producer />
        <Broker />
        <Grid
          templateRows="repeat(3, 1fr)"
          templateColumns="repeat(1, 1fr)"
          gap={4}
        >
          <GridItem rowSpan={1}>
            <Worker />
          </GridItem>
          <GridItem rowSpan={1}>
            <Worker />
          </GridItem>
          <GridItem rowSpan={1}>
            <Worker />
          </GridItem>
        </Grid>
        <Grid
          templateRows="repeat(5, 1fr)"
          templateColumns="repeat(1, 1fr)"
          gap={4}
        >
          <GridItem rowSpan={1}>
            <Olt />
          </GridItem>
          <GridItem rowSpan={1}>
            <Olt />
          </GridItem>
          <GridItem rowSpan={1}>
            <Olt />
          </GridItem>
          <GridItem rowSpan={1}>
            <Olt />
          </GridItem>
          <GridItem rowSpan={1}>
            <Olt />
          </GridItem>
        </Grid>
      </Stack>
    </Box>
  );
};

export default Simulation;
