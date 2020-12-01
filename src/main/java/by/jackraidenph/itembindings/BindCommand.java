package by.jackraidenph.itembindings;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BindCommand implements ICommand {

    @Override
    public String getName() {
        return "bindtoitem";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return I18n.format("description.command");

    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        ItemStack item = ((EntityPlayer) sender.getCommandSenderEntity()).getHeldItem(EnumHand.MAIN_HAND);
        if (!item.hasTagCompound())
            item.setTagCompound(new NBTTagCompound());

        if (args.length == 1 && args[0].equals("null")) {
            item.getTagCompound().removeTag("binding");
            if (item.getTagCompound().getSize() == 0)
                item.setTagCompound(null);
            return;
        }

        String arrayString = Arrays.toString(args).replaceAll(",", "");
        item.getTagCompound().setString("binding", arrayString.substring(1, arrayString.length() - 1));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> list = new ArrayList<>();
        if (args.length == 1)
            server.getCommandManager().getCommands().forEach((x, y) -> {
                if (y.checkPermission(server, sender))
                    list.add(x);
            });
        else
            server.getCommandManager().getCommands().forEach((x, y) -> {
                if (x.equals(args[0])) {
                    list.addAll(y.getTabCompletions(server, sender, Arrays.copyOfRange(args, 1, args.length), targetPos));
                }
            });
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
