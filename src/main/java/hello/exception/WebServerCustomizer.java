package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/* 사실 우리가 커스텀해서 만든 WebServerCustomizer를 쓰지 않아도 됨.
에러가 발생하면 오류 페이지로 /error를 기본 요청한다 -> 그리고 스프링부트가 자동 등록한 BasicErrorController는 이 경로를 기본으로 받는다.
즉, 개발자는 오류 페이지만 등록하면 된다는 것! 개발자는 오류페이지 화면만 BasicErrorController가 제공하는 룰과 우선순위에 따라서 등록하면 된다.
정적 HTML이면 정적 리소스를 만들고, 동적으로 오류 화면을 만들고 싶으면 뷰 템플릿 경로에 오류 페이지 파일을 만들어서 넣어두기만 하면 된다.
 */
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageRuntime = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageRuntime);
    }
}
