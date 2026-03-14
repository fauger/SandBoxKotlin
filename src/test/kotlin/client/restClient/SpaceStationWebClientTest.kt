package client.restClient

import client.SpaceStationInterface
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpaceStationWebClientTest {

    @Autowired
    private lateinit var spaceStationTemplate: SpaceStationRestClient

    @Test
    fun requestPosition() {
        runBlocking {
            spaceStationTemplate.whereIsSpaceStation().also(::println)
                .also { it?.iss_position?.latitude.shouldNotBe(null) }
        }
        }

}
