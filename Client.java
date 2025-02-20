import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Client implements ActionListener {

    static JFrame f = new JFrame();
    JTextField text;
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();

    static DataOutputStream dout;

    Client() {

        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 350, 70);
        p1.setLayout(null);
        f.add(p1);

        // back button
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);

        // close the app when on back arrow click
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // profile image
        // ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/user.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        // videoCall button
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(220, 20, 24, 24);
        p1.add(video);

        // audioCall button
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(270, 20, 24, 24);
        p1.add(phone);

        // settings button
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 20, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel morevert = new JLabel(i15);
        morevert.setBounds(310, 20, 10, 20);
        p1.add(morevert);

        // name
        JLabel name = new JLabel("Someone");
        name.setBounds(100, 20, 100, 21);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Helvetica", Font.BOLD, 17));
        p1.add(name);

        // status
        JLabel status = new JLabel("online");
        status.setBounds(100, 35, 100, 26);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Helvetica", Font.PLAIN, 11));
        p1.add(status);

        // chat panel
        a1 = new JPanel();
        a1.setBounds(5, 75, 324, 380);
        a1.setBackground(new Color(237, 237, 233));
        f.add(a1);

        // chat text typing field
        text = new JTextField();
        text.setBounds(5, 465, 250, 40);
        text.setFont(new Font("Helvicta", Font.PLAIN, 14));
        f.add(text);

        // send button
        JButton send = new JButton("Send");
        send.setBounds(260, 465, 70, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("Helvicta", Font.PLAIN, 13));
        f.add(send);

        f.setSize(350, 560);
        f.setLocation(300, 150);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String out = text.getText();

            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            if (dout != null) {
                dout.writeUTF(out);
            }

            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel(out);
        output.setFont(new Font("San-serif", Font.PLAIN, 15));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(6, 10, 6, 15));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setFont(new Font("Helvicta", Font.PLAIN, 10));
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {

        new Client();

        try {
            Socket s = new Socket("172.22.192.1", 6001);

            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while (true) {
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                System.out.println(msg);
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);

                f.validate();

                // s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
