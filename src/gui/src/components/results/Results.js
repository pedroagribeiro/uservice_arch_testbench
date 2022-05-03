import React, { useState, useEffect } from 'react';
import {
  Box,
  Spinner,
  Heading,
  HStack,
  Button,
  Divider,
  VStack,
  FormControl,
  Select,
} from '@chakra-ui/react';
import { Formik, Form, Field } from 'formik';
import axios from 'axios';
import { Link } from 'react-router-dom';
import SimulationButtonBox from './SimulationButtonBox';

const Results = () => {
  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const [loading, setLoading] = useState(true);
  const [result, setResults] = useState(null);
  const [displayable, setDisplayable] = useState(null);

  const url = 'http://' + api_host + ':' + api_port + '/results';

  useEffect(() => {
    setLoading(true);
    axios
      .get(url)
      .then(response => {
        setResults(response.data.sort((a, b) => a.id - b.id));
        setDisplayable(response.data.sort((a, b) => a.id - b.id));
      })
      .catch(err => console.log(err))
      .finally(() => {
        setLoading(false);
      });
  }, [url]);

  if (loading) {
    return (
      <HStack spacing={4}>
        <Spinner size="xl" />
        <Heading>Loading</Heading>
      </HStack>
    );
  }

  const availableWorkers = () => {
    if (!result) return [];
    const available_workers = [];
    result.forEach(r => {
      if (!available_workers.includes(r.workers))
        available_workers.push(r.workers);
    });
    return available_workers;
  };

  const availableOlts = () => {
    if (!result) return [];
    const available_olts = [];
    result.forEach(r => {
      if (!available_olts.includes(r.olts)) available_olts.push(r.olts);
    });
    return available_olts;
  };

  const availableMessages = () => {
    if (!result) return [];
    const available_messages = [];
    result.forEach(r => {
      if (!available_messages.includes(r.requests))
        available_messages.push(r.requests);
    });
    return available_messages;
  };

  const availableArchitectures = () => {
    if (!result) return [];
    const available_architectures = [];
    result.forEach(r => {
      if (!available_architectures.includes(r.algorithm))
        available_architectures.push(r.algorithm);
    });
    return available_architectures;
  };

  const availableSequences = () => {
    if (!result) return [];
    const available_sequences = [];
    result.forEach(r => {
      if (!available_sequences.includes(r.sequence))
        available_sequences.push(r.sequence);
    });
    return available_sequences;
  };

  const result_filter_test = (result, values) => {
    if (values.status) {
      if (result.status !== values.status) return false;
    }
    if (values.olts) {
      if (result.olts !== parseInt(values.olts)) return false;
    }
    if (values.workers) {
      if (result.workers !== parseInt(values.workers)) return false;
    }
    if (values.messages) {
      if (result.requests !== parseInt(values.messages)) return false;
    }
    if (values.algorithm) {
      if (result.architecture !== parseInt(values.algorithm)) return false;
    }
    if (values.sequence) {
      if (result.sequence !== parseInt(values.sequence)) return false;
    }
    return true;
  };

  const updateDisplayableResults = values => {
    const new_results = [];
    result.forEach(r => {
      if (result_filter_test(r, values)) {
        new_results.push(r);
      }
    });
    setDisplayable(new_results);
  };

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
        <Box bg="gray.50" borderWidth="1px" borderRadius="lg" minW="100%" p={2}>
          <Formik
            initialValues={{
              workers: undefined,
              messages: undefined,
              olts: undefined,
              algorithm: undefined,
              sequence: undefined,
              status: undefined,
            }}
            onSubmit={values => {
              updateDisplayableResults(values);
            }}
          >
            {props => (
              <Form>
                <HStack>
                  <Field name="olts">
                    {({ field }) => (
                      <FormControl>
                        <Select {...field} placeholder="Olts">
                          {availableWorkers().map((r, i) => (
                            <option key={i} value={r}>
                              {r}
                            </option>
                          ))}
                        </Select>
                      </FormControl>
                    )}
                  </Field>
                  <Field name="workers">
                    {({ field }) => (
                      <FormControl>
                        <Select {...field} placeholder="Workers">
                          {availableOlts().map((r, i) => (
                            <option key={i} value={r}>
                              {r}
                            </option>
                          ))}
                        </Select>
                      </FormControl>
                    )}
                  </Field>
                  <Field name="messages">
                    {({ field }) => (
                      <FormControl>
                        <Select {...field} placeholder="Messages">
                          {availableMessages().map((r, i) => (
                            <option key={i} value={r}>
                              {r}
                            </option>
                          ))}
                        </Select>
                      </FormControl>
                    )}
                  </Field>
                  <Field name="algorithm">
                    {({ field }) => (
                      <FormControl>
                        <Select {...field} placeholder="Architecture">
                          {availableArchitectures().map((r, i) => (
                            <option key={i} value={r}>
                              {r}
                            </option>
                          ))}
                        </Select>
                      </FormControl>
                    )}
                  </Field>
                  <Field name="sequence">
                    {({ field }) => (
                      <FormControl>
                        <Select {...field} placeholder="Sequence">
                          {availableSequences().map((r, i) => (
                            <option key={i} value={r}>
                              {r}
                            </option>
                          ))}
                        </Select>
                      </FormControl>
                    )}
                  </Field>
                  <Field name="status">
                    {({ field }) => (
                      <FormControl>
                        <Select {...field} placeholder="Status">
                          <option value="queued">Queued</option>
                          <option value="on_going">On Going</option>
                          <option value="finished">Finished</option>
                        </Select>
                      </FormControl>
                    )}
                  </Field>
                  <Button minW="175px" type="submit" colorScheme="teal">
                    Apply Filters
                  </Button>
                </HStack>
              </Form>
            )}
          </Formik>
        </Box>
        {displayable.map((r, i) => (
          <SimulationButtonBox key={i} {...r} />
        ))}
      </VStack>
    </Box>
  );
};

export default Results;
