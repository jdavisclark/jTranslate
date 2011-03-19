import java.util.LinkedList;

public class test
{
	public static void main(String[] args)
	{
		String[] oldList = new String[]{"dog", "cat", "tree", "car", "davis"};
		LinkedList<String> newList = from oldList where x => (x.startsWith("ca") || x.equals("davis")) && x.length() <= 25;		
		
		for(String s : newList)
			System.out.println(s);
	}
}