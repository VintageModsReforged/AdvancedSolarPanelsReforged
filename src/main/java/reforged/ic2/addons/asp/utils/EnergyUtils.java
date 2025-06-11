package reforged.ic2.addons.asp.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import mods.vintage.core.helpers.StackHelper;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class EnergyUtils {

    public static final DecimalFormatSymbols ROOT;
    public static final DecimalFormat THERMAL_GEN;
    public static final DecimalFormat SOLAR_TURBINE;
    public static final DecimalFormat NATURAL;
    public static final DecimalFormat DECIMAL;
    public static final DecimalFormat EU_FORMAT;
    public static final DecimalFormat EU_READER_FORMAT;
    public static final DecimalFormat CABLE_LOSS_FORMAT;

    public static ItemStack getCharged(Item item, int charge) {
        if (!(item instanceof IElectricItem)) {
            throw new IllegalArgumentException(item + " must be an instanceof IElectricItem");
        } else {
            ItemStack ret = new ItemStack(item);
            ElectricItem.manager.charge(ret, charge, Integer.MAX_VALUE, true, false);
            return ret;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addEnergyInfo(List tooltips, IElectricItem item,  ItemStack stack) {
        tooltips.add(FormattedTranslator.AQUA.format("message.info.energy",
                getCharge(stack),
                item.getMaxCharge(stack),
                FormattedTranslator.WHITE.format("message.info.energy.tier",
                        FormattedTranslator.YELLOW.literal(item.getTier(stack) + ""))));
    }

    public static int getCharge(ItemStack stack) {
        NBTTagCompound tag = StackHelper.getOrCreateTag(stack);
        return tag.getInteger("charge");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addChargeVariants(Item item, List list) {
        list.add(getCharged(item, 0));
        list.add(getCharged(item, Integer.MAX_VALUE));
    }

    public static int getPowerFromTier(int tier) {
        return 8 << Math.min(tier, 13) * 2;
    }

    public static int getTierFromPower(int value) {
        return value <= 8 ? 0 : (int) Math.ceil(Math.log((double) value * 0.125) * (1.0 / Math.log(4.0)));
    }

    public static String formatNumber(double number, int digits) {
        return formatNumber(number, digits, false);
    }

    public static String formatInt(int number, int digits) {
        return formatInt(number, digits, false);
    }

    public static String formatInt(int number, int digits, boolean fixedLength) {
        return formatNumber(number, digits, fixedLength);
    }

    public static String formatNumber(double number, int digits, boolean fixedLength) {
        String suffix = "";
        boolean allow = (number >= 1.0E9 ? String.valueOf((long) number) : String.valueOf(number)).length() > digits;
        double outputNumber = number;

        int actualDigits;
        for (actualDigits = 0; actualDigits < "kmbt".length() && outputNumber >= 1000.0 && allow; ++actualDigits) {
            outputNumber /= 1000.0;
            suffix = Character.toString("kmbt".charAt(actualDigits));
        }

        actualDigits = digits - suffix.length();
        if (outputNumber % 1.0 == 1.0) {
            ++actualDigits;
        }

        int naturalLength = NATURAL.format((int) outputNumber).length();
        int decimalLength = DECIMAL.format(outputNumber - (double) ((int) outputNumber)).length();
        StringBuilder patternBuilder = new StringBuilder();

        for (int i = 1; actualDigits > 1 && naturalLength > 1; ++i) {
            patternBuilder.insert(0, "#");
            --actualDigits;
            --naturalLength;
            if (i % 2 == 0 && actualDigits > 1 && naturalLength > 1) {
                if (actualDigits == 2 || naturalLength == 2) {
                    break;
                }

                patternBuilder.insert(0, ",");
                --actualDigits;
                --naturalLength;
            }
        }

        patternBuilder.append("0");
        if (actualDigits > 1 && decimalLength > 0) {
            patternBuilder.append(".");
            --actualDigits;

            while (actualDigits > 0 && decimalLength > 0) {
                patternBuilder.append("#");
                --actualDigits;
                --decimalLength;
            }
        }

        String pattern = patternBuilder.toString();
        String output = (new DecimalFormat(pattern + suffix, ROOT)).format(outputNumber);
        String fill = "";
        int length = output.length();
        if (fixedLength && output.length() < digits) {
            for (int i = 0; i < digits - length; ++i) {
                fill = fill.concat(" ");
            }

            output = fill + output;
        }

        return output;
    }

    static {
        ROOT = new DecimalFormatSymbols(Locale.ROOT);
        THERMAL_GEN = new DecimalFormat("#0.00", ROOT);
        SOLAR_TURBINE = new DecimalFormat("#00.00", ROOT);
        NATURAL = new DecimalFormat("###,##0", ROOT);
        DECIMAL = new DecimalFormat(".#########", ROOT);
        EU_FORMAT = new DecimalFormat("###,###", ROOT);
        EU_READER_FORMAT = new DecimalFormat("###,###.##", ROOT);
        CABLE_LOSS_FORMAT = new DecimalFormat("0.####", ROOT);
    }
}
