package wolox.training.exceptions;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("The book doesn't exists");
    }

}
