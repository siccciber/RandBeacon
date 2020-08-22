package br.gov.inmetro.beacon.input.queue;

import br.gov.inmetro.beacon.input.randomness.domain.EntropyDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BeaconEntropyQueueSender {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "beacon_pulse_data";

    private static final String ROUTING_KEY_REGULAR = "pulse.regular";

    private static final String ROUTING_KEY_SYNC = "pulse.sync";

    @Autowired
    public BeaconEntropyQueueSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRegular(EntropyDto noiseDto) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_REGULAR, noiseDto);
    }

    public void sendSync(List<EntropyDto> list) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_SYNC, list);
    }

}
