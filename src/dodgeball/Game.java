package dodgeball;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private static final int WIDTHDIMENSION = 5;
    private static final int HEIGHTDIMENSION = 5;
    private static final int NUMBEROFPLAYERS = 10;
    private static final int REDRAWDELAY = 50;
    private static Room room;
    private static Ball ball;
    private static List<Player> players;

    public static void main(String[] args) {
        room = new Room(WIDTHDIMENSION, HEIGHTDIMENSION);
        int row = ThreadLocalRandom.current().nextInt(HEIGHTDIMENSION);
        int column = ThreadLocalRandom.current().nextInt(WIDTHDIMENSION);
        ball = new Ball(room, new int[]{row, column});
        players = new ArrayList<>();
        for(int i = 0; i < NUMBEROFPLAYERS; ++i) {
            players.add(new Player(String.valueOf((char)('A' + i)), room));
            players.get(i).start();
        }
        ball.start();
        while (room.getNumberOfActivePlayers() > 1) {
            try {
                Thread.sleep(REDRAWDELAY);
                room.drawRoom();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.print("The winner is: ");
        players.stream().filter(Thread::isAlive).forEach(System.out::println);
        players.stream().filter(Thread::isAlive).forEach(Player::setPassive);
        try {
            ball.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        players.forEach((player) -> {
            try {
                player.join();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
}
