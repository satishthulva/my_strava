package com.satish.datamodels.filter;

/**
 * @author satish.thulva.
 **/
public enum FilterField {
    RUN_DATE("time"),
    DISTANCE("distance_in_km");

    private String datastoreFieldName;

    private FilterField(String fieldName) {
        this.datastoreFieldName = fieldName;
    }

    public String getDatastoreFieldName() {
        return datastoreFieldName;
    }
}
