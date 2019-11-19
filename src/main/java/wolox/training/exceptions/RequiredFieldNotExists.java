package wolox.training.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Required Field not exists")
public class RequiredFieldNotExists extends RuntimeException {

    public RequiredFieldNotExists(String message) {
        super(message);
    }

}
