package z_zweite_Klasse_PLF;

public class Main {

    public static void main(String[] args) {
        // create new ports for switch1, switch2 and router1
        L2Port[] ports1 = new L2Port[24];
        for (int i=0; i<24; i++) {
            ports1[i] = new L2Port(0x00027E, 0x001234+i);
        }
        L2Port[] ports2 = new L2Port[12];
        for (int i=0; i<12; i++) {
            ports2[i] = new L2Port(0x00027E, 0x005678+i);
        }
        L2Port routerPort0 = new L3Port(0x00027E, 0x000815, 192, 168, 1, 254);
        L2Port routerPort1 = new L3Port(0x00027E, 0x000816, 10, 0, 79, 254);
        routerPort1.setMaxSpeed(10000000);
        System.out.println(ports1[0]);
        System.out.println(routerPort1);
// create new devices with these ports
        NetworkDevice switch1 = new Switch("Cisco Catalyst 9300", ports1);
        NetworkDevice switch2 = new Switch("Cisco Catalyst 2960", ports2);
        NetworkDevice router1 = new Router("Cisco 2911", routerPort0, routerPort1);
        System.out.println(switch1);
        System.out.println(switch2);
        System.out.println(router1);
// check devices for unique MAC and IP addresses
        System.out.println(switch1.getId() + " - Unique MAC address check: " +
                (switch1.checkPorts() ? "OK" : "FAIL"));
        System.out.println(switch2.getId() + " - Unique MAC address check: " +
                (switch2.checkPorts() ? "OK" : "FAIL"));
        System.out.println(router1.getId() + " - Unique MAC && IP address check: " +
                (router1.checkPorts() ? "OK" : "FAIL"));
// connect devices
        router1.addPatchCable(0, switch1, 5);
        router1.addPatchCable(1, switch2, 3);
//// create new lab and add devices
//        CiscoLab myLab = new CiscoLab("HTL Wien 3 Rennweg", 3, 5);
//        myLab.addDevice(2, 2, switch1);
//        myLab.addDevice(1, 4, switch2);
//        myLab.addDevice(0, 0, router1);
//        System.out.println(myLab);
//// get maximum path speeds
//        System.out.println("From Switch1 to Router1: " + getPathSpeed(switch1, router1) + " bps");
//        System.out.println("From Switch2 to Router1: " + getPathSpeed(switch2, router1) + " bps");
//        System.out.println("From Switch1 to Switch2 via Router1: " +
//                getPathSpeed(switch1, router1, switch2) + " bps");

    }
}
