package wolox.training.services;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.DTO.BookDTO;

@Service
class OpenLibraryService {

    private static final String API_URL = "http://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String FORMAT_PARAMETER = "&format=json&jscmd=data";

    BookDTO bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, BookDTO> optionalResponse = restTemplate
            .exchange(
                API_URL + isbn + FORMAT_PARAMETER,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, BookDTO>>() {
                }).getBody();
        if (optionalResponse != null) {
            String isbnFromResponse = optionalResponse.keySet().iterator().next();
            BookDTO bookDTO = optionalResponse.values().iterator().next();
            bookDTO.setIsbnFromResponse(isbnFromResponse);
            return bookDTO;
        }
        throw new BookNotFoundException("Book by ISBN not found in external service");
    }

}
