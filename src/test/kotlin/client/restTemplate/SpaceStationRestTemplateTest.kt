package client.restTemplate

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpaceStationRestTemplateTest {

    @Autowired
    private lateinit var spaceStationTemplate: SpaceStationRestTemplate

    @Autowired
    @Qualifier("spaceStationRestTemplateClient")
    private lateinit var spaceStationClient: SpaceStationInterface

    @Test
    fun requestPosition() {
        spaceStationTemplate.whereIsSpaceStation().also (::println).also { it.iss_position.latitude.shouldNotBe(null) }
    }

    @Test
    fun requestPositionWithInterface() {
        spaceStationClient.whereIsSpaceStation().also (::println).also { it.iss_position.latitude.shouldNotBe(null) }

    }
}
