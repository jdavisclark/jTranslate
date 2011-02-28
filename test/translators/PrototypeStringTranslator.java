
import jtranslate.Translator;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrototypeStringTranslator implements Translator
{
	public static Pattern protoElementPattern = Pattern.compile("\\[[^\\[\\]]+\\]");

	@Override
	public String translate(MatchResult mat)
	{
		String contents = mat.group(0).substring(1);
		Matcher elmMat = protoElementPattern.matcher(contents);
		while(elmMat.find())
		{
			String group = elmMat.group();
			contents = contents.replace(group, "\"+"+group.substring(1, group.length()-1)+"+\"");
		}

		return "("+contents+")";
	}

}
