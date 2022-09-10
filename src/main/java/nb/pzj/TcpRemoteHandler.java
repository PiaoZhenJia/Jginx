package nb.pzj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class TcpRemoteHandler extends Thread {

    Random random = new Random();
    final int localPort;
    final String[] remoteLocationList;

    public TcpRemoteHandler(int localPort, String[] remoteLocationList, String name) {
        super(name);
        this.localPort = localPort;
        this.remoteLocationList = remoteLocationList;
    }

    @Override
    public void run() {
        System.out.println("TcpRemoteHandler " + getName() + " start: listen [ " + localPort + " ] remote to " + Arrays.toString(remoteLocationList));
        try (ServerSocket serverSocket = new ServerSocket(localPort)) {
            while (true) {
                Socket accept = serverSocket.accept();
                for (int i = 0; i < 5; i++) {
                    try {
                        openOneSocket(accept);
                        break;
                    } catch (RuntimeException e) {
                        System.out.println("Failed open connection, times = " + i);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openOneSocket(Socket clientSocket) throws IOException {
        //find one server
        String server = remoteLocationList[random.nextInt(remoteLocationList.length)];
        Socket remoteServerSocket = new Socket(server.split(":")[0], Integer.valueOf(server.split(":")[1]));
        ThreadPoolKeeper.getThreadPool().execute(
                new TcpRemoteThread(remoteServerSocket, clientSocket, getName() + " @ " + clientSocket.getInetAddress().getHostAddress() + " <-- " + server));
        ThreadPoolKeeper.getThreadPool().execute(
                new TcpRemoteThread(clientSocket, remoteServerSocket, getName() + " @ " + clientSocket.getInetAddress().getHostAddress() + " --> " + server));
    }
}
