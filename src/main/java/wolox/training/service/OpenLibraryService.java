package wolox.training.service;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.model.BookDTO;

@Service
public class OpenLibraryService {

    private static final String API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String FORMAT_PARAMETER = "&format=json&jscmd=data";

    public Optional<BookDTO> bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
            .getForEntity(API_URL + isbn + FORMAT_PARAMETER, String.class);
        Optional<BookDTO> optionalBookDTO = BookDTO.buildBookDTO(response.getBody());
        return optionalBookDTO;
    }

}
