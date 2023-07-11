package signals.trading.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import signals.trading.algo.Algo;
import signals.trading.repository.SignalConfigurationRepository;
import signals.trading.exception.InvalidSignalTypeException;
import signals.trading.model.SignalConfiguration;

@Service
@RequiredArgsConstructor
public class SignalService {

    private final SignalConfigurationRepository signalConfigurationRepository;
    private final ConfigurableSignalProcessor signalProcessor;

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
