package mana.doodleking.global.exception;

import lombok.extern.slf4j.Slf4j;
import mana.doodleking.global.response.APIResponse;
import mana.doodleking.global.response.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DoodleKingExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> ExceptionHandler(MethodArgumentNotValidException e) {
        String errorMessage = "유효성 검사 : " + e.getBindingResult()
                .getAllErrors()
                .getFirst()
                .getDefaultMessage();

        return ResponseEntity.status(400).body(APIResponse.failure(ResultCode.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIResponse<Void>> ExceptionHandler(Exception e) {
        return ResponseEntity.status(400).body(APIResponse.failure(ResultCode.BAD_REQUEST, e.getMessage()));
    }
}
