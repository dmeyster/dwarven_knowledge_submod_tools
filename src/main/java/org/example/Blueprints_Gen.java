package org.example;

import java.io.*;
import java.util.*;

public class Blueprints_Gen {
    public void generateFile() {
        Scanner input = new Scanner(System.in);
        System.out.println("choose unit to generate blueprint/punk for: (type in one of: INFAN/ARTIFICER/CAVAL/ARTIL)");
        String chosenUnit = input.nextLine();

        File Punks = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\events\\DA_Metallurgy_events_" + chosenUnit + "_4.txt");
        File Blueprints = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\events\\DA_Metallurgy_events_" + chosenUnit + "_1.txt");
        //File Metals = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\events\\DA_Metallurgy_events_" + chosenUnit + "_2.txt");
        //File Runes = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\events\\DA_Metallurgy_events_" + chosenUnit + "_3.txt");

        String chosenType = "1";
        while (!Objects.equals(chosenType, "end")){
            System.out.println("choose to generate punk or blueprint, type end - end");
            chosenType = input.nextLine();
            if (Objects.equals(chosenType, "punk")){
                try (RandomAccessFile raf1 = new RandomAccessFile(Punks, "rw")){

                    if (raf1.length() == 0){
                        raf1.writeBytes(getStartingBlock(chosenUnit, 4));
                        raf1.writeBytes(getEndBlock(chosenUnit, "punk"));
                    }
                    raf1.seek(raf1.length() - 131);   //setting pointer 1 line higher than the start of endBlock to replace it with new punk option, endBlock length is 130

                    //generate stat changes

                    System.out.println("type name of punk tech");
                    String punkName = input.nextLine();
                    raf1.writeBytes(getPunkName(chosenUnit, punkName));

                    System.out.println("type change to cost of blueprint from punk tech, if no change - 0");
                    int punkCost = Integer.parseInt(input.nextLine());
                    raf1.writeBytes(getCost(punkCost));

                    System.out.println("type change to metal strength of blueprint from punk tech, if no change - 0");
                    int punkStrength = Integer.parseInt(input.nextLine());
                    raf1.writeBytes(getStrength(punkStrength));

                    System.out.println("type change to magical conductivity of blueprint from punk tech, if no change - 0");
                    int punkMagicConductivity = Integer.parseInt(input.nextLine());
                    raf1.writeBytes(getMagicConductivity(punkMagicConductivity));

                    System.out.println("type change to electrical conductivity of blueprint from punk tech, if no change - 0");
                    int punkElectricalConductivity = Integer.parseInt(input.nextLine());
                    raf1.writeBytes(getElectricalConductivity(punkElectricalConductivity));

                    System.out.println("type change to amount of rune slots of blueprint from punk tech, if no change - 0");
                    int punkRuneSloths = Integer.parseInt(input.nextLine());
                    raf1.writeBytes(getRuneSloths(punkRuneSloths));

                    raf1.writeBytes("    }");
                    raf1.writeBytes(getEndBlock(chosenUnit, "punk"));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(chosenType, "blueprint")) {
                try (RandomAccessFile raf2 = new RandomAccessFile(Blueprints, "rw")){

                    String startingBlock = getStartingBlock(chosenUnit, 0); //4 here is number to add to event number to get correct one, for example infantry events is 50 + 4 to get infantry punks
                    String endBlock = getEndBlock(chosenUnit, "blueprint");

                    if (raf2.length() == 0){
                        raf2.writeBytes(startingBlock);
                        raf2.writeBytes(endBlock);
                    }
                    raf2.seek(raf2.length() - endBlock.length() + 1);   //setting pointer to the start of endBlock to replace it with new punk option

                    /*String correctModifierName = switch (chosenUnit) {
                        case "INFAN" -> "is_musketeer_modifier";
                        case "ARTIFICER" -> "is_rajput_modifier";
                        case "CAVAL" -> "is_qizilbash_modifier";
                        case "ARTIL" -> "is_caravel_modifier";
                        default -> throw new IllegalArgumentException("Unexpected unit type: " + chosenUnit);
                    };*/

                    System.out.println("type name of blueprint tech");
                    String blueprintName = input.nextLine();

                    System.out.println("type requirement(s) if none left - type none (e.g. DA_completed.Alchemy_1 = yes /none)");
                    List<String> blueprintRequirements = new ArrayList<>();
                    while (true) {
                        String requirement = input.nextLine().trim();
                        if (requirement.equalsIgnoreCase("none")) {
                            break;
                        }
                        if (!requirement.isEmpty()) {
                            blueprintRequirements.add(requirement);
                        }
                    }
                    System.out.println("type required punk name, if none type - none (e.g. Tesla/none)");
                    String blueprintPunk = input.nextLine();

                    System.out.println("type cost of blueprint (e.g. 10)");
                    String blueprintCost = input.nextLine();

                    System.out.println("type number of rune slots of blueprint (e.g. 2)");
                    String blueprintNRuneSlots = input.nextLine();

                    System.out.println("type combat ability of chosen unit (power) of blueprint (e.g. 0.02)");
                    String blueprintPower = input.nextLine();

                    System.out.println("type fire damage of blueprint (e.g. 0.02)");
                    String blueprintFire = input.nextLine();

                    System.out.println("type shock damage of blueprint (e.g. 0.02)");
                    String blueprintShock = input.nextLine();

                    System.out.println("type fire damage received of blueprint (e.g. 0.02)");
                    String blueprintFireReceived = input.nextLine();

                    System.out.println("type shock damage received of blueprint (e.g. 0.02)");
                    String blueprintShockReceived = input.nextLine();

                    System.out.println("type Unique modifier, if there is none - type none(e.g. Vault_Blade_of_Severance/none)");
                    String blueprintUnique = input.nextLine();

                    System.out.println("type if blueprint have electrical stats of blueprint tech (e.g. yes/no)");
                    String blueprintElectrical = input.nextLine();

                    String blueprintElectricalPower = "";
                    String blueprintElectricalFire = "";
                    String blueprintElectricalShock = "";
                    String blueprintElectricalFireReceived = "";
                    String blueprintElectricalShockReceived = "";

                    if (blueprintElectrical.equals("yes")) {
                        System.out.println("type fire damage of blueprintElectrical (e.g. 0.02)");
                        blueprintElectricalPower = input.nextLine();

                        System.out.println("type fire damage of blueprintElectrical (e.g. 0.02)");
                        blueprintElectricalFire = input.nextLine();

                        System.out.println("type shock damage of blueprintElectrical (e.g. 0.02)");
                        blueprintElectricalShock = input.nextLine();

                        System.out.println("type fire damage received of blueprintElectrical (e.g. 0.02)");
                        blueprintElectricalFireReceived = input.nextLine();

                        System.out.println("type shock damage received of blueprintElectrical (e.g. 0.02)");
                        blueprintElectricalShockReceived = input.nextLine();
                    }

                    raf2.writeBytes(getBlueprint(chosenUnit, blueprintPunk, blueprintName, blueprintRequirements,
                            blueprintCost, blueprintNRuneSlots, blueprintPower, blueprintFire, blueprintShock, blueprintFireReceived, blueprintShockReceived,
                            blueprintUnique, blueprintElectrical,
                            blueprintElectricalPower, blueprintElectricalFire, blueprintElectricalShock, blueprintElectricalFireReceived, blueprintElectricalShockReceived));

                    raf2.writeBytes(getEndBlock(chosenUnit, "blueprint"));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String getStartingBlock(String chosenUnit, int stage) { //stage here is number to add to event number to get correct one, for example infantry events is 50 + 4 to get infantry punks
        int eventId = getCorrectEventNumber(chosenUnit, stage);
        return """
                namespace = DA_Metallurgy_events
                
                country_event = {
                    id = DA_Metallurgy_events.%d # %s punk
                    title = "DA_Metallurgy_events.%d.t"
                    desc = "DA_Metallurgy_events.%d.d"
                    picture = {
                        trigger = {
                            NOT = {
                                AND = {
                                #	culture_group = dwarven
                                #	culture_group = harimari
                                #	culture_group = centaur
                                #	culture_group = ogre
                                #	culture_group = gnollish
                                #	culture_group = orcish
                                    culture_group = goblin
                                #	culture_group = harpy
                                #	culture_group = halfling
                                #	culture_group = gnomish
                                #	culture_group = kobold
                                #	culture_group = elven
                                #	culture_group = lizardfolk
                                }
                            }
                        }
                        picture = DA_forge_blueprint_eventPicture			#up to replacement
                    }
                    picture = {
                        trigger = { culture_group = goblin }
                        picture = DA_forge_blueprint_goblin_eventPicture	#up to replacement
                    }
                
                    trigger = {
                        always = yes
                    }
                    is_triggered_only = yes
                
                """.formatted(eventId, chosenUnit, eventId, eventId);
    }

    private String getEndBlock(String chosenUnit, String type) { //stage here is number to add to event number to get correct one, for example infantry events is 50 + 4 to get infantry punks
        return 	"""	
                    
                    	after = {
                    		hidden_effect = {
                    			set_country_flag = DA_selected_%s_%s
                    			clr_country_flag = DA_UNIT_DESIGN_IN_USE
                    		}
                    	}
                    }
                    """.formatted(type, chosenUnit);
    }

    private String getPunkName(String chosenUnit, String name){
        int eventId = getCorrectEventNumber(chosenUnit, 4);
        return """
                    option = {
                        name = "DA_Metallurgy_events.%d.%s"
                            trigger = {
                                custom_trigger_tooltip = {
                                    tooltip = DA_Unlocked_this_%s
                                    has_country_flag = DA_Unlocked.%s.%s
                                }
                                has_country_flag = DA_Unlocked.%s.%s
                            }
                """.formatted(eventId, name, "punk", "punk", name, "punk", name);
    }

    private String getCost(int statChange){
        if (statChange != 0) {
            return """
                            change_variable = {
                                which = DA_Cost_$DA_unit$
                                value = %d
                            }
                    """.formatted(statChange);
        }
        return ("       # no cost change for this punk\n");
    }

    private String getStrength(int statChange){
        if (statChange != 0) {
            return """
                            change_variable = {
                                which = DA_Metal_strength_STAT_$DA_unit$
                                value = %d
                            }
                    """.formatted(statChange);
        }
        return ("       # no strength change for this punk\n");
    }

    private String getMagicConductivity(int statChange){
        if (statChange != 0) {
            return """
                            change_variable = {
                                which = DA_Metal_Magic_Conductivity_STAT_$DA_unit$
                                value = %d
                            }
                    """.formatted(statChange);
        }
        return ("       # no magic conductivity change for this punk\n");
    }

    private String getElectricalConductivity(int statChange){
        if (statChange != 0) {
            return """
                            change_variable = {
                                which = DA_Metal_electrical_Conductivity_STAT_$DA_unit$
                                value = %d
                            }
                    """.formatted(statChange);
        }
        return ("       # no electric conductivity change for this punk\n");
    }

    private String getRuneSloths(int statChange){
        if (statChange != 0) {
            return """
                            change_variable = {
                                which = DA_Runes_Slots_STAT_MAX_$DA_unit$
                                value = %d
                            }
                    """.formatted(statChange);
        }
        return ("       # no runes amount change for this punk\n");
    }

    private int getCorrectEventNumber(String chosenUnit, int stage){
        return switch (chosenUnit) {
            case "INFAN" -> 50 + stage;
            case "ARTIFICER" -> 40 + stage;
            case "CAVAL" -> 60 + stage;
            case "ARTIL" -> 70 + stage;
            default -> throw new IllegalStateException("Unexpected value: " + chosenUnit);
        };
    }


    private String getBlueprint(String chosenUnit, String blueprintPunk, String blueprintName, List<String> blueprintRequirements,
                                String blueprintCost, String blueprintNRuneSlots, String blueprintPower, String blueprintFire, String blueprintShock, String blueprintFireReceived, String blueprintShockReceived,
                                String blueprintUnique, String blueprintElectrical,
                                String blueprintElectricalPower, String blueprintElectricalFire, String blueprintElectricalShock, String blueprintElectricalFireReceived, String blueprintElectricalShockReceived) {

        int eventId = getCorrectEventNumber(chosenUnit, 0);
        String requirementsToString;

        if (blueprintRequirements.isEmpty() || blueprintRequirements.getFirst().equalsIgnoreCase("none")) {
            requirementsToString = "#no requirements";
        } else {
            // Join the list with newlines and tabs for indentation
            requirementsToString = String.join("\n\t\t\t", blueprintRequirements);
        }

        blueprintPunk = blueprintPunk.equalsIgnoreCase("none")
                ? "#DA_Blueprint = none"
                : "has_country_flag = DA_Unlocked." + blueprintUnique;

        blueprintUnique = blueprintUnique.equalsIgnoreCase("none")
                ? "#DA_Unique = none"
                : "DA_Unique = " + blueprintUnique;

        String electricalSection = getElectricalSection(blueprintElectrical, blueprintElectricalPower, blueprintElectricalFire, blueprintElectricalShock, blueprintElectricalFireReceived, blueprintElectricalShockReceived);

        return """
                \toption = {
                		name = "DA_Metallurgy_events.%d.%s"
                		highlight = yes
                		trigger = {
                			custom_trigger_tooltip = {
                				tooltip = DA_Unlocked_this_Blueprint
                				has_country_flag = DA_Unlocked.%s
                				has_country_flag = DA_Unlocked.%s
                			}
                			%s
                		}
                		DA_apply_Blueprint = {
                			DA_unit = %s
                			DA_Cost = %s
                			DA_RUNE_SLOTS = %s
                			DA_power = %s
                			DA_fire = %s
                			DA_shock = %s
                			DA_Fire_rec = %s
                			DA_Shock_rec = %s
                			%s
                			%s
                		}
                	}
                """.formatted(eventId, blueprintName, blueprintPunk, blueprintName, requirementsToString, chosenUnit,
                blueprintCost, blueprintNRuneSlots, blueprintPower, blueprintFire, blueprintShock, blueprintFireReceived, blueprintShockReceived,
                blueprintUnique, electricalSection);
    }

    private String getElectricalSection(String isElectrical, String power, String fire, String shock, String fireRec, String shockRec) {
        if (isElectrical.equals("no") || isElectrical.equals("none")) {
            return "#DA_Electrical = none";
        }
        return """
                DA_Electrical = yes
                \t\t\tDA_power.electric = %s
                \t\t\tDA_fire.electric = %s
                \t\t\tDA_shock.electric = %s
                \t\t\tDA_Fire_rec.electric = %s
                \t\t\tDA_Shock_rec.electric = -%s
                """.formatted(power, fire, shock, fireRec, shockRec);
    }
}