package reforged.ic2.addons.asp.utils.molecular;

import mods.vintage.core.helpers.ConfigHelper;
import reforged.ic2.addons.asp.References;

import java.io.File;
import java.util.*;

public class MTRecipeManager {

    public static final MTRecipeManager instance = new MTRecipeManager();
    private static final List<RecipeRecord> transformerRecipes = new ArrayList<RecipeRecord>();

    private MTRecipeManager() {}

    public List<RecipeRecord> getRecipes() {
        return Collections.unmodifiableList(transformerRecipes);
    }

    public void initRecipes() {
        transformerRecipes.clear();
        File configFile = ConfigHelper.getConfigFileFor(References.MOD_ID + "/MT_Recipes");
        transformerRecipes.addAll(MTRecipeConfig.parse(configFile));
    }
}
