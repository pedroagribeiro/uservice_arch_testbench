FROM rabbitmq:3.9.13-management

RUN echo "consumer_timeout = 86400000\n" >> /etc/rabbitmq/conf.d/10-default-guest-user.conf

ENTRYPOINT ["rabbitmq-server"]
