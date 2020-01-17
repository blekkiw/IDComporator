import javax.swing.*;
import java.awt.*;
import java.io.File;


public class GUI extends Thread {

ComparatorID comparatorID;

   public GUI (ComparatorID comparatorID) {
       this.comparatorID=comparatorID;
   }


    @Override
    public void run() {
//     JFileChooser jFileChooser = new JFileChooser();
//     File file = jFileChooser.getSelectedFile();
//
//     String absolutePath = file.getAbsolutePath();
     mainWork();

    }

    private void mainWork () {
        JLabel jLabel = new JLabel();
        JFrame jFrame = new JFrame("ComparatorID");
        jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//        jFrame.add(jLabel);
        JProgressBar jProgressBar = new JProgressBar();
        jProgressBar.setIndeterminate(true);
        jFrame.add(jProgressBar);
jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension sSize = Toolkit.getDefaultToolkit ().getScreenSize ();
        int h = (int) sSize.getHeight();
        int w = (int) sSize.getWidth();
        jFrame.setLocation(w/2-150,h/2-150);
        jFrame.setSize(300,300);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jProgressBar.setBounds(10,10,10,10);
//        String s="     working";
        while (comparatorID.itisAlive()) {

//            s+=".";
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (s.length()>30) {
//                s="     working";
//            }
//            jLabel.setText(s);
        }
        jFrame.setVisible(false);


        if (comparatorID.isIdentical()) {
            identical();
        } else {
            different();
        }
        jFrame.dispose();

    }

    private static void identical () {
        JOptionPane.showMessageDialog(null,"Files are identical", "identical", JOptionPane.INFORMATION_MESSAGE );
    }
    private static void different () {
        JOptionPane.showMessageDialog(null,"Files are different", "different", JOptionPane.ERROR_MESSAGE );
    }
}
