package zuun.studying.firstapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@MapperScan("zuun.studying.firstapp.mapper")
@SpringBootApplication
@EnableJpaAuditing
public class FirstappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstappApplication.class, args);
	}

}
