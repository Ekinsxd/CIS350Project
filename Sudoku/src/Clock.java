import java.awt.*;
import java.io.Serializable;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

public class Clock {
    public static void main(String[] args) {
        //JPanel clockPanel = new JPanel();
        //SimpleClock clock1 = new SimpleClock();
        //clockPanel.add(clock1);
        //clockPanel.setVisible(true);
    }

    static class SimpleClock extends JPanel implements Serializable {
        String stringTime;
        int hour, minute, second, day;

        /*
        strHour, strMinute, strSecond adds a 0 if it's less than
        10 so it stays a consistent 00:00:00 format.
        or else it'll be 0:0:0 if all below 10
         */
        String strHour = "";
        String strMinute = "";
        String strSecond = "";

        public void setStringTime(String abc) {
            this.stringTime = abc;
        }

        SimpleClock() {  //make a timer to update every 1000 milliseconds
            Timer t = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    repaint();
                    second++;
                    timeHandler();
                }
            });
            t.start();
        }

        private void timeHandler(){// no support for days, the tiemr will not count after the game had been closed
            if (second >= 60){
                second = 0;
                minute++;
            }
            if (minute >= 60){
                minute = 0;
                hour++;
            }
            if (hour >= 24){
                hour = 0;
                day++;
            }
        }

        @Override
        public void paintComponent(Graphics v) {
            super.paintComponent(v);
//            Calendar rite = Calendar.getInstance();
//            hour = rite.get(Calendar.HOUR_OF_DAY);
//            minute = rite.get(Calendar.MINUTE);
//            second = rite.get(Calendar.SECOND);
//            hour -= startHour;
//            minute -= startMinute;
//            second -= startSecond;
            if (hour < 10) {
                this.strHour = "0";
            }
            if (hour >= 10) {
                this.strHour = "";
            }
            if (minute < 10) {
                this.strMinute = "0";
            }
            if (minute >= 10) {
                this.strMinute = "";
            }
            if (second < 10) {
                this.strSecond = "0";
            }
            if (second >= 10) {
                this.strSecond = "";
            }
            setStringTime(day + ":" + strHour + hour + ":" + strMinute + minute + ":" + strSecond + second);
            v.setColor(Color.BLACK);
            Font Font1 = new Font("ComicSans", Font.BOLD, 18);
            v.setFont(Font1);
            v.drawString(stringTime, (int)(this.getWidth()/2.33), 20);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 20);
        }
    }
}
