package com.example.beacon.vdf.application.combination;

import br.gov.inmetro.beacon.library.ciphersuite.suite0.CipherSuiteBuilder;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CombinationUncornCumulativeHashTest {

    private SeedSourceDto seedSourceDto1;
    private SeedSourceDto seedSourceDto2;
    private SeedSourceDto seedSourceDto3;

    @Before
    public void init(){

        seedSourceDto1 = new SeedSourceDto();
        seedSourceDto1.setTimeStamp("2022-04-02T19:50:00.000Z");
        seedSourceDto1.setSeed("2E1A3C018A8D03407AD113F1A6F0E6BBE4F1CCDADA939E013EFD3B71AE81A4552EBBEB168179FC45FC39B824F550F43BC8E1C1953EBD9E401888C7276C1B94C7");
        seedSourceDto1.setDescription("Last precommitment NIST");
        seedSourceDto1.setUri("https://beacon.nist.gov/beacon/2.0/chain/1/pulse/1792454");

        seedSourceDto2 = new SeedSourceDto();
        seedSourceDto2.setTimeStamp("2022-04-02T19:50:00.000Z");
        seedSourceDto2.setSeed("d70ab6656ad1a6117332e3ec0d5918e4c4990f5f42528d28cf22d780166d0090979f1bdf2a854c2461b757c70b6e2bdc170ec89d8cd1cd916e6b42396b8d59c6");
        seedSourceDto2.setDescription("Last precommitment Chile");
        seedSourceDto2.setUri("https://beacon.clcert.cl/beacon/2.0/chain/4/pulse/1897960");

        seedSourceDto3 = new SeedSourceDto();
        seedSourceDto3.setTimeStamp("2022-04-02T19:50:00.000Z");
        seedSourceDto3.setSeed("fcb0354fdb2ccd3e74df669ba7880a27feea9118dec737781ff3e0791dac4a76ec0a61dbc06f6d5f69e00aa3b7395e1fe73b85ed10f1502115a4eecb5ece735b");
        seedSourceDto3.setDescription("Local Precommitment");
        seedSourceDto3.setUri("http://localhost:8080/beacon/2.0/chain/1/pulse/1054223");
    }


    @Test
    public void testOneSeedV2(){
        List<SeedSourceDto> seeds = new ArrayList<>();
        seeds.add(seedSourceDto1);

        CombinationUncornCumulativeHash combinationUncornCumulativeHash = new CombinationUncornCumulativeHash();
        List<SeedUnicordCombinationVo> resultList = combinationUncornCumulativeHash.calcSeedConcat(CipherSuiteBuilder.build(0), seeds);

        Assert.assertEquals("413572b69baa09c1c6c996a4c2203d0b81c5750dcb577d227eedd5c1f4df750660cf202e1f68534a19e2c3dec62a86c87f50c006302b5f27a4a064ea70eabd30",
                resultList.get(0).getCumulativeHash());

        //https://emn178.github.io/online-tools/sha512.html
    }

    @Test
    public void testTwoSeeds(){
        List<SeedSourceDto> seeds = new ArrayList<>();

        seeds.add(seedSourceDto1);
        seeds.add(seedSourceDto2);

        CombinationUncornCumulativeHash combinationUncornCumulativeHash = new CombinationUncornCumulativeHash();
        List<SeedUnicordCombinationVo> resultList = combinationUncornCumulativeHash.calcSeedConcat(CipherSuiteBuilder.build(0), seeds);

        Assert.assertEquals("c23e377470c3008dc629c1dc01326f724f9f622bb15ba8600d75ae97ad1fdd2afce2a36596206f5f739d58a408aac1be5428f2e86323c8e137cd2e7264d8e380",
                resultList.get(1).getCumulativeHash());

        //https://emn178.github.io/online-tools/sha512.html
    }

    @Test
    public void testThreeSeeds(){
        List<SeedSourceDto> seeds = new ArrayList<>();

        seeds.add(seedSourceDto1);
        seeds.add(seedSourceDto2);
        seeds.add(seedSourceDto3);

        CombinationUncornCumulativeHash combinationUncornCumulativeHash = new CombinationUncornCumulativeHash();
        List<SeedUnicordCombinationVo> resultList = combinationUncornCumulativeHash.calcSeedConcat(CipherSuiteBuilder.build(0), seeds);

        Assert.assertEquals("cf5e22ac8bbf706ec2499b5e211aa49944cff1dc12582b2c5369f3679ae4f2560a6904206a042194e1f05a8ecf720c0afd6221d0cd59e7246797cc54457125f2",
                resultList.get(2).getCumulativeHash());

        //https://emn178.github.io/online-tools/sha512.html
    }

}