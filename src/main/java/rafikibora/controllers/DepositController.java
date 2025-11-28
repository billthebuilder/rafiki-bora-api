package rafikibora.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rafikibora.dto.DepositDto;
import rafikibora.services.DepositService;
import lombok.extern.slf4j.Slf4j;
import rafikibora.handlers.LogUtil;

@RestController
@RequestMapping("/api/transactions/deposit")
@Slf4j
public class DepositController {
    @Autowired
    private DepositService depositService;

//    @Autowired
//    Transaction transaction;

    @PostMapping
    public ResponseEntity<?> createDeposit(@RequestBody DepositDto depositDto) {


        log.info("Deposit request received: processingCode={}, merchantPan={}, customerPan={}, amount={}, dateTime={}, terminal={}, merchant={}, currency={}",
                depositDto.getProcessingCode(), depositDto.getMerchantPan(), depositDto.getCustomerPan(),
                depositDto.getAmountTransaction(), depositDto.getDateTimeTransmission(), depositDto.getTerminal(),
                depositDto.getMerchant(), depositDto.getCurrencyCode());

        boolean status;
        try{
            depositService.performDeposit(depositDto);
            status = true;
        }catch (Exception ex){
            LogUtil.logException(log, "Error performing deposit transaction", ex);
            status = false;
        }
        if(status){
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deposit transaction is invalid");
        }
    }
}
