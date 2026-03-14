package client.webclient

import client.SpaceStation
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

/**
Client web non bloquant et asynchrone
 + Fluent API
 **/

@Component
open class SpaceStationWebClient(private val webClient: WebClient) {

    suspend fun whereIsSpaceStation(): SpaceStation {
        return webClient.get()
            .uri("/iss-now.json")
            .retrieve()
            .awaitBody<SpaceStation>()
            //.block() -> pour rendre synchrone si besoin
    }

}

