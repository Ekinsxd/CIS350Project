import java.awt.*;  
import java.util.*;  
import javax.swing.*;  
import java.awt.event.ActionListener;  
import javax.swing.Timer;  
import java.awt.event.ActionEvent;

public class Clock {
    public static void main(String[] args) {  
        JFrame frm = new JFrame();  
        frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        SimpleClock clock1 = new SimpleClock();  
        frm.add(clock1);  
        frm.pack();  
        frm.setVisible(true);  
    }

    static class SimpleClock extends JPanel {  
        String stringTime;  
        int hour, minute, second;


        Calendar rite = Calendar.getInstance();
        final int startHour = rite.get(Calendar.HOUR_OF_DAY);
        final int startMinute = rite.get(Calendar.MINUTE);
        int startSecond = rite.get(Calendar.SECOND);

        /*
        aHour, bMinute, cSecond adds a 0 if it's less than
        10 so it stays a consistent 00:00:00 format.
        or else it'll be 0:0:0 if all below 10
         */
        String aHour = "";  
        String bMinute = "";  
        String cSecond = "";  

        public void setStringTime(String abc) {  
            this.stringTime = abc;  
        }

        public int Number(int a, int b) {  
            return (a <= b) ? a : b;  
        }

        SimpleClock() {  
            Timer t = new Timer(1000, new ActionListener() {  
                public void actionPerformed(ActionEvent e) {  
                    repaint();  
                }  
            });  
            t.start();  
        }
        
        @Override  
        public void paintComponent(Graphics v) {  
            super.paintComponent(v);  
            Calendar rite = Calendar.getInstance();  
            hour = rite.get(Calendar.HOUR_OF_DAY);  
            minute = rite.get(Calendar.MINUTE);  
            second = rite.get(Calendar.SECOND);
            hour -= startHour;
            minute -= startMinute;
            second -= startSecond;
            if (hour > 12) {
                hour = hour - 12;
            }
            if (second == 0) {
                startSecond = 0;
            }

            if (hour < 10) {  
                this.aHour = "0";  
            }  
            if (hour >= 10) {  
                this.aHour = "";  
            }  
            if (minute < 10) {  
                this.bMinute = "0";  
            }  
            if (minute >= 10) {  
                this.bMinute = "";  
            }  
            if (second < 10) {  
                this.cSecond = "0";  
            }  
            if (second >= 10) {  
                this.cSecond = "";  
            }  

            setStringTime(aHour + hour + ":" + bMinute + minute + ":" + cSecond + second);  
            v.setColor(Color.BLACK);  
            int length = Number(this.getWidth(), this.getHeight());  
            Font Font1 = new Font("SansSerif", Font.PLAIN, length / 20);
            v.setFont(Font1);
            v.drawString(stringTime, (int) length - 50, length / 15);
        }

        @Override  
        public Dimension getPreferredSize() {  
            return new Dimension(200, 200);  
        }
    }  
}  
