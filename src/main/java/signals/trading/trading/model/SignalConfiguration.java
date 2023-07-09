package signals.trading.trading.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "signal_configurations")
@Data
@NoArgsConstructor
public class SignalConfiguration {
    @Id
    private Integer id;

    @Column(name = "steps")
    private String steps;

}
