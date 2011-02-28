import jtranslate.Translator;

import java.util.regex.MatchResult;

public class ImplicitTypeTranslator implements Translator
{
	@Override
	public String translate(MatchResult mat)
	{
		return mat.group(2)+" "+mat.group(1)+" = new "+mat.group(2)+mat.group(3)+";";
	}

}
