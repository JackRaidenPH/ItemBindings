package by.jackraidenph.itembindings;

import by.jackraidenph.itembindings.proxy.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ItemBindings.MODID, name = ItemBindings.NAME, version = ItemBindings.VERSION)
public class ItemBindings {
    public static final String MODID = "itembindings";
    public static final String NAME = "Item Bindings Mod";
    public static final String VERSION = "1.0.2";
    @SidedProxy(clientSide = "by.jackraidenph.itembindings.proxy.ClientProxy", serverSide = "by.jackraidenph.itembindings.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static Logger MOD_LOGGER;

    @SubscribeEvent
    public void onDescription(ItemTooltipEvent e) {
        if (e.getItemStack().hasTagCompound() && e.getItemStack().getTagCompound().hasKey("binding")) {
            String line = TextFormatting.LIGHT_PURPLE + e.getItemStack().getTagCompound().getString("binding");
            e.getToolTip().add(line);
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.RightClickItem e) {
        if (e.getItemStack().hasTagCompound())
            if (e.getItemStack().getTagCompound().hasKey("binding")) {
                if (e.getEntityPlayer().getServer() != null)
                    e.getEntityPlayer().getServer().getCommandManager().executeCommand(e.getEntityPlayer(), e.getItemStack().getTagCompound().getString("binding"));
            }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        MinecraftForge.EVENT_BUS.register(this);
        MOD_LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MOD_LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
