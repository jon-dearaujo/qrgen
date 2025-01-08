package dev.jhas.qrgen.newqr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


public final class QRNameBuilder {
    private static final Logger logger = LoggerFactory.getLogger(QRNameBuilder.class);
    private static final String DEFAULT_QR_NAME = "qrcode";

    public static String fromUrlDomain(String content) {
        Objects.requireNonNull(content, "content can't be null");
        URI uri;
        try {
            uri = new URI(content);
        } catch (URISyntaxException e) {
            logger.error("Could not parse URI. Returning default name", e);
            return DEFAULT_QR_NAME;
        }
        return uri.getHost() != null ?
                "%s-%s".formatted(uri.getHost(), DEFAULT_QR_NAME) :
                DEFAULT_QR_NAME;
    }
}
