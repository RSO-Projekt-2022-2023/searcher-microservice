package si.fri.rso.searcher.lib;

public class Polnilnice {

    private Integer polnilniceId;
    private Float coord_north;
    private Float coord_east;
    private Integer chargers;
    private Integer available;
    private Double distance;
    private Integer time;
    private String city;
    private String street;
    private String created;

    public Float getCoord_north() {
        return coord_north;
    }

    public void setCoord_north(Float coord_north) {
        this.coord_north = coord_north;
    }

    public Float getCoord_east() {
        return coord_east;
    }

    public void setCoord_east(Float coord_east) {
        this.coord_east = coord_east;
    }

    public Integer getChargers() {
        return chargers;
    }

    public void setChargers(Integer chargers) {
        this.chargers = chargers;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getPolnilniceId() {
        return polnilniceId;
    }

    public void setPolnilniceId(Integer searcherId) {
        this.polnilniceId = searcherId;
    }

    public void setDistance(Double distance) { this.distance = distance; }

    public Double getDistance() { return distance; }

    public void setTime(Integer time) { this.time = time; }

    public Integer getTime() { return time; }

    public void setCity(String city) { this.city = city; }

    public String getCity() { return city; }

    public void setStreet(String street) { this.street = street; }

    public String getStreet() { return street; }

    public String getCreated() { return created; }

    public void setCreated(String created) { this.created = created; }

}
