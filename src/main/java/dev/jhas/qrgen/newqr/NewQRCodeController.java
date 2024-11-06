package dev.jhas.qrgen.newqr;

import io.nayuki.qrcodegen.QrCode;
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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Controller
public class NewQRCodeController {

    private Logger logger = LoggerFactory.getLogger(NewQRCodeController.class);

    @GetMapping("/newqr")
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
                    new GenerateResponse(null, "Empty content"));
        }
        QrCode qrCode = QrCode.encodeText(body.content(), QrCode.Ecc.MEDIUM);
        var img = toImage(qrCode);
        var baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
        } catch (IOException e) {
            logger.error("Error writing image data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new GenerateResponse(null, e.getMessage())
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new GenerateResponse(Base64.getEncoder().encodeToString(baos.toByteArray()), null));
    }

    private static BufferedImage toImage(QrCode qr) {
        final var lightColor = 0xFFFFFF;
        final var darkColor = 0x000000;
        final var scale = 12;
        final var border = 2;
        Objects.requireNonNull(qr);

        BufferedImage result = new BufferedImage(
                (qr.size + border * 2) * scale,
                (qr.size + border * 2) * scale,
                BufferedImage.TYPE_INT_RGB
        );
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / scale - border, y / scale - border);
                result.setRGB(x, y, color ? darkColor : lightColor);
            }
        }
        return result;
    }
}
