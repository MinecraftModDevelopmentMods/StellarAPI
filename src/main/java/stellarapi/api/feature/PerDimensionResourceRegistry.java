package stellarapi.api.feature;

import java.util.Set;

public class PerDimensionResourceRegistry {
	
	private Set<String> resourceIds;
	
	public void registerResourceId(String id)
	{
		resourceIds.add(id);
	}

}
