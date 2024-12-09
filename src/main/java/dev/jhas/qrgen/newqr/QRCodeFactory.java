package dev.jhas.qrgen.newqr;

import io.nayuki.qrcodegen.QrCode;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Base64;

public class QRCodeFactory {

    private Logger logger = LoggerFactory.getLogger(QRCodeFactory.class);

    public buildFor(String content) {
        if (content == null) return null;

        QrCode qrCode = QrCode.encodeText(content, QrCode.Ecc.MEDIUM);
        var image = toImage(qrCode);
        var baos = ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            logger.error("Error writing image data", e);
            return null;
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static BufferedImage toImage(QrCode qr) {
        final var lightColor = 0xFFFFFF;
        final var darkColor = 0x000000;
        final var scale = 12;
        final var border = 2;
        Objects.requireNonNull(qr);
        final var size = (qr.size + border * 2) * scale;
        BufferedImage result = new BufferedImage(
                size,
                size,
                BufferedImage.TYPE_INT_RGB
        );
        for (int y = 0; y < result.getHeight(); y++) {
            final var yValue = y / scale - border;
            for (int x = 0; x < result.getWidth(); x++) {
                final var xValue = x / scale - border;
                boolean color = qr.getModule(xValue, yValue);
                result.setRGB(x, y, color ? darkColor : lightColor);
            }
        }
        return result;
    }
}