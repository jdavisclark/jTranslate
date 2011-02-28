import java.util.LinkedList;

public class test
{
    private String _name;
		public String name() { return _name+"***"; }
		public void name(String value) { _name = "_name: "+value; }

    
    private int _age;
		public int age() { return _age; }
		public void age(int value) { _age = value; }


	public static void main(String[] args)
	{
        int number = 2342342;
		String[] oldList = new String[]{"dog", "cat", "tree", "car", "davis", ("the number\'s value  is "+number+""};
		LinkedList<String> newList = new LinkedList<String>();
		for(String x : oldList) {
			if(x.startsWith("ca") || x.equals("davis"))
				newList.add(x);
		}
		
		for(String s : newList)
			System.out.println(s);

        LinkedList<String> implicitDecl = new LinkedList<String>();
        String literalTest = "some\\path\\li\"teral";
        int someNumber = 6543169;

        // Need to change this...
        String name = "John Smith");
	}
}
	
	