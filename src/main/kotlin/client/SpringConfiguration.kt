package client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SpringConfiguration

fun main(args: Array<String>) {
    runApplication<SpringConfiguration>(*args)
}