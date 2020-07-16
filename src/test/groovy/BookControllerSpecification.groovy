import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import wolox.training.controllers.BookController
import wolox.training.models.Book
import wolox.training.repositories.BookRepository

class BookControllerSpecification extends Specification {

    @Autowired
    BookController bookController;

    @Autowired
    BookRepository bookRepository;

    /**
     * Runs once per Specification
     */
    def setupSpec(){

    }

    /**
     * Runs before every feature
     */
    def setup(){
        bookRepository = Stub(BookRepository)
        bookRepository.findById(_) >> { int id ->
            if (id == 1) {
                Book book = new Book()
                book.setAuthor("Pepe Mujica")
                book.setImage("url")
                book.setTitle("Dramatic title")
                book.setSubtitle("A drama story")
                book.setYear("1987")
                book.setPublisher("Editorial Losbe")
                book.setPages(910)
                book.setIsbn("00000001")
                return book
            }
            else if (id == 2) {
                Book book = new Book()
                book.setAuthor("Sarah O'Connor")
                book.setImage("url")
                book.setTitle("Terror title")
                book.setSubtitle("A terror story")
                book.setYear("2010")
                book.setPublisher("Editorial Terrorifica")
                book.setPages(1000)
                book.setIsbn("00000002")
                return book
            } else {
                return null
            }
        }
    }

    def "groovy check scenario: one plus one should equal two"() {
        expect:
            1 + 1 == 2
    }

}