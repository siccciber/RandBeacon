package com.example.beacon.interfac.infra;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class ExternalEntity {

    @Column(name = "ext.srcId")
    private String sourceId;

    @Column(name = "ext.status")
    private short statusCode;

    @Column(name = "ext.value")
    private String value;

    public ExternalEntity() {
    }

    public static ExternalEntity newExternalEntity(){
        return new ExternalEntity("00000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000",
                new Short("0"),
                "000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "00000000000000000000000000000000000000000000000000");
    }

    public ExternalEntity(String sourceId, short statusCode, String value) {
        this.sourceId = sourceId;
        this.statusCode = statusCode;
        this.value = value;
    }

}