package worldsets.api.provider;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import worldsets.api.provider.IProviderRegistry.*;

public class ProviderBuilder<P extends IProvider> {
	private ResourceLocation registryName;
	private Class<P> registryType;
	private ResourceLocation defaultKey;
	private boolean addToLast = false;
	private Comparator<P> providerComparator;
	private List<ResourceLocation> dependencies = Lists.newArrayList();

	private List<AddCallback<P>> addCallback = Lists.newArrayList();
	private List<CreateCallback<P>> createCallback = Lists.newArrayList();
	private List<SubstitutionCallback<P>> substitutionCallback = Lists.newArrayList();
	private List<ClearCallback<P>> clearCallback = Lists.newArrayList();

	public ProviderBuilder<P> setName(ResourceLocation name)
	{
		this.registryName = name;
		return this;
	}

	public ProviderBuilder<P> setType(Class<P> type)
	{
		this.registryType = type;
		return this;
	}

	/**
	 * Sets default key.
	 * Completely optional.
	 * */
	public ProviderBuilder<P> setDefaultKey(ResourceLocation key)
	{
		this.defaultKey = key;
		return this;
	}

	/**
	 * Determines if it will add the element to the first or last on addition.
	 * It adds to the first by default.
	 * */
	public ProviderBuilder<P> setAddToLast(boolean last) {
		this.addToLast = last;
		return this;
	}

	/**
	 * Gives a comparator to determine the order.
	 * */
	public ProviderBuilder<P> setComparator(Comparator<P> comparator) {
		this.providerComparator = comparator;
		return this;
	}

	public ProviderBuilder<P> addDependencies(ResourceLocation... depRegIDs) {
		for(ResourceLocation id : depRegIDs)
			dependencies.add(id);
		return this;
	}

	@SuppressWarnings("unchecked")
	public ProviderBuilder<P> addCallback(Object inst)
	{
		if (inst instanceof AddCallback)
			this.add((AddCallback<P>)inst);
		if (inst instanceof CreateCallback)
			this.add((CreateCallback<P>)inst);
		if (inst instanceof SubstitutionCallback)
			this.add((SubstitutionCallback<P>)inst);
		if (inst instanceof ClearCallback)
			this.add((ClearCallback<P>)inst);
		return this;
	}

	public ProviderBuilder<P> add(AddCallback<P> add)
	{
		this.addCallback.add(add);
		return this;
	}

	public ProviderBuilder<P> add(CreateCallback<P> create)
	{
		this.createCallback.add(create);
		return this;
	}

	public ProviderBuilder<P> add(ClearCallback<P> clear)
	{
		this.clearCallback.add(clear);
		return this;
	}

	public ProviderBuilder<P> add(SubstitutionCallback<P> sub)
	{
		this.substitutionCallback.add(sub);
		return this;
	}

	@SuppressWarnings("deprecation")
	public IProviderRegistry<P> create()
	{
		return ProviderRegistry.createRegistry(this.registryName, this.registryType,
				this.addToLast, this.providerComparator, getAdd(), getCreate(), getClear(), getSubstitution());
	}

	@Nullable
	private AddCallback<P> getAdd()
	{
		if (this.addCallback.isEmpty())
			return null;
		if (this.addCallback.size() == 1)
			return this.addCallback.get(0);

		return new AddCallback<P>()
		{
			@Override
			public void onAdd(P obj, Map<ResourceLocation, ?> slaveset)
			{
				for (AddCallback<P> cb : ProviderBuilder.this.addCallback)
					cb.onAdd(obj, slaveset);
			}
		};
	}

	@Nullable
	private CreateCallback<P> getCreate()
	{
		if (this.createCallback.isEmpty())
			return null;
		if (this.createCallback.size() == 1)
			return this.createCallback.get(0);

		return new CreateCallback<P>()
		{
			@Override
			public void onCreate(Map<ResourceLocation, ?> slaveset)
			{
				for (CreateCallback<P> cb : ProviderBuilder.this.createCallback)
					cb.onCreate(slaveset);
			}
		};
	}

	@Nullable
	private SubstitutionCallback<P> getSubstitution()
	{
		if (this.substitutionCallback.isEmpty())
			return null;
		if (this.substitutionCallback.size() == 1)
			return this.substitutionCallback.get(0);

		return new SubstitutionCallback<P>()
		{
			@Override
			public void onSubstitution(Map<ResourceLocation, ?> slaveset, P original, P replacement)
			{
				for (SubstitutionCallback<P> cb : ProviderBuilder.this.substitutionCallback)
					cb.onSubstitution(slaveset, original, replacement);
			}
		};
	}

	@Nullable
	private ClearCallback<P> getClear()
	{
		if (this.clearCallback.isEmpty())
			return null;
		if (this.clearCallback.size() == 1)
			return this.clearCallback.get(0);

		return new ClearCallback<P>()
		{
			@Override
			public boolean missingOnServer(Map<ResourceLocation, ?> slaveset, Set<P> missingOnServer, List<P> providers) {
				boolean result = false;
				for (MissingCallback<P> cb : ProviderBuilder.this.clearCallback)
					result = result || cb.missingOnServer(slaveset, missingOnServer, providers);
				return result;
			}
		};
	}
}