package com.personal.eventhandler.model.weather;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SensorMetadata {
    private final double longitude;
    private final double latitude;
    private final String id;
    private final List<String> tags;
    private final String location;
}