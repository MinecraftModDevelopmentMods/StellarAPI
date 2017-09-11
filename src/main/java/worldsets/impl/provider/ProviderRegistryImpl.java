package worldsets.impl.provider;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;
import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistry;

public class ProviderRegistryImpl<P extends IProvider> implements IProviderRegistry<P> {

	private Class<P> providerType;
	private Map<ResourceLocation, P> providers;
	private Collection<ResourceLocation> listIDs;
	private ResourceLocation defaultKey;
	private P defaultValue;

	private boolean addToFirst;
	private Comparator<P> comparator;

	// TODO Providers fill in details

	public ProviderRegistryImpl(ResourceLocation defkey, boolean addFirst, Comparator<P> comp) {
		this.defaultKey = defkey;
		this.addToFirst = addFirst;
		this.comparator = comp;

		this.providers = Maps.newHashMap();

		if(comp != null)
			this.listIDs = Sets.newTreeSet(Ordering.from(comp).onResultOf(
					Functions.forMap(this.providers)));
		else this.listIDs = Lists.newArrayList();
	}

	@Override
	public void register(ResourceLocation key, P provider) {
		// TODO Providers check key duplication
		if(this.comparator == null && this.addToFirst)
			((List<ResourceLocation>)listIDs).add(0, key);
		else listIDs.add(key);
	}

	@Override
	public P getProvider(ResourceLocation key) {
		if(!providers.containsKey(key))
			return this.defaultValue;
		else return providers.get(key);
	}

	@Override
	public Iterable<ResourceLocation> keys() {
		return Iterables.unmodifiableIterable(this.listIDs);
	}

	@Override
	public ResourceLocation getDefaultKey() {
		return this.defaultKey;
	}

	@Override
	public void substitute(ResourceLocation key, P replacement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<P> getProviderType() {
		return this.providerType;
	}

	@Override
	public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Iterator<P> iterator() {
		Iterators.unmodifiableIterator(
				Iterators.transform(listIDs.iterator(), Functions.forMap(this.providers)));
		return null;
	}
}
