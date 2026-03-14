package designPattern

import javax.print.Doc

/** see [designPattern.FactoryDemo] first.
 * On essaye de rajouter plusieurs Eléments différents (paragraph, header, link)
 * notre class Document et ses implémentations ont une méthode pour chaque type d'élément
 * On peut extraire ces méthodes dans une abstract Factory
 * La factory fonctionne pour un type de Product
 * L'abstract factory permet d'abstraire une famille de type de Product
 *
 * Abstract Factory < ---- client "importe" par compostion  < ---- A l'appel du client on injecte une concrete factory
 *      ^                                    ^
 *      |                                    |
 *      |                                    |
 *      |                           utlise Abstract Product
 *      |                                     |
 * Concrete Factories ----------> crée des produits concrets
 */
class AbstractFactoryDemo {

    class Document(private val elementFactory: ElementFactory) { // Document est ici le "Client"

        private val elements = mutableListOf<Element>()

        fun addParagraph(text: String) = elements.add(elementFactory.createParagraph(text))
        fun addHeading(text: String, level: Int) = elements.add(elementFactory.createHeading(text, level))

        fun render() = elements.joinToString("\n") { it.render() }

    }

    interface ElementFactory { // Abstract Factory
        fun createParagraph(text: String): Paragraph
        fun createHeading(text: String, level: Int): Heading
    }

    //Concrete factories pour chaque "variant
    class HTMLFactory : ElementFactory {
        override fun createParagraph(text: String) = HTMLParagraph(text)
        override fun createHeading(text: String, level: Int) = HTMLHeading(text, level)
    }

    class MarkdownFactory : ElementFactory {
        override fun createParagraph(text: String) = MarkdownParagraph(text)
        override fun createHeading(text: String, level: Int) = MarkdownHeading(text, level)
    }

    interface Element { // Product
        fun render(): String
    }

    abstract class Paragraph(private val text: String) : Element
    abstract class Heading(private val text: String, private val level: Int) : Element {
        init {
            require(level in 1..6) { "Heading level must be between 1 and 6" }
        }
    }

    abstract class Link(private val url: String, private val text: String) : Element

    //Concrete products
    class HTMLParagraph(val text: String) : Paragraph(text) {
        override fun render() = "<p>$text</p>"
    }

    class MarkdownParagraph(val text: String) : Paragraph(text) {
        override fun render() = "$text\n"
    }

    class HTMLHeading(private val text: String, private val level: Int) : Heading(text, level) {
        override fun render() = "<h$level>$text</h$level>"
    }

    class MarkdownHeading(val text: String, private val level: Int) : Heading(text, level) {
        override fun render() = "${"#".repeat(level)} $text"
    }

    class HTMLLink(private val url: String, private val text: String) : Link(url, text) {
        override fun render() = "<a href=\"$url\">$text</a>"
    }

    class MarkdownLink(private val url: String, private val text: String) : Link(url, text) {
        override fun render() = "[$text]($url)"
    }


    fun main() {
        val htmlDocument = Document(HTMLFactory())
        htmlDocument.addHeading("Title", 1)
        htmlDocument.addParagraph("Hello, world!")
        htmlDocument.addParagraph("This is a paragraph.")
        println(htmlDocument.render())

        val markdownDocument = Document(MarkdownFactory())
        markdownDocument.addHeading("Title", 1)
        markdownDocument.addParagraph("Hello, world!")
        markdownDocument.addParagraph("This is a paragraph.")
        println(markdownDocument.render())
    }

    /** Rendre plus kotlinesque -> en kotlin les fonctions sont des first class citizen*/


    /** 1. On peut zapper l'inteface Element en replaçant par une fonction*/
    interface ElementK {
        val render : () -> String
    }

    interface ElementFactoryK {
        fun createParagraph(text: String): ParagraphK
        fun createHeading(text: String, level: Int): HeadingK
    }

    class ParagraphK(override val render : () -> String) : ElementK
    class HeadingK(private val level: Int, private val doRender : (Int) -> String) : ElementK {
        init {
            require(level in 1..6) { "Heading level must be between 1 and 6" }
        }
        override val render : () -> String = { doRender(level) }
    }

    object MarkdownFactoryK : ElementFactoryK { // pas d'état utiliser objet
        override fun createParagraph(text: String) = ParagraphK { "$text\n" }
        override fun createHeading(text: String, level: Int) = HeadingK(level) { level -> "${"#".repeat(level)} $text" }
    }

    /** 2. Directement traiter tous en string ce qui suipprime Parapgrah et Heading*/
    val HTMLFactoryK = ElementFactoryK2(
        createParagraph = { text -> "<p>$text</p>" },
        createHeading = { text, level -> "<h$level>$text</h$level>" }
    )

    class ElementFactoryK2(
        private val createParagraph: (String) -> String,
        private val createHeading: (String, Int) -> String,
    )
}