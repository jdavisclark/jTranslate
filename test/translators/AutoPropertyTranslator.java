import jtranslate.Translator;

import java.util.regex.MatchResult;

public class AutoPropertyTranslator implements Translator
{
	@Override
	public String translate(MatchResult mat)
	{
		String name = mat.group(3).trim();
		String privateName = "_"+name.substring(0, 1).toLowerCase()+name.substring(1, name.length());
		String wrapped = "private "+mat.group(2)+" "+privateName+";\n";

		String getBody = "return "+privateName+";";
        if(!mat.group(4).equals("get;"))
        {
            String matGet = mat.group(4);
            getBody = matGet.substring(matGet.indexOf('{')+1, matGet.lastIndexOf('}')).trim().replace(name, privateName);
        }

		String setBody = privateName+" = value;";
        if(!mat.group(5).equals("set;"))
        {
            String matSet = mat.group(5);
            setBody = matSet.substring(matSet.indexOf('{')+1, matSet.lastIndexOf('}')).trim().replace("\r", "").replace("\n", "").replace("\t", "").replace(name, privateName);
        }

		String getter = "\t\t"+mat.group(1)+" "+mat.group(2)+" "+name+"() { "+getBody+" }\n";
		String setter = "\t\t"+mat.group(1)+" void "+name+"("+mat.group(2)+" value) { "+setBody+" }\n";

		return wrapped+getter+setter;
	}

}
