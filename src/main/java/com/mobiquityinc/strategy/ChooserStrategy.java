package com.mobiquityinc.strategy;

import com.mobiquityinc.data.PackageConfiguration;
import com.mobiquityinc.data.PackageInfo;

import java.util.List;

/**
 * Abstract class for the package chooser strategy.
 */
public abstract class ChooserStrategy {

    /**
     * Calculate the current weight of the chosen packages.
     * @param chosenPackages the chosen packages.
     * @return the current weight of the chosen packages.
     */
    double currentWeight(List<PackageInfo> chosenPackages) {
        double packageWeight = 0;
        for (PackageInfo response : chosenPackages) {
            packageWeight += response.getWeight();
        }
        return packageWeight;
    }

    /**
     * Calculate the current cost of the chosen packages.
     * @param chosenPackages the chosen packages.
     * @return the current cost of the chosen packages.
     */
    int currentCost(List<PackageInfo> chosenPackages) {
        int packageCost = 0;
        for (PackageInfo response : chosenPackages) {
            packageCost += response.getCost();
        }
        return packageCost;
    }

    /**
     * Abstract method for choosing the packages.
     * @param maxWeight maximum weight for the packages.
     * @param packages input of PackageInfo to use for creating the package configuration.
     * @return the package configuration which optimizes the cost and weight.
     */
    public abstract PackageConfiguration choosePackages(double maxWeight, PackageInfo[] packages);
}
