package br.com.fiap.cheffy.config;
import br.com.fiap.cheffy.exceptions.model.Problem;
import br.com.fiap.cheffy.model.enums.ExceptionsKeys;
import br.com.fiap.cheffy.exceptions.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static final String GENERIC_ERROR_MESSAGE = ExceptionsKeys.GENERIC_ERROR_MESSAGE.toString();
    private static final String ARGUMENT_NOT_VALID_ERROR = ExceptionsKeys.ARGUMENT_NOT_VALID_ERROR.toString();
    private static final String ERROR_ON_DESERIALIZATION = ExceptionsKeys.ERROR_ON_DESERIALIZATION.toString();
    private static final String INVALID_FORMAT_ERROR = ExceptionsKeys.INVALID_FORMAT_ERROR.toString();
    private static final String PROPERTY_BINDING_ERROR = ExceptionsKeys.PROPERTY_BINDING_ERROR.toString();
    private static final String GENERIC_RESOURCE_NOT_FOUND = ExceptionsKeys.GENERIC_RESOURCE_NOT_FOUND.toString();

    @ExceptionHandler(OperationNotAllowedException.class)
    private ResponseEntity<Object> handleOperationNotAllowedException(OperationNotAllowedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage()) ;

        HttpStatus httpStatusCode = HttpStatus.BAD_REQUEST;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(TokenExpiredException.class)
    private ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage()) ;

        HttpStatus httpStatusCode = HttpStatus.UNAUTHORIZED;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(LoginFailedException.class)
    private ResponseEntity<Object> handleLoginFailedException(LoginFailedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage()) ;
        String detail = ex.getOriginalMessage();

        HttpStatus httpStatusCode = HttpStatus.UNAUTHORIZED;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                detail)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(RegisterFailedException.class)
    private ResponseEntity<Object> handleRegisterFailedException(RegisterFailedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage()) ;

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {

        String title = ex.getEntityName() + getExceptionName(ex);;
        String message = ex.getId() != null ? getMessage(ex.getMessage()) + ex.getId() : getMessage(GENERIC_RESOURCE_NOT_FOUND);

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(getMessage(GENERIC_RESOURCE_NOT_FOUND))
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if(rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(invalidFormatException, headers, status, request);
        } else if(rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBinding(propertyBindingException, headers, status, request);
        }

        String detail = getMessage(ERROR_ON_DESERIALIZATION);

        Problem problem = createProblemBuilder(
                status,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format(getMessage(PROPERTY_BINDING_ERROR),
                path, ex.getReferringClass().getSimpleName());

        Problem problem = createProblemBuilder(
                status,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();


        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format(getMessage(INVALID_FORMAT_ERROR),
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(
                status,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String userMessage = getMessage(ARGUMENT_NOT_VALID_ERROR);
        String detail = getMessage(ERROR_ON_DESERIALIZATION);

        List<Problem.Field> problemFields = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldErrors -> {
                    String message = messageSource.getMessage(fieldErrors, LocaleContextHolder.getLocale());

                    return Problem.Field.builder()
                            .name(fieldErrors.getField())
                            .userMessage(message)
                            .build();
                }).toList();

        HttpStatus exceptionStatus = HttpStatus.BAD_REQUEST;


        Problem problem = createProblemBuilder(
                exceptionStatus,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(userMessage)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, headers, exceptionStatus, request);
    }

    @ExceptionHandler(InvalidOperationException.class)
    private ResponseEntity<Object> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    //Falback para tudo que não for tratado pelo ResponseEntityExceptionHandler
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
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
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

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status,
                                                        String title, String detail) {
        return Problem.builder()
                .status(status.value())
                .title(title)
                .timestamp(LocalDateTime.now())
                .detail(detail);
    }

    private static String getExceptionName(Exception ex) {
        return ex.getClass().getSimpleName();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }


}
