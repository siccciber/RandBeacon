package com.example.beacon.vdf.application.combination;

import br.gov.inmetro.beacon.library.ciphersuite.suite0.CipherSuiteBuilder;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CombinationUncornCumulativeHashTest {

    @Test
    public void testOneSeed(){
        List<SeedSourceDto> seeds = new ArrayList<>();

        SeedSourceDto seedSourceDto1 = new SeedSourceDto(DateUtil.getTimeStampFormated(ZonedDateTime.now()),
                "uri", "aSeed1Test", "aDescription", null);

        seeds.add(seedSourceDto1);

        CombinationUncornCumulativeHash combinationUncornCumulativeHash = new CombinationUncornCumulativeHash();
        List<SeedUnicordCombinationVo> resultList = combinationUncornCumulativeHash.calcSeedConcat(CipherSuiteBuilder.build(0), seeds);

        Assert.assertEquals("8ee0a0899c2d82b2e5b3fdc3b6c4e7d63fb35e33194089e129a131f575c87541811f8ebdb66fceb8e84e655c2e307f592a2046fdd2ace9ffa2ce6076f35814c0",
                resultList.get(0).getCumulativeHash());

        //https://emn178.github.io/online-tools/sha512.html
    }

    @Test
    public void testTwoSeeds(){
        List<SeedSourceDto> seeds = new ArrayList<>();

        SeedSourceDto seedSourceDto1 = new SeedSourceDto(DateUtil.getTimeStampFormated(ZonedDateTime.now()),
                "uri", "aSeed1", "aDescription", null);
        SeedSourceDto seedSourceDto2 = new SeedSourceDto(DateUtil.getTimeStampFormated(ZonedDateTime.now()),
                "uri", "aSeed2", "aDescription2", null);

        seeds.add(seedSourceDto1);
        seeds.add(seedSourceDto2);

        CombinationUncornCumulativeHash combinationUncornCumulativeHash = new CombinationUncornCumulativeHash();
        List<SeedUnicordCombinationVo> resultList = combinationUncornCumulativeHash.calcSeedConcat(CipherSuiteBuilder.build(0), seeds);

        Assert.assertEquals("175a91bccfefb53dad84d4a1ea49e66e2fe846f3210e3550c1fd874bdbe283965815b6f8db2caa5e9d0fc15b6fbd8810f3017e6ac315e828e75191a94128ceda",
                resultList.get(1).getCumulativeHash());

        //https://emn178.github.io/online-tools/sha512.html
    }

    @Test
    public void testThreeSeeds(){
        List<SeedSourceDto> seeds = new ArrayList<>();

        SeedSourceDto seedSourceDto1 = new SeedSourceDto(DateUtil.getTimeStampFormated(ZonedDateTime.now()),
                "uri", "aSeed1", "aDescription", null);
        SeedSourceDto seedSourceDto2 = new SeedSourceDto(DateUtil.getTimeStampFormated(ZonedDateTime.now()),
                "uri", "aSeed2", "aDescription2", null);
        SeedSourceDto seedSourceDto3 = new SeedSourceDto(DateUtil.getTimeStampFormated(ZonedDateTime.now()),
                "uri", "aSeed2Test3", "aDescription2", null);

        seeds.add(seedSourceDto1);
        seeds.add(seedSourceDto2);
        seeds.add(seedSourceDto3);

        CombinationUncornCumulativeHash combinationUncornCumulativeHash = new CombinationUncornCumulativeHash();
        List<SeedUnicordCombinationVo> resultList = combinationUncornCumulativeHash.calcSeedConcat(CipherSuiteBuilder.build(0), seeds);

        Assert.assertEquals("132543ac76c98f8b17c4893a275acb0871deea83726929dc391f93de435e763b9c739adaad329e0cdbc29e4c578f31ab702af4682c979f0837a7e9327299d179",
                resultList.get(2).getCumulativeHash());

        //https://emn178.github.io/online-tools/sha512.html
    }


}