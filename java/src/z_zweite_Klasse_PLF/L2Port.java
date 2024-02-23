package z_zweite_Klasse_PLF;

public class L2Port {

    protected long mac;

    public long getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(long maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    protected long maxSpeed = 1000000000;

    public L2Port(int oui, int serialNumber) {
        this.mac = ((long) oui << 24) | serialNumber;
    }

    @Override
    public String toString() {
        String s = String.format("%012X", mac);
        s = s.replaceAll("(..)", "$1-");
        return "MAC address: " + s.substring(0, s.length() - 1) + ", maximum speed: " + maxSpeed + " bps";
    }
}
