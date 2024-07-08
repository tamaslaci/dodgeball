package dodgeball;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Room {
    private final int WIDTHDIMENSION;
    private final int HEIGHTDIMENSION;
    private final List<List<Thread>> room;
    private int numberOfActivePlayers;

    public Room(int width, int height) {
        this.WIDTHDIMENSION = width;
        this.HEIGHTDIMENSION = height;
        this.numberOfActivePlayers = 0;
        this.room = Collections.synchronizedList(new ArrayList<>());
        var roomRow = new ArrayList<Thread>();
        IntStream.range(0, this.getHeight())
                .forEach((i) -> roomRow.add(new Empty()));
        IntStream.range(0, this.getWidth())
                .forEach((i) -> this.room.add(Collections.synchronizedList(new ArrayList<>(roomRow))));
    }
    public int getWidth() {
        return this.WIDTHDIMENSION;
    }
    public int getHeight() {
        return this.HEIGHTDIMENSION;
    }
    public synchronized void drawRoom() {
        System.out.print("\033[H\033[2J");
        System.out.print("\u001B[0;0H");
        System.out.print("+");
        IntStream.range(0, this.getWidth()).forEach((i) -> System.out.print("-"));
        System.out.println("+");
        room.forEach((row) -> {
            System.out.print("|");
            row.forEach(System.out::print);
            System.out.println("|");
        });
        System.out.print("+");
        IntStream.range(0, this.getWidth()).forEach((i) -> System.out.print("-"));
        System.out.println("+");
        this.notifyAll();
    }
    public synchronized Thread getPosition(int[] position) {
        return room.get(position[0]).get(position[1]);
    }
    public synchronized int getNumberOfActivePlayers() {
        return this.numberOfActivePlayers;
    }
    public synchronized int[] getBallPosition() {
        int row = (int) room.stream().takeWhile((l) -> l.stream().noneMatch((i) -> i instanceof Ball)).count();
        int column = (int) room.get(row).stream().takeWhile((i) -> !(i instanceof Ball)).count();
        return new int[]{row, column};
    }
    public synchronized void setPosition(int[] position, Thread object) {
        if(object instanceof Player) ++this.numberOfActivePlayers;
        this.room.get(position[0]).set(position[1], object);
    }
    public synchronized void movePosition(int[] from, int[] to, Thread object) {
        this.room.get(from[0]).set(from[1], new Empty());
        this.room.get(to[0]).set(to[1], object);
    }
    public synchronized void emptyPosition(int[] position) {
        if(this.getPosition(position) instanceof Player) --this.numberOfActivePlayers;
        this.room.get(position[0]).set(position[1], new Empty());
    }
}
