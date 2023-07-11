package signals.trading;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = {TradingApplicationTests.Initializer.class})
class TradingApplicationTests {

    @Container
    static MySQLContainer database = new MySQLContainer("mysql:8.0.26")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext,
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword());
        }
    }
    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @BeforeEach
    public void setupDatabase() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS signal_configurations (id INT PRIMARY KEY, steps TEXT);");
            stmt.execute("INSERT IGNORE INTO signal_configurations (id, steps) VALUES " +
                    "(1, '[{\"action\":\"setUp\"},{\"action\":\"setAlgoParam\",\"argument\":[1,60]},{\"action\":\"performCalc\"},{\"action\":\"submitToMarket\"}]')," +
                    "(2, '[{\"action\":\"reverse\"},{\"action\":\"setAlgoParam\",\"argument\":[1,80]},{\"action\":\"submitToMarket\"}]')," +
                    "(3, '[{\"action\":\"setAlgoParam\",\"argument\":[1,90]},{\"action\":\"setAlgoParam\",\"argument\":[2,15]},{\"action\":\"performCalc\"},{\"action\":\"submitToMarket\"}]')," +
                    "(4, '[{\"action\":\"invalid\",\"argument\":[1,90]},{\"action\":\"setAlgoParam\",\"argument\":[2,15]},{\"action\":\"performCalc\"},{\"action\":\"submitToMarket\"}]')," +
                    "(5, '[{\"action\":\"setUp\"},{\"action\":\"setAlgoParam\",\"argument\":[1,60]},{\"action\":\"stMarket\"}]');");
            stmt.close();
        }
    }

    @Test
    public void testTypeMismatchSignal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/signal/{signalType}","abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidSignal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/signal/{signalType}", 99999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testProcessSignal_TypoInActionValue_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/signal/{signalType}", 4)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProcessSignal_TypoInAction_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/signal/{signalType}", 5)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testValidSignal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/signal/{signalType}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Signal processed"));
    }

}
