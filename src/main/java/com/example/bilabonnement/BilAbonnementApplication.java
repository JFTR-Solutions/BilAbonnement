package com.example.bilabonnement;

import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class BilAbonnementApplication {
  @Value("${spring.datasource.url}")
  private String db_url;

  @Value("${spring.datasource.username}")
  private String uid;

  @Value("${spring.datasource.password}")
  private String pwd;

  public String getDb_url() {
    return db_url;
  }

  public String getUid() {
    return uid;
  }

  public String getPwd() {
    return pwd;
  }


  public static void main(String[] args) {

    SpringApplication.run(BilAbonnementApplication.class, args);
/*    BilAbonnementApplication bilAbonnementApplication = new BilAbonnementApplication();
    System.out.println(bilAbonnementApplication.getDb_url());
    System.out.println(bilAbonnementApplication.getUid());
    System.out.println(bilAbonnementApplication.getPwd());

    ConnectionManager.createConnection(bilAbonnementApplication.getDb_url(), bilAbonnementApplication.getUid(), bilAbonnementApplication.getPwd());*/



  }

}
