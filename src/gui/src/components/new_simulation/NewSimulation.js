import React, { useState, useRef } from 'react';
import { Formik, Form, Field } from 'formik';
import {
  Box,
  VStack,
  FormControl,
  FormLabel,
  Input,
  Button,
  FormErrorMessage,
  useDisclosure,
  AlertDialog,
  AlertDialogOverlay,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
} from '@chakra-ui/react';
import axios from 'axios';

const NewSimulation = () => {
  const [submitting, setSubmitting] = useState(false);
  const [response, setResponse] = useState(null);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const cancelRef = useRef();

  const api_host = process.env.REACT_APP_BACKEND_HOST;
  const api_port = process.env.REACT_APP_BACKEND_PORT;

  const url = 'http://' + api_host + ':' + api_port + '/orchestration';

  const validateOlts = value => {
    let error = null;
    if (value < 1 || value > 10) {
      error =
        'The number of deployed olts is 10. Therefore the input must be between 1 and 10.';
    }
    return error;
  };

  const validateWorkers = value => {
    let error = null;
    if (value < 1 || value > 10) {
      error =
        'The number of deployed workers is 10. Therefore then input must be between 1 and 10.';
    }
    return error;
  };

  const validateMessages = value => {
    let error = null;
    if (value < 1) {
      error = 'The number of messages must be greater than 0.';
    }
    return error;
  };

  const validateAlgorithm = value => {
    let error = null;
    if (value < 1 || value > 4) {
      error =
        'The supported algorithms are 1, 2, 3 and 4. No other values will be accepted.';
    }
    return error;
  };

  const validateSequence = value => {
    let error = null;
    if (value < 1 || value > 3) {
      error =
        'The supported sequences are 1, 2 and 3. No other values will be accepted.';
    }
    return error;
  };

  const submitRequest = values => {
    setSubmitting(true);
    axios
      .post(url, values)
      .then(response => {
        setResponse(response.data);
        onOpen();
      })
      .catch(err => console.log(err))
      .finally(() => setSubmitting(false));
  };

  return (
    <Box minH="86vh" minW="100%" alignItems="center">
      <Box mx="35%" my="4%" p={6} borderWidth={2} rounded="md">
        <Formik
          initialValues={{
            workers: null,
            messages: null,
            olts: null,
            algorithm: null,
            sequence: null,
          }}
          onSubmit={(values, actions) => {
            submitRequest(values);
          }}
        >
          {props => (
            <Form>
              <VStack spacing={4} align="flex-start">
                <Field name="olts" validate={validateOlts}>
                  {({ field, form }) => (
                    <FormControl
                      isInvalid={form.errors.olts && form.touched.olts}
                    >
                      <FormLabel htmlFor="olts">Olts</FormLabel>
                      <Input {...field} id="olts" placeholder="Olts" />
                      <FormErrorMessage>{form.errors.olts}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="workers" validate={validateWorkers}>
                  {({ field, form }) => (
                    <FormControl
                      isInvalid={form.errors.workers && form.touched.workers}
                    >
                      <FormLabel htmlFor="workers">Workers</FormLabel>
                      <Input {...field} id="workers" placeholder="Workers" />
                      <FormErrorMessage>{form.errors.workers}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="messages" validate={validateMessages}>
                  {({ field, form }) => (
                    <FormControl
                      isInvalid={form.errors.messages && form.touched.messages}
                    >
                      <FormLabel htmlFor="messages">Messages</FormLabel>
                      <Input {...field} id="messages" placeholder="Messages" />
                      <FormErrorMessage>
                        {form.errors.messages}
                      </FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="algorithm" validate={validateAlgorithm}>
                  {({ field, form }) => (
                    <FormControl
                      isInvalid={
                        form.errors.algorithm && form.touched.algorithm
                      }
                    >
                      <FormLabel htmlFor="algorithm">Algorithm</FormLabel>
                      <Input
                        {...field}
                        id="algorithm"
                        placeholder="Algorithm"
                      />
                      <FormErrorMessage>
                        {form.errors.algorithm}
                      </FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="sequence" validate={validateSequence}>
                  {({ field, form }) => (
                    <FormControl
                      isInvalid={form.errors.sequence && form.touched.sequence}
                    >
                      <FormLabel htmlFor="sequence">Sequence</FormLabel>
                      <Input {...field} id="sequence" placeholder="Sequence" />
                      <FormErrorMessage>
                        {form.errors.sequence}
                      </FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Button
                  type="submit"
                  colorScheme="teal"
                  isLoading={submitting}
                  isFullWidth
                >
                  Submit orchestration
                </Button>
              </VStack>
            </Form>
          )}
        </Formik>
        <AlertDialog
          isOpen={isOpen}
          leastDestructiveRef={cancelRef}
          onClose={onClose}
        >
          <AlertDialogOverlay>
            <AlertDialogContent>
              <AlertDialogHeader fontSize="md" fontWeight="bold">
                Request submitted
              </AlertDialogHeader>
              <AlertDialogBody>
                <strong>{response}</strong>
              </AlertDialogBody>
              <AlertDialogFooter>
                <Button ref={cancelRef} onClick={onClose}>
                  OK
                </Button>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialogOverlay>
        </AlertDialog>
      </Box>
    </Box>
  );
};

export default NewSimulation;
