package reforged.ic2.addons.asp;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ASPRecipes {

    public static void initRecipes() {

        OreDictionary.registerOre("gemDiamond", Items.getItem("industrialDiamond"));
        OreDictionary.registerOre("gemDiamond", Item.diamond);
        OreDictionary.registerOre("ingotIridium", ASPBlocksItems.Component.IRIDIUM_INGOT.getStack());

        if (AdvancedSolarPanelsConfig.DOUBLE_SLAB) {
            GameRegistry.addShapedRecipe(new ItemStack(Block.stoneDoubleSlab),
                    "S ", "S ",
                    'S', new ItemStack(Block.stoneSingleSlab, 1, 0));
            GameRegistry.addShapelessRecipe(new ItemStack(Block.stoneSingleSlab, 2, 0), new ItemStack(Block.stoneDoubleSlab, 1, 0));
        }

        Recipes.compressor.addRecipe(Items.getItem("iridiumOre"), ASPBlocksItems.Component.IRIDIUM_INGOT.getStack());
        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.IRIDIUM_IRON_PLATE.getStack(),
                "XXX", "X#X", "XXX",
                'X', Item.ingotIron,
                '#', "ingotIridium");

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.REINFORCED_IRIDIUM_IRON_PLATE.getStack(),
                "CXC", "X#X", "CXC",
                'C', Items.getItem("advancedAlloy"),
                'X', Items.getItem("carbonPlate"),
                '#', ASPBlocksItems.Component.IRIDIUM_IRON_PLATE.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.IRRADIANT_REINFORCED_PLATE.getStack(),
                "CSC", "X#X", "CDC",
                'C', Item.redstone,
                'D', "gemDiamond",
                'X', new ItemStack(Item.dyePowder, 1, 4),
                'S', ASPBlocksItems.Component.SUNNARIUM_PART.getStack(),
                '#', ASPBlocksItems.Component.REINFORCED_IRIDIUM_IRON_PLATE.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SPECTRAL_REINFORCED_PLATE.getStack(),
                "CSC", "X#X", "CDC",
                'C', Item.redstone,
                'D', "gemDiamond",
                'X', new ItemStack(Item.dyePowder, 1, 4),
                'S', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_PART.getStack(),
                '#', ASPBlocksItems.Component.IRRADIANT_REINFORCED_PLATE.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SUNNARIUM_PART.getStack(),
                "U", "G", "U",
                'U', Items.getItem("matter"),
                'G', Item.lightStoneDust);

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SUNNARIUM.getStack(),
                "XXX", "XXX", "XXX",
                'X', ASPBlocksItems.Component.SUNNARIUM_PART.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SUNNARIUM_ALLOY.getStack(),
                "XXX", "X#X", "XXX",
                'X', ASPBlocksItems.Component.SUNNARIUM.getStack(),
                '#', Items.getItem("iridiumPlate"));

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.IRRADIANT_URANIUM.getStack(),
                " X ", "X#X", " X ",
                'X', Item.lightStoneDust,
                '#', Items.getItem("uraniumIngot"));

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.ENRICHED_SUNNARIUM.getStack(),
                "XXX", "X#X", "XXX",
                'X', ASPBlocksItems.Component.IRRADIANT_URANIUM.getStack(),
                '#', ASPBlocksItems.Component.SUNNARIUM.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.ENRICHED_SUNNARIUM_ALLOY.getStack(),
                " X ", "X#X", " X ",
                'X', ASPBlocksItems.Component.ENRICHED_SUNNARIUM.getStack(),
                '#', ASPBlocksItems.Component.SUNNARIUM_ALLOY.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_PART.getStack(),
                "XXX", "X#X", "XXX",
                'X', Item.redstone,
                '#', ASPBlocksItems.Component.SUNNARIUM.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SPECTRAL_SUNNARIUM.getStack(),
                "XXX", "XXX", "XXX",
                'X', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_PART.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_ALLOY.getStack(),
                "RXR", "X#X", "RXR",
                'X', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM.getStack(),
                'R', Items.getItem("iridiumPlate"),
                '#', ASPBlocksItems.Component.ENRICHED_SUNNARIUM_ALLOY.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.ENDER_SUNNARIUM_PART.getStack(),
                " X ", "X#X", " X ",
                'X', Item.eyeOfEnder,
                '#', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_PART.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.ENDER_SUNNARIUM.getStack(),
                "XXX", "X#X", "XXX",
                'X', ASPBlocksItems.Component.ENDER_SUNNARIUM_PART.getStack(),
                '#', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.ENDER_SUNNARIUM_ALLOY.getStack(),
                " X ", "X#X", " X ",
                'X', ASPBlocksItems.Component.ENDER_SUNNARIUM.getStack(),
                '#', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_ALLOY.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.IRRADIANT_GLASS_PANE.getStack(6),
                "XXX", "@#@", "XXX",
                'X', Items.getItem("reinforcedGlass"),
                '@', ASPBlocksItems.Component.IRRADIANT_URANIUM.getStack(),
                '#', Item.lightStoneDust);

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.SPECTRAL_GLASS_PANE.getStack(6),
                "XXX", "@#@", "XXX",
                'X', ASPBlocksItems.Component.IRRADIANT_GLASS_PANE.getStack(),
                '@', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM_PART.getStack(),
                '#', Item.redstone);

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.MT_CORE.getStack(),
                "X#X", "X X", "X#X",
                '#', copyWithWildCard(Items.getItem("reactorReflectorThick")),
                'X', ASPBlocksItems.Component.IRRADIANT_GLASS_PANE.getStack());

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.QUANTUM_CORE.getStack(),
                "CXC", "X#X", "CXC",
                'C', ASPBlocksItems.Component.ENRICHED_SUNNARIUM_ALLOY.getStack(),
                'X', Item.netherStar,
                '#', Item.eyeOfEnder);

        Recipes.advRecipes.addRecipe(ASPBlocksItems.Component.PHOTONIC_CORE.getStack(),
                "CXC", "X#X", "CXC",
                'C', ASPBlocksItems.Component.ENDER_SUNNARIUM_ALLOY.getStack(),
                'X', Item.netherStar,
                '#', ASPBlocksItems.Component.QUANTUM_CORE.getStack());

        if (AdvancedSolarPanelsConfig.ADVANCED_SOLAR) {
            if (AdvancedSolarPanelsConfig.HARD_RECIPES) {
                Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.ADVANCED.getStack(),
                        "###", "PSP", "XHX",
                        '#', ASPBlocksItems.Component.IRRADIANT_GLASS_PANE.getStack(),
                        'P', Items.getItem("advancedAlloy"),
                        'S', Items.getItem("solarPanel"),
                        'X', Items.getItem("advancedCircuit"),
                        'H', ASPBlocksItems.Component.IRRADIANT_REINFORCED_PLATE.getStack());
            } else {
                Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.ADVANCED.getStack(),
                        "###", "PSP", "XHX",
                        '#', Items.getItem("reinforcedGlass"),
                        'P', Items.getItem("advancedAlloy"),
                        'S', Items.getItem("solarPanel"),
                        'X', Items.getItem("advancedCircuit"),
                        'H', Items.getItem("advancedMachine"));
            }
        }

        if (AdvancedSolarPanelsConfig.HYBRID_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.HYBRID.getStack(),
                    "#B#", "PSP", "XHX",
                    '#', Items.getItem("carbonPlate"),
                    'B', Block.blockLapis,
                    'P', Items.getItem("iridiumPlate"),
                    'S', ASPBlocksItems.Panels.ADVANCED.getStack(),
                    'X', Items.getItem("advancedCircuit"),
                    'H', ASPBlocksItems.Component.ENRICHED_SUNNARIUM.getStack());

            Recipes.advRecipes.addShapelessRecipe(ASPBlocksItems.Panels.HYBRID.getStack(8), ASPBlocksItems.Panels.ULTIMATE.getStack());
        }

        if (AdvancedSolarPanelsConfig.ULTIMATE_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.ULTIMATE.getStack(),
                    " B ", "CSC", "#C#",
                    'B', Block.blockLapis,
                    'C', Items.getItem("coalChunk"),
                    'S', ASPBlocksItems.Panels.ADVANCED.getStack(),
                    '#', ASPBlocksItems.Component.ENRICHED_SUNNARIUM_ALLOY.getStack());

            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.ULTIMATE.getStack(),
                    "XXX", "X#X", "XXX",
                    '#', Items.getItem("advancedCircuit"),
                    'X', ASPBlocksItems.Panels.HYBRID.getStack());
        }

        if (AdvancedSolarPanelsConfig.QUANTUM_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.QUANTUM.getStack(),
                    "XXX", "X#X", "XXX",
                    '#', ASPBlocksItems.Component.QUANTUM_CORE.getStack(),
                    'X', ASPBlocksItems.Panels.ULTIMATE.getStack());
        }

        Recipes.advRecipes.addRecipe(new ItemStack(ASPBlocksItems.ASP_TRANSFORMER),
                "ATA", "C#C", "ATA",
                'A', Items.getItem("advancedMachine"),
                'T', Items.getItem("hvTransformer"),
                'C', Items.getItem("advancedCircuit"),
                '#', ASPBlocksItems.Component.MT_CORE.getStack());

        if (AdvancedSolarPanelsConfig.SPECTRAL_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.SPECTRAL.getStack(),
                    "###", "PSP", "XHX",
                    '#', ASPBlocksItems.Component.SPECTRAL_GLASS_PANE.getStack(),
                    'P', Items.getItem("iridiumPlate"),
                    'S', ASPBlocksItems.Panels.QUANTUM.getStack(),
                    'X', Items.getItem("advancedCircuit"),
                    'H', ASPBlocksItems.Component.SPECTRAL_REINFORCED_PLATE.getStack());
        }

        if (AdvancedSolarPanelsConfig.SINGULAR_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.SINGULAR.getStack(),
                    "#B#", "PSP", "XHX",
                    '#', Items.getItem("carbonPlate"),
                    'B', Block.blockNetherQuartz,
                    'P', Items.getItem("iridiumPlate"),
                    'S', ASPBlocksItems.Panels.SPECTRAL.getStack(),
                    'X', Items.getItem("advancedCircuit"),
                    'H', ASPBlocksItems.Component.SPECTRAL_SUNNARIUM.getStack());

            Recipes.advRecipes.addShapelessRecipe(ASPBlocksItems.Panels.SINGULAR.getStack(4), ASPBlocksItems.Panels.LIGHT_ABSORBING.getStack());
        }

        if (AdvancedSolarPanelsConfig.LIGHT_ABSORBING_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.LIGHT_ABSORBING.getStack(),
                    " B ", "CSC", "#C#",
                    'B', Block.blockRedstone,
                    'C', Items.getItem("coalChunk"),
                    'S', ASPBlocksItems.Panels.SPECTRAL.getStack(),
                    '#', ASPBlocksItems.Component.ENDER_SUNNARIUM_ALLOY.getStack());

            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.LIGHT_ABSORBING.getStack(),
                    " X ", "X#X", " X ",
                    '#', Items.getItem("advancedCircuit"),
                    'X', ASPBlocksItems.Panels.SINGULAR.getStack());

            Recipes.advRecipes.addShapelessRecipe(ASPBlocksItems.Panels.LIGHT_ABSORBING.getStack(4), ASPBlocksItems.Panels.PHOTONIC.getStack());
        }

        if (AdvancedSolarPanelsConfig.PHOTONIC_SOLAR) {
            Recipes.advRecipes.addRecipe(ASPBlocksItems.Panels.PHOTONIC.getStack(),
                    " X ", "X#X", " X ",
                    '#', ASPBlocksItems.Component.PHOTONIC_CORE.getStack(),
                    'X', ASPBlocksItems.Panels.LIGHT_ABSORBING.getStack());
        }

        if (AdvancedSolarPanelsConfig.ADVANCED_HELMET) {
            Recipes.advRecipes.addRecipe(new ItemStack(ASPBlocksItems.ADVANCED_HELMET),
                    " B ", "#H#", "CTC",
                    'B', ASPBlocksItems.Panels.ADVANCED.getStack(),
                    '#', Items.getItem("advancedCircuit"),
                    'H', copyWithWildCard(Items.getItem("nanoHelmet")),
                    'C', Items.getItem("doubleInsulatedGoldCableItem"),
                    'T', Items.getItem("lvTransformer"));
        }

        if (AdvancedSolarPanelsConfig.HYBRID_HELMET) {
            Recipes.advRecipes.addRecipe(new ItemStack(ASPBlocksItems.HYBRID_HELMET),
                    " B ", "#H#", "CTC",
                    'B', ASPBlocksItems.Panels.HYBRID.getStack(),
                    '#', Items.getItem("advancedCircuit"),
                    'H', copyWithWildCard(Items.getItem("quantumHelmet")),
                    'C', Items.getItem("glassFiberCableItem"),
                    'T', Items.getItem("hvTransformer"));
        }

        if (AdvancedSolarPanelsConfig.ULTIMATE_HELMET) {
            Recipes.advRecipes.addRecipe(new ItemStack(ASPBlocksItems.ULTIMATE_HELMET),
                    " B ", "#H#", "CTC",
                    'B', ASPBlocksItems.Panels.ULTIMATE.getStack(),
                    '#', Items.getItem("advancedCircuit"),
                    'H', copyWithWildCard(Items.getItem("quantumHelmet")),
                    'C', Items.getItem("glassFiberCableItem"),
                    'T', Items.getItem("hvTransformer"));

            Recipes.advRecipes.addRecipe(new ItemStack(ASPBlocksItems.ULTIMATE_HELMET),
                    "B", "H",
                    'B', ASPBlocksItems.Panels.ULTIMATE.getStack(),
                    'H', copyWithWildCard(new ItemStack(ASPBlocksItems.HYBRID_HELMET)));
        }
    }

    public static ItemStack copyWithWildCard(ItemStack itemStack) {
        ItemStack ret = itemStack.copy();
        Item.dyePowder.setDamage(ret, 32767);
        return ret;
    }
}