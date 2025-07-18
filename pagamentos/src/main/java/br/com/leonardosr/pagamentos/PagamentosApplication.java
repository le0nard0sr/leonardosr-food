package br.com.leonardosr.pagamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PagamentosApplication {

  public static void main(String[] args) {
    SpringApplication.run(PagamentosApplication.class, args);
  }
}
