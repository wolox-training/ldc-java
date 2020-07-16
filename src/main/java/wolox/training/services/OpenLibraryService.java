package wolox.training.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import wolox.training.models.DTO.BookDTO;

@Service
class OpenLibraryService {

    private static final String SERVER_URL = "http://openlibrary.org";
    private static final String BOOKS_URI = "/api/books";


    Optional<BookDTO> bookInfo(String isbn) {
        ClientResponse clientResponse = WebClient
                .builder()
                .baseUrl(SERVER_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(BOOKS_URI)
                        .queryParam("bibkeys", ("ISBN:" + isbn))
                        .queryParam("format", "json")
                        .queryParam("jscmd", "data")
                        .build()
                )
                .exchange()
                .block();
        // The response will be an object with ISBN as a key and the entire object as the value
        Optional<Map<String, LinkedHashMap<String, Object>>> response = Optional
                .ofNullable(clientResponse.bodyToMono(Map.class).block());
        return response.map(responseData -> responseData.get("ISBN:" + isbn))
                .map(book -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    BookDTO bookDTO = objectMapper.convertValue(book, BookDTO.class);
                    bookDTO.setIsbn(isbn);
                    return bookDTO;
                });
    }

}
