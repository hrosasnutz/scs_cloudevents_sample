
# Cloud Events with Spring Cloud Stream sample project

This is a sample project on use io.cloudevents and spring cloud stream for streaming events on kafka binder. This project was build with java 17.


## Host

To run this project, you will need to add the following dns to hosts file

`127.0.0.1 kafka`


## Usage/Examples

Start docker stack with next command:
```bash
docker-compose up -d
```
Build application:
```bash
gradlew build -x test
java -jar build/libs/scs_cloudevents_sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```
Create a customer:
```bash
curl -X POST -H "content-type: application/json" http://localhost:8000/api/customers -d '{"name":"John Doe","vip":false,"birthdate":"2002-10-13"}'
```
Sample response:
```log
2022-11-25 14:38:50.915 DEBUG 13040 --- [nio-8000-exec-1] i.c.s.service.CustomerService            : Send command: CreateCustomerCommand(name=John Doe, vip=false, birthdate=2002-10-13)
2022-11-25 14:38:50.970 DEBUG 13040 --- [container-0-C-1] i.c.s.listener.CustomerListener          : To save Customer: GenericMessage [payload=CloudEvent{id='e31b1956-dea9-47bd-91ed-c6509659ed4c', source=/api/customers, type='io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand', datacontenttype='application/json', subject='customer', time=2022-11-25T14:38:50.917908300-05:00, data=BytesCloudEventData{value=[123, 34, 110, 97, 109, 101, 34, 58, 34, 74, 111, 104, 110, 32, 68, 111, 101, 34, 44, 34, 118, 105, 112, 34, 58, 102, 97, 108, 115, 101, 44, 34, 98, 105, 114, 116, 104, 100, 97, 116, 101, 34, 58, 34, 50, 48, 48, 50, 45, 49, 48, 45, 49, 51, 34, 125]}, extensions={}}, headers={ce-datacontenttype=application/json, ce-subject=customer, ce-specversion=1.0, deliveryAttempt=1, ce-id=e31b1956-dea9-47bd-91ed-c6509659ed4c, kafka_timestampType=CREATE_TIME, ce-time=2022-11-25T14:38:50.9179083-05:00, kafka_receivedMessageKey=[B@352a9a12, kafka_receivedTopic=io.cloudevents.customer.commands, target-protocol=kafka, ce-source=/api/customers, kafka_offset=0, scst_nativeHeadersPresent=true, ce-type=io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand, message-type=cloudevent, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@2d7c9222, source-type=kafka, id=f56957bf-efa1-32cc-f009-8693834e3709, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTimestamp=1669405130933, kafka_groupId=savers, timestamp=1669405130970}]
Hibernate: select customer0_.uuid as uuid1_0_0_, customer0_.birthdate as birthdat2_0_0_, customer0_.name as name3_0_0_, customer0_.vip as vip4_0_0_ from customer customer0_ where customer0_.uuid=?
2022-11-25 14:38:51.035  INFO 13040 --- [container-0-C-1] i.c.s.service.CustomerService            : New customer was created with 53a0261e-b448-46e8-b0ad-c1216d570147
Hibernate: insert into customer (birthdate, name, vip, uuid) values (?, ?, ?, ?)
2022-11-25 14:38:51.065 DEBUG 13040 --- [container-0-C-1] i.c.s.listener.CustomerListener          : customer created message: GenericMessage [payload=CloudEvent{id='53a0261e-b448-46e8-b0ad-c1216d570147', source=/api/customers/53a0261e-b448-46e8-b0ad-c1216d570147, type='io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent', datacontenttype='application/json', subject='customer', time=2022-11-25T14:38:51.050435500-05:00, data=BytesCloudEventData{value=[123, 34, 117, 117, 105, 100, 34, 58, 34, 53, 51, 97, 48, 50, 54, 49, 101, 45, 98, 52, 52, 56, 45, 52, 54, 101, 56, 45, 98, 48, 97, 100, 45, 99, 49, 50, 49, 54, 100, 53, 55, 48, 49, 52, 55, 34, 44, 34, 110, 97, 109, 101, 34, 58, 34, 74, 111, 104, 110, 32, 68, 111, 101, 34, 44, 34, 118, 105, 112, 34, 58, 102, 97, 108, 115, 101, 44, 34, 98, 105, 114, 116, 104, 100, 97, 116, 101, 34, 58, 34, 50, 48, 48, 50, 45, 49, 48, 45, 49, 51, 34, 125]}, extensions={}}, headers={ce-datacontenttype=application/json, ce-subject=customer, ce-specversion=1.0, ce-id=53a0261e-b448-46e8-b0ad-c1216d570147, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, ce-time=2022-11-25T14:38:51.0504355-05:00, kafka_receivedMessageKey=[B@4df8273c, kafka_receivedTopic=io.cloudevents.customers, ce-source=/api/customers/53a0261e-b448-46e8-b0ad-c1216d570147, kafka_offset=0, scst_nativeHeadersPresent=true, ce-type=io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@7ecafae8, id=a12f1266-c566-d374-041d-efa8f2c13191, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTimestamp=1669405131056, kafka_groupId=loggers, timestamp=1669405131064}]
2022-11-25 14:38:51.070  INFO 13040 --- [container-0-C-1] i.c.s.listener.CustomerListener          : New customer created: CustomerCreatedEvent(uuid=53a0261e-b448-46e8-b0ad-c1216d570147, name=John Doe, vip=false, birthdate=2002-10-13)
```

## Acknowledgements

- [Spring.io](https://spring.io/)
- [Spring Cloud Stream doc](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/)
- [CloudEvents.io](https://cloudevents.io/)
- [CloudEvents sdk Java doc](https://cloudevents.github.io/sdk-java/)


## License

[Apache License](https://choosealicense.com/licenses/apache-2.0/)

