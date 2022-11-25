package io.cloudevents.scs_cloudevents_sample

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("local")
@SpringBootTest
@EmbeddedKafka(
	topics = ["io.cloudevents.customer.commands", "io.cloudevents.customer.events"],
	partitions = 1,
	ports = [9291]
)
class ScsCloudeventsSampleApplicationTests {

	@Test
	fun contextLoads() {
	}

	
}
