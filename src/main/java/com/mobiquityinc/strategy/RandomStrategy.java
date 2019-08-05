package com.mobiquityinc.strategy;

import com.mobiquityinc.data.PackageConfiguration;
import com.mobiquityinc.data.PackageInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Random strategy for choosing the packages.
 */
@Slf4j
public class RandomStrategy extends ChooserStrategy {

    private final static long MAX_TRIES = 200; // maximum tries to find the optimal solution (should be calculated)

    /**
     * Return whether the next index is already in the list of package info.
     * @param thisTry list of package info.
     * @param nextIndex next index to find
     * @return false if nextIndex is not found; true otherwise.
     */
    private boolean isNotUsed(List<PackageInfo> thisTry, int nextIndex) {
        for (PackageInfo packageInfo : thisTry) {
            if (nextIndex == packageInfo.getIndex()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the index into the package info which equals the nextIndex.
     * @param packages list of package info.
     * @param nextIndex next index to find.
     * @return the index into the package info which equals the nextIndex.
     */
    private int getPackageIndex(PackageInfo[] packages, int nextIndex) {
        for (int i = 0; i < packages.length; i++) {
            if (packages[i].getIndex() == nextIndex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method for choosing the packages based on random choices. Go through the packages MAX_TRIES and
     * randomly pick which packages might fit.
     * @param maxWeight maximum weight for the packages.
     * @param packages input of PackageInfo to use for creating the package configuration.
     * @return the package configuration which optimizes the cost and weight.
     */
    public PackageConfiguration choosePackages(double maxWeight, PackageInfo[] packages) {
        Random rand = new Random();
        PackageConfiguration packageConfiguration = new PackageConfiguration();
        List<PackageInfo> chosenPackages = null;

        for (int i = 0; i < MAX_TRIES; i++) {
            List<PackageInfo> thisTry = new ArrayList<>();

            for (int j = 0; j < packages.length; j++) {
                int nextIndex = rand.nextInt(packages.length) + 1;
                if (isNotUsed(thisTry, nextIndex)) {
                    int index = getPackageIndex(packages, nextIndex);
                    double newWeight = this.currentWeight(thisTry) + packages[index].getWeight();
                    if (newWeight < maxWeight) {
                        thisTry.add(packages[index]);
                    }
                }
            }

            if (chosenPackages == null) {
                chosenPackages = thisTry;
                log.debug("set chosen packages " + chosenPackages);
            } else {
                double tryWeight = this.currentWeight(thisTry);
                int tryCost = this.currentCost(thisTry);
                double chosenWeight = this.currentWeight(chosenPackages);
                int chosenCost = this.currentCost(chosenPackages);
                if ((tryCost == chosenCost && tryWeight < chosenWeight) || tryCost > chosenCost) {
                    chosenPackages = thisTry;
                    log.debug("set chosen packages " + chosenPackages);
                }
            }
        }

        // Sort the chosen packages so the indexes are in order and add them to the package configuration.
        Comparator<PackageInfo> compareById = Comparator.comparingInt(PackageInfo::getIndex);
        chosenPackages.sort(compareById);
        packageConfiguration.setChosenPackages(chosenPackages.toArray(new PackageInfo[0]));
        return packageConfiguration;
    }

}
