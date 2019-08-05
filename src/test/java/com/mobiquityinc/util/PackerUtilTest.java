package com.mobiquityinc.util;

import com.mobiquityinc.exception.APIException;
import org.junit.Test;

public class PackerUtilTest {

    @Test(expected = APIException.class)
    public void testNullInputStream() {
        PackerUtil packerUtil = new PackerUtil();
        packerUtil.openInputStream(null);
    }

    @Test(expected = APIException.class)
    public void testFileNotFound() {
        PackerUtil packerUtil = new PackerUtil();
        packerUtil.openInputStream("filenotfound.txt");
    }
}