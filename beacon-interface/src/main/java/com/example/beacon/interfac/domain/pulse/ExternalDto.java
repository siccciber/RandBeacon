package com.example.beacon.interfac.domain.pulse;

import com.example.beacon.interfac.infra.ExternalEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExternalDto {

    private String sourceId;

    private short statusCode;

    private String value;

    private ExternalDto(String sourceId, short statusCode, String value) {
        this.sourceId = sourceId;
        this.statusCode = statusCode;
        this.value = value;
    }

    public static ExternalDto newExternalFromEntity(ExternalEntity entity){
        return new ExternalDto(entity.getSourceId(), entity.getStatusCode(), entity.getValue());
    }

    @Override
    public String toString() {
        return "External{" +
                "sourceId='" + sourceId + '\'' +
                ", statusCode=" + statusCode +
                ", value='" + value + '\'' +
                '}';
    }
}
