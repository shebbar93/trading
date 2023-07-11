package signals.trading.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import signals.trading.algo.Algo;
import signals.trading.exception.InvalidActionException;
import signals.trading.model.SignalConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ConfigurableSignalProcessor {
    private ObjectMapper mapper = new ObjectMapper();

    public void processSignal(Algo algo, SignalConfiguration configuration) {

        try {
            List<Map<String, Object>> steps = mapper.readValue(configuration.getSteps(), new TypeReference<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> step : steps) {
                String actionString = (String) step.get("action");
                if (actionString == null) {
                    throw new InvalidActionException("Action cannot be null. Exception caught because of invalid action keyword", null);
                }
                try {
                    AlgoAction action = AlgoAction.valueOf(actionString.toUpperCase());
                    switch (action) {
                        case SETUP:
                            algo.setUp();
                            break;
                        case PERFORMCALC:
                            algo.performCalc();
                            break;
                        case SUBMITTOMARKET:
                            algo.submitToMarket();
                            break;
                        case REVERSE:
                            algo.reverse();
                            break;
                        case CANCELTRADES:
                            algo.cancelTrades();
                            break;
                        case SETALGOPARAM:
                            List<Integer> arguments = (List<Integer>) step.get("argument");
                            int param1 = arguments.get(0);
                            int param2 = arguments.get(1);
                            algo.setAlgoParam(param1, param2);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid action: " + step.get("action"));
                    }
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid action: " + actionString, e);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing steps", e);
        }
    }
}
