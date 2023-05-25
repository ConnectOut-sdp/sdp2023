package com.sdpteam.connectout.QrCode;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import android.graphics.Bitmap;

public class QRcodeGeneratorTest {

    private QRcodeGenerator qrCode;

    @Before
    public void setUp() {
        qrCode = new QRcodeGenerator();
    }

    @Test
    public void testGenerateQRCodeWithData() throws IllegalAccessException {
        String data = "https://connect-out.com/events/0123456789";
        Bitmap bitmap = qrCode.generateQRCode(data);
        assertNotNull(bitmap);
    }

    //@Test(expected = IllegalAccessException.class)
    public void testGenerateQRCodeWithNullData() throws IllegalAccessException {
        qrCode.generateQRCode(null);
    }

    //@Test(expected = IllegalAccessException.class)
    public void testGenerateQRCodeWithEmptyData() throws IllegalAccessException {
        qrCode.generateQRCode("");
    }
}

