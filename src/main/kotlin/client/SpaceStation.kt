package client

import java.time.Instant

data class IssPosition(val longitude: String, val latitude: String)

data class SpaceStation (val timestamp: Instant, val iss_position: IssPosition, val message: String)
