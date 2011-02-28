import jtranslate.Translator;

import java.util.regex.MatchResult;

public class LambdaTranslator implements Translator
{
	@Override
	public String translate(MatchResult mat)
	{
		StringBuilder sb = new StringBuilder();
		String
            generic = mat.group(2),
            struct = mat.group(1)+generic,
		    newName = mat.group(3),
		    source = mat.group(4),
		    itVar = mat.group(6),
		    condition = mat.group(7);

		sb.append(struct+" "+newName+" = new "+struct+"();\n");
		sb.append("\t\tfor("+generic.substring(1, generic.length()-1)+" "+itVar+" : "+source+") {\n");
		sb.append("\t\t\tif("+condition+")\n");
		sb.append("\t\t\t\t"+newName+".add("+itVar+");\n\t\t}");

		return sb.toString();
	}
}
