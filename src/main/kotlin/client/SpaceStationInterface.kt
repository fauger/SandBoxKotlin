package client

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange(url = "http://api.open-notify.org")
interface SpaceStationInterface {

    @GetExchange("/iss-now.json")
    fun whereIsSpaceStation(): SpaceStation

}