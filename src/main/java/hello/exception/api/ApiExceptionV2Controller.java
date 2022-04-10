package hello.exception.api;

import hello.exception.ex.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    // ControllerAdvice에 전부 이식함 (그럼 컨트롤러마다 중복으로 안붙히고 적용 및 관리가 가능!)
//    // 현재 컨트롤러에서 ()안에 해당되는 예외가 터지면 여기서 잡아 로직이 실행됨 (그리고 @RestController이기 때문에 그대로 json형태로 반환 됨)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 정상 반환이라 @ResponseStatus를 안넣어주면 http 상태코드가 200으로 나감.
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExHandler(IllegalArgumentException e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("BAD", e.getMessage());
//    }
//
//    // 매개변수에 예외타입이 무엇인지 잘 지정해서 넘겨주면(아래에선 UserException), @ExceptionHandler의 ()에 예외클래스 지정을 생략해줘도 됨.
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
//        log.error("[exceptionHandler] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST); // @ResponseStatus를 안넣어줘도 ResponseEntity로 넘기면 두번째 파라미터에 상태코드 지정이 가능!
//    }
//
//    // Exception는 최상위 예외클래스이기 때문에, 실수로 잡지 못한 모든 예외들은 여기에 공통으로 걸리게 할 수 있음
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
