import java.util.Random;

public class Lynx extends Animal {


    public Lynx(int age) {
        super(age);
    }

    @Override
    public boolean age() {
        boolean bool = false;
        if (age == 7) {  //die at age 7
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }

    @Override
    public boolean birth() {
        Random rand = new Random();
        int x = rand.nextInt(99);
        boolean bool = false;
        if (age >= 2) {             //start to breed at age 3
            if (x >= 0 && x < 18) {  //birth rate 40%
                bool = true;
            } else {
                bool = false;
            }
        }
        return bool;
    }
    public boolean eat(){
        Random rand = new Random();
        int x = rand.nextInt(99);
        boolean bool = false;
        if (age >= 0) {             //start to eat at age 2
            if (x >= 0 && x < 1) {  //predation rate 2%
                bool = true;
            } else {
                bool = false;
            }
        }
        return bool;
    }
}


