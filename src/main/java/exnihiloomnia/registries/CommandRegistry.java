package exnihiloomnia.registries;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.barrel.BarrelCraftingRegistry;
import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.crook.CrookRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.HeatRegistry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.sifting.SieveRegistry;
import mezz.jei.JustEnoughItems;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRegistry extends CommandBase {
    private static final List<String> aliases = new ArrayList<>();

    public CommandRegistry() {
        aliases.add("enoreg");
    }

    @Override
    public String getCommandName() {
        return "exnihiloregistry";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return ENO.MODID + ".commands.registry.usage";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1)
            return  getListOfStringsMatchingLastWord(args, "reload", "clear", "load");
        else if (args.length == 2)
            return getListOfStringsMatchingLastWord(args, "heat", "crucible", "sieve", "hammer", "compost", "crook", "barrel");
        else
            return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new WrongUsageException(ENO.MODID + ".commands.registry.usage", 0);
        else {
            String command = args[0];
            IRegistry reg = null;
            if (args.length == 2)
                reg = getRegistryFromString(args[1]);

            ENO.config.load();
            ENORegistries.configure(ENO.config);
            if (ENO.config.hasChanged())
                ENO.config.save();

            if (reg == null) {
                switch (command) {
                    case "clear":
                        ENORegistries.clear();
                        break;
                    case "load":
                        ENORegistries.initialize();
                        break;
                    case "reload":
                        ENORegistries.clear();
                        ENORegistries.initialize();
                        break;
                    default:
                        throw new WrongUsageException(ENO.MODID + ".commands.registry.usage", 0);
                }
            }
            else if (command.equals("clear")) {
                reg.clear();
            } else if (command.equals("load")) {
                reg.initialize();
            } else if (command.equals("reload")) {
                reg.clear();
                reg.initialize();
            }
            else
                throw new WrongUsageException(ENO.MODID + ".commands.registry.usage", 0);

            if (Loader.isModLoaded("JEI"))
                JustEnoughItems.getProxy().restartJEI();
        }
    }

    private IRegistry getRegistryFromString(String s) {
        switch (s) {
            case "heat":
                return HeatRegistry.INSTANCE;
            case "crucible":
                return CrucibleRegistry.INSTANCE;
            case "sieve":
                return SieveRegistry.INSTANCE;
            case "hammer":
                return HammerRegistry.INSTANCE;
            case "compost":
                return CompostRegistry.INSTANCE;
            case "crook":
                return CrookRegistry.INSTANCE;
            case "barrel":
                return BarrelCraftingRegistry.INSTANCE;
            default:
                return null;
        }
    }
}
