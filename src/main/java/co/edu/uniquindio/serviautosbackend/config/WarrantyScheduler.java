package co.edu.uniquindio.serviautosbackend.config;

import co.edu.uniquindio.serviautosbackend.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarrantyScheduler {
    
    private final WarrantyService warrantyService;
    
    // Ejecutar diariamente a las 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void closeExpiredWarranties() {
        warrantyService.checkAndCloseExpiredWarranties();
    }
}

