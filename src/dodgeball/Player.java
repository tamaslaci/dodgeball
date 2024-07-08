package dodgeball;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Player extends Thread {
    private final int DELAYFORMOVE = 100;
    private final Room room;
    private boolean isActive;
    private final String name;

    public Player(String name, Room room) {
        this.name = name;
        this.room = room;
        this.isActive = false;
    }
    @Override
    public void run() {
        int[] position = new int[2];
        synchronized (this.room) {
            while (!this.isActive) {
                position[0] = ThreadLocalRandom.current().nextInt(this.room.getHeight());
                position[1] = ThreadLocalRandom.current().nextInt(this.room.getWidth());
                if (this.room.getPosition(position) instanceof Empty) {
                    this.room.setPosition(position, this);
                    this.isActive = true;
                }
            }
            try {
                this.room.wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        int[] newPosition = new int[]{position[0], position[1]};
        while (this.isActive) {
            synchronized (this.room) {
                try {
                    if(this.isActive) this.room.wait();
                    if(!this.isActive) return;
                    boolean moved = false;
                    while (!moved && this.isActive) {
                        Thread.sleep(DELAYFORMOVE);
                        newPosition[0] += ThreadLocalRandom.current().nextInt(-1, 2);
                        newPosition[1] += ThreadLocalRandom.current().nextInt(-1, 2);
                        if (newPosition[0] < this.room.getHeight() && newPosition[0] > -1 &&
                                newPosition[1] < this.room.getWidth() && newPosition[1] > -1 &&
                                this.room.getPosition(newPosition) instanceof Empty) {
                            this.room.movePosition(position, newPosition, this);
                            position[0] = newPosition[0];
                            position[1] = newPosition[1];
                            moved = true;
                        } else {
                            newPosition[0] = position[0];
                            newPosition[1] = position[1];
                        }
                    }
                    int[] direction = new int[]{this.room.getBallPosition()[0], this.room.getBallPosition()[1]};
                    int[] contraDirection = new int[]{position[0], position[1]};
                    if (Math.abs(this.room.getBallPosition()[0] - position[0]) == 1 &&
                            this.room.getBallPosition()[1] == position[1] && this.isActive) {
                        if (!((Ball) this.room.getPosition(this.room.getBallPosition())).inMove()) {
                            while (Arrays.equals(direction, position) || Arrays.equals(direction, this.room.getBallPosition()) ||
                                    direction[0] < 0 || direction[0] >= this.room.getHeight() ||
                                    direction[1] < 0 || direction[1] >= this.room.getWidth()) {
                                int rowRandom = ThreadLocalRandom.current().nextInt(-1, 2);
                                int row = this.room.getBallPosition()[0] + rowRandom;
                                int columnRandom = ThreadLocalRandom.current().nextInt(-1, 2);
                                int column = this.room.getBallPosition()[1] + columnRandom;
                                direction = new int[]{row, column};
                                contraDirection = new int[]{position[0] - rowRandom, position[1] - columnRandom};
                            }
                            ((Ball) this.room.getPosition(this.room.getBallPosition())).throwBall(direction);
                        }
                        if (!(contraDirection[0] < 0 || contraDirection[0] >= this.room.getHeight() ||
                                contraDirection[1] < 0 || contraDirection[1] >= this.room.getWidth()) &&
                                (this.room.getPosition(contraDirection) instanceof Empty)) {
                            this.room.movePosition(position, contraDirection, this);
                            position[0] = contraDirection[0];
                            position[1] = contraDirection[1];
                        }
                    } else if (Math.abs(this.room.getBallPosition()[1] - position[1]) == 1 &&
                            this.room.getBallPosition()[0] == position[0] && this.isActive) {
                        if (!((Ball) this.room.getPosition(this.room.getBallPosition())).inMove()) {
                            while (Arrays.equals(direction, position) || Arrays.equals(direction, this.room.getBallPosition()) ||
                                    direction[0] < 0 || direction[0] >= this.room.getHeight() ||
                                    direction[1] < 0 || direction[1] >= this.room.getWidth()) {
                                int rowRandom = ThreadLocalRandom.current().nextInt(-1, 2);
                                int row = this.room.getBallPosition()[0] + rowRandom;
                                int columnRandom = ThreadLocalRandom.current().nextInt(-1, 2);
                                int column = this.room.getBallPosition()[1] + columnRandom;
                                direction = new int[]{row, column};
                                contraDirection = new int[]{position[0] - rowRandom, position[1] - columnRandom};
                            }
                            ((Ball) this.room.getPosition(this.room.getBallPosition())).throwBall(direction);
                        }
                        if (!(contraDirection[0] < 0 || contraDirection[0] >= this.room.getHeight() ||
                                contraDirection[1] < 0 || contraDirection[1] >= this.room.getWidth()) &&
                                (this.room.getPosition(contraDirection) instanceof Empty)) {
                            this.room.movePosition(position, contraDirection, this);
                            position[0] = contraDirection[0];
                            position[1] = contraDirection[1];
                        }
                    }
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    public void setPassive() {
        this.isActive = false;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
