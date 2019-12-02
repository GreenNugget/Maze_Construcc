
public class TimeKeeper {

    public void TimeKeeper(int min, int sec) {
        if (sec + seconds <= 60) {
            minutes += min;
            seconds = sec + seconds;
        } else {
            minutes += min;
            minutes += 1 * ((sec + seconds) / 60);
            seconds = (sec + seconds) % 60;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    int minutes = 0;
    int seconds = 0;
}
