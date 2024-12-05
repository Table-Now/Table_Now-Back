package zerobase.tableNow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TableNowApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableNowApplication.class, args);
	}

}
