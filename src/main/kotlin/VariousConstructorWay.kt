//SEE https://blog.kotlin-academy.com/item-30-consider-factory-functions-instead-of-constructors-e1c747fc475
// https://x.com/marcinmoskala/status/1803343720344940545

/* 1. Companion Object Factory Function
Companion object works in class or interface
 */

class DatabaseConnector( override val url : String) : Connector

interface Connector {
    val url : String
    companion object {
        fun newInstance(url : String): Connector? = when(url) {
                "database" -> DatabaseConnector(url)
                else -> null
            }
    }
}
val database = Connector.newInstance("database")

// Convention naming for factory functions
// from — A type    -conversion :  Date.from(instant)
// of — An aggregation operation : EnumSet.of(JACK, QUEEN, KING)
// valueOf — An alternative to from and of : BigInteger.valueOf(Integer.MAX_VALUE)
// instance or getInstance — Used in singletons : Calendar.getInstance()
// createInstance or newInstance - returns a new instance : Array.newInstance(classObject, arrayLen)
//get<Type> - he type of object returned : Files.getFileStore(path)
//new<Type> - Like get<Type> but may return a new instance : Files.newBufferedReader(path)

/* 2. Extension factory functions
if no access to the class, for example 3rd libraries
 */
interface Car {
    val name: String
    companion object {} //empty companion is mandatory to use extension
}

class Ferrari

fun Car.Companion.Ferrari(): Ferrari = Ferrari()

val ferrari = Car.Ferrari()

/*3. Top-level functions
specify top-level functions that are used to create objects
like listOf, mapOf, etc.*/

fun IntListOfString(vararg elements: String) = elements.map { it.toInt() }
val intList = IntListOfString("1", "2", "3")

/*4. fake interface constructor - Like `List(4)`
function returning private class or object.
Commonly used in kotlin team librairies
- Easy to change impl if used in sdk
- Can return different impl based on input
@see lazy {  }, Mutex
*/
interface Train
private class TGV : Train
private class Eurostar : Train

fun Train(destination: String): Train? = when (destination) {
    "France" -> TGV()
    "UK" -> Eurostar()
    else -> null
}

val train = Train("France")

/*5. fake constructor with invoke`
function returning private class or object.
Not recommended - prefer option 4
*/

class IceCream private constructor(private val scoops: Int) {

    companion object {
        operator fun invoke(): IceCream? = invoke(0)
        operator fun invoke(scoops: Int): IceCream? =
        if (scoops < 0) { null } else { IceCream(scoops) }
        }
    }

val noIceCream = IceCream()
val someIceCream = IceCream(1)
val moreIceCream = IceCream(2)
val invalidIceCream = IceCream(-1)