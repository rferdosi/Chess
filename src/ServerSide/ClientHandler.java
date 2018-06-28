package ServerSide;

import General.Board.Move;
import General.Board.Side;
import General.Game;
import General.Request;
import General.User.Player;
import General.User.SimpleUser;
import General.User.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User user;
    final private int ID;
    private boolean exitRequested = false;
    private Thread myThread;
//    private Player pairedPlayer;
//    private ClientHandler pairedCLientHandler;


    ClientHandler(Socket socket, InputStream ois, OutputStream oos, int ID) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(oos);
        this.ois = new ObjectInputStream(ois);
        this.ID = ID;
    }

    public void setThread(Thread myThread) {
        this.myThread = myThread;
    }

    @Override
    public void run() {
        while (!exitRequested) {
            try {
                Request request = (Request) ois.readObject();
                System.out.println(request);
                switch (request) {
                    case SIGN_IN:
                        login();
                        break;
                    case SIGN_UP_KEY:
                        register();
                        break;
                    case SEND_USER:
                        user = (User) ois.readObject();
                        break;
                    case EXIT:
                        exitRequested = true;
                        break;
                    case GET_USERS_TO_STRING:
                        sendSearchedUsers();
                        break;
                    case NEW_GAME_REQUEST:
                        newGameRequest();
                        break;
                    case MOVE:
                        Move move = (Move) ois.readObject();
//                        pairedPlayer.getSimpleUser()

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.err.println("Exiting");
        Thread.currentThread().interrupt();


    }


    private void register() throws IOException, ClassNotFoundException {

        boolean isEmailAccepted = true;
        boolean isUsernameAccepted = true;
        String username = (String) ois.readObject();
        for (User user1 : Server.getRegisteredUsers()) {
            if (user1.getUsername().equals(username)) {
                isUsernameAccepted = false;
                break;
            }
        }
        oos.writeObject(isUsernameAccepted);
        String email = (String) ois.readObject();
        for (User user1 : Server.getRegisteredUsers()) {
            if (user1.getEmail().equals(email)) {
                isEmailAccepted = false;
                break;
            }
        }
        oos.writeObject(isEmailAccepted);
        oos.writeObject(isUsernameAccepted);
        user = (User) ois.readObject();
        Server.getRegisteredUsers().add(user);
        Server.saveData();
        System.out.println("REG DN!");
    }


    private void login() throws IOException, ClassNotFoundException {
        user = (User) ois.readObject();
        boolean isLoggedIn = false;
        for (User user1 : Server.getRegisteredUsers()) {
            if (user1.equals(user)) {
                user = user1;
                isLoggedIn = true;
                try {
                    oos.writeObject(true);
                    oos.writeObject(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!isLoggedIn)
            oos.writeObject(false);
    }

    private void newGameRequest() throws IOException, ClassNotFoundException {
        System.out.println("now");
        SimpleUser requestedUser = (SimpleUser) ois.readObject();
        boolean isRated = ois.readBoolean();
        Side side = (Side) ois.readObject();
        if (side == Side.Automatic) {
            int i = (int) (Math.random() * 2);
            if (i == 1)
                side = Side.White;
            else
                side = Side.Black;
        }
        Player player1 = new Player(user.getSimpleUser(), side);
        Player player2 = new Player(requestedUser, side.getOutherSide());
        Game game = new Game(player1, player2, isRated);
//        pairedPlayer = player2;
//        for (ClientHandler clientHandler : Server.getActiveClients()) {
//            if (clientHandler.user.getSimpleUser().equals(pairedPlayer.getSimpleUser())){
//                pairedCLientHandler
//            }
//        }
//        Socket socket1 = new Socket()
        oos.writeObject(game);
        System.out.println("dn");
    }

    private void sendSearchedUsers() throws IOException {
        String username = ois.readUTF();
        ArrayList<SimpleUser> users = new ArrayList<>();
        for (User user1 : Server.getRegisteredUsers()) {
            if (user1.getUsername().contains(username)) {
                users.add(user1.getSimpleUser());
            }
        }
        oos.writeObject(users);
    }

}