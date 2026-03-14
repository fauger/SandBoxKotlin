package client.restClient

import client.SpaceStation
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

/**
Similaire à webClient mais bloquant
 Ne necessite pas d'ajouter la dépendance webFlux !
 **/


@Component
open class SpaceStationRestClient(private val restClient: RestClient) {

    fun whereIsSpaceStation(): SpaceStation? {
        return restClient.get()
            .uri("/iss-now.json")
            .retrieve()
            //.body<SpaceStation>() // do not work need upgared to spring 6.2
            .body(SpaceStation::class.java)

    }

}