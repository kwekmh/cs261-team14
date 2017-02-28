package uk.ac.warwick.dcs.cs261.team14;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.ac.warwick.dcs.cs261.team14.data.IngestorController;

/**
 * Created by Ming on 2/11/2017.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
@EnableAsync
@PropertySource("classpath:user.properties")
public class Application {

    @Autowired
    private IngestorController ingestorController;

    public static void main(String [] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
