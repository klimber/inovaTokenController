package br.com.klimber.inova.model;

import java.util.List;
import lombok.Data;

@Data
public class AvailableFeatures {

    private List<AvailableFeature> features;

}

@Data
class AvailableFeature {
    String name;
    String state;
    String extendedState;
    AdditionalFeatureInfo additionalInfo;

}

@Data
class AdditionalFeatureInfo {
    Integer usage;
}
