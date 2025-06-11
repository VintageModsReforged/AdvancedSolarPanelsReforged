package reforged.ic2.addons.asp.utils.molecular;

import mods.vintage.core.helpers.ConfigHelper;
import net.minecraft.item.ItemStack;
import reforged.ic2.addons.asp.References;

import java.io.File;
import java.util.*;

public class MTRecipeManager {

    public static final MTRecipeManager instance = new MTRecipeManager();
    private static final List<RecipeRecord> transformerRecipes = new ArrayList<RecipeRecord>();

    private MTRecipeManager() {}

    public boolean hasRecipeFor(ItemStack input) {
        if (input == null) return false;
        List<RecipeRecord> recipes = getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            RecipeRecord recipe = recipes.get(i);
            if (input.isItemEqual(recipe.input)) {
                return true;
            }
        }
        return false;
    }

    public static void addDefaultRecipe(Object input, Object output, int energy) {
        String inputStr = formatEntry(input, getStackSize(input));
        String outputStr = formatEntry(output, getStackSize(output));
        StringBuilder comment = new StringBuilder();
        comment.append(" # ");
        if (input instanceof ItemStack) {
            comment.append(getReadableName((ItemStack) input));
        } else {
            comment.append((String) input);
        }
        comment.append(" → ");
        if (output instanceof ItemStack) {
            comment.append(getReadableName((ItemStack) output));
        } else {
            comment.append((String) output);
        }
        MTRecipeConfig.defaultLines.add(inputStr + "; " + outputStr + "; " + energy + comment);
    }

    private static String formatEntry(Object obj, int size) {
        if (obj instanceof String) {
            return formatOreDictEntry((String) obj, size);
        } else if (obj instanceof ItemStack) {
            return formatItemStackEntry((ItemStack) obj, size);
        }
        throw new RuntimeException("Unsupported type: " + obj.getClass().getCanonicalName());
    }

    private static String getReadableName(ItemStack stack) {
        String name = stack.getDisplayName();
        if (name == null || name.trim().isEmpty()) {
            name = stack.getItem().getItemDisplayName(stack); // Fallback
        }
        return name + (stack.getItemDamage() != 0 ? " (meta " + stack.getItemDamage() + ")" : "");
    }

    private static String formatOreDictEntry(String oreName, int size) {
        return "oredict:" + oreName + ":" + size;
    }

    private static String formatItemStackEntry(ItemStack stack, int size) {
        String base = String.valueOf(stack.itemID);
        int meta = stack.getItemDamage();
        if (meta != 0) {
            base += "-" + meta;
        }
        return base + ":" + size;
    }

    // Helper method to get stack size for input/output
    private static int getStackSize(Object obj) {
        if (obj instanceof ItemStack) {
            return ((ItemStack) obj).stackSize;
        } else if (obj instanceof String) {
            // OreDict entries usually default to size 1 if not specified
            return 1;
        } else {
            throw new RuntimeException("Unsupported type for stack size: " + obj.getClass());
        }
    }

    public List<RecipeRecord> getRecipes() {
        return Collections.unmodifiableList(transformerRecipes);
    }

    public void initRecipes() {
        transformerRecipes.clear();
        File configFile = ConfigHelper.getConfigFileFor(References.MOD_ID + "/MT_Recipes");
        transformerRecipes.addAll(MTRecipeConfig.parse(configFile));
    }
}
