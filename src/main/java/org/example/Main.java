package org.example;

import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Dw_Kn_Modifiers_Gen modifiers_generator = new Dw_Kn_Modifiers_Gen();
        Blueprints_Gen blueprintsGen = new Blueprints_Gen();

        System.out.println("choose to generate punks/blueprints or regen modifiers files e.g. gen/regen");
        String chosenAction = input.nextLine();
        if (chosenAction.equals("gen")){
            blueprintsGen.generateFile();
        } else if (chosenAction.equals("regen")) {
            modifiers_generator.generateFile();
        }
    }
}