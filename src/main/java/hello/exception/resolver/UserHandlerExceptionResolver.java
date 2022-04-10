package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.ex.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            // 서블릿컨테이너까지 안가고, 스프링mvc에서 예외처리가 끝나게되는 것. 결과적으로 was입장에서는 정상처리가 된 것임.(즉, 여기서 예외를 한방에 다처리해버리는 것이 핵심)
            // 서블릿 컨테이너까지 예외가 다시 올라가면 복잡하고 지저분하게 추가 프로세스가 실행되는데, 이를 ExceptionResolver가 깔끔한 과정으로 처리되게 해주는 거임.
            // 근데 문제는 아래처럼 구현하려면 상당히 복잡해지는게 사실임. 그래서 실제론 이렇게까진 안하고 스프링이 제공하는 ExceptionResolver를 활용하게 됨!
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if ("application/json".equals(acceptHeader)) {
                    HashMap<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    return new ModelAndView();
                }
                else  {
                    // text/html이면 이게호출됨
                    return new ModelAndView("error/500");
                }
            }
        } catch (Exception e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
