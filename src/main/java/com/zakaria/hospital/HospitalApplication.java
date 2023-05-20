package com.zakaria.hospital;

import com.zakaria.hospital.entities.Patient;
import com.zakaria.hospital.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class HospitalApplication implements CommandLineRunner {

	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//
		Patient patient = Patient.builder()
				.name("zakaria")
				.birthdate(new Date())
				.score(33)
				.sick(false)
				.build();
		// patientRepository.save(new Patient(null, "za", new Date(),true, 44));
		// patientRepository.save(new Patient(null, "the", new Date(),true, 45));
		// patientRepository.save(new Patient(null, "H2", new Date(),true, 33));
	}

	@Bean
	PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	CommandLineRunner commandLineRunnerJdbcUssrs(JdbcUserDetailsManager jdbcUserDetailsManager){
		PasswordEncoder passwordEncoder = passwordEncoder();
		return args -> {
			jdbcUserDetailsManager.createUser(
					// User.withUsername("user1").password(passwordEncoder.encode("12345")).roles("USER").build()
					User.withUsername("user1").password(passwordEncoder.encode("12345")).authorities("USER").build()
					// permissions
			);
			jdbcUserDetailsManager.createUser(
					User.withUsername("admin").password(passwordEncoder.encode("12345")).roles("ADMIN", "USER").build()
			);
		};
	}
}
