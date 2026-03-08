import java.util.*;

interface Command{
    void execute();
    void undo();
}

class Light{
    void on(){System.out.println("Light ON");}
    void off(){System.out.println("Light OFF");}
}

class Door{
    void open(){System.out.println("Door OPEN");}
    void close(){System.out.println("Door CLOSE");}
}

class Thermostat{
    int temp = 20;

    void up(){
        temp++;
        System.out.println("Temp: " + temp);
    }

    void down(){
        temp--;
        System.out.println("Temp: " + temp);
    }
}

class LightOn implements Command{
    Light l;
    LightOn(Light l){this.l=l;}
    public void execute(){l.on();}
    public void undo(){l.off();}
}

class DoorOpen implements Command{
    Door d;
    DoorOpen(Door d){this.d=d;}
    public void execute(){d.open();}
    public void undo(){d.close();}
}

class TempUp implements Command{
    Thermostat t;
    TempUp(Thermostat t){this.t=t;}
    public void execute(){t.up();}
    public void undo(){t.down();}
}

class Invoker{
    Stack<Command> history = new Stack<>();

    void run(Command c){
        c.execute();
        history.push(c);
    }

    void undo(){
        if(history.isEmpty()){
            System.out.println("Nothing to undo");
            return;
        }
        history.pop().undo();
    }
}

abstract class Beverage{

    final void prepare(){
        boil();
        brew();
        pour();
        if(customerWantsCondiments()){
            add();
        }
    }

    void boil(){System.out.println("Boiling water");}
    void pour(){System.out.println("Pouring cup");}

    abstract void brew();
    abstract void add();

    boolean customerWantsCondiments(){return true;}
}

class Tea extends Beverage{
    void brew(){System.out.println("Steeping tea");}
    void add(){System.out.println("Adding lemon");}
}

class Coffee extends Beverage{

    Scanner sc = new Scanner(System.in);

    void brew(){System.out.println("Brewing coffee");}

    void add(){System.out.println("Adding milk & sugar");}

    boolean customerWantsCondiments(){
        System.out.println("Add milk and sugar? yes/no");
        String a = sc.nextLine();
        return a.equalsIgnoreCase("yes");
    }
}

interface Mediator{
    void send(String msg, User sender);
    void add(User u);
}

class ChatRoom implements Mediator{

    List<User> users = new ArrayList<>();

    public void add(User u){
        users.add(u);
    }

    public void send(String msg, User sender){
        for(User u:users){
            if(u!=sender){
                u.receive(msg,sender.name);
            }
        }
    }
}

abstract class User{

    Mediator m;
    String name;

    User(Mediator m,String name){
        this.m=m;
        this.name=name;
    }

    abstract void send(String msg);
    abstract void receive(String msg,String sender);
}

class ChatUser extends User{

    ChatUser(Mediator m,String name){
        super(m,name);
    }

    void send(String msg){
        System.out.println(name+" sends: "+msg);
        m.send(msg,this);
    }

    void receive(String msg,String sender){
        System.out.println(name+" received from "+sender+": "+msg);
    }
}

public class Main {

    public static void main(String[] args) {

        Light light = new Light();
        Door door = new Door();
        Thermostat t = new Thermostat();

        Invoker remote = new Invoker();

        remote.run(new LightOn(light));
        remote.run(new DoorOpen(door));
        remote.run(new TempUp(t));

        remote.undo();

        Beverage tea = new Tea();
        tea.prepare();

        System.out.println();

        Beverage coffee = new Coffee();
        coffee.prepare();

        ChatRoom chat = new ChatRoom();

        User a = new ChatUser(chat,"Ali");
        User b = new ChatUser(chat,"Aruzhan");
        User c = new ChatUser(chat,"Dias");

        chat.add(a);
        chat.add(b);
        chat.add(c);

        a.send("Hello everyone");
        b.send("Hi Ali");
    }
}
