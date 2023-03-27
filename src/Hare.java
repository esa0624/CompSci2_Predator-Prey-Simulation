import java.util.Random;

public class Hare extends Animal{
    public Hare(int age){
        super(age);
    }
    @Override
    public boolean birth(){
        Random rand = new Random();
        int x = rand.nextInt(99);
        boolean bool = false;
        if (age >= 1) {             //start to breed at age 1
            if (x >= 0 && x < 95) { //birth rate 90%
                bool = true;
            } else {
                bool = false;
            }
        }
        return bool;
    }

    @Override
    public boolean age(){
        boolean bool = false;
        if (age == 4) {     //die at age 5
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }
}


