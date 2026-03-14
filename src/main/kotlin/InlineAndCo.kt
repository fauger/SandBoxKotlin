//SEE https://www.youtube.com/watch?v=T9sAlxqYFYc

private fun main() {
    processRecordWithNonLocalReturn("Alice", "Bob", "Charlie", "Dave")
    processRecordWithScopeInsideInlineFunction("Alice", "Bob", "Charlie", "Dave")
}

/**
 * Exemple de non local return dans une fonction inline
 * Pratique dans le cas de boucle par exemple
 * A noter : continue et break ne sont pas encore supporté
 */
private fun processRecordWithNonLocalReturn(vararg records: String) {
    for (record in records) {
        executeAndMeasure(record) {
            if (record.startsWith("c")) return@processRecordWithNonLocalReturn
            //comme la fonction est inline nous pouvons utiliser un non local return pour sortir de la boucle (equivalent d'un break)
            //return@executeAndMeasure (equivalent d'un continue) est cependant possible même sans inline
            println("Processing record $record")
        }
    }
}

/**
 * Exemple avec crossInline
 */
private fun processRecordWithScopeInsideInlineFunction(vararg records: String) {
    for (record in records) {
        executeAndMeasureInsideSCope(record) {
            //if (record.startsWith("c")) return@processRecordWithScopeInsideInlineFunction
            // return n'est pas possible car la lambda est marqué comme crossinline
            println("Processing record $record")
        }
    }
}

/**
 * 1. Example avec inline. inline copy le code de executeAndMeasure dans le code appelant
 */
private inline fun executeAndMeasure(label: String, block: () -> Unit) {
    val start = System.nanoTime()
    block()
    val end = System.nanoTime()
    println("$label took ${end - start} ns")
}

/**
2. Exemple avec crossIn line. La lambda n'est pas executé dans le body de la function mais dans un autre contexte -> ici Thread{..}
Dans ce cas besoin de crossinline block, sinon erreur du compilateur car un non local return est possible
crossinline garantie qu'il n'y a pas de non local return
 */
private inline fun executeAndMeasureInsideSCope(label: String, crossinline block: () -> Unit) {
    Thread {
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        println("$label took ${end - start} ns")
    }.start()
}

/**
 * 3. Example avec noinline. Si la fonction est inline alors toutes ses lambdas sont aussi inline
 * OnError n'est plus manipulable comme une reference de fonction et ne pas pas  être passé en paramétre à une autre fonction
 * A moins que l'on utilise noinline
 */
private inline fun executeAndMeasureAndCatchException(label: String, crossinline block: () -> Unit, noinline onError : (Exception) -> Unit) {
    Thread {
        try {
            val start = System.nanoTime()
            block()
            val end = System.nanoTime()
            println("$label took ${end - start} ns")
        }
        catch (e: Exception) {
            ErrorProcessor().processError(label, onError)
        }
    }.start()
}


class ErrorProcessor {
    fun processError(label: String, e: (Exception) -> Unit) {
        println("$label $e")
    }
}
