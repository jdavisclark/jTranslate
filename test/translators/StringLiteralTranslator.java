import jtranslate.Translator;

import java.util.regex.*;



public class StringLiteralTranslator implements Translator
{
	@Override
	public String translate(MatchResult match)
	{
        Pattern p = Pattern.compile("\\\\t|\\\\b|\\\\n|\\\\r|\\\\f|\\\\'|\\\\\"");
        String input = match.group().substring(1);
        Matcher mat1 = p.matcher(input);
        while(mat1.find()) {
            input = input.replace(mat1.group(), "_@"+mat1.group().substring(1)+"@_");
        }

        input = input.replace("\\", "\\\\");

        Matcher mat2 = Pattern.compile("_@(.)@_").matcher(input);
        while(mat2.find()) {
            String c = mat2.group(1);
            input = input.replace("_@"+c+"@_", "\\"+c);
        }

        return input;
	}

}
