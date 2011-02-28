import java.util.LinkedList;

public class test
{
    public String name {
        get { return name+"***"; }
        set { name = "name: "+value; }
    }
    
    public int age {get; set;}

	public static void main(String[] args)
	{
        int number = 2342342;
		String[] oldList = new String[]{"dog", "cat", "tree", "car", "davis", ~"the number\'s value  is [number]"};
		LinkedList<String> newList = from oldList where x => x.startsWith("ca") || x.equals("davis");
		
		for(String s : newList)
			System.out.println(s);

        var implicitDecl = new LinkedList<String>();
        String literalTest = #"some\path\li\"teral";
        int someNumber = #NUMBER;

        // Need to change this...
        String name = "#NAME";
	}
}
	
	