import java.util.Random;

public class Animal {

    int age;
    int birthRate;
    int maxAge;
    int birthAge;
    public Animal(int age){
        this.age = age;
    }
    public int getAge(){
        return age;
    }
    public boolean age(){
        boolean bool = false;
        if (age == maxAge) {
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }

    public boolean birth(){
        Random rand = new Random();
        int x = rand.nextInt(99);
        boolean bool = false;
        if (age >= birthAge) {
            if (x >= 0 && x < birthRate) {
                bool = true;
            } else {
                bool = false;
            }
        }
        return bool;
    }


}
