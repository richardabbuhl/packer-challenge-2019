package com.mobiquityinc.strategy;

import com.mobiquityinc.data.PackageConfiguration;
import com.mobiquityinc.data.PackageInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Loop strategy for choosing the packages.
 */
@Slf4j
public class LoopStrategy extends ChooserStrategy {

    /**
     * Method for choosing the packages based on looping. Go through the packages one by one. Pick the first one
     * and then see what fits, then the second, and so on.
     * @param maxWeight maximum weight for the packages.
     * @param packages input of PackageInfo to use for creating the package configuration.
     * @return the package configuration which optimizes the cost and weight.
     */
    public PackageConfiguration choosePackages(double maxWeight, PackageInfo[] packages) {
        PackageConfiguration packageConfiguration = new PackageConfiguration();
        ArrayList<PackageInfo> chosenPackages = null;

        for (int i = 0; i < packages.length; i++) {
            ArrayList<PackageInfo> thisTry = new ArrayList<>();
            double currentWeight = packages[i].getWeight();
            if (currentWeight < maxWeight) {
                log.debug("add to this try " + packages[i]);
                thisTry.add(packages[i]);
            }

            for (int j = 0; j < packages.length; j++) {
                if (i != j) {
                    double newWeight = this.currentWeight(thisTry) + packages[j].getWeight();
                    if (newWeight < maxWeight) {
                        thisTry.add(packages[j]);
                        log.debug("add to this try " + packages[i]);
                    }
                }
            }

            if (chosenPackages == null) {
                chosenPackages = thisTry;
                log.debug("set chosen packages = " + chosenPackages + " weight = " + this.currentWeight(chosenPackages));
            } else {
                double tryWeight = this.currentWeight(thisTry);
                int tryCost = this.currentCost(thisTry);
                double chosenWeight = this.currentWeight(chosenPackages);
                int chosenCost = this.currentCost(chosenPackages);
                log.debug("try cost = " + tryCost + " chosenCost = " + chosenCost);
                if ((tryCost == chosenCost && tryWeight < chosenWeight) || tryCost > chosenCost) {
                    chosenPackages = thisTry;
                    log.debug("set chosen packages " + chosenPackages);
                }
            }
        }

        // Sort the chosen packages so the indexes are in order and add them to the package configuration.
        if (chosenPackages != null) {
            Comparator<PackageInfo> compareById = Comparator.comparingInt(PackageInfo::getIndex);
            chosenPackages.sort(compareById);
            packageConfiguration.setChosenPackages(chosenPackages.toArray(new PackageInfo[0]));
        }
        return packageConfiguration;
    }

}
