package z_zweite_Klasse_PLF;

import java.util.Objects;

public class L2Port {

    protected long mac;

    protected L2Port connectedTo;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        L2Port l2Port = (L2Port) o;
        return mac == l2Port.mac;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mac);
    }

    public void connectTo(L2Port port) {
        port.connectedTo = this;
        connectedTo = port;
    }
}
