package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Assert;
import org.junit.Test;

public class PackerTest {

    @Test
    public void testEmptyPath() {
        try {
            Packer.pack(null);
            Assert.fail();

        } catch (APIException e) {
            Assert.assertEquals(e.getMessage(), "filePath is null");
        }
    }

    @Test
    public void testPathNotFound() {
        try {
            Packer.pack("/file-not-found.txt");
            Assert.fail();

        } catch (APIException e) {
            Assert.assertEquals(e.getMessage(), "filePath cannot be found /file-not-found.txt");
        }
    }

    @Test
    public void testWeightMissing() {
        try {
            Packer.pack("/test-weight-missing.txt");
            Assert.fail();

        } catch (APIException e) {
            Assert.assertEquals(e.getMessage(), "weight is empty");
        }
    }

    @Test
    public void testWeightInvalid() {
        try {
            Packer.pack("/test-weight-invalid.txt");
            Assert.fail();

        } catch (APIException e) {
            Assert.assertEquals(e.getMessage(), "Weight format exception For input string: \"W\"");
        }
    }

    @Test
    public void testWeightTooBig() {
        try {
            Packer.pack("/test-weight-too-big.txt");
            Assert.fail();

        } catch (APIException e) {
            Assert.assertEquals(e.getMessage(), "weight is great than 100");
        }
    }

    @Test
    public void testLine1() {
        String response = Packer.pack("/test-line1.txt");
        Assert.assertEquals(response, "4" + System.getProperty("line.separator"));
    }

    @Test
    public void testLine2() {
        String response = Packer.pack("/test-line2.txt");
        Assert.assertEquals(response, "-" + System.getProperty("line.separator"));
    }

    @Test
    public void testLine3() {
        String response = Packer.pack("/test-line3.txt");
        Assert.assertEquals(response, "2,7" + System.getProperty("line.separator"));
    }

    @Test
    public void testLine4() {
        String response = Packer.pack("/test-line4.txt");
        Assert.assertEquals(response, "8,9" + System.getProperty("line.separator"));
    }

    @Test
    public void testOrig() {
        String response = Packer.pack("/test-orig.txt");
        Assert.assertEquals(response,
                "4" + System.getProperty("line.separator") +
                        "-" + System.getProperty("line.separator") +
                        "2,7" + System.getProperty("line.separator") +
                        "8,9" + System.getProperty("line.separator")
                );
    }

    @Test
    public void test15() {
        String response = Packer.pack("/test-15.txt");
        Assert.assertEquals(response, "7,9,10,11" + System.getProperty("line.separator"));
    }
}