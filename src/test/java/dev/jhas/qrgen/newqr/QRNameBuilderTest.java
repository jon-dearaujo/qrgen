package dev.jhas.qrgen.newqr;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QRNameBuilderTest {

    @Test
    void testFromUrlDomain_ValidUrl() {
        String url = "https://www.example.com/path?query=1";
        String result = QRNameBuilder.fromUrlDomain(url);
        assertEquals("www.example.com-qrcode", result, "The domain name should be extracted correctly from the URL.");
    }

    @Test
    void testFromUrlDomain_NullUrl() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            QRNameBuilder.fromUrlDomain(null);
        });
        assertEquals("content can't be null", exception.getMessage(), "Passing null should throw a NullPointerException with the correct message.");
    }

    @Test
    void testFromUrlDomain_EmptyUrl() {
        String url = "";
        String result = QRNameBuilder.fromUrlDomain(url);
        assertEquals("qrcode", result, "An empty URL should return the default QR name.");
    }
}
