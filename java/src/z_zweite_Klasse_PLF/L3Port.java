package z_zweite_Klasse_PLF;

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
}
