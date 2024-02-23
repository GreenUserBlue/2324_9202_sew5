package z_zweite_Klasse_PLF;

public class Switch extends NetworkDevice{
    private static int id_counter = 1;

    public Switch(String model, L2Port... ports) {
        super(model, ports);
        id = "Switch" + id_counter++;
    }
}
