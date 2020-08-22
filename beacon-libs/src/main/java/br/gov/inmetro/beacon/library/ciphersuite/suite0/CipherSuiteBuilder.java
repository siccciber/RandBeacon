package br.gov.inmetro.beacon.library.ciphersuite.suite0;

public class CipherSuiteBuilder {

    public static ICipherSuite build(int suite){
        if (suite==0){
            return new CipherSuiteZero();
        } else {
            throw new CipherSuiteAlgoritmException("Invalid suite");
        }
    }

}
