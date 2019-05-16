package wolox.training.service;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.model.BookDTO;

@Service
class OpenLibraryService {

    private static final String API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String FORMAT_PARAMETER = "&format=json&jscmd=data";

    Optional<BookDTO> bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
            .getForEntity(API_URL + isbn + FORMAT_PARAMETER, String.class);
        return BookDTO.buildBookDTO(response.getBody());
    }

}
