package io.github.xbeeant.eoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  eoffice server application
 *
 * @author beeant
 */
@SpringBootApplication(scanBasePackages = "io.github.xbeeant")
public class EofficeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EofficeServerApplication.class, args);
    }

}
