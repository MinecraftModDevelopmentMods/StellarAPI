package stellarapi.feature.command;

import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import stellarapi.feature.perdimres.PerDimensionResourceData;
import stellarapi.feature.perdimres.PerDimensionResourceRegistry;

public class CommandPerDimensionResource extends CommandBase {

	@Override
	public String getCommandName() {
		return "perdimres";
	}
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "command.perdimres.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length < 1)
	        throw new WrongUsageException("command.perdimres.usage");
		
		PerDimensionResourceData data = PerDimensionResourceData.getData(sender.getEntityWorld());
		
		if(args[0].equals("list")) {
			String list = "";
			for(Map.Entry<String, ResourceLocation> entry :
				data.getResourceMap().entrySet()) {
				list = String.format("%s\n (%s -> %s)", list, entry.getKey(), entry.getValue());
			}
			if(list.contains("\n"))
				list = list.substring(list.indexOf('\n'));
			
			sender.addChatMessage(new ChatComponentTranslation("command.perdimres.list", list));
		
		} else if(args[0].equals("available")) {
			String list = "";
			for(String id : PerDimensionResourceRegistry.getInstance().getResourceIds())
				list = String.format("%s, %s", list, id);
			
			if(list.contains(","))
				list = list.substring(list.indexOf(','));
			
			sender.addChatMessage(new ChatComponentTranslation("command.perdimres.available", list));
		
		} else if(args[0].equals("set")) {
			if(args.length < 3)
		        throw new WrongUsageException("command.perdimres.usage");
			String resourceId = args[1];
			String resourceLocation = args[2];
			
			if(PerDimensionResourceRegistry.getInstance().getResourceIds().contains(resourceId)) {
				data.addToResourceMap(resourceId, new ResourceLocation(resourceLocation));
				addChatMessage(sender, new ChatComponentTranslation("command.perdimres.set.success", resourceId, resourceLocation), EnumChatFormatting.AQUA);
			}
			else addChatMessage(sender, new ChatComponentTranslation("command.perdimres.set.fail", resourceId), EnumChatFormatting.RED);
			
		} else if(args[0].equals("remove")) {
			if(args.length < 2)
		        throw new WrongUsageException("command.perdimres.usage");
			String resourceId = args[1];
			if(data.getResourceMap().containsKey(resourceId)) {
				data.removeFromResourceMap(resourceId);
				addChatMessage(sender, new ChatComponentTranslation("command.perdimres.remove.success", resourceId), EnumChatFormatting.AQUA);
			}
			else addChatMessage(sender, new ChatComponentTranslation("command.perdimres.remove.fail", resourceId), EnumChatFormatting.RED);
		}
	}
	
	private void addChatMessage(ICommandSender sender, IChatComponent component, EnumChatFormatting color) {
		component.setChatStyle(component.getChatStyle().setColor(color));
		sender.addChatMessage(component);
	}

}
