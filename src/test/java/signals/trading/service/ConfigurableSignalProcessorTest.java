package signals.trading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import signals.trading.algo.Algo;
import signals.trading.exception.InvalidActionException;
import signals.trading.model.SignalConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConfigurableSignalProcessorTest {
    private ObjectMapper mapper;
    private ConfigurableSignalProcessor processor;
    private Algo algo;
    private SignalConfiguration signalConfiguration;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        processor = new ConfigurableSignalProcessor();
        algo = mock(Algo.class);
        signalConfiguration = mock(SignalConfiguration.class);
    }

    @Test
    public void testProcessSignal_SetupAction_CallsSetup() throws JsonProcessingException {
        Map<String, Object> step = new HashMap<>();
        step.put("action", "setup");
        String stepsJson = mapper.writeValueAsString(Collections.singletonList(step));

        when(signalConfiguration.getSteps()).thenReturn(stepsJson);

        processor.processSignal(algo, signalConfiguration);

        verify(algo).setUp();
    }

    @Test
    public void testProcessSignal_PerformCalcAction_CallsPerformCalc() throws JsonProcessingException {
        Map<String, Object> step = new HashMap<>();
        step.put("action", "performCalc");
        String stepsJson = mapper.writeValueAsString(Collections.singletonList(step));

        when(signalConfiguration.getSteps()).thenReturn(stepsJson);

        processor.processSignal(algo, signalConfiguration);

        verify(algo).performCalc();
    }

    @Test
    public void testProcessSignal_InvalidAction_ThrowsException() throws JsonProcessingException {
        Map<String, Object> step = new HashMap<>();
        step.put("action", "invalidAction");
        String stepsJson = mapper.writeValueAsString(Collections.singletonList(step));

        when(signalConfiguration.getSteps()).thenReturn(stepsJson);

        assertThrows(IllegalArgumentException.class, () -> processor.processSignal(algo, signalConfiguration));
    }

    @Test
    public void testProcessSignal_InvalidSteps_ThrowsException() {
        when(signalConfiguration.getSteps()).thenReturn("invalidSteps");

        assertThrows(RuntimeException.class, () -> processor.processSignal(algo, signalConfiguration));
    }

    @Test
    public void testProcessSignal_Type_in_Action_ThrowsException() throws JsonProcessingException{
        Map<String, Object> step = new HashMap<>();
        step.put("act", "performCalc");
        String stepsJson = mapper.writeValueAsString(Collections.singletonList(step));

        when(signalConfiguration.getSteps()).thenReturn(stepsJson);

        assertThrows(InvalidActionException.class, () -> processor.processSignal(algo, signalConfiguration));
    }
}
