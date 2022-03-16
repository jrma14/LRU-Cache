import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTester
{
	private class DataProviderClass<T, U> implements DataProvider<T, U>
	{
		private HashMap<T, U> hash = new HashMap<T, U>();
		public int numElements = 0;

		public void add(T key, U data)
		{
			numElements++;
			hash.put(key, data);
		}

		@Override
		public U get(T key)
		{
			return hash.get(key);
		}
	}

	@Test
	public void leastRecentlyUsedIsCorrect()
	{
		DataProviderClass<Integer,String> provider = createWithNumElements(10);
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		for(Integer i = 0; i < 10; i++)
		{
			cache.get(i);
		}
		assertEquals(cache.getNumMisses(), 10);
		for(Integer i = 5; i < 10; i++)
		{
			cache.get(i);
		}
		assertEquals(cache.getNumMisses(), 10);
		for(Integer i = 0; i < 5; i++)
		{
			cache.get(i);
		}
		assertEquals(cache.getNumMisses(), 15);
	}

	@Test
	public void cacheClearingCorrectly()
	{
		DataProviderClass<Integer, String> provider = createWithNumElements(10);
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
		for(int i = 0; i < 5; i++)
		{
			cache.get(i);
		}
		assertTrue(cache.getNumMisses() == 5);
		cache.get(5);
		cache.get(0);
		assertTrue(cache.getNumMisses() == 7);

	}

	@Test
	public void constantTime()
	{
		float[] averageTimeCost = new float[100];
		DataProviderClass<Integer, String> provider = createWithNumElements(1000000);
		for(int i = 1; i <= averageTimeCost.length; i++)
		{
			Cache<Integer, String> cache = new LRUCache<Integer, String>(provider, i*1000);
			float[] diffs = new float[100000];
			for(int j = 0; j < diffs.length; j++)
			{
				long first = System.nanoTime();
				cache.get((int) (Math.random() * provider.numElements));
				long second = System.nanoTime();
				diffs[j] = second - first;
			}
			averageTimeCost[i-1] = average(diffs);
		}
		float count = 0;
		for(int i = 0; i < averageTimeCost.length-1; i++)
		{
			for(int j = i + 1; j < averageTimeCost.length; j++)
			{
				if(averageTimeCost[j] > averageTimeCost[i])
				{
					count++;
				}
			}
		}
		int numPairs = (averageTimeCost.length * (averageTimeCost.length-1))/2;
		System.out.println(count/numPairs);
		assertTrue(count/numPairs > 0.4 && count/numPairs < 0.6);
	}

	public DataProviderClass<Integer,String> createWithNumElements(int num)
	{
		DataProviderClass<Integer,String> provider = new DataProviderClass<>();
		for(Integer i = 0; i < num; i++)
		{
			provider.add(i,i.toString());
		}
		return provider;
	}

	public float average(float[] arr)
	{
		float sum = 0;
		for(int i = 0; i < arr.length; i++)
		{
			sum += arr[i];
		}
		return sum/arr.length;
	}
}                                                                                                                                                                                                                           