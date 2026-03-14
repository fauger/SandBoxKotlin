package client.restTemplate

import client.SpaceStation
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.support.RestTemplateAdapter
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.invoker.HttpServiceProxyFactory

/**
Pros:

Switchable underlying HTTP client library
Supports declarative HTTP interface (voir exemple ci dessous)
Highly configurable
Usable in older versions of Spring Framework

Cons:

Having multiple overloads of a method makes this library hard to use
The classic Spring template pattern is old-fashioned
Not suitable for non-blocking environments (for example, WebFlux)
**/

/** 1. Utilisation classique de RestTemplate */
@Component
open class SpaceStationRestTemplate(restTemplateBuilder: RestTemplateBuilder) {
    private val restTemplate = restTemplateBuilder.build()

    fun whereIsSpaceStation(): SpaceStation {
        return restTemplate.getForObject("http://api.open-notify.org/iss-now.json", SpaceStation::class.java) ?: error("No data")
    }

}

/** 2. Exemple de déclarative RestClient via une interface et l'annotation HttpExchange (intégré dans spring boot 6)
 * Et creation d'un proxy qui implemente l'interface avec restTemplate */
@HttpExchange(url = "http://api.open-notify.org")
interface SpaceStationInterface {

    @GetExchange("/iss-now.json")
    fun whereIsSpaceStation(): SpaceStation

}

@Configuration
open class RestTemplateConfig {

    @Bean
    open fun spaceStationRestTemplateClient() : SpaceStationInterface { // injecter via bean
        val restTemplate = RestTemplate()
        val adapter = RestTemplateAdapter.create(restTemplate)
        return HttpServiceProxyFactory.builderFor(adapter).build().createClient(SpaceStationInterface::class.java)
    }
}