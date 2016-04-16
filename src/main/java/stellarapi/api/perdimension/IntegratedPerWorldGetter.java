package stellarapi.api.perdimension;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.World;

/**
 * Integrated per world getter. Pretty straightforward.
 * */
public class IntegratedPerWorldGetter<T> {
	
	private List<IPerWorldGetter<T>> listGetters = Lists.newArrayList();
	
	public void register(IPerWorldGetter<T> getter) {
		listGetters.add(getter);
	}
	
	public T get(World world, T defaultValue) {
		T object = defaultValue;
		for(IPerWorldGetter<T> getter : this.listGetters)
			if(getter.accept(world, object))
				object = getter.get(world, object);
		
		return object;
	}

}
