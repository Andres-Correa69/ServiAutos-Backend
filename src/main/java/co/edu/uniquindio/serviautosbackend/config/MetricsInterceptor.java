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
        // Excluir endpoints de actuator y health check para evitar problemas
        String uri = request.getRequestURI();
        if (uri != null && (uri.startsWith("/actuator") || uri.equals("/health"))) {
            return true;
        }
        
        // Guardar el tiempo de inicio y crear el sample del timer
        request.setAttribute("startTime", System.currentTimeMillis());
        Timer.Sample sample = Timer.start(meterRegistry);
        request.setAttribute("timerSample", sample);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            // Excluir endpoints de actuator y health check
            String uri = request.getRequestURI();
            if (uri != null && (uri.startsWith("/actuator") || uri.equals("/health"))) {
                return;
            }
            
            Long startTime = (Long) request.getAttribute("startTime");
            Timer.Sample sample = (Timer.Sample) request.getAttribute("timerSample");
            
            if (startTime != null && sample != null) {
                String method = request.getMethod();
                int status = response.getStatus();

                // Contador de peticiones por método y endpoint
                Counter.builder("http.requests.total")
                        .description("Total de peticiones HTTP")
                        .tag("method", method)
                        .tag("uri", uri)
                        .tag("status", String.valueOf(status))
                        .register(meterRegistry)
                        .increment();

                // Detener el timer y registrar la duración
                sample.stop(Timer.builder("http.request.duration")
                        .description("Duración de las peticiones HTTP")
                        .tag("method", method)
                        .tag("uri", uri)
                        .tag("status", String.valueOf(status))
                        .register(meterRegistry));
            }
        } catch (Exception e) {
            // Si hay algún error en las métricas, no debe afectar la petición
            // Solo loguear el error (en producción usar un logger)
            System.err.println("Error al registrar métricas: " + e.getMessage());
        }
    }
}

