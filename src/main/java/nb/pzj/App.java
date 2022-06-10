package nb.pzj;

public class App {
    public static void main(String[] args) {
        new TcpHandler(81,"api.ptx99.com",9525).start();
    }
}
