package com.example.beacon.interfac.infra;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "list_value_unicorn")
public class ListValueUnicornEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private String type;

    private String uri;

    @ManyToOne
    @JoinColumn(name = "pulse_id")
    private PulseEntity pulseEntity;

    public ListValueUnicornEntity() {
    }

    public ListValueUnicornEntity(String value, String type, String uri, PulseEntity pulseEntity) {
        this.value = value;
        this.type = type;
        this.uri = uri;
        this.pulseEntity = pulseEntity;
    }

}
