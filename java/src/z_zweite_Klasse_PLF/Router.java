package z_zweite_Klasse_PLF;

public class Router extends NetworkDevice {

    private static int id_counter = 1;

    public Router(String model, L2Port... ports) {
        super(model, ports);
        id = "Router" + id_counter++;
    }
}
