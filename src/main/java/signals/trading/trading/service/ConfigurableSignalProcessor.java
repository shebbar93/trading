package signals.trading.trading.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import signals.trading.trading.algo.Algo;
import signals.trading.trading.model.SignalConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigurableSignalProcessor {
    private ObjectMapper mapper = new ObjectMapper();

    public void processSignal(Algo algo, SignalConfiguration configuration) {

        try {
            List<Map<String, Object>> steps = mapper.readValue(configuration.getSteps(), new TypeReference<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> step : steps) {
                String action = (String) step.get("action");
                switch (action) {
                    case "setUp":
                        algo.setUp();
                        break;
                    case "performCalc":
                        algo.performCalc();
                        break;
                    case "submitToMarket":
                        algo.submitToMarket();
                        break;
                    case "reverse":
                        algo.reverse();
                        break;
                    case "cancelTrades":
                        algo.cancelTrades();
                        break;
                    case "setAlgoParam":
                        int param = (int) step.get("param");
                        int value = (int) step.get("value");
                        algo.setAlgoParam(param, value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid action");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing steps", e);
        }
    }
}
