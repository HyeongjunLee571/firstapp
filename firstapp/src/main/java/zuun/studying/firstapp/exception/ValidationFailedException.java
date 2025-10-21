package zuun.studying.firstapp.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationFailedException extends RuntimeException {

    private final BindingResult bindingResult;

    public ValidationFailedException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
