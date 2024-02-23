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

        String res = "";

        res += oneLine ;
        res += "|" + center(id, maxLen - 2) + "|\n";
        res += oneLine;

        for (int j = 0; j < devices[0].length; j++) {
            for (int i = 0; i < devices.length; i++) {
                if(devices[i][j]!=null){
                    res += "|" + center(devices[i][j].getId(), oneItem - 2) + "|";
                }else{
                    res += "|" + center("", oneItem - 2) + "|";
                }
            }
            res+="\n";
            for (int i = 0; i < devices.length; i++) {
                if(devices[i][j]!=null){
                    res += "|" + center(devices[i][j].getModel(), oneItem - 2) + "|";
                }else{
                    res += "|" + center("", oneItem - 2) + "|";
                }
            }
            res+="\n";
            res += oneLine;
        }

        return res;
    }
}
