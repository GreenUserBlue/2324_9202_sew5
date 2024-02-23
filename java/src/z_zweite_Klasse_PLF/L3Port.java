package z_zweite_Klasse_PLF;

import java.util.Objects;

public class L3Port extends L2Port {

    private String ip;

    public L3Port(int oui, int serialNumber, int a3, int a2, int a1, int a0) {
        super(oui, serialNumber);
        ip = a3 + "." + a2 + "." + a1 + "." + a0;
    }


    @Override
    public String toString() {
        return super.toString() + ", IPv4 address: " + ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        L3Port l3Port = (L3Port) o;
        return Objects.equals(ip, l3Port.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ip);
    }
}
