package signals.trading.trading.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import signals.trading.trading.algo.Algo;
import signals.trading.trading.exception.InvalidSignalTypeException;
import signals.trading.trading.model.SignalConfiguration;
import signals.trading.trading.repository.SignalConfigurationRepository;

@Service
@RequiredArgsConstructor
public class SignalService {

    private final SignalConfigurationRepository signalConfigurationRepository;
    private final ConfigurableSignalProcessor signalProcessor = new ConfigurableSignalProcessor();

    public void processSignal(Integer signalType) {
        if (signalType == null) {
            throw new InvalidSignalTypeException("Signal type must be provided");
        }
        SignalConfiguration configuration = signalConfigurationRepository.findById(signalType)
                .orElseThrow(() -> new InvalidSignalTypeException("Invalid signal type"));
        Algo algo = new Algo();
        signalProcessor.processSignal(algo, configuration);
        algo.doAlgo();
    }
}
