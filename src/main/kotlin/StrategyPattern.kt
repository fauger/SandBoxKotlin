
/** Etape 1. Exemple de situtation ou le strategy pattern est utile
 * https://www.youtube.com/watch?v=-Ak44LFwlwI **/
interface FormField {
    val name: String
    val value: String
    fun isValid() : Boolean
}

class EmailField(override val value: String) : FormField {
    override val name = "email"
    override fun isValid(): Boolean = value.contains("@") && value.contains(".")
}

class UsernamesField(override val value: String, val validator : Validator) : FormField {
    override val name = "username"
    override fun isValid(): Boolean = value.isNotEmpty()
}

class PasswordField(override val value: String, val validator : Validator) : FormField {
    override val name = "password"
    override fun isValid(): Boolean = value.length > 8
}

/** Etape 2. Exemple de situtation ou le strategy pattern est utile
Encapsulate which vary = Extract what differs
On peut creer une interface validator pour encapsuler la partie qui différe à l'étape 1 et passer ce qui différe dans les constructeurs
 On alors plus besoin de différentes classes qui peuvent être remplacé par FormFieldImpl **/

//Dans le strategy pattern c'est partie s'apelle la strategy
// Et les implémentations des concretes strategies
fun interface Validator {
    fun isValid(value : String) : Boolean
}

class EmailValidator : Validator {
    override fun isValid(value : String) : Boolean = value.contains("@") && value.contains(".")
}
class UsernameValidator : Validator {
    override fun isValid(value : String) : Boolean = value.isNotEmpty()
}
class PasswordValidator : Validator {
    override fun isValid(value : String) : Boolean = value.length > 8
}

//Dans le strategy pattern c'est partie s'apelle le context
class FormFieldImpl(override val name : String, override val value: String, private val validator : Validator) : FormField {
    override fun isValid(): Boolean = validator.isValid(value)
}

/** Etape 3. Rendre plus kotlinesque **/

//utiliser fun interface
val EmailValidatorAlt = Validator { it.contains("@") && it.contains(".") }
val UsernameValidatorAlt = Validator { it.isNotEmpty() }
val PasswordValidatorAlt = Validator { it.length > 8 }

//utiliser des extensions
typealias ValidatorAlt = (String) -> Boolean
val EmailValidatorAlt2 : ValidatorAlt = { it.contains("@") && it.contains(".") }
val UsernameValidatorAlt2 : ValidatorAlt = { it.isNotEmpty() }
val PasswordValidatorAlt2 : ValidatorAlt = { it.length > 8 }

fun ValidatorAlt.optional() : ValidatorAlt = { it.isEmpty() || this(it) }

class FormFieldImplAlt(override val name : String, override val value: String, private val validator : ValidatorAlt) : FormField {
    override fun isValid(): Boolean = validator(value)
}


fun main() {
    val emailField = FormFieldImplAlt("email", "test@exmample.com", EmailValidatorAlt2.optional())
}