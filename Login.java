import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/* ----------- ROUNDED TEXT FIELD ----------- */

class RoundedTextField extends JTextField {

    public RoundedTextField(int size){
        super(size);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g){
        g.setColor(new Color(0,102,204));
        g.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
    }
}

/* ----------- LOGIN SCREEN ----------- */

public class Login extends JFrame implements ActionListener{

    RoundedTextField user,pass;
    JButton login;

    Login(){

        setTitle("College Timetable System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel background = new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;

                GradientPaint gp=new GradientPaint(
                        0,0,new Color(0,102,204),
                        getWidth(),getHeight(),new Color(153,204,255));

                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };

        background.setLayout(new BorderLayout());
        setContentPane(background);

        /* LOGO */

        ImageIcon icon=new ImageIcon("ses_logo.png");
        Image img=icon.getImage().getScaledInstance(150,150,Image.SCALE_SMOOTH);

        JLabel logo=new JLabel(new ImageIcon(img));
        logo.setHorizontalAlignment(JLabel.CENTER);

        JLabel title=new JLabel("College Timetable Management System",JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,32));
        title.setForeground(Color.WHITE);

        JPanel top=new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(logo,BorderLayout.CENTER);
        top.add(title,BorderLayout.SOUTH);

        add(top,BorderLayout.NORTH);

        /* LOGIN CARD */

        JPanel card=new JPanel(new GridLayout(3,2,20,20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(40,60,40,60));

        JLabel l1=new JLabel("Username");
        JLabel l2=new JLabel("Password");

        l1.setFont(new Font("Arial",Font.BOLD,16));
        l2.setFont(new Font("Arial",Font.BOLD,16));

        user=new RoundedTextField(20);
        pass=new RoundedTextField(20);

        login=new JButton("Login");
        login.setBackground(new Color(0,102,204));
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Arial",Font.BOLD,16));

        login.addActionListener(this);

        card.add(l1);
        card.add(user);
        card.add(l2);
        card.add(pass);
        card.add(new JLabel());
        card.add(login);

        JPanel center=new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);

        add(center,BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){

        if(user.getText().equals("admin") && pass.getText().equals("admin")){
            new Dashboard();
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Invalid Login");
        }
    }

    public static void main(String args[]){
        new Login();
    }
}

/* ----------- DASHBOARD ----------- */

class Dashboard extends JFrame implements ActionListener{

    JButton add,view,exit;

    Dashboard(){

    setTitle("Dashboard");
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    JPanel bg = new JPanel();
    bg.setLayout(new BorderLayout());

    // Light pink background
    bg.setBackground(new Color(255,220,235));

    setContentPane(bg);

    JLabel title = new JLabel("Admin Dashboard",JLabel.CENTER);
    title.setFont(new Font("Arial",Font.BOLD,32));
    title.setForeground(new Color(180,0,90));

    add(title,BorderLayout.NORTH);

    add = button("Add Timetable");
    view = button("View Timetable");
    exit = button("Exit");

    add.addActionListener(this);
    view.addActionListener(this);
    exit.addActionListener(e->dispose());

    JPanel panel = new JPanel(new GridLayout(3,1,30,30));

    // IMPORTANT
    panel.setOpaque(false);

    panel.setBorder(BorderFactory.createEmptyBorder(200,500,200,500));

    panel.add(add);
    panel.add(view);
    panel.add(exit);

    add(panel,BorderLayout.CENTER);

    setVisible(true);
}

    JButton button(String text){

        JButton b=new JButton(text);
        b.setFont(new Font("Arial",Font.BOLD,18));
        b.setBackground(new Color(0,102,204));
        b.setForeground(Color.WHITE);
        return b;
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==add) new AddTimetable();
        if(e.getSource()==view) new ViewTimetable();
    }
}

/* ----------- DATA CLASS ----------- */

class TimetableEntry implements Serializable{

    String day,start,end,subject,faculty,room;

    TimetableEntry(String d,String s,String e,String sub,String f,String r){

        day=d;
        start=s;
        end=e;
        subject=sub;
        faculty=f;
        room=r;
    }
}

/* ----------- DATA STORAGE ----------- */

