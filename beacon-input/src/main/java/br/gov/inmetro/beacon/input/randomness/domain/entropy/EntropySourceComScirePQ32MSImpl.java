package br.gov.inmetro.beacon.input.randomness.domain.entropy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
    https://crunchify.com/how-to-run-windowsmac-commands-in-java-and-return-the-text-result/
*/
@Component
@Profile({"producao", "test"})
class EntropySourceComScirePQ32MSImpl implements IEntropySource {

    @Value("${beacon.entropy.command}")
    private String command;

    @Value("${beacon.entropy.command.return.line}")
    private String lineReturn;

    private static final String DESCRIPTION = "device";

    private static final Logger logger = LoggerFactory.getLogger(EntropySourceComScirePQ32MSImpl.class);

    @Override
    public EntropySourceDto getNoise512Bits() throws NoiseSourceReadException {
        String s;

        try {

            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // read the output from the command
//            System.out.println("Here is the standard output of the command:\n");
            int linha = 1;

//            String collect = stdInput.lines().collect(Collectors.joining());
//
//            if (StringUtils.isEmpty(collect)){
////                logger.error("DEVICE: device not available");
//                throw new NoiseSourceReadException("Device not available");
//            }

            while ((s = stdInput.readLine()) != null) {
                logger.warn("Line:" + s);
//                if (linha == 57){
                if (linha == Integer.parseInt(lineReturn)){
//                    return s.replaceAll(" ", "");
                    return new EntropySourceDto(s.replaceAll(" ", ""), DESCRIPTION);
                }
                linha++;
            }

//            logger.warn("Linha:" + linha);
            if (linha == 1){
                throw new NoiseSourceReadException("Device not available");
            }

        } catch (Exception e){
            throw new NoiseSourceReadException(e.getMessage());
        }

        return null;
    }

}
