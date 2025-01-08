package dev.jhas.qrgen.newqr;

import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NewQRCodeController {

    private final Logger logger = LoggerFactory.getLogger(NewQRCodeController.class);

    @GetMapping("/")
    public String newQrCode() {
        return "newqr";
    }

    @PostMapping(
            value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenerateResponse> generate(@RequestBody GenerateRequest body) {
        logger.info(body.toString());
        if (body.content() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenerateResponse(null, null,"Empty content"));
        }
        var image = new QRCodeFactory().buildFor(body.content());
        var qrName = QRNameBuilder.fromUrlDomain(body.content());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new GenerateResponse(image, qrName, null));
    }
}
