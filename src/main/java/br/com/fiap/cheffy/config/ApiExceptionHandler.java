package br.com.fiap.cheffy.config;
import br.com.fiap.cheffy.exceptions.ApiInternalServerErrorException;
import br.com.fiap.cheffy.exceptions.model.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Locale;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static final String GENERIC_ERROR_MESSAGE = "GENERIC_ERROR_MESSAGE";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {

        String detail = getMessage(GENERIC_ERROR_MESSAGE);

        Problem problem = createProblemBuilder(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiInternalServerErrorException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        log.error("EXCEÇÃO NÃO TRATADA: {}", String.valueOf(ex));


        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);

    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {

        if (body == null) {
            body = Problem.builder()
                    .status(status.value())
                    .title(ex.getClass().getSimpleName())
                    .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                    .build();

            log.error("######### Exceção com Problem nulo: {}", String.valueOf(ex));

        } else if (body instanceof String string) {
            body = Problem.builder()
                    .status(status.value())
                    .title(string)
                    .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status,
                                                        String title, String detail) {
        return Problem.builder()
                .status(status.value())
                .title(title)
                .timestamp(LocalDateTime.now())
                .detail(detail);
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }


}
