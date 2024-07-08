package dodgeball;

public class Ball extends Thread{
    private final int DELAYFORMOVE = 50;
    private final Room room;
    private int[] position;
    private int[] moveTo;

    public Ball(Room room, int[] position) {
        this.room = room;
        this.position = position;
        this.moveTo = position;
        this.room.setPosition(this.position, this);
    }
    public synchronized boolean inMove() {
        return this.position[0] != this.moveTo[0] || this.position[1] != this.moveTo[1];
    }
    @Override
    public void run() {
        while(this.room.getNumberOfActivePlayers() > 1){
            synchronized (this.room) {
                try {
                    if(this.room.getNumberOfActivePlayers() > 1) this.room.wait();
                    Thread.sleep(DELAYFORMOVE);
                    boolean moveToInRoom = this.moveTo[0] > -1 && this.moveTo[0] < this.room.getHeight() &&
                            this.moveTo[1] > -1 && this.moveTo[1] < this.room.getWidth();
                    if (this.inMove() && moveToInRoom) {
                        if (this.room.getPosition(this.moveTo) instanceof Player) {
                            ((Player) this.room.getPosition(this.moveTo)).setPassive();
                            this.room.emptyPosition(this.moveTo);
                            this.room.movePosition(this.position, this.moveTo, this);
                            this.position[0] = this.moveTo[0];
                            this.position[1] = this.moveTo[1];
                        } else {
                            this.room.movePosition(this.position, this.moveTo, this);
                            int[] nextPosition = new int[]{this.moveTo[0], this.moveTo[1]};
                            int[] nextMoveTo = new int[]{this.moveTo[0], this.moveTo[1]};
                            nextMoveTo[0] += this.moveTo[0] - this.position[0];
                            nextMoveTo[1] += this.moveTo[1] - this.position[1];
                            this.position = new int[]{nextPosition[0], nextPosition[1]};
                            this.moveTo = new int[]{nextMoveTo[0], nextMoveTo[1]};
                        }
                    } else {
                        this.moveTo[0] = this.position[0];
                        this.moveTo[1] = this.position[1];
                    }
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    public synchronized void throwBall(int[] to){
        this.moveTo = to;
    }
    @Override
    public String toString() {
        return "o";
    }
}
