package stellarapi.feature.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.CommandTime;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.PeriodHelper;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.daywake.DaytimeChecker;
import stellarapi.api.daywake.EnumDaytimeDescriptor;

public class FixedCommandTime extends CommandTime {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 1) {
			if (args[0].equals("set")) {
				long i1;

				if (args[1].equals("day")) {
					i1 = this.getDay(sender.getEntityWorld());
				} else if (args[1].equals("night")) {
					i1 = this.getNight(sender.getEntityWorld());
				} else if (this.getDescriptor(args[1]) != null) {
					i1 = this.getTime(sender.getEntityWorld(), this.getDescriptor(args[1]));
				} else {
					i1 = parseInt(args[1], 0);
				}

				this.setAllWorldTimes(server, i1);
				notifyCommandListener(sender, this, "commands.time.set", new Object[] { Long.valueOf(i1) });
				return;
			}

			if (args[0].equals("add")) {
				int l = parseInt(args[1], 0);
				this.incrementAllWorldTimes(server, l);
				notifyCommandListener(sender, this, "commands.time.added", new Object[] { Integer.valueOf(l) });
				return;
			}

			if (args[0].equals("query")) {
				if (args[1].equals("daytime")) {
					int k = this.getDayTime(sender.getEntityWorld());
					sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, k);
					notifyCommandListener(sender, this, "commands.time.query", new Object[] { Integer.valueOf(k) });
					return;
				}

				if (args[1].equals("day")) {
					int j = this.getDayNumber(sender.getEntityWorld());
					sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
					notifyCommandListener(sender, this, "commands.time.query", new Object[] { Integer.valueOf(j) });
					return;
				}

				if (args[1].equals("gametime")) {
					int i = (int) (sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
					sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
					notifyCommandListener(sender, this, "commands.time.query", new Object[] { Integer.valueOf(i) });
					return;
				}
			}
		}

		throw new WrongUsageException("commands.time.usage", new Object[0]);
	}

	protected void setAllWorldTimes(MinecraftServer server, long time) {
		for (int i = 0; i < server.worlds.length; ++i) {
			server.worlds[i].setWorldTime(time);
		}
	}

	protected void incrementAllWorldTimes(MinecraftServer server, long amount) {
		for (int i = 0; i < server.worlds.length; ++i) {
			WorldServer worldserver = server.worlds[i];
			worldserver.setWorldTime(worldserver.getWorldTime() + amount);
		}
	}

	public long getDay(World world) {
		long defaultValue = 1000L;

		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();
		return checker.timeForCertainDescriptor(world, EnumDaytimeDescriptor.MORNING, defaultValue);
	}

	public long getNight(World world) {
		long defaultValue = 13000L;

		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();
		return checker.timeForCertainDescriptor(world, EnumDaytimeDescriptor.EVENING, defaultValue);
	}
	
	public EnumDaytimeDescriptor getDescriptor(String str) {
		for(EnumDaytimeDescriptor desc : EnumDaytimeDescriptor.values()) {
			if(str.toUpperCase().equals(desc.name().toLowerCase()))
				return desc;
		}
		
		return null;
	}
	
	public long getTime(World world, EnumDaytimeDescriptor descriptor) {
		DaytimeChecker checker = StellarAPIReference.getDaytimeChecker();
		return checker.timeForCertainDescriptor(world, descriptor, 0);
	}

	public int getDayTime(World world) {
		CelestialPeriod period = PeriodHelper.getDayPeriod(world);
		if (period != null) {
			return (int) (period.getPeriodLength() * period.getBiasedOffset(world.getWorldTime(), 0.0f, -0.25));
		} else
			return (int) (world.getWorldTime() % 24000L);
	}

	public int getDayNumber(World world) {
		CelestialPeriod period = PeriodHelper.getDayPeriod(world);
		if (period != null) {
			return (int) Math.floor(world.getWorldTime() / period.getPeriodLength()
					- period.getBiasedOffset(world.getWorldTime(), 0.0f, -0.25));
		} else
			return (int) (world.getWorldTime() / 24000L % 2147483647L);
	}
}
