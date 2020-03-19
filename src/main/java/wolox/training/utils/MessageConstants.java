package wolox.training.utils;

final public class MessageConstants {

    private MessageConstants() {

    }

    static public String getBookByKeyNotFoundMessage(String key, String value) {
        return ("Book with " + key + ": " + value + " not found");
    }

    static public String getUserByKeyNotFoundMessage(String key, String value) {
        return ("User with " + key + ": " + value + " not found");
    }

    static public String getEntityNotFoundMessage(Class klass) {
        return (klass.getName() + " not found");
    }

}
