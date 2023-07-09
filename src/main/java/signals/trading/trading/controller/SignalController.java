package signals.trading.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signals.trading.trading.service.SignalService;

@RestController
@RequestMapping("/api/signal")
public class SignalController {

    @Autowired
    private SignalService signalService;

    @GetMapping("/{signalType}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleSignal(@PathVariable Integer signalType){
        signalService.processSignal(signalType);
        return ResponseEntity.ok("Signal processed");
    }
}
