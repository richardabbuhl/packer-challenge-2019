package com.mobiquityinc.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Configuration for packages (once packed).
 */
@Getter
@Setter
@ToString
public class PackageConfiguration {
    PackageInfo[] chosenPackages;
}
