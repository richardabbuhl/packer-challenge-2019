package com.mobiquityinc.packer;

import com.mobiquityinc.data.PackageConfiguration;
import com.mobiquityinc.data.PackageInfo;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.strategy.ChooserStrategy;
import com.mobiquityinc.strategy.RandomStrategy;
import com.mobiquityinc.util.PackerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Packer which takes a file with package info and created packing configurations which optimize
 * the weight and cost.
 */
@Slf4j
public class Packer {

    private static final int  MAX_TOTAL_WEIGHT = 100; // Max value for the total

    private final ChooserStrategy chooser = new RandomStrategy(); // Strategy for choosing the packages.

    /**
     * Retrieve the maximum weight.
     * @param weightString string containing the maximum weight.
     * @return an integer containing the maximum weight.
     */
    private int getMaxWeight(String weightString) {
        weightString = weightString.trim();
        if (StringUtils.isEmpty(weightString)) {
            log.error("weight is empty");
            throw new APIException("weight is empty");
        }
        int weight;
        try {
            weight = Integer.parseInt(weightString);
        } catch (NumberFormatException exception) {
            log.error("Weight format exception " + exception.getMessage());
            throw new APIException("Weight format exception " + exception.getMessage());
        }
        if (weight > MAX_TOTAL_WEIGHT) {
            log.error("weight is greater than {}", MAX_TOTAL_WEIGHT);
            throw new APIException("weight is great than " + MAX_TOTAL_WEIGHT);
        }
        return weight;
    }

    /**
     * Retrieve the packages.
     * @param packagesString string containing the package information.
     * @return an array of PackageInfo containing the package information.
     */
    private PackageInfo[] getPackages(String packagesString) {
        ArrayList<PackageInfo> packages = new ArrayList<>();
        String regex = "\\(.*?\\)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(packagesString);
        while (m.find()) {
            String packString = m.group().subSequence(1, m.group().length()-1).toString();
            String[] tokens = packString.split(",");
            int index = Integer.parseInt(tokens[0]);
            double weight = Double.parseDouble(tokens[1]);
            int cost;
            // Work-around for Intellij vs Maven.
            if (StringUtils.isNumeric(tokens[2].substring(1))) {
                // In Intellij unit tests the Euro sign is one character.
                cost = Integer.parseInt(tokens[2].substring(1));
            } else {
                // In Maven the Euro sign is three characters.
                cost = Integer.parseInt(tokens[2].substring(3));
            }
            PackageInfo packageRequest = new PackageInfo(index, weight, cost);
            packages.add(packageRequest);
        }
        return packages.toArray(new PackageInfo[0]);
    }

    /**
     * Given a filename parse the file and create package configuration for sending the package while
     * optimizing the cost and weight.
     * @param filePath path to the packages file.
     * @return an array of PackageConfiguration.
     */
    private static PackageConfiguration[] packAndReturnResponse (String filePath) {
        PackerUtil packerUtil = new PackerUtil();
        Packer packer = new Packer();
        InputStream stream = packerUtil.openInputStream(filePath);
        Scanner scanner = new Scanner(stream);

        ArrayList<PackageConfiguration> packageConfigurations = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(":");
            if (tokens.length > 1) {
                if (tokens[0] == null) {
                    log.error("warn: weight is null");
                }
                int weight = packer.getMaxWeight(tokens[0]);
                PackageInfo[] packages = packer.getPackages(tokens[1]);
                log.debug("weight = " + weight);
                log.debug("packages " + new ArrayList<>(Arrays.asList(packages)));
                PackageConfiguration packageConfiguration = packer.chooser.choosePackages(weight, packages);
                packageConfigurations.add(packageConfiguration);
                log.debug("response " + packageConfiguration);

            } else {
                log.error("something wrong with input line " + line);
            }
        }
        return packageConfigurations.toArray(new PackageConfiguration[0]);
    }

    /**
     * Given a filename parse the file and create package configuration for sending the package while
     * optimizing the cost and weight.
     * @param filePath path to the packages file.
     * @return a string which on every line contains the package configuration.
     * @throws APIException if an error occurs creating the package configuration.
     */
    public static String pack (String filePath) throws APIException {
        PackageConfiguration[] packageResponses = Packer.packAndReturnResponse(filePath);
        StringBuilder buffer = new StringBuilder();
        for (PackageConfiguration response : packageResponses) {
            if (response.getChosenPackages().length > 0) {
                for (int i = 0; i < response.getChosenPackages().length; i++) {
                    PackageInfo packageInfo = response.getChosenPackages()[i];
                    buffer.append(packageInfo.getIndex());
                    if (i + 1 < response.getChosenPackages().length) {
                        buffer.append(",");
                    }
                }

            } else {
                buffer.append("-");
            }
            buffer.append(System.getProperty("line.separator"));
        }
        log.debug(buffer.toString());
        return buffer.toString();
    }

}
