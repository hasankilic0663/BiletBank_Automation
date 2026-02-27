package hooks;

import config.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestLifecycleHooks {

    private static final String SYSTEM_PROPERTY_KEY = "LATEST_REGISTERED_AGENCY";
    private static final String OUTPUT_FILE_NAME = "registered-agencies-log.txt";

    @Before
    public void initializeTestEnvironment() {
        DriverFactory.getDriver();
        System.out.println("[HOOK] WebDriver initialized.");
    }

    @After
    public void finalizeTestExecution(Scenario scenario) {

        try {
            if (scenario.isFailed()) {
                System.out.println("[HOOK] Scenario failed. No agency record will be logged.");
                return;
            }

            String registeredAgency = System.getProperty(SYSTEM_PROPERTY_KEY);

            if (registeredAgency == null || registeredAgency.trim().isEmpty()) {
                System.out.println("[HOOK] No registered agency information found in system properties.");
                return;
            }

            Path outputDirectory = Paths.get("target");
            Files.createDirectories(outputDirectory);

            Path logFilePath = outputDirectory.resolve(OUTPUT_FILE_NAME);

            String contentToWrite = registeredAgency.trim() + System.lineSeparator();

            Files.write(
                    logFilePath,
                    contentToWrite.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            System.out.println("[HOOK] Agency successfully logged to file: " + logFilePath.toAbsolutePath());

        } catch (IOException exception) {
            throw new RuntimeException("Failed to write agency log file.", exception);
        } finally {
            DriverFactory.quitDriver();
            System.out.println("[HOOK] WebDriver terminated.");
        }
    }
}