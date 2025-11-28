package rafikibora.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rafikibora.dto.SaleDto;
import rafikibora.services.SaleService;
import lombok.extern.slf4j.Slf4j;
import rafikibora.handlers.LogUtil;


@RestController
@RequestMapping("/api/transactions/sale")
@Slf4j
public class SaleController {
    @Autowired
     private SaleService saleService;

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleDto saleDto) {
        log.info("Sale request received: pan={}, processingCode={}, amount={}, terminal={}, merchant={}, currency={}",
                saleDto.getPan(), saleDto.getProcessingCode(), saleDto.getAmountTransaction(),
                saleDto.getTerminal(), saleDto.getMerchant(), saleDto.getCurrencyCode());

        boolean status;
        try{
            saleService.performSale(saleDto);
            status = true;
        }catch (Exception ex){
            LogUtil.logException(log, "Error performing sale transaction", ex);
            status = false;
        }
        if(status){
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sale transaction is invalid");
        }
    }
}
