package reforged.ic2.addons.asp.utils.molecular;

import com.google.common.collect.Lists;
import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reforged.ic2.addons.asp.ASPBlocksItems;
import reforged.ic2.addons.asp.AdvancedSolarPanels;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MTRecipeConfig {

    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final List<String> defaultLines = new ArrayList<String>();
    public static boolean filled = false;
    public static String configVersion = "1.0";

    public static void populateDefaults() {
        if (filled) return;

        // Vanilla
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.skull, 1, 1), new ItemStack(Item.netherStar), 250000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.ingotIron), Items.getItem("iridiumOre"), 9000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.netherrack), new ItemStack(Item.gunpowder, 2), 70000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.sand), new ItemStack(Block.gravel), 50000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.dirt), new ItemStack(Block.blockClay), 50000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.coal, 1, 1), new ItemStack(Item.coal, 1, 0), 60000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.lightStoneDust), ASPBlocksItems.Component.SUNNARIUM_PART.getStack(), 1000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.redstone), ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_PART.getStack(), 10000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.glowStone), ASPBlocksItems.Component.SUNNARIUM.getStack(), 9000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.blockRedstone), ASPBlocksItems.Component.SPECTRAL_SUNNARIUM.getStack(), 90000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.cloth, 1, 4), new ItemStack(Block.glowStone), 500000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.cloth, 1, 11), new ItemStack(Block.blockLapis), 500000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Block.cloth, 1, 14), new ItemStack(Block.blockRedstone), 500000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.dyePowder, 1, 4), "gemSapphire", 5000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.redstone), "gemRuby", 5000000);
        MTRecipeManager.addDefaultRecipe(new ItemStack(Item.coal), Items.getItem("industrialDiamond"), 9000000);
        MTRecipeManager.addDefaultRecipe(Items.getItem("industrialDiamond"), new ItemStack(Item.diamond), 1000000);

        // OreDict-Based
        MTRecipeManager.addDefaultRecipe("dustTitanium", "dustChrome", 500000);
        MTRecipeManager.addDefaultRecipe("ingotTitanium", "ingotChrome", 500000);
        MTRecipeManager.addDefaultRecipe("gemNetherQuartz",  "gemCertusQuartz", 500000);
        MTRecipeManager.addDefaultRecipe("ingotCopper", "ingotNickel", 300000);
        MTRecipeManager.addDefaultRecipe("ingotTin", "ingotSilver", 500000);
        MTRecipeManager.addDefaultRecipe("ingotSilver", "ingotGold", 500000);
        MTRecipeManager.addDefaultRecipe("ingotGold", "ingotPlatinum", 9000000);

        filled = true;
    }

    public static void writeGuide(BufferedWriter bw) throws IOException {
        String[] lines = {
                "##################################################################################################",
                "#                        AdvancedSolarPanels Molecular Transformer Recipes                       #",
                "##################################################################################################",
                "# Format of recipe: \"inputItemID:stackSize;outputItemID:outputStackSize;energy\"                  #",
                "# InputItem (outputItem) format:                                                                 #",
                "# \"oredict:forgeOreDictName\" or \"itemID-meta\"                                                    #",
                "# New line = new recipe.                                                                         #",
                "# Add \"#\" before line to skip parsing recipe                                                     #",
                "##################################################################################################",
                "version=" + configVersion,
                "##################################################################################################"
        };
        for (String line : lines) {
            bw.write(line + NEW_LINE);
        }
    }

    public static void fillWithInitials(BufferedWriter bw) throws IOException {
        populateDefaults();
        writeGuide(bw);
        for (String s : defaultLines) {
            bw.write(s + NEW_LINE);
        }
    }

    public static List<RecipeRecord> parse(File f) {
        List<RecipeRecord> list = Lists.newArrayList();
        try {
            if (!f.exists()) {
                AdvancedSolarPanels.LOG.log(Level.FINE, "MT recipes config file not found. Create default recipes.");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                fillWithInitials(bw);
                bw.close();
                fos.close();
            }
            AdvancedSolarPanels.LOG.log(Level.FINE, "* * * * * * Start parsing MT recipes * * * * * * ");
            FileInputStream fis = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.replace("\r", "").replace("\n", "");
                if (line.trim().startsWith("#"))
                    continue;
                if (line.indexOf("#") != 0 && line.indexOf("version") != 0) {
                    if (line.indexOf("#") > 0) {
                        line = line.substring(0, line.indexOf("#"));
                        line = line.trim();
                    }
                } else if (line.indexOf("version") == 0) {
                    if (line.indexOf("#") > 0) {
                        line = line.substring(0, line.indexOf("#"));
                        line = line.trim();
                    }
                    String tmpString = line.substring(line.indexOf("=") + 1);
                    if (!tmpString.isEmpty())
                        configVersion = tmpString;
                    continue;
                }
                String[] spaceSplit = line.trim().split("; ".trim());
                if (spaceSplit.length != 3) {
                    AdvancedSolarPanels.LOG.log(Level.FINE, String.format("Ignoring line %d, incorrect format.", lineNumber));
                    continue;
                }
                String input = spaceSplit[0].trim();
                String output = spaceSplit[1].trim();
                int energy = -1;
                try {
                    energy = Integer.parseInt(spaceSplit[2].trim());
                } catch (Exception e) {
                    AdvancedSolarPanels.LOG.log(Level.FINE, String.format("Ignoring line %d, energy number is incorrect.", lineNumber));
                    continue;
                }
                List<ItemStack> inputStacks = wrap(input);
                if (inputStacks == null) {
                    AdvancedSolarPanels.LOG.log(Level.FINE, String.format("Ignoring line %d, failed to parse input.", lineNumber));
                    continue;
                }
                List<ItemStack> outputStacks = wrap(output);
                if (outputStacks == null) {
                    AdvancedSolarPanels.LOG.log(Level.FINE, String.format("Ignoring line %d, failed to parse output.", lineNumber));
                    continue;
                }
                ItemStack outputStack = outputStacks.get(0);
                for (ItemStack is : inputStacks) {
                    RecipeRecord recipeToAdd = new RecipeRecord();
                    recipeToAdd.input = is.copy();
                    recipeToAdd.output = outputStack.copy();
                    recipeToAdd.energy = energy;
                    list.add(recipeToAdd);
                }
            }
            br.close();
            fis.close();
            AdvancedSolarPanels.LOG.log(Level.FINE, "* * * * * * Finished parsing MT recipes * * * * * * ");
            AdvancedSolarPanels.LOG.log(Level.FINE, String.format("Config loading completed, %d recipes parsed.", list.size()));
            AdvancedSolarPanels.LOG.log(Level.FINE, "* * * * * * Loaded recipes list * * * * * * ");
            for (RecipeRecord record : list) {
                Object input = record.input;
                Object output = record.output;
                int energy = record.energy;
                String result = "";
                result = result
                        + ((input != null) ? ((ItemStack) input).getDisplayName() : (String) input);
                result = result + " -> ";
                result = result
                        + ((output != null) ? ((ItemStack) output).getDisplayName() : (String) output);
                result = result + String.format(" [%d EU]", energy);
                AdvancedSolarPanels.LOG.log(Level.FINE, result);
            }
            AdvancedSolarPanels.LOG.log(Level.FINE, "* * * * * * * * * * * * * * * * * * * * *");
        } catch (Throwable t) {
            AdvancedSolarPanels.LOG.log(Level.FINE, "Fatal error occurred parsing MT recipes config. (" + t + ")");
        }
        return list;
    }

    public static List<ItemStack> wrap(String s) {
        List<ItemStack> list = Lists.newArrayList();

        // OreDict case: "oredict:name:count"
        if (s.startsWith("oredict:")) {
            String[] split = s.split(":");
            if (split.length < 2) return null;
            String oreName = split[1];
            int stackSize = 1;
            if (split.length >= 3) {
                try {
                    stackSize = Integer.parseInt(split[2]);
                } catch (NumberFormatException ignored) {}
            }
            List<ItemStack> oreStacks = OreDictionary.getOres(oreName);
            if (oreStacks.isEmpty()) return null;
            for (ItemStack oreStack : oreStacks) {
                ItemStack copy = oreStack.copy();
                copy.stackSize = stackSize;
                list.add(copy);
            }
            return list.isEmpty() ? null : list;
        }

        // Numeric ID with optional meta, format: "id-meta:count" or "id:count"
        try {
            String[] split = s.split(":");
            if (split.length < 2) return null;

            String idMetaPart = split[0];
            int stackSize = Integer.parseInt(split[1]);
            if (stackSize <= 0) stackSize = 1;

            int id;
            int meta = 0;

            if (idMetaPart.contains("-")) {
                String[] idMetaSplit = idMetaPart.split("-");
                if (idMetaSplit.length != 2) return null;
                id = Integer.parseInt(idMetaSplit[0]);
                meta = Integer.parseInt(idMetaSplit[1]);
            } else {
                id = Integer.parseInt(idMetaPart);
            }

            Item item = Item.itemsList[id];
            if (item == null) return null;

            ItemStack stack = new ItemStack(item, stackSize, meta);
            list.add(stack);
            return list;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
