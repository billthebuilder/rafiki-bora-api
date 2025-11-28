package rafikibora.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rafikibora.dto.ReceiveMoneyRequestDto;
import rafikibora.dto.ReceiveMoneyResponseDto;
import rafikibora.services.ReceiveMoneyService;
import lombok.extern.slf4j.Slf4j;
import rafikibora.handlers.LogUtil;


@RestController
@RequestMapping("/api/transactions/receive_money")
@Slf4j
public class ReceiveMoneyController {

    @Autowired
    private ReceiveMoneyService receiveMoneyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReceiveMoneyResponseDto receiveMoneyTransaction(@RequestBody ReceiveMoneyRequestDto req, HttpServletRequest request) {
        log.info("Incoming receive money request: processingCode={}, pan={}, amount={}, currency={}, transmissionDate={}, tid={}, mid={}",
                req.getProcessingCode(), req.getPan(), req.getTxnAmount(), req.getCurrency(), req.getTransmissionDateTime(), req.getTid(), req.getMid());
        ReceiveMoneyResponseDto resp = new ReceiveMoneyResponseDto();

        try {
            resp = receiveMoneyService.receiveMoney(req);
        } catch (Exception ex){
            LogUtil.logException(log, "Failed to process receive money transaction", ex);
            resp.setMessage("96");
        }
        return resp;
    }
}
