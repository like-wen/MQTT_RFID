import org.lkw.RfidLink;
import org.lkw.Select;

public class sqlTest {
    public static void main(String[] args) {
        RfidLink rfidLink = Select.selectById("0645F6E6");
        System.out.println(rfidLink.toString());

    }
}
