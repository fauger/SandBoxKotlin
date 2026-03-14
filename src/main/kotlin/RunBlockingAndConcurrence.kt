import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    //1. je suis sur le main thread, j'ai de la concurrence mais pas de parallelisme
    // (note : The default CoroutineDispatcher for this builder is an internal implementation of event loop)
    runBlocking {
        launch {
            delay(1000L) // Ici le thread main est relaché pendant le delay
            log (" World!")
        }
        log(" Hello")
    }
    log("i'm outside runBlocking")

    //2. Si je dispatch mes thread sur un autre dispatcher que le main est ce que j'ai du parallelisme ?
    runBlocking {
        launch(Dispatchers.Default) { doSomething("something") }
        launch(Dispatchers.Default) { doSomething("otherThing") }
        launch {
            delay(1000L)
            doSomething("on main thread")
        }
    }
}

private fun log(message: String) {
    println("{${Thread.currentThread().name}] $message")
}

private suspend fun doSomething(message: String){
    log("i'm doing $message")
}
