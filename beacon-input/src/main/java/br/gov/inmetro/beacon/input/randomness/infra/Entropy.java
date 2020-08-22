package br.gov.inmetro.beacon.input.randomness.infra;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Data
public class Entropy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Lob
    private String rawData;

    @NotNull
    private int period;

    @NotNull
    private ZonedDateTime timeStamp;

    @NotNull
    private String deviceDescription;

    @NotNull
    private String noiseSource;

    private boolean sent = true;

    public Entropy sentRemote(boolean value){
        this.sent = value;
        return this;
    }

    public Entropy setSent(boolean sent) {
        this.sent = sent;
        return this;
    }
}
