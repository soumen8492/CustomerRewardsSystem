package CustomerRewardsApp.customerRewardsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RewardsExceptionHandler {

    @ExceptionHandler(CustomerIdNotFoundException.class)
    public ResponseEntity<String> customerIdNotFoundException(CustomerIdNotFoundException custIdNotFoundExp)
    {
        return new ResponseEntity<>(custIdNotFoundExp.toString(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> transactionNotFoundException(TransactionNotFoundException trnNotFoundExp)
    {
        return new ResponseEntity<>(trnNotFoundExp.toString(), HttpStatus.NOT_FOUND);
    }

}
