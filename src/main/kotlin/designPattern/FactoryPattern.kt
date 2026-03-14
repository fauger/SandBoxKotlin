package designPattern

/**
 * Abstract Product                    Abstract Creator
 *      ^                                    ^
 *      |                                    |
 *      |                                    |
 * Concrete Product <--instanciate-- Concrete Creator
 */
/**1. Basic proposition mais pas resilient au changement */
class FactoryDemo {
    class BadDocument(private val format: Format) {
        private val elements = mutableListOf<String>()

        fun addParagraph(text: String) {
            val paragraph = when (format) {
                Format.HTML -> "<p>$text</p>"
                Format.MARKDOWN -> "$text\n"
            }
            elements.add(paragraph)
        }

        fun render() = elements.joinToString("\n")

        enum class Format {
            HTML,
            MARKDOWN
        }

    }

    fun badMain() {
        val document = BadDocument(BadDocument.Format.MARKDOWN)
        document.addParagraph("Hello, world!")
        document.addParagraph("This is a paragraph.")
        println(document.render())
    }

    /**2. Amélioration Phase 1 - on extrait les types de rendu du paragraph dans 2 documents spécialisés  */

    abstract class Document() { // Document est le creator

        /*
    - On pourrait utiliser les String, mais généralement on utiilise des types plus forts appelés Products
    On va introduire le type Element en tant que <product> -
    private val elements = mutableListOf<String>()
    protected abstract fun createParagraph(text: String): String */

        private val elements = mutableListOf<Element>()
        protected abstract fun createParagraph(text: String): Element


        fun addParagraph(text: String) = elements.add(createParagraph(text))

        fun render() = elements.joinToString("\n") { it.render() }

    }

    // Les implémentations de Document sont des croncretes creators, ils fournissent des products
    class HTMLDocument : Document() {
        override fun createParagraph(text: String) = HTMLParagraph(text)
    }

    class MarkdownDocument : Document() {
        override fun createParagraph(text: String) = MarkdownParagraph(text)
    }

    /**2. Amélioration Phase 3 - On ajoute un vrai product Element plutôt que string */
    interface Element { // Element est le product
        fun render(): String
    }

    abstract class Paragraph(private val text: String) : Element

    //Concrete products
    class HTMLParagraph(val text: String) : Paragraph(text) {
        override fun render() = "<p>$text</p>"
    }

    class MarkdownParagraph(val text: String) : Paragraph(text) {
        override fun render() = "$text\n"
    }

    fun main() {
        val htmlDocument = HTMLDocument()
        htmlDocument.addParagraph("Hello, world!")
        htmlDocument.addParagraph("This is a paragraph.")
        println(htmlDocument.render())

        val markdownDocument = MarkdownDocument()
        markdownDocument.addParagraph("Hello, world!")
        markdownDocument.addParagraph("This is a paragraph.")
        println(markdownDocument.render())
    }
}