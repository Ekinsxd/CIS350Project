import java.awt.*;
import java.io.Serializable;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

/*****************************************************************
* Class that creates a timer and displays it on the GUI of the 
* Sudoku game board.
* @author Ethan Tran, Matthew Davis, and Cole Hyink
* @version 2020.12.10
*****************************************************************/
public class Clock {
    public static class SimpleClock extends JPanel implements Serializable {
        /*Holds the hour, minute, second, day for printing on screen*/
        String stringTime;
        
        /*Variables used for storing the current time from timer*/
        int hour, minute, second, day;
        
        /*Timer to keep track of time spent on current game board*/
        Timer t;
        
        /*strHour, strMinute, strSecond will add a 0 if the time is
        * below 10, otherwise it won't contain anything. For a consistent
        * 00:00:00 format*/
        String strHour = "";
        
        /* String of the current timer minute*/
        String strMinute = "";
        
        /* String of the current timer second*/
        String strSecond = "";

        /************************************************************
        * Constructor that creates the timer and starts the timer
        ***********************************************************/
        SimpleClock() {
            t = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    repaint();
                    second++;
                    timeHandler();
                }
            });
            t.start();
        }

        /************************************************************
        * Method to stop the timer
        ***********************************************************/
        public void stopTimer(){
            t.stop();
        }

        /************************************************************
        * Method to restart the timer
        ***********************************************************/
        public void restartTimer(){
            t.restart();
        }

        /************************************************************
        * Method to reset the timer clock
        ***********************************************************/
        public void resetClock(){
            second = 0;
            minute = 0;
            hour = 0;
            day = 0;
        }

        /************************************************************
        * Method that ensures the the time is displayed correctly
        ***********************************************************/
        private void timeHandler(){
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
            stringTime = (day + ":" + strHour + hour + ":" + strMinute + minute + ":" + strSecond + second);

        }

        /************************************************************
        * Returns the current time on the timer
        *
        * @return Returns the current time in a string format
        ***********************************************************/
        public String getStringTime() {
            return stringTime;
        }

        /************************************************************
        * Displays the time on the Sudoku board
        *
        * @param v The graphics on the panel
        ***********************************************************/
        @Override
        public void paintComponent(Graphics v) {
            super.paintComponent(v);
            timeHandler();
            v.setColor(Color.BLACK);
            Font Font1 = new Font("ComicSans", Font.BOLD, 18);
            v.setFont(Font1);
            v.drawString(stringTime, (int)(this.getWidth()/2.33), 20);
        }

        /************************************************************
        * Returns the current time on the timer
        *
        * @return Returns a preferred dimension size
        ***********************************************************/
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 20);
        }
    }
}
