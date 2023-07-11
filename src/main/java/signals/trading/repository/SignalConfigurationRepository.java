package signals.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import signals.trading.model.SignalConfiguration;

public interface SignalConfigurationRepository extends JpaRepository<SignalConfiguration, Integer>{
}
