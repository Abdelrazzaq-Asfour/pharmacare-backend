//package com.pharmacare;
//
//import com.pharmacare.model.User;
//import com.pharmacare.repository.UserRepository;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.boot.CommandLineRunner;
//
//@SpringBootApplication
//public class PharmacareApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(PharmacareApplication.class, args);
//	}
//
//	@Bean
//	CommandLineRunner initAdminPassword(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		return args -> {
//			userRepository.findByUsername("admin").ifPresent(user -> {
//				user.setPasswordHash(passwordEncoder.encode("123456"));
//				userRepository.save(user);
//				System.out.println(">>> Admin password successfully updated to 123456 <<<");
//			});
//		};
//	}
//}


package com.pharmacare;

import com.pharmacare.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class PharmacareApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmacareApplication.class, args);
	}

	// تعريف الـ PasswordEncoder هنا يحل المشكلة جذرياً ليصبح متاحاً للتطبيق وللاختبارات
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner initAdminPassword(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			try {
				userRepository.findByUsername("admin").ifPresent(user -> {
					user.setPasswordHash(passwordEncoder.encode("123456"));
					userRepository.save(user);
					System.out.println(">>> Admin password successfully updated to 123456 <<<");
				});
			} catch (Exception e) {
				// تجاهل الخطأ في بيئة الاختبارات إذا كانت الجداول لم تُنشأ بعد
				System.out.println(">>> Skipping admin password init: tables not ready yet. <<<");
			}
		};
	}
}