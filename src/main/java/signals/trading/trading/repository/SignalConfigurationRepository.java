package signals.trading.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import signals.trading.trading.model.SignalConfiguration;

public interface SignalConfigurationRepository extends JpaRepository<SignalConfiguration, Integer>{
}
