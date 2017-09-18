package dinson.customview.api;

/**
 * @author Dinson - 2017/9/18
 */
public class wifi {
    String mac_address;
    String signal_strength;
    String age;

    @Override
    public String toString() {
        return "wifi{" +
            "mac_address='" + mac_address + '\'' +
            ", signal_strength='" + signal_strength + '\'' +
            ", age='" + age + '\'' +
            '}';
    }

    public wifi(String mac_address, String signal_strength, String age) {
        this.mac_address = mac_address;
        this.signal_strength = signal_strength;
        this.age = age;
    }
}
