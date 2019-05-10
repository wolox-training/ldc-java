package wolox.training.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.model.BookDTO;

@Service
public class OpenLibraryService {

    private static final String API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String FORMAT_PARAMETER = "&format=json&jscmd=data";

    public BookDTO bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        return (restTemplate
            .getForObject(API_URL + isbn + FORMAT_PARAMETER, BookDTO.class));
    }
}
