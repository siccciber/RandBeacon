package com.example.beacon.vdf.application.combination;

import br.gov.inmetro.beacon.library.ciphersuite.suite0.ICipherSuite;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.sources.SeedSourceDto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CombinationUncornCumulativeHash {

    private List<SeedUnicordCombinationVo> seedUnicordCombinationVos = new ArrayList<>();

    public List<SeedUnicordCombinationVo> calcSeedConcat(ICipherSuite cipherSuite, List<SeedSourceDto> seedList) {

        String currentValue = "";
        List<SeedUnicordCombinationVo> out = new ArrayList<>();

        for (int i = 0; i < seedList.size(); i++) {
            SeedSourceDto dto = seedList.get(i);

            if (i == 0){
                currentValue = cipherSuite.getDigest(concatValues(seedList.get(0)));
            } else {
                String teste = currentValue + cipherSuite.getDigest(concatValues(dto));
                currentValue = cipherSuite.getDigest(teste);
            }

            String cumulativeDigest = currentValue;
            ZonedDateTime parse = ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME);
            out.add(new SeedUnicordCombinationVo(dto.getUri(), dto.getSeed(), dto.getDescription(), cumulativeDigest, parse));
        }

        return out;
    }

    private String concatValues(SeedSourceDto dto){
        String concatValue = dto.getTimeStamp() + dto.getSeed().trim() + dto.getDescription().trim() + dto.getUri().trim();
        return concatValue;
    }

}

