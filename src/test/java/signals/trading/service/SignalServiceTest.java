package signals.trading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import signals.trading.algo.Algo;
import signals.trading.exception.InvalidSignalTypeException;
import signals.trading.model.SignalConfiguration;
import signals.trading.repository.SignalConfigurationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignalServiceTest {

    @Mock
    private SignalConfigurationRepository signalConfigurationRepository;

    @Mock
    private ConfigurableSignalProcessor signalProcessor;

    @InjectMocks
    private SignalService signalService;

    private SignalConfiguration mockSignalConfiguration;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        mockSignalConfiguration = new SignalConfiguration();
        mockSignalConfiguration.setId(1);
        mockSignalConfiguration.setSteps("[{\"action\":\"SETUP\"},{\"action\":\"PERFORMCALC\"}]");
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testProcessSignal_NullSignalType_ThrowsInvalidSignalTypeException() {
        assertThrows(InvalidSignalTypeException.class, () -> {
            signalService.processSignal(null);
        });
    }

    @Test
    public void testProcessSignal_InvalidSignalType_ThrowsInvalidSignalTypeException() {
        when(signalConfigurationRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(InvalidSignalTypeException.class, () -> {
            signalService.processSignal(1);
        });
    }

    @Test
    public void testProcessSignal_ValidSignalType_ProcessesSuccessfully() throws JsonProcessingException {
        when(signalConfigurationRepository.findById(eq(1))).thenReturn(Optional.of(mockSignalConfiguration));
        doNothing().when(signalProcessor).processSignal(any(Algo.class), eq(mockSignalConfiguration));

        signalService.processSignal(1);

        verify(signalProcessor).processSignal(any(Algo.class), eq(mockSignalConfiguration));
        verify(signalConfigurationRepository).findById(eq(1));
    }
}
