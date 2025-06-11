package reforged.ic2.addons.asp.tiles;

import mods.vintage.core.platform.lang.FormattedTranslator;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;

public class SolarPanels {

    public static class Advanced extends TileEntityAdvancedSolarPanel {
        public Advanced() {
            super(AdvancedSolarPanelsConfig.ADVANCED);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.YELLOW.format("block.solar.advanced.name");
        }
    }

    public static class Hybrid extends TileEntityAdvancedSolarPanel {
        public Hybrid() {
            super(AdvancedSolarPanelsConfig.HYBRID);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.AQUA.format("block.solar.hybrid.name");
        }
    }

    public static class Ultimate extends TileEntityAdvancedSolarPanel {
        public Ultimate() {
            super(AdvancedSolarPanelsConfig.ULTIMATE);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.LIGHT_PURPLE.format("block.solar.ultimate.name");
        }
    }

    public static class Quantum extends TileEntityAdvancedSolarPanel {
        public Quantum() {
            super(AdvancedSolarPanelsConfig.QUANTUM);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.DARK_AQUA.format("block.solar.quantum.name");
        }
    }

    public static class Spectral extends TileEntityAdvancedSolarPanel {
        public Spectral() {
            super(AdvancedSolarPanelsConfig.SPECTRAL);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.GOLD.format("block.solar.spectral.name");
        }
    }

    public static class Singular extends TileEntityAdvancedSolarPanel {
        public Singular() {
            super(AdvancedSolarPanelsConfig.SINGULAR);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.GRAY.format("block.solar.singular.name");
        }
    }

    public static class LightAbsorbing extends TileEntityAdvancedSolarPanel {
        public LightAbsorbing() {
            super(AdvancedSolarPanelsConfig.LIGHT_ABSORBING);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.RED.format("block.solar.light_absorbing.name");
        }
    }

    public static class Photonic extends TileEntityAdvancedSolarPanel {
        public Photonic() {
            super(AdvancedSolarPanelsConfig.PHOTONIC);
        }

        @Override
        public String getInvName() {
            return FormattedTranslator.GREEN.format("block.solar.photonic.name");
        }
    }
}
