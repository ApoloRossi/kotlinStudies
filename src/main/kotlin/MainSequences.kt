import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.measureNanoTime

/**collections, inline/noinline/crossinline, reified, data classes

 - Sequences
 - Drop
 - Shuffle

* https://winterbe.com/posts/2018/07/23/kotlin-sequence-tutorial/
* https://kotlinlang.org/docs/sequences.html#iterable
*/
fun main(args: Array<String>) {

    sequenceChangeOrderWhenHasIntermediaryOperation()
}

private fun sequencesVsList() {
    val words = "The quick brown fox jumps over the lazy dog".split(" ") //.asSequence()
    val lengthsList = words.filter { println("filter: $it"); it.length > 3 }
        .map { println("length: ${it.length}"); it.length }
        .take(4)

    println("Lengths of first 4 words longer than 3 chars:")
    println(lengthsList) //.toList()
}

/*
*Sequences
- map()
- filter()
- take()
- drop().
* Those are intermediate functions that does not have a state.
*
* If a sequence operation returns another sequence, which is produced lazily, it's called intermediate.
* Otherwise, the operation is terminal. Examples of terminal operations are toList() or sum().
* Sequence elements can be retrieved only with terminal operations.
*/

private fun dropValueOnList() {
    print(listOf(1, 2, 3, 4, 5).asSequence().drop(1).toList())
}

private fun shuffleExtension() {
    val result = listOf(1, 2, 3, 4, 5).asSequence().shuffle().toList()

    print(result)
}

fun <T> Sequence<T>.shuffle(): Sequence<T> {
    return toMutableList()
        .apply { shuffle() }
        .asSequence()
}

private fun transformAndFlatCollections() {
    val result = sequenceOf(listOf(1, 2, 3, 4), listOf(4, 5, 6))
        .flatMap {
            it
        }

    print(result)
}

private fun plusAndMinus() {
    val result = sequenceOf("a", "b", "c")
        .plus("d")
        .minus("c")
        .map { it.uppercase(Locale.getDefault()) }
        .toList()

    print(result)
}

private fun withIndex() {
    /**
     * withIndex gets the element position to manipulate the value by its index.
     * */
    val result = sequenceOf("a", "b", "c", "d")
        .withIndex()
        .filter { it.index % 2 == 0 }
        .map { it.value }
        .toList()

    print(result)
}

private fun sortDistinctMax() {

    val resultOrderByAge = persons
        .sortedBy { it.age }
        .toList()

    val resultDistinct = persons
        .distinctBy { it.name }
        .toList()

    val resultMax = persons
        .maxBy { it.age }

    val resultGroupBy = persons
        .groupBy { it.name }

    val resultAssociatedBy = persons
        .associateBy { it.name }

    val resultAny = sequenceOf(1, 2, 3, 4, 5)
        .filter { it % 2 == 1 }
        .any { it % 3 == 0 }


    println(resultOrderByAge)
    println(resultDistinct)
    println(resultMax)
    println(resultGroupBy)
    println(resultAssociatedBy)
    print(resultAny)

}

private fun measureTimeSequencesVsList() {
    val list = generateSequence(1) { it + 1 }
        .take(50_000_000)

    measure {
        list
            .filter { it % 3 == 0 }
            .average()
    }
}

fun measure(block: () -> Unit) {
    val nanoTime = measureNanoTime(block)
    val millis = TimeUnit.NANOSECONDS.toMillis(nanoTime)
    print("$millis ms")
}

private fun dataReduceOpBeforeDataTransformationsOp() {
    /**
     * always put data-reducing operations such as filter
     * before data-transforming operations such as map and you’re good to go.
     * */
    sequenceOf("a", "b", "c", "d")
        .filter {
            println("filter: $it")
            it.startsWith("a", ignoreCase = true)
        }
        .map {
            println("map: $it")
            it.toUpperCase()
        }
        .forEach {
            println("forEach: $it")
        }
}

private fun lazynessProcessing() {

    /**
     * No exemplo abaixo é possível notar que o map só é executado
     * caso o any() retorne true. Dessa forma, evita que todos os elementos do map
     * sejam processados sem necessidade.
    * */
    sequenceOf("a", "b", "c")
        .map {
            println("map: $it")
            it.uppercase(Locale.getDefault())
        }
        .any {
            println("any: $it")
            it.startsWith("B")
        }
}

private fun sequenceChangeOrderWhenHasIntermediaryOperation() {
    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).filter {
        if (it <= 5) {
            println("filter:$it")
            return@filter true
        }
        false
    }.forEach {
        println(it)
    }
}

private fun sequenceTake() {
    generateSequence(7) { it + 2 }.take(5).forEach {
        println(it)
    }
}

fun generateSequenceFromFunction() {
    //Números impares de 1 a 100.
    val oddNumber = generateSequence(1) {
        if(it + 2 <= 100) {
            it + 2
        } else null
    }

    println(oddNumber.forEach { println(it) })
}

//Uso do Foreach
fun printOnlyOdd(list : List<Int>) {
    list.forEach {
        if(it % 2 != 0) {
            println(it)
        }
    }
}



private fun fibonacciSequence() {
    val result = generateSequence(Pair(0, 1)) {
        Pair(it.second, (it.first + it.second))
    }.take(10).toList()

    println(result)
}

data class Person(val name: String, val age: Int)

val persons = listOf(
    Person("Peter", 16),
    Person("Anna", 28),
    Person("Anna", 23),
    Person("Sonya", 39)
)