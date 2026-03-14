package client.restClient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory


@Configuration
open class RestClientConfig {

    @Bean
    open fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl("http://api.open-notify.org")
            .build()
    }

/*    @Bean
    open fun spaceStationWebClient2(builder: WebClient.Builder) : SpaceStationInterface { // injecter via bean
        val webClient = builder.baseUrl("http://api.open-notify.org").build()
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build().createClient(SpaceStationInterface::class.java)
    }*/


}

