package fi.dy.masa.litematica.config;

import java.io.File;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mumfrey.liteloader.core.LiteLoader;
import fi.dy.masa.litematica.Reference;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.selection.AreaSelectionMode;
import fi.dy.masa.litematica.util.JsonUtils;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.config.options.ConfigString;

public class Configs
{
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Generic
    {
        public static final ConfigOptionList    SELECTION_MODE          = new ConfigOptionList( "selectionMode", AreaSelectionMode.CORNERS, "The area selection mode to use");
        public static final ConfigString        TOOL_ITEM               = new ConfigString(     "toolItem", "minecraft:stick", "The item to use as the \"tool\" for selections etc.");
        public static final ConfigBoolean       TOOL_ITEM_ENABLED       = new ConfigBoolean(    "toolItemEnabled", true, "If true, then the \"tool\" item can be used to control selections etc.");
        public static final ConfigBoolean       VERBOSE_LOGGING         = new ConfigBoolean(    "verboseLogging", false, "If enabled, a bunch of debug messages will be printed to the console");

        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                SELECTION_MODE,
                TOOL_ITEM,
                TOOL_ITEM_ENABLED,
                VERBOSE_LOGGING
        );
    }

    public static class Visuals
    {
        public static final ConfigDouble        GHOST_BLOCK_ALPHA               = new ConfigDouble(     "ghostBlockAlpha", 0.5, 0, 1, "The alpha value of the ghost blocks, when rendering them as translucent");
        public static final ConfigDouble        PLACEMENT_BOX_SIDE_ALPHA        = new ConfigDouble(     "placementBoxSideAlpha", 0.2, 0, 1, "The alpha value of the sub-region boxes' side");
        public static final ConfigBoolean       RENDER_BLOCKS_AS_TRANSLUCENT    = new ConfigBoolean(    "renderBlocksAsTranslucent", false, "If enabled, then the schematics are rendered using translucent \"ghost blocks\"");
        public static final ConfigBoolean       RENDER_SELECTION_BOX_SIDES      = new ConfigBoolean(    "renderSelectionBoxSides", true, "If enabled, then the area selection boxes will have their side quads rendered");
        public static final ConfigBoolean       RENDER_PLACEMENT_BOX_SIDES      = new ConfigBoolean(    "renderPlacementBoxSides", false, "If enabled, then the placed schematic sub-region boxes will have their side quads rendered");
        public static final ConfigColor         SELECTION_BOX_SIDE_COLOR        = new ConfigColor(      "selectionBoxSideColor", "0x30FFFFFF", "If enabled, then the area selection boxes will have their side quads rendered");

        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                GHOST_BLOCK_ALPHA,
                PLACEMENT_BOX_SIDE_ALPHA,
                RENDER_BLOCKS_AS_TRANSLUCENT,
                RENDER_SELECTION_BOX_SIDES,
                RENDER_PLACEMENT_BOX_SIDES,
                SELECTION_BOX_SIDE_COLOR
                );
    }

    public static void load()
    {
        File configFile = new File(LiteLoader.getCommonConfigFolder(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigValues(root, "Generic", Generic.OPTIONS);
                ConfigUtils.readConfigValues(root, "Visuals", Visuals.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", ImmutableList.copyOf(Hotkeys.values()));
            }
        }

        DataManager.setToolItem(Generic.TOOL_ITEM.getStringValue());
    }

    public static void save()
    {
        File dir = LiteLoader.getCommonConfigFolder();

        if (dir.exists() && dir.isDirectory())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigValues(root, "Generic", Generic.OPTIONS);
            ConfigUtils.writeConfigValues(root, "Visuals", Visuals.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", ImmutableList.copyOf(Hotkeys.values()));

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }
}
