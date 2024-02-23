package z_zweite_Klasse_PLF;

public class NetworkDevice {

    protected String model;

    protected L2Port[] ports;


    public String getId() {
        return id;
    }

    protected String id;

    public NetworkDevice(String model, L2Port... ports) {
        this.model = model;
        this.ports = ports;
    }

    @Override
    public String toString() {
        return
                "------------------------\n" +
                        "|" + center(id, 22) + "|\n" +
                        "|" + center(model, 22) + "|\n" +
                        "|" + center(ports.length + " ports", 22) + "|\n" +
                        "------------------------\n"
                ;
    }

    private String center(String model, int length) {
        if (model.length() >= length) {
            return model;
        }
        StringBuilder res = new StringBuilder(model);
        while (res.length() < length) {
            res = new StringBuilder(" " + res + " ");
        }

        if (res.length() != length) {   //if it is odd
            return res.substring(1);
        }
        return res.toString();
    }

    public boolean checkPorts() {
        for (int i = 0; i < ports.length; i++) {
            for (int j = i + 1; j < ports.length; j++) {
                if (ports[i].equals(ports[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addPatchCable(int myPortNumber, NetworkDevice otherDevice, int otherPortNumber) {
        ports[myPortNumber].connectTo(otherDevice.ports[otherPortNumber]);
    }
}
