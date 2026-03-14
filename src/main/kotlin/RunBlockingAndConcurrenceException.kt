import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() {
    runBlocking {
        val test = listOf(1, 2, 3, 4, 5)
        supervisorScope {
            try {
                val list2 = test.map {
                    async {
                        if (it == 3) throw IllegalStateException("error") else println(it)
                        it

                    }
                }.awaitAll()
                list2.map(::println)
            } catch (exception: Exception) {
                println("exception $exception")
                null
            }
        }
        println("Fin traitement")
    }
}
