import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<I, S> implements Cache<I, S>
{
	private class Node<I, S>
	{
		public I key;
		public S data;
		public Node<I, S> prev;
		public Node<I, S> next;
		public Node(I key, S data)
		{
			this.key = key;
			this.data = data;
		}
	}


	private Node<I, S> head, tail;
	private int numMisses = 0;
	private int capacity;
	private DataProvider<I, S> provider;
	private HashMap<I, Node<I, S>> data = new HashMap<>();

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<I, S> provider, int capacity)
	{
		this.provider = provider;
		this.capacity = capacity;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public S get (I key)
	{
		Node<I, S> nodeFromCache = data.get(key);
		if(nodeFromCache != null)
		{
			remove(nodeFromCache);
			add(nodeFromCache);
			return nodeFromCache.data;
		}
		else
		{
			numMisses++;
			Node<I, S> nodeFromProvider = new Node<I, S>(key, provider.get(key));
			if(nodeFromProvider.data != null)
			{
				if(data.size() == capacity)
				{
					Node<I, S> LRU = head;
					data.remove(LRU.key);
					remove(LRU);
				}
				add(nodeFromProvider);
				data.put(key, nodeFromProvider);
				return nodeFromProvider.data;
			}
		}
		return null;
	}

	/**
	 * add the given node into the end of the list
	 * @param node the node to be added
	 * @return true after the node was successfully added
	 */
	public boolean add(Node<I, S> node)
	{
		if(head == null)
		{
			head = tail = node;
		}
		else
		{
			node.prev = tail;
			node.next = null;
			tail.next = node;
			tail = node;
		}
		return true;
	}

	/**
	 * removes the given node from the list
	 * @param node the node to be removed from the list
	 */
	public void remove(Node<I, S> node)
	{
		if(node.prev == null)
		{
			head = node.next;
		}
		else
		{
			node.prev.next = node.next;
		}
		if (node.next == null)
		{
			tail = node.prev;
		}
		else
		{
			node.next.prev = node.prev;
		}
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses ()
	{
		return numMisses;
	}
}
