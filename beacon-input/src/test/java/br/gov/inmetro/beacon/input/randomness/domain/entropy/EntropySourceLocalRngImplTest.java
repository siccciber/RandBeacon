package br.gov.inmetro.beacon.input.randomness.domain.entropy;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EntropySourceLocalRngImplTest {

    @Test
    public void teste() throws NoSuchAlgorithmException {

        byte[] bytes = new byte[64];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(bytes);
        Assert.assertTrue(bytes[0] != 0);
    }

}