package co.edu.uniquindio.serviautosbackend.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MetricsInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime != null) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();

            // Contador de peticiones por método y endpoint
            Counter.builder("http.requests.total")
                    .description("Total de peticiones HTTP")
                    .tag("method", method)
                    .tag("uri", uri)
                    .tag("status", String.valueOf(status))
                    .register(meterRegistry)
                    .increment();

            // Timer para medir tiempo de respuesta
            Timer.Sample sample = Timer.start(meterRegistry);
            sample.stop(Timer.builder("http.request.duration")
                    .description("Duración de las peticiones HTTP")
                    .tag("method", method)
                    .tag("uri", uri)
                    .tag("status", String.valueOf(status))
                    .register(meterRegistry));
        }
    }
}

