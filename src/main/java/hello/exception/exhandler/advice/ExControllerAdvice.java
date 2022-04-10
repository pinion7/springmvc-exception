package hello.exception.exhandler.advice;

import hello.exception.ex.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 여러 코드에서 발생하는 exception들을 모두 모아서 처리해주는 것! (마치 AOP를 컨트롤러에 적용하는 느낌!)
// 아래는 사실 RestController에 한한 적용임. 이외에도 ControllerAdvice는 특정 대상 컨트롤러를 각기 다르게 지정하여 예외처리해줄 수도 있음. 자세한건 자료 참고해보기
@Slf4j
//@RestControllerAdvice(basePackages = "hello.exception.api") // 이런식으로 대상 패키지 경로를 지정해줄 수도 있음
@RestControllerAdvice
public class ExControllerAdvice {

    // 현재 컨트롤러에서 ()안에 해당되는 예외가 터지면 여기서 잡아 로직이 실행됨 (그리고 @RestController이기 때문에 그대로 json형태로 반환 됨)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 정상 반환이라 @ResponseStatus를 안넣어주면 http 상태코드가 200으로 나감.
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    // 매개변수에 예외타입이 무엇인지 잘 지정해서 넘겨주면(아래에선 UserException), @ExceptionHandler의 ()에 예외클래스 지정을 생략해줘도 됨.
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST); // @ResponseStatus를 안넣어줘도 ResponseEntity로 넘기면 두번째 파라미터에 상태코드 지정이 가능!
    }

    // Exception는 최상위 예외클래스이기 때문에, 실수로 잡지 못한 모든 예외들은 여기에 공통으로 걸리게 할 수 있음
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
