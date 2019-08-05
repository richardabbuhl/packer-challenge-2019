package com.mobiquityinc.util;

import com.mobiquityinc.exception.APIException;

import java.io.InputStream;

/**
 * Utility class for packer.
 */
public class PackerUtil {

    /**
     * Look in different places to retrieve the file.
     * @param filePath pathname of the file
     * @return an open InputStream if filePath can be found.
     */
    private InputStream getResourceAsStream(String filePath)
    {
        InputStream result = getClass().getClassLoader().getResourceAsStream(filePath);
        if (result == null) {
            result = getClass().getResourceAsStream(filePath);
            if (result == null) {
                result = ClassLoader.getSystemResourceAsStream(filePath);
            }
        }
        return result;
    }

    /**
     * Check filePath and attempt to return an InputStream.
     * @param filePath pathname of the file
     * @return an open InputStream if filePath can be found.
     */
    public InputStream openInputStream(String filePath) {
        if (filePath == null) {
            throw new APIException("filePath is null");
        }

        InputStream inputStream = this.getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new APIException("filePath cannot be found " + filePath);
        }

        return inputStream;
    }

}
