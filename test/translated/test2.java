import java.util.LinkedList;

public class test
{
	public static void main(String[] args)
	{
		String[] oldList = new String[]{"dog", "cat", "tree", "car", "davis"};
		LinkedList<String> newList = new LinkedList<String>();
		for(String x : oldList) {
			if((x.startsWith("ca") || x.equals("davis")) && x.length() <= 25)
				newList.add(x);
		}		
		
		for(String s : newList)
			System.out.println(s);
	}
}