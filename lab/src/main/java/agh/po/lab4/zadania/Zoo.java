package agh.po.lab4.zadania;

import agh.po.lab4.animals.Animal;
import agh.po.lab4.animals.Dog;
import agh.po.lab4.animals.Parrot;
import agh.po.lab4.animals.Snake;

import java.util.Random;

public class Zoo {
    Animal[] animals = new Animal[100];

    public Zoo() {
       wypelnijTablice();
    }

    private void wypelnijTablice() {
        Random random = new Random();

        String[] names = {"pawel", "lukasz", "kamil", "bartek", "karol", "tadeusz", "sławek", "wojtek", "rafał", "kuba"};

        for (int i = 0; i < animals.length; i++) {
            int animalType = random.nextInt(3);
            String name = names[random.nextInt(names.length)];

            switch (animalType) {
                case 0:
                    animals[i] = new Dog(name);
                    break;
                case 1:
                    animals[i] = new Parrot(name);
                    break;
                case 2:
                    animals[i] = new Snake(name);
                    break;
            }
        }
    }

    public int countLegs() {
        int totalLegs = 0;
        for (Animal animal : animals) {
            totalLegs += animal.getLegs();
        }
        return totalLegs;
    }

    public Animal[] getAnimals() {
        return animals;
    }

    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        int suma = zoo.countLegs();
        System.out.println("Total number of legs in the zoo: " + suma);

        Animal[] animals = zoo.getAnimals();
        System.out.println("Animals in the zoo:");
        for (int i = 0; i < animals.length; i++) {
            if (animals[i] != null) {
                int legs = animals[i].getLegs();
                System.out.println("Animal " + (i + 1) + " has " + legs + " legs.");
                animals[i].makeSound();

                // System.out.println((i + 1) + ": " + animals[i].getDescription());
            }
        }
    }
}
