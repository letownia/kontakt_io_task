package letownia.model;

public class TemperatureMeasurement {
    private final Double time;
    private final Double temperature;

    public TemperatureMeasurement(Double time, Double temperature) {
        this.time = time;
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getTime() {
        return time;
    }
}
