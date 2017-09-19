package stellarapi.internal.coordinates;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.coordinates.CCoordInstance;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.CoordContext;
import stellarapi.api.coordinates.ICoordElement;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.lib.math.Matrix4;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CCoordSystem implements ICoordSystem {
	private final WorldSet worldSet;

	private final IForgeRegistry<CCoordinates> registry = SAPIRegistries.getCoordRegistry();
	private List<CCoordInstance> builtInstances = null;
	private Map<ResourceLocation, CCoordInstance> builtInstanceMap = null;

	private final IProviderRegistry<ICoordProvider> coordProvRegistry = ProviderRegistry.findRegistry(ICoordProvider.class);
	private ResourceLocation providerID = null;
	private ICoordHandler handler = null;

	public CCoordSystem(WorldSet worldSet) { this.worldSet = worldSet; }

	@Override
	public void setProviderID(ResourceLocation providerID) {
		if(!coordProvRegistry.containsKey(providerID))
			throw new IllegalArgumentException(String.format("There's no provider for providerID %s", providerID));
		ICoordProvider provider = coordProvRegistry.getProvider(providerID);

		int cnt = 0;
		for(CCoordinates coords : this.registry)
			if(coords.getDefaultParentID() == null && !provider.overrideSettings(this.worldSet, coords))
				cnt++;
		if(cnt != 1)
			return;

		this.providerID = providerID;
		this.handler = provider.generateHandler(this.worldSet);
	}
	@Override
	public ResourceLocation getProviderID() { return this.providerID; }
	@Override
	public ICoordHandler getHandler() { return this.handler; }

	@Override
	public void setupPartial() { this.setup(false); }
	@Override
	public void setupComplete() { this.setup(true); }

	public void setup(boolean needComplete) {
		Map<ResourceLocation, CCoordInstance> instances = Maps.newHashMap();
		handler.instantiate(this.registry, instances);

		// Finds the missing instances.
		for(ResourceLocation key : registry.getKeys())
			if(!instances.containsKey(key))
				instances.put(key, CCoordInstance.of(key, registry.getValue(key), instances));
 
		List<CCoordInstance> lackCElements = Lists.newArrayList();
		for(CCoordInstance coord : instances.values()) {
			if(coord.getParent() == CCoordInstance.PLACEHOLDER) {
				if(coord.getOrigin() == null)
					throw new IllegalStateException("Placeholder can't be used for generated instance");

				ResourceLocation parentID = coord.getOrigin().getDefaultParentID();
				if(instances.containsKey(parentID))
					coord.setParent(instances.get(parentID));
				else throw new IllegalStateException("No mapping for the parent ID which should be injected correctly");
			}

			if(!coord.hasCoordElements())
				lackCElements.add(coord);
			else {
				// TODO CoordSystem check for position-specifics and collect error
			}
		}

		// Find root coordinates. Gives error if there's multiple roots.
		List<CCoordInstance> roots = Lists.newArrayListWithCapacity(1);
		for(CCoordInstance coord : instances.values())
			if(coord.isRoot())
				roots.add(coord);

		// Builds children map
		Multimap<CCoordInstance, CCoordInstance> childMap = HashMultimap.create();
		for(CCoordInstance coord : instances.values())
			if(coord.getParent() != null)
				childMap.put(coord.getParent(), coord);

		ImmutableList.Builder<CCoordInstance> builder = ImmutableList.builder();
		ImmutableMap.Builder<ResourceLocation, CCoordInstance> mapBuilder = ImmutableMap.builder();
		ListMultimap<CCoordInstance, CCoordInstance> cyclicMap = ArrayListMultimap.create(); // Stores cycles in reverse order
		Stack<CCoordInstance> stack = new Stack();

		// Put things in order, from parents to children
		for(CCoordInstance coord : roots) {
			stack.clear();
			stack.push(coord);

			while(!stack.isEmpty()) {
				CCoordInstance current = stack.pop();
				if(stack.contains(current)) { // Dead loop detection
					CCoordInstance tracked = current;
					do {
						tracked = current.getParent();
						cyclicMap.put(current, tracked);
					} while(tracked != current);
					continue;
				}

				builder.add(current);
				mapBuilder.put(current.getID(), current);

				for(CCoordInstance next : childMap.get(current))
					stack.push(next);
			}
		}
		// TODO CoordSystem collect errors for instances lacking coordinate elements (complete only)
		// TODO CoordSystem compose errors if more than 1 root (complete only)
		// TODO CoordSystem collect errors for loops

		this.builtInstances = builder.build();
		this.builtInstanceMap = mapBuilder.build();
	}

	@Override
	public Iterable<ICoordElement> getElements(CCoordinates coordinates) {
		CCoordInstance instance = builtInstanceMap.get(registry.getKey(coordinates));
		List<List<ICoordElement>> iterables = Lists.newArrayList();
		while(!instance.isRoot()) {
			iterables.add(0, Arrays.asList(instance.getCoordElements()));
			instance = instance.getParent();
		}

		return Iterables.concat(iterables);
	}

	@Override
	public CCoordinates commonAncestor(CCoordinates coordA, CCoordinates coordB) {
		CCoordInstance insA = builtInstanceMap.get(registry.getKey(coordA));
		CCoordInstance insB = builtInstanceMap.get(registry.getKey(coordB));

		List<CCoordInstance> insAList = Lists.newArrayList();
		List<CCoordInstance> insBList = Lists.newArrayList();

		while(insA != null) {
			insAList.add(0, insA);
			insA = insA.getParent();
		}
		while(insB != null) {
			insBList.add(0, insB);
			insB = insB.getParent();
		}

		int i = 1;
		for(; i < Math.min(insAList.size(), insBList.size()); i++) {
			if(insAList.get(i) != insBList.get(i))
				break;
		}

		while(--i >= 0) {
			if(insAList.get(i).getOrigin() != null)
				return insAList.get(i).getOrigin();
		}

		// TODO CoordSystem exception
		throw new IllegalStateException("");
	}

	@Override
	public boolean isDescendant(CCoordinates ancestor, CCoordinates descendant) {
		CCoordInstance insAnc = builtInstanceMap.get(registry.getKey(ancestor));
		CCoordInstance insDes = builtInstanceMap.get(registry.getKey(descendant));

		while(insDes != null) {
			if(insAnc == insDes)
				return true;
			insDes = insDes.getParent();
		}

		return false;
	}

	@Override
	public Iterable<ICoordElement> between(CCoordinates ancestor, CCoordinates descendant) {
		CCoordInstance insAnc = builtInstanceMap.get(registry.getKey(ancestor));
		CCoordInstance insDes = builtInstanceMap.get(registry.getKey(descendant));
		List<List<ICoordElement>> iterables = Lists.newArrayList();
		while(insAnc != insDes) {
			if(insDes.isRoot())
				return null;
			iterables.add(0, Arrays.asList(insDes.getCoordElements()));
			insDes = insDes.getParent();
		}
		return Iterables.concat(iterables);
	}

	@Override
	public Map<CCoordinates, Matrix4> evaluateAll(CoordContext context, boolean timeOffset1) {
		Map<CCoordInstance, Matrix4> evaluatedMap = Maps.newHashMap();		
		for(CCoordInstance instance : this.builtInstances) {
			if(instance.isRoot()) {
				evaluatedMap.put(instance, new Matrix4().setIdentity());
				continue;
			}

			Matrix4 current = new Matrix4(evaluatedMap.get(instance.getParent()));
			for(ICoordElement element : instance.getCoordElements()) {
				if(!context.supportContext(element.requiredContextTypes()));
					// TODO CoordSystem exception
				current.preMult(element.transformMatrix(context));
			}
		}

		ImmutableMap.Builder<CCoordinates, Matrix4> mapBuilder = ImmutableMap.builder();
		for(Map.Entry<CCoordInstance, Matrix4> entry : evaluatedMap.entrySet())
			if(entry.getKey().getOrigin() != null)
				mapBuilder.put(entry.getKey().getOrigin(), entry.getValue());
		return mapBuilder.build();
	}
}