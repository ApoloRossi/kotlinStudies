import kotlin.properties.Delegates
import kotlin.reflect.KProperty

//https://kotlinlang.org/docs/delegation.html#overriding-a-member-of-an-interface-implemented-by-delegation

/*
* Lazy properties
* Observable properties
*
* */

fun main(args: Array<String>) {
    val user = User()
    println(user.name)
    user.name = "Larissa"
    user.surname = "Gulin"
    println(user.surname)
}
/**Observable Property*/
class User {
    //Observe the value after changes
    var name: String by Delegates.observable("<no name>") {
            prop, old, new ->
        println("$old -> $new")
    }

    //Observe right before the change, so it's possible to put a rule to avoid updates.
    var surname: String by Delegates.vetoable("<no surname>") { _, old, new ->
        new.first().isUpperCase()
    }
}

/**Lazy Property*/
private fun loadLazyVar() {
    //Note that computed is called once.
    println(lazyValue)
    println(lazyValue)
}

val lazyValue: String by lazy {
    println("computed!")
    "Hello"
}

/**Default Delegate Property*/

private fun testDelegateProperty() {
    val example = Example()
    println(example.p)
    example.p = "Teste"
}


class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

class Example {
    var p: String by Delegate()
}

