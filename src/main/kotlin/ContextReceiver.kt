import java.util.UUID.randomUUID

//https://blog.rockthejvm.com/kotlin-context-receivers/


data class User(
    val id: String = randomUUID().toString(),
    val firstName: String? = null,
    val lastName: String? = null,
    val tel: String? = null,
)

/*
In Kotlin, we call the UserDataSource the dispatcher receiver of the toJson function.
In this way, we limit the visibility of the save function,
which allows us to call it only inside the scope.
We say that the save function is a context-dependent construct.
 */
class UserDataSource {
    private fun aMethodFromUserDataSource() = println("aMethodFromUserDataSource")
    fun User.save() = println("save user $id : $this").also { aMethodFromUserDataSource() }
    // User est appelé le receiver - The receiver is the object on which the extension function is invoked -
    // Ici this = le receiver (user) mais aussi le dispatcher (UserDataSource)
    //  --> Je peux appeler une méthode de l'un ou l'autre
}

interface Logger {
    fun info(message: String)
}

val consoleLogger = object : Logger {
    override fun info(message: String) {
        println("[INFO] $message")
    }
}

/**  je precise que cette fonction doit être utilisé dans ces 2 context particulier **/
context(UserDataSource, Logger)
fun User.saveSpecific() = println("save user $id ${save()}").also { info("un log") }


fun main() {
    val myUserDataSource = UserDataSource()
    val user = User()
    // user.save() // pas possible car je ne suis pas dans le contest UserDataSource
    //user.saveSpecific() //  erreur car No required context receiver found
    with(myUserDataSource) {
        with(consoleLogger) {
            user.save()
            user.saveSpecific()
        }
    }
}
