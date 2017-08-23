package stellarapi.api.lib.config;

import java.util.Map;

public interface IDCfgCollection {
	/** Checks if this collection is configurable. */
	public boolean isConfigurable();
	/** Checks if this collection is order-configurable. */
	public boolean isOrderConfigurable();

	/** Checks if there's a child node for certain key. stable version - no influence from request */
	public boolean hasChildNode(String key);
	
	/** Gets the child node for certain key.  stable version - no influence from request */
	public IDCfgNode getChildNode(String key);


	/**
	 * Request add the node with the requested key after the key.
	 * @param locKey the location of the key ahead of where the node will be added.
	 *    when it's <code>null</code>, the new node will be added on the first index.
	 * @param requestKey the requested key for the new location.
	 * */
	public void requestAdd(String locKey, String requestKey);

	/**
	 * Request remove the node for the key. returns true if removing it is successful.
	 * Can revert back add request.
	 * */
	public boolean requestRemove(String key);

	/**
	 * Request change the key for the node.
	 * Exchanges the two if there's two nodes for both parameter keys. <p>
	 * Prone to be broken by addition/removal of keys, so keep track of them. <p>
	 * 
	 * Does not applied to anywhere till the next sync.
	 * */
	public void requestChangeKey(String oldKey, String newKey);


	/** Cycles the order of the given keys in forward direction. */
	public void requestChangeOrder(String... keys);

	/**
	 * Request iterator.
	 * The node can be <code>null</code> when it's requested.
	 * All request is applied to this iterator except for the change-key request.
	 * 
	 * Beware the comodification.
	 * */
	public ICfgIterator<Map.Entry<String, IDCfgNode>> requestIterator();
}