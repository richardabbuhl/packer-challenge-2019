package com.mobiquityinc.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Info about a packages.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PackageInfo {
    int index;
    double weight;
    int cost;
}
