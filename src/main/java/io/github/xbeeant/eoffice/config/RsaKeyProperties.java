package io.github.xbeeant.eoffice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaobiao
 * @version 2022/1/20
 */
@Configuration
public class RsaKeyProperties {
    @Value("${rsa.private-key}")
    private String privateKey;

    @Value("${rsa.public-key}")
    private String publicKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
