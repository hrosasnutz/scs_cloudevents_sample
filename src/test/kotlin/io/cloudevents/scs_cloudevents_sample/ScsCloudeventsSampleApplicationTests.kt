package io.cloudevents.scs_cloudevents_sample

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
@EmbeddedKafka(
	topics = ["io.cloudevents.customer.commands", "io.cloudevents.customer.events"],
	partitions = 1,
	ports = [9092]
)
class ScsCloudeventsSampleApplicationTests {

	@Test
	fun contextLoads() {
	}

	
}
