package stellarapi.api.lib.config;

import java.util.Iterator;

/**
 * Iterator for configuration nodes. <p>
 * {@link #remove()} and {@link #add(String)} operations can only request deletions and additions, 
 *  which are processed on synchronization. (It can only be denied by a restriction)
 * */
public interface ICfgIterator<T> extends Iterator<T> {
    /**
     * Request a new config node between the last returned object from {@link #next()}
     *  and what will be returned from {@link #next()}.<p>
     * Only works for configurable collection part.
     * @param keyToRequest the unique key to request -
     *  there shouldn't be duplicate among add-requested key
     * @throws UnsupportedOperationException if it's not from a configurable collection node.
     */
    void add(String keyToRequest);
}