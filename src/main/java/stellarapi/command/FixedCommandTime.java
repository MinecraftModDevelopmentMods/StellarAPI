package stellarapi.command;

import net.minecraft.command.CommandTime;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyProvider;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.mc.DaytimeChecker;
import stellarapi.api.mc.EnumDaytimeDescriptor;

public class FixedCommandTime extends CommandTime {
	
	@Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 1)
        {
            long i;

            if (args[0].equals("set"))
            {
                if (args[1].equals("day"))
                {
                    i = this.getDay(sender.getEntityWorld());
                }
                else if (args[1].equals("night"))
                {
                    i = this.getModifiedTime(sender.getEntityWorld());
                }
                else
                {
                    i = parseIntWithMin(sender, args[1], 0);
                }

                this.setTime(sender, i);
                func_152373_a(sender, this, "commands.time.set", new Object[] {Long.valueOf(i)});
                return;
            }

            if (args[0].equals("add"))
            {
                i = parseIntWithMin(sender, args[1], 0);
                this.addTime(sender, i);
                func_152373_a(sender, this, "commands.time.added", new Object[] {Long.valueOf(i)});
                return;
            }
        }

        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }
	
	/**
     * Set the time in the server object.
     */
    protected void setTime(ICommandSender p_71552_1_, long p_71552_2_)
    {
        for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j)
        {
            MinecraftServer.getServer().worldServers[j].setWorldTime(p_71552_2_);
        }
    }

    /**
     * Adds (or removes) time in the server object.
     */
    protected void addTime(ICommandSender p_71553_1_, long p_71553_2_)
    {
        for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j)
        {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[j];
            worldserver.setWorldTime(worldserver.getWorldTime() + p_71553_2_);
        }
    }
	
	public long getDay(World world) {
		long defaultValue = 1000L;
		
		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();
		
		return checker.timeForCertainDescriptor(world, EnumDaytimeDescriptor.MORNING, defaultValue);
	}
	
	public long getMidnight(World world) {
		if(!StellarAPIReference.hasSkyProvider(world)) {
			return (long) (timeOffset * 24000);
		}/**=>Daytime descriptor?*/
		
		long time = world.getWorldTime();
    	ISkyProvider skyProvider = StellarAPIReference.getSkyProvider(world);
    	double wakeDayOffset = timeOffset;
		double currentDayOffset = skyProvider.getDaytimeOffset(time);
		double dayLength = skyProvider.getDayLength();

    	double modifiedWorldTime = time + (-wakeDayOffset - currentDayOffset) * dayLength;
    	while(modifiedWorldTime < time)
    		modifiedWorldTime += dayLength;
    	
    	return (long) modifiedWorldTime;
	}

}
