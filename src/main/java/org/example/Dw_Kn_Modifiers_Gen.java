package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dw_Kn_Modifiers_Gen {
    public void generateFile() {
        List<String> stats = List.of(
                "power",
                "fire_damage",
                "shock_damage",
                "shock_damage_received",
                "fire_damage_received"
        );
        Map<String, List<String>> Units = new LinkedHashMap<>();
        Units.put("INFAN", List.of("infantry", "is_musketeer_modifier"));
        Units.put("ARTIFICER", List.of("artificer", "is_rajput_modifier"));
        Units.put("CAVAL", List.of("cavalry", "is_qizilbash_modifier"));
        Units.put("ARTIL", List.of("artillery", "is_caravel_modifier"));

        //change file path destinations if needed

        File targetFileModifiers = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\common\\event_modifiers\\DA_metallurgy_new_modifiers.txt");
        File targetFileEffects = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\common\\scripted_effects\\DA_apply_and_remove_infcav_metallurgy_mod_effects.txt");
        File targetFileEffectsCost = new File("C:\\Users\\dmeys\\Documents\\Paradox Interactive\\Europa Universalis IV\\modding\\Dwarven-Knowledge-Dev-fork-dmeyster\\common\\scripted_effects\\DA_Metallurgy_Unit_Costs_effects.txt");

        //DA_metallurgy_new_modifiers.txt
        try (PrintWriter out = new PrintWriter(targetFileModifiers)){

            out.println("""
                # all metallurgy modifiers for infantry, artificers and cavalry special units
                
                ######################################################
                ################ costs  modifiers ####################
                ######################################################
                """);

            // iterating over units for cost modifiers
            for (Map.Entry<String, List<String>> entry : Units.entrySet()) {
                String unitCode = entry.getKey();              // "INFAN"
                String tag = entry.getValue().get(1);          // "is_musketeer_modifier"
                out.println("DA_cost_" + unitCode + "_0 = {");
                out.println("    " + tag + " = yes");
                out.println("}");

                for (int i = 1; i <= 66; i++) {
                    out.println("DA_cost_" + unitCode + "_" + i + " = {");
                    out.printf("    land_maintenance_modifier = %.3f%n", 0.025f * i);
                    out.printf("    reinforce_cost_modifier = %.3f%n", 0.025f * i);
                    out.printf("    reinforce_speed = %.2f%n", -0.01f * i);
                    out.println("    " + tag + " = yes");
                    out.println("}");
                }
                out.println();
            }

            // total cost
            out.println("DA_TOTAL_COST_MOD_0 = {");
            out.println("}");

            for (int i = 1; i <= 200; i++) {
                out.println("DA_TOTAL_COST_MOD_"+ i +" = {");
                out.printf("    land_forcelimit_modifier = %.3f%n", -0.005F * i);
                out.printf("    reduced_liberty_desire = %.1f%n", 0.1 * i);
                if (i >= 30){
                    out.println("    capped_by_forcelimit = yes");
                }
                out.println("}");
            }

            for (Map.Entry<String, List<String>> entry : Units.entrySet()) {
                String unitCode = entry.getKey();              // "INFAN"
                String fullUnitName = entry.getValue().get(0); // "infantry"
                String tag = entry.getValue().get(1);          // "is_musketeer_modifier"

                String line = "################ " + fullUnitName.toUpperCase() + " MODIFIERS ";
                String filler = "#".repeat(Math.max(0, 54 - line.length()));
                out.println("######################################################");
                out.println(line + filler);                                                         //same amount of # with all unit types (diff num of characters)
                out.println("######################################################");
                out.println();

                for (String stat : stats) {
                    boolean isDamageReceived = stat.endsWith("_received");
                    int endValue = isDamageReceived ? 80 : 100;
                    boolean isPowerStat = stat.endsWith("power");
                    String currentStat = isPowerStat ? fullUnitName + "_" + stat : stat;
                    out.println("# " + stat);
                    for (int i = -100; i <= endValue; i++) {
                        if (i == 0) continue;
                        out.printf("DA_" + stat + "." + unitCode + ".%.2f = {%n", 0.01f * i);
                        out.println("    " + tag + " = yes");
                        out.printf("    " + currentStat + " = %.2f%n", 0.01f * i);
                        out.println("}");
                    }
                }
                out.println();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //DA_apply_and_remove_infcav_metallurgy_mod_effects.txt
        try (PrintWriter out2 = new PrintWriter(targetFileEffects)){
            out2.println("DA_remove_metallurgy_infcav_modifiers_effect = {");
            for (String stat : stats) {
                boolean isDamageReceived = stat.endsWith("_received");
                int startValue = isDamageReceived ? -80 : -100;

                for (int i = startValue; i <= 100; i++) {
                    if (i == 0) continue;
                    out2.printf("    remove_country_modifier = DA_" + stat + ".$DA_unit$.%.2f%n", -0.01f * i);
                }
            }
            out2.println("}");
            out2.println();

            out2.println("DA_apply_metallurgy_infcav_modifiers_effect = {");
            out2.println();
            for (String stat : stats) {
                boolean isDamageReceived = stat.endsWith("_received");
                int startValue = isDamageReceived ? -80 : -100;

                String line = "################ " + stat.toUpperCase() + " MODIFIERS ";
                String filler = "#".repeat(Math.max(0, 54 - line.length()));
                out2.println("######################################################");
                out2.println(line + filler);                                                         //same amount of # with all unit types (diff num of characters)
                out2.println("######################################################");
                out2.println();

                for (int i = startValue; i <= 100; i++) {
                    if (i == 0) continue;
                    out2.println("    if = {");
                    out2.printf("        limit = { is_variable_equal = { which = DA_" + stat + "_TOTAL_$DA_unit$ value = %.2f } }%n", -0.01f * i);
                    out2.printf("        add_country_modifier = { name = DA_" + stat + ".$DA_unit$.%.2f duration = -1 hidden = yes }%n", -0.01f * i);
                    out2.println("    }");
                }
                out2.println();
            }
            out2.println("}");
            out2.println();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //DA_Metallurgy_Unit_Costs_effects.txt
        try (PrintWriter out3 = new PrintWriter(targetFileEffectsCost)){

            out3.println("DA_Remove_cost_modifiers = {");

            // iterating over units for cost modifiers
            for (Map.Entry<String, List<String>> entry : Units.entrySet()) {
                String unitCode = entry.getKey();              // "INFAN"
                for (int i = 1; i <= 66; i++) {
                    out3.println("    remove_country_modifier = DA_cost_" + unitCode + "_" + i);
                }
                out3.println();
            }

            // total cost
            out3.println("    remove_country_modifier = DA_TOTAL_COST_MOD_0");

            for (int i = 1; i <= 200; i++) {
                out3.println("    remove_country_modifier = DA_TOTAL_COST_MOD_" + i);
            }
            out3.println("}");
            out3.println();

            out3.print("""
                DA_Apply_cost = {
                    set_variable = {
                            which = DA_TOTAL_COST
                            value = 0
                    }
                    change_variable = {
                            which = DA_TOTAL_COST
                            which = DA_Cost_INFAN
                    }
                    change_variable = {
                            which = DA_TOTAL_COST
                            which = DA_Cost_ARTIFICER
                    }
                    change_variable = {
                            which = DA_TOTAL_COST
                            which = DA_Cost_CAVAL
                    }
                    change_variable = {
                            which = DA_TOTAL_COST
                            which = DA_Cost_ARTIL
                    }
                """);

            for (Map.Entry<String, List<String>> entry : Units.entrySet()) {
                String unitCode = entry.getKey();              // "INFAN"
                out3.println();
                for (int i = 1; i <= 66; i++) {
                    out3.println("    if = { limit = { is_variable_equal = { which = DA_Cost_" + unitCode + " value = " + i + " } } add_country_modifier = { name = DA_Cost_" + unitCode + "_" + i + " duration = -1 hidden = yes } }");

                }
            }
            out3.println("}");
            out3.println();

            out3.print("""
                DA_Apply_cost_fake = {
                    tooltip = {
                """);
            for (int i = 1; i <= 66; i++) {
                out3.println("        if = { limit = { is_variable_equal = { which = DA_Cost_$DA_unit$ value = " + i + " } } add_country_modifier = { name = DA_cost_$DA_unit$_" + i + " duration = -1 } }");
            }
            out3.print("""
                    }
                }
                """);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}