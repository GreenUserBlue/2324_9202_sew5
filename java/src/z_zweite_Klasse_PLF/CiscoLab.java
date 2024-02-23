package z_zweite_Klasse_PLF;

import static z_zweite_Klasse_PLF.Main.center;

public class CiscoLab {

    private String id;

    private NetworkDevice[][] devices;

    public CiscoLab(String id, int racks, int devicesPerRack) {
        this.id = id;
        devices = new NetworkDevice[racks][devicesPerRack];
    }

    public void addDevice(int rack, int pos, NetworkDevice device) {
        devices[rack][pos] = device;
    }

    @Override
    public String toString() {
        final int oneItem = 24;
        int maxLen = devices.length * oneItem;
        String oneLine = "------------------------".repeat(devices.length) + "\n";

        StringBuilder res = new StringBuilder();

        res.append(oneLine);
        res.append("|").append(center(id, maxLen - 2)).append("|\n");
        res.append(oneLine);

        for (int j = 0; j < devices[0].length; j++) {
            for (NetworkDevice[] networkDevices : devices) {
                if (networkDevices[j] != null) {
                    res.append("|").append(center(networkDevices[j].getId(), oneItem - 2)).append("|");
                } else {
                    res.append("|").append(center("", oneItem - 2)).append("|");
                }
            }
            res.append("\n");
            for (NetworkDevice[] device : devices) {
                if (device[j] != null) {
                    res.append("|").append(center(device[j].getModel(), oneItem - 2)).append("|");
                } else {
                    res.append("|").append(center("", oneItem - 2)).append("|");
                }
            }
            res.append("\n");
            res.append(oneLine);
        }
        return res.toString();
    }
}
