package br.gov.inmetro.beacon.input.randomness.domain.entropy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Profile({"default", "validacao"})
class EntropySourceLocalRngImpl implements IEntropySource {

    private final Environment env;

    private static final String DESCRIPTION = "LocalRNG";

    @Autowired
    EntropySourceLocalRngImpl(Environment env) {
        this.env = env;
    }

    @Override
    public EntropySourceDto getNoise512Bits() throws Exception {
        byte[] bytes = new byte[64];
        SecureRandom.getInstance(env.getProperty("beacon.entropy.local.rng")).nextBytes(bytes);
        return new EntropySourceDto(bytesToHex(bytes), DESCRIPTION);
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}