class TimetableData{

    static ArrayList<TimetableEntry> list=new ArrayList<>();
    static String FILE="timetable.dat";

    static{

        try{
            ObjectInputStream o=new ObjectInputStream(new FileInputStream(FILE));
            list=(ArrayList<TimetableEntry>)o.readObject();
            o.close();
        }catch(Exception e){
            list=new ArrayList<>();
        }
    }

    static void addEntry(TimetableEntry t){
        list.add(t);
        save();
    }

    static void deleteEntry(int i){
        list.remove(i);
        save();
    }

    static void save(){

        try{
            ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream(FILE));
            o.writeObject(list);
            o.close();
        }catch(Exception e){}
    }
}

/* ----------- ADD TIMETABLE ----------- */

class AddTimetable extends JFrame implements ActionListener{

    JComboBox day,start,end;
    JTextField subject,faculty,room;
    JButton save,back;

    AddTimetable(){

        setTitle("Add Timetable");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel bg=new JPanel(new GridLayout(7,2,20,20));
        bg.setBorder(BorderFactory.createEmptyBorder(100,400,100,400));
        bg.setBackground(new Color(255,220,235));
        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

        String times[]={
                "10:00 AM","11:00 AM","12:00 PM",
                "12:50 PM","1:50 PM","2:50 PM",
                "3:00 PM","4:00 PM","5:00 PM"
        };

        day=new JComboBox(days);
        start=new JComboBox(times);
        end=new JComboBox(times);

        subject=new JTextField();
        faculty=new JTextField();
        room=new JTextField();

        save=button("Save");
        back=button("Back");

        save.addActionListener(this);

        back.addActionListener(e->{
            new Dashboard();
            dispose();
        });

        bg.add(new JLabel("Day")); bg.add(day);
        bg.add(new JLabel("Start Time")); bg.add(start);
        bg.add(new JLabel("End Time")); bg.add(end);
        bg.add(new JLabel("Subject")); bg.add(subject);
        bg.add(new JLabel("Faculty")); bg.add(faculty);
        bg.add(new JLabel("Room")); bg.add(room);
        bg.add(back); bg.add(save);

        add(bg);

        setVisible(true);
    }

    JButton button(String t){

        JButton b=new JButton(t);
        b.setBackground(new Color(0,102,204));
        b.setForeground(Color.WHITE);
        return b;
    }

    public void actionPerformed(ActionEvent e){

        TimetableData.addEntry(new TimetableEntry(
                (String)day.getSelectedItem(),
                (String)start.getSelectedItem(),
                (String)end.getSelectedItem(),
                subject.getText(),
                faculty.getText(),
                room.getText()
        ));

        JOptionPane.showMessageDialog(this,"Saved");

        new Dashboard();
        dispose();
    }
}

/* ----------- VIEW TIMETABLE ----------- */

class ViewTimetable extends JFrame implements ActionListener{

    JTable table;
    DefaultTableModel model;
    JButton delete,back;

    ViewTimetable(){

        setTitle("View Timetable");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        String col[]={"Day","Start","End","Subject","Faculty","Room"};

        model=new DefaultTableModel(col,0);

        for(TimetableEntry t:TimetableData.list){

            model.addRow(new String[]{
                    t.day,t.start,t.end,t.subject,t.faculty,t.room
            });
        }

        table=new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(0,102,204));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane sp=new JScrollPane(table);

        delete=new JButton("Delete");
        back=new JButton("Back");

        delete.setBackground(new Color(0,102,204));
        delete.setForeground(Color.WHITE);

        back.setBackground(new Color(0,102,204));
        back.setForeground(Color.WHITE);

        delete.addActionListener(this);

        back.addActionListener(e->{
            new Dashboard();
            dispose();
        });

        JPanel p=new JPanel();
        p.add(delete);
        p.add(back);

        add(sp,BorderLayout.CENTER);
        add(p,BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){

        int row=table.getSelectedRow();

        if(row==-1){
            JOptionPane.showMessageDialog(this,"Select Row");
            return;
        }

        TimetableData.deleteEntry(row);
        model.removeRow(row);
    }
